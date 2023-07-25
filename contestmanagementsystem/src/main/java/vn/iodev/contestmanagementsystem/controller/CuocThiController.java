package vn.iodev.contestmanagementsystem.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.contestmanagementsystem.config.CMSConfiguration;
import vn.iodev.contestmanagementsystem.exception.ResourceNotFoundException;
import vn.iodev.contestmanagementsystem.helper.ExcelHelper;
import vn.iodev.contestmanagementsystem.model.CuocThi;
import vn.iodev.contestmanagementsystem.model.DanhMuc;
import vn.iodev.contestmanagementsystem.model.DanhMucId;
import vn.iodev.contestmanagementsystem.model.DanhSachThi;
import vn.iodev.contestmanagementsystem.model.DoanThi;
import vn.iodev.contestmanagementsystem.model.DoiThi;
import vn.iodev.contestmanagementsystem.model.FileIO;
import vn.iodev.contestmanagementsystem.model.HuanLuyenVien;
import vn.iodev.contestmanagementsystem.model.ImportResponse;
import vn.iodev.contestmanagementsystem.model.KhoiThi;
import vn.iodev.contestmanagementsystem.model.LoaiTinhTrangCuocThi;
import vn.iodev.contestmanagementsystem.model.ThiSinh;
import vn.iodev.contestmanagementsystem.payload.FileIOResponse;
import vn.iodev.contestmanagementsystem.payload.KetQuaCaNhanResponse;
import vn.iodev.contestmanagementsystem.payload.KetQuaDongDoiResponse;
import vn.iodev.contestmanagementsystem.payload.NoiDungThi;
import vn.iodev.contestmanagementsystem.payload.ResponseMessage;
import vn.iodev.contestmanagementsystem.payload.DanhSachHLVTrongDoan;
import vn.iodev.contestmanagementsystem.payload.DanhSachThiSinhTrongDoanResponse;
import vn.iodev.contestmanagementsystem.payload.DongThongKeResponse;
import vn.iodev.contestmanagementsystem.payload.ToChucResponse;
import vn.iodev.contestmanagementsystem.repository.CuocThiRepository;
import vn.iodev.contestmanagementsystem.repository.DanhMucRepository;
import vn.iodev.contestmanagementsystem.repository.DanhSachThiRepository;
import vn.iodev.contestmanagementsystem.repository.DoanThiRepository;
import vn.iodev.contestmanagementsystem.repository.DoiThiRepository;
import vn.iodev.contestmanagementsystem.repository.FileIORepository;
import vn.iodev.contestmanagementsystem.repository.HuanLuyenVienRepository;
import vn.iodev.contestmanagementsystem.repository.KhoiThiRepository;
import vn.iodev.contestmanagementsystem.repository.ThiSinhRepository;
import vn.iodev.contestmanagementsystem.security.VaiTroChecker;
import vn.iodev.contestmanagementsystem.service.ExcelService;
import vn.iodev.contestmanagementsystem.service.FileStorageService;
import vn.iodev.contestmanagementsystem.service.ToChucService;
import vn.iodev.contestmanagementsystem.validator.CuocThiValidator;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Slf4j
public class CuocThiController {
    @Autowired
    CuocThiRepository cuocThiRepository;

    @Autowired
    FileStorageService storageService;

    @Autowired
    FileIORepository fileIORepository;

    @Autowired
    CMSConfiguration configuration;

    @Autowired
    ToChucService toChucService;

    @Autowired
    DoanThiRepository doanThiRepository;

    @Autowired
    HuanLuyenVienRepository huanLuyenVienRepository;

    @Autowired
    ThiSinhRepository thiSinhRepository;

    @Autowired
    KhoiThiRepository khoiThiRepository;

    @Autowired
    DanhSachThiRepository danhSachThiRepository;

    @Autowired
    DoiThiRepository doiThiRepository;
    
    @Autowired
    DanhMucRepository danhMucRepository;

    @Autowired
    ExcelService fileService;

    @Autowired
    VaiTroChecker vaiTroChecker;
    
    @GetMapping("/cuocthis")
    public List<CuocThi> getAllCuocThis(@RequestParam(defaultValue = "1") int page, 
            @RequestParam(defaultValue = "15") int size, 
            @RequestParam(defaultValue = "", required = false) String tuKhoa, 
            @RequestParam(required = false) String serieCuocThi, 
            @RequestParam(required = false) Integer lanToChuc, 
            @RequestParam(required = false) String toChucId,
            @RequestParam(required = false) String tinhTrang) {
        log.info("API GET /cuocthis");
        Pageable paging = PageRequest.of(page - 1, size);
        List<CuocThi> cuocThis;
        List<Integer> tinhTrangs = new ArrayList<>();
        if (tinhTrang != null && !tinhTrang.isEmpty()) {
            for (String tempTinhTrang : tinhTrang.split(",")) {
                try {
                    tinhTrangs.add(Integer.parseInt(tempTinhTrang));
                }
                catch (NumberFormatException e) {
                    log.debug("API GET /cuocthis", e);
                }
            }
        }
        else {
            tinhTrangs.add(LoaiTinhTrangCuocThi.MODANGKY);
            tinhTrangs.add(LoaiTinhTrangCuocThi.DONGDANGKY);
            tinhTrangs.add(LoaiTinhTrangCuocThi.DAKETTHUC);
        }
        cuocThis = cuocThiRepository.findToChucByMultipleConditions(tuKhoa, serieCuocThi, lanToChuc, toChucId, tinhTrangs, paging);
        String baseUrl = configuration.getGatewayUrl();

        for (CuocThi cuocThi : cuocThis) {
            List<FileIOResponse> files = storageService.getAllFilesByCuocThiId(cuocThi.getId()).map(fileIO -> {
                String fileDownloadUri = String.format("%s/api/files/cuocthis/%s/hinhanhs/%s", baseUrl, cuocThi.getId(), fileIO.getId());
          
                return new FileIOResponse(
                    fileIO.getId(),
                    fileIO.getName(),
                    fileDownloadUri,
                    fileIO.getType(),
                    fileIO.getData().length);
              }).collect(Collectors.toList());
            if (files.size() > 0) {
                cuocThi.setHinhAnh(files.get(0).getUrl());
            }
        }
        return cuocThis;
    }

    @GetMapping("/cuocthis/{id}")
    public ResponseEntity<CuocThi> getCuocThiById(@PathVariable(value = "id") String cuocThiId)
        throws ResourceNotFoundException {
        log.info("API GET /cuocthis/{id}");
        CuocThi cuocThi = cuocThiRepository.findById(cuocThiId)
          .orElseThrow(() -> new ResourceNotFoundException("CuocThi not found for this id :: " + cuocThiId));
        String baseUrl = configuration.getGatewayUrl();

        List<FileIOResponse> files = storageService.getAllFilesByCuocThiId(cuocThi.getId()).map(fileIO -> {
            String fileDownloadUri = String.format("%s/api/files/cuocthis/%s/hinhanhs/%s", baseUrl, cuocThi.getId(), fileIO.getId());
      
            return new FileIOResponse(
                fileIO.getId(),
                fileIO.getName(),
                fileDownloadUri,
                fileIO.getType(),
                fileIO.getData().length);
          }).collect(Collectors.toList());
        if (files.size() > 0) {
            cuocThi.setHinhAnh(files.get(0).getUrl());
        }
        return ResponseEntity.ok().body(cuocThi);
    }
    
    @GetMapping("/cuocthis/thongtincuocthi/{maCuocThi}")
    public ResponseEntity<CuocThi> getCuocThiByMaCuocThi(@PathVariable(value = "maCuocThi") String maCuocThi)
        throws ResourceNotFoundException {
    	log.info("API GET /cuocthis/thongtincuocthi/{maCuocThi}");
        Optional<CuocThi> cuocThiOpt = cuocThiRepository.findByMaCuocThi(maCuocThi);
        
        if(cuocThiOpt.isPresent()) {
        	 return ResponseEntity.ok().body(cuocThiOpt.get());
        }else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
       
    }

    private void validateRelationConstraint(CuocThi cuocThi) throws Exception {
        if (cuocThi.getLanToChuc() != null && cuocThi.getLanToChuc() < 0) {
            throw new ValidationException("LanToChuc must greater than 0");
        }
        if (cuocThi.getToChucId() != null) {
            ToChucResponse toChuc = toChucService.getToChucById(cuocThi.getToChucId());
            if (toChuc != null && toChuc.getTenGoi() != null && cuocThi.getDonViToChuc() == null) {
                cuocThi.setDonViToChuc(toChuc.getTenGoi());
            } 
        }
    }
    @PostMapping("/cuocthis")
    public ResponseEntity<CuocThi> createCuocThi(
        @Valid @RequestBody CuocThi cuocThi,
        @RequestHeader("id") String id,
        @RequestHeader("email") String email,
        @RequestHeader("vaiTros") String vaiTros) {
        log.info("API POST /cuocthis");
        try {
            if (vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
                validateRelationConstraint(cuocThi);
                CuocThiValidator.getInstance().validate(cuocThi);
                CuocThi _cuocThi = cuocThiRepository.save(new CuocThi(
                                                            cuocThi.getTenGoi(), 
                                                            cuocThi.getTiengAnh(), 
                                                            cuocThi.getSerieCuocThi(), 
                                                            cuocThi.getLanToChuc(), 
                                                            cuocThi.getDonViToChuc(), 
                                                            cuocThi.getDiaDiemToChuc(),
                                                            cuocThi.getToChucId(), 
                                                            cuocThi.getNgayBatDau(), 
                                                            cuocThi.getNgayKetThuc(), 
                                                            cuocThi.getThongTinMoTa(), 
                                                            cuocThi.getWebsite(),
                                                            cuocThi.getTinhTrang()));
                return new ResponseEntity<>(_cuocThi, HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
        }
        catch (Exception e) {
            log.debug("API POST /cuocthis", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/cuocthis/{id}")
    public ResponseEntity<CuocThi> updateCuocThi(@PathVariable("id") String id, @RequestBody CuocThi cuocThi,
        @RequestHeader("vaiTros") String vaiTros) {
        log.info("API PUT /cuocthis/{id}");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Optional<CuocThi> cuocThiData = cuocThiRepository.findById(id);
        if (cuocThiData.isPresent()) {
            try {
                validateRelationConstraint(cuocThi);
                CuocThiValidator.getInstance().validate(cuocThi);

                CuocThi _cuocThi = cuocThiData.get();
                if (cuocThi.getTenGoi() != null) {
                    _cuocThi.setTenGoi(cuocThi.getTenGoi());
                }
                if (cuocThi.getTiengAnh() != null) {
                    _cuocThi.setTiengAnh(cuocThi.getTiengAnh());
                }
                if (cuocThi.getSerieCuocThi() != null) {
                    _cuocThi.setSerieCuocThi(cuocThi.getSerieCuocThi());
                }
                if (cuocThi.getLanToChuc() != null) {
                    if (cuocThi.getLanToChuc() < 0) {
                        throw new ValidationException("LanToChuc must greater than 0");
                    }
                    _cuocThi.setLanToChuc(cuocThi.getLanToChuc());
                }
                if (cuocThi.getDonViToChuc() != null) {
                    _cuocThi.setDonViToChuc(cuocThi.getDonViToChuc());
                }
                if (cuocThi.getToChucId() != null) {
                    if (cuocThi.getToChucId() != null) {
                        ToChucResponse toChuc = toChucService.getToChucById(cuocThi.getToChucId());
                        if (toChuc != null && toChuc.getTenGoi() != null && cuocThi.getDonViToChuc() == null) {
                            _cuocThi.setDonViToChuc(toChuc.getTenGoi());
                            _cuocThi.setToChucId(cuocThi.getToChucId());
                        } 
                    }
                }
                if (cuocThi.getNgayBatDau() != null) {
                    _cuocThi.setNgayBatDau(cuocThi.getNgayBatDau());
                }
                if (cuocThi.getNgayKetThuc() != null) {
                    _cuocThi.setNgayKetThuc(cuocThi.getNgayKetThuc());
                }
                if (cuocThi.getThongTinMoTa() != null) {
                    _cuocThi.setThongTinMoTa(cuocThi.getThongTinMoTa());
                }
                if (cuocThi.getWebsite() != null) {
                    _cuocThi.setWebsite(cuocThi.getWebsite());
                }
                if (cuocThi.getHinhAnh() != null) {
                    _cuocThi.setHinhAnh(cuocThi.getHinhAnh());
                }
                if (cuocThi.getTinhTrang() != null) {
                    _cuocThi.setTinhTrang(cuocThi.getTinhTrang());
                }
                
                return new ResponseEntity<>(cuocThiRepository.save(_cuocThi), HttpStatus.OK);
            }
            catch (Exception e) {
                log.debug("API PUT /cuocthis/{id}", e);
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/cuocthis/{id}/hinhanhs")
    public ResponseEntity<ResponseMessage> updateHinhAnhCuocThi(@PathVariable("id") String id, @RequestParam("hinhAnhs") MultipartFile[] hinhAnhFiles,
        @RequestHeader("vaiTros") String vaiTros) {
        log.info("API PUT /cuocthis/{id}/hinhanhs");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<CuocThi> cuocThiData = cuocThiRepository.findById(id);
        if (cuocThiData.isPresent()) {
            CuocThi _cuocThi = cuocThiData.get();
            for (MultipartFile hinhAnhFile : hinhAnhFiles) {
                try {
                    FileIO fileIO = storageService.store(hinhAnhFile, _cuocThi);
                    fileIORepository.save(fileIO);
                } catch (IOException e) {
                    log.debug("API PUT /cuocthis/{id}/hinhanhs", e);
                } 
            }
          
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Update hinhAnhs successfully!"));
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/cuocthis/{id}/hinhanhs")
    public ResponseEntity<List<FileIOResponse>> getHinhAnhsCuocThi(@PathVariable("id") String id) {
        log.info("GET PUT /cuocthis/{id}/hinhanhs");
        Optional<CuocThi> cuocThiData = cuocThiRepository.findById(id);
        String baseUrl = configuration.getGatewayUrl();

        if (cuocThiData.isPresent()) {
            List<FileIOResponse> files = storageService.getAllFilesByCuocThiId(id).map(fileIO -> {
                String fileDownloadUri = String.format("%s/api/files/cuocthis/%s/hinhanhs/%s", baseUrl, id, fileIO.getId());
          
                return new FileIOResponse(
                    fileIO.getId(),
                    fileIO.getName(),
                    fileDownloadUri,
                    fileIO.getType(),
                    fileIO.getData().length);
              }).collect(Collectors.toList());
          
            return ResponseEntity.status(HttpStatus.OK).body(files);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/files/cuocthis/{cuocThiId}/hinhanhs/{id}", produces = {
        MediaType.IMAGE_JPEG_VALUE,
        MediaType.IMAGE_PNG_VALUE,
        MediaType.IMAGE_GIF_VALUE
    })
    public ResponseEntity<byte[]> getFile(@PathVariable("cuocThiId") String cuocThiId, @PathVariable("id") String id) {
        log.info("API GET /files/cuocthis/{cuocThiId}/hinhanhs/{id}");

        if (!cuocThiRepository.existsById(cuocThiId)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        FileIO fileIO = storageService.getFile(id);

        return ResponseEntity.ok()
            // .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + toChuc.getLogoFileName() + "\"")
            .body(fileIO.getData());
    }
    
    @DeleteMapping("/cuocthis/{cuocThiId}/hinhanhs/{id}")
    public ResponseEntity<HttpStatus> deleteHinhAnhCuocThi(@PathVariable("cuocThiId") String cuocThiId, @PathVariable("id") String id, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API DELETE /cuocthis/{cuocThiId}/hinhanhs/{id}");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!cuocThiRepository.existsById(cuocThiId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            fileIORepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            log.debug("API DELETE /cuocthis/{cuocThiId}/hinhanhs/{id}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/cuocthis/{id}")
    public ResponseEntity<HttpStatus> deleteCuocThi(@PathVariable("id") String id, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API DELETE /cuocthis/{id}");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            cuocThiRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            log.debug("API DELETE /cuocthis/{id}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<DanhSachThi> filterByThiSinhIds(List<DanhSachThi> lDanhSachThis, List<String> thiSinhIds) {
        List<DanhSachThi> lstResult = new ArrayList<>();
        if (lDanhSachThis == null) {
            return lstResult;
        }
        for (DanhSachThi dst : lDanhSachThis) {
            if (thiSinhIds.contains(dst.getThiSinhId())) {
                lstResult.add(dst);
            }
        }

        return lstResult;
    }

    private List<DanhSachThi> filterByDoiThiIds(List<DanhSachThi> lDanhSachThis, List<String> doiThiIds) {
        List<DanhSachThi> lstResult = new ArrayList<>();
        if (lDanhSachThis == null) {
            return lstResult;
        }
        for (DanhSachThi dst : lDanhSachThis) {
            if (doiThiIds.contains(dst.getDoiThiId())) {
                lstResult.add(dst);
            }
        }

        return lstResult;
    }

    @GetMapping(value = "/cuocthis/{cuocThiId}/thongke")
    public ResponseEntity<List<DongThongKeResponse>> thongKe(@PathVariable("cuocThiId") String cuocThiId) {
        log.info("API DELETE /cuocthis/{cuocThiId}/thongke");
        if (!cuocThiRepository.existsById(cuocThiId)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<DongThongKeResponse> result = new ArrayList<>();
        List<KhoiThi> khoithis = khoiThiRepository.findByCuocThiId(cuocThiId);

        List<DoanThi> lstDoanThi = doanThiRepository.findByCuocThiId(cuocThiId);
        List<HuanLuyenVien> lstAllHuanLuyenViens = huanLuyenVienRepository.findByCuocThiId(cuocThiId);
        List<ThiSinh> lstAllThiSinhs = thiSinhRepository.findByCuocThiId(cuocThiId);
        List<DoiThi> lstAllDoiThis = doiThiRepository.findByCuocThiId(cuocThiId);
        Map<String, List<HuanLuyenVien>> mapDoanThiHlvs = new HashMap<>();
        Map<String, List<ThiSinh>> mapDoanThiTs = new HashMap<>();
        Map<String, List<DoiThi>> mapDoanThiDoiThis = new HashMap<>();

        for (HuanLuyenVien hlv : lstAllHuanLuyenViens) {
            List<HuanLuyenVien> tempHlvs = null;
            if (mapDoanThiHlvs.containsKey(hlv.getDoanThiId())) {
                tempHlvs = mapDoanThiHlvs.get(hlv.getDoanThiId());
            }
            else {
                tempHlvs = new ArrayList<>();
                mapDoanThiHlvs.put(hlv.getDoanThiId(), tempHlvs);
            }
            if (tempHlvs != null && !tempHlvs.contains(hlv)) {
                tempHlvs.add(hlv);
            }
        }
        for (ThiSinh ts : lstAllThiSinhs) {
            List<ThiSinh> tempTs = null;
            if (mapDoanThiTs.containsKey(ts.getDoanThiId())) {
                tempTs = mapDoanThiTs.get(ts.getDoanThiId());
            }
            else {
                tempTs = new ArrayList<>();
                mapDoanThiTs.put(ts.getDoanThiId(), tempTs);
            }
            if (tempTs != null && !tempTs.contains(ts)) {
                tempTs.add(ts);
            }
        }
        for (DoiThi dt : lstAllDoiThis) {
            List<DoiThi> tempDt = null;
            if (mapDoanThiDoiThis.containsKey(dt.getDoanThiId())) {
                tempDt = mapDoanThiDoiThis.get(dt.getDoanThiId());
            }
            else {
                tempDt = new ArrayList<>();
                mapDoanThiDoiThis.put(dt.getDoanThiId(), tempDt);
            }
            if (tempDt != null && !tempDt.contains(dt)) {
                tempDt.add(dt);
            }
        }
        List<DanhSachThi> lDanhSachThis = danhSachThiRepository.findByCuocThiId(cuocThiId);
        Map<String, List<DanhSachThi>> mapKhoiThiDst = new HashMap<>();
        for (DanhSachThi dst : lDanhSachThis) {
            List<DanhSachThi> tempDst = null;
            if (mapKhoiThiDst.containsKey(dst.getKhoiThiId())) {
                tempDst = mapKhoiThiDst.get(dst.getKhoiThiId());
            }
            else {
                tempDst = new ArrayList<>();
                mapKhoiThiDst.put(dst.getKhoiThiId(), tempDst);
            }
            if (tempDst != null && !tempDst.contains(dst)) {
                tempDst.add(dst);
            }
        }

        for (DoanThi doanThi : lstDoanThi) {
            DongThongKeResponse response = new DongThongKeResponse();
            response.setDoanThi(doanThi);
            // response.setSoHuanLuyenVien(huanLuyenVienRepository.countByCuocThiIdAndDoanThiId(cuocThiId, doanThi.getId()));
            // response.setSoThiSinh(thiSinhRepository.countByCuocThiIdAndDoanThiId(cuocThiId, doanThi.getId()));
            if (mapDoanThiHlvs.containsKey(doanThi.getId())) {
                response.setSoHuanLuyenVien((long)mapDoanThiHlvs.get(doanThi.getId()).size());
            }
            if (mapDoanThiTs.containsKey(doanThi.getId())) {
                response.setSoThiSinh((long)mapDoanThiTs.get(doanThi.getId()).size());
            }
            List<NoiDungThi> lstNoiDungThi = new ArrayList<>();

            for (KhoiThi khoiThi : khoithis) {
                NoiDungThi noiDungThi = new NoiDungThi();
                noiDungThi.setTenNoiDung(khoiThi.getTenGoi());
                noiDungThi.setKhoiThiId(khoiThi.getId());
                noiDungThi.setThiTapThe(khoiThi.getThiTapThe());
                noiDungThi.setThiSangTao(khoiThi.getThiSangTao());
                
                // List<ThiSinh> lstThiSinhs = thiSinhRepository.findByCuocThiIdAndDoanThiId(cuocThiId, doanThi.getId());
                // List<DoiThi> lstDoiThis = doiThiRepository.findByCuocThiIdAndDoanThiId(cuocThiId, doanThi.getId());
                List<ThiSinh> lstThiSinhs = mapDoanThiTs.containsKey(doanThi.getId()) ? mapDoanThiTs.get(doanThi.getId()) : new ArrayList<>();
                List<DoiThi> lstDoiThis = mapDoanThiDoiThis.containsKey(doanThi.getId()) ? mapDoanThiDoiThis.get(doanThi.getId()) : new ArrayList<>();

                List<String> thiSinhIds = new ArrayList<>();
                List<String> doiThiIds = new ArrayList<>();
                for (ThiSinh thiSinh : lstThiSinhs) {
                    if (!thiSinhIds.contains(thiSinh.getId())) {
                        thiSinhIds.add(thiSinh.getId());
                    }
                }
                for (DoiThi doiThi : lstDoiThis) {
                    if (!doiThiIds.contains(doiThi.getId())) {
                        doiThiIds.add(doiThi.getId());
                    }
                }
                // List<DanhSachThi> thiSinhThamGiaThis = danhSachThiRepository.findByCuocThiIdAndKhoiThiIdAndThiSinhIdIn(cuocThiId, khoiThi.getId(), thiSinhIds);
                // List<DanhSachThi> doiThiThamGiaThis = danhSachThiRepository.findByCuocThiIdAndKhoiThiIdAndDoiThiIdIn(cuocThiId, khoiThi.getId(), doiThiIds);

                List<DanhSachThi> thiSinhThamGiaThis = filterByThiSinhIds(mapKhoiThiDst.get(khoiThi.getId()), thiSinhIds);
                List<DanhSachThi> doiThiThamGiaThis = filterByDoiThiIds(mapKhoiThiDst.get(khoiThi.getId()), doiThiIds);

                thiSinhIds.clear();
                for (DanhSachThi dst : thiSinhThamGiaThis) {
                    if (!thiSinhIds.contains(dst.getThiSinhId())) {
                        thiSinhIds.add(dst.getThiSinhId());
                    }
                }
                doiThiIds.clear();
                for (DanhSachThi dst : doiThiThamGiaThis) {
                    if (!doiThiIds.contains(dst.getDoiThiId())) {
                        doiThiIds.add(dst.getDoiThiId());
                    }
                }

                noiDungThi.setSoDoi(doiThiIds.size());
                noiDungThi.setSoThiSinh(thiSinhIds.size());

                lstNoiDungThi.add(noiDungThi);
            }

            response.setNoiDungThi(lstNoiDungThi);

            result.add(response);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/cuocthis/{cuocThiId}/khoithis/{khoiThiId}/giaicanhan")
    public ResponseEntity<List<KetQuaCaNhanResponse>> ketQuaCaNhan(@PathVariable(value = "cuocThiId") String cuocThiId, @PathVariable(value = "khoiThiId") String khoiThiId)
        throws ResourceNotFoundException {
        log.info("API GET /cuocthis/{cuocThiId}/khoithis/{khoiThiId}/giaicanhan");
        List<KetQuaCaNhanResponse> lstKetquas = new ArrayList<>();
        Optional<CuocThi> cuocThiData = cuocThiRepository.findById(cuocThiId);
        Optional<KhoiThi> khoiThiData = khoiThiRepository.findById(khoiThiId);
        List<ThiSinh> thiSinhs = thiSinhRepository.findByCuocThiId(cuocThiId);
        Map<String, ThiSinh> mapThiSinhs = new HashMap<>();
        for (ThiSinh ts : thiSinhs) {
            mapThiSinhs.put(ts.getId(), ts);
        }
        List<DoanThi> doanThis = doanThiRepository.findByCuocThiId(cuocThiId);
        Map<String, DoanThi> mapDoanThis = new HashMap<>();
        for (DoanThi dt : doanThis) {
            mapDoanThis.put(dt.getId(), dt);
        }
        if (cuocThiData.isPresent() && khoiThiData.isPresent()) {
            List<DanhSachThi> danhSachThis = danhSachThiRepository.getDanhSachThiCaNhan(cuocThiData.get(), khoiThiId);
            for (DanhSachThi dst : danhSachThis) {
                if (dst.getThiSinhId() != null && !dst.getThiSinhId().isEmpty() && (dst.getDoiThiId() == null || dst.getDoiThiId().isEmpty())) {
                    KetQuaCaNhanResponse row = new KetQuaCaNhanResponse();
                    row.setThuTuXepHang(dst.getThuTuXepHang());
                    row.setSoBaoDanh(dst.getSoBaoDanh());
                    row.setHangGiaiThuong(dst.getHangGiaiThuong());
                    if (mapThiSinhs.containsKey(dst.getThiSinhId())) {
                        ThiSinh ts = mapThiSinhs.get(dst.getThiSinhId());
                        row.setTenThiSinh(ts.getHoTen());
                        row.setMaThiSinh(ts.getId());
                        if (mapDoanThis.containsKey(ts.getDoanThiId())) {
                            row.setTenDoanThi(mapDoanThis.get(ts.getDoanThiId()).getTenGoi());
                            row.setMaDoanThi(ts.getDoanThiId());
                        }
                    }
                    if (dst.getHangGiaiThuong() != null && !dst.getHangGiaiThuong().isEmpty()) {
                        Optional<DanhMuc> giaiThuong = danhMucRepository.findById(new DanhMucId("C_HangGiaiThuong", dst.getHangGiaiThuong()));
                        if (giaiThuong.isPresent()) {
                            row.setTenGiaiThuong(giaiThuong.get().getGiaTri());
                        }
                        
                        lstKetquas.add(row);
                    }
                   
                }
            }
        }
        return ResponseEntity.ok().body(lstKetquas);
    }

    @GetMapping("/cuocthis/{cuocThiId}/khoithis/{khoiThiId}/giaitapthe")
    public ResponseEntity<List<KetQuaDongDoiResponse>> ketQuaTapThe(@PathVariable(value = "cuocThiId") String cuocThiId, @PathVariable(value = "khoiThiId") String khoiThiId)
        throws ResourceNotFoundException {
        log.info("API GET /cuocthis/{cuocThiId}/khoithis/{khoiThiId}/giaitapthe");
        List<KetQuaDongDoiResponse> lstKetquas = new ArrayList<>();
        Optional<CuocThi> cuocThiData = cuocThiRepository.findById(cuocThiId);
        Optional<KhoiThi> khoiThiData = khoiThiRepository.findById(khoiThiId);
        List<ThiSinh> thiSinhs = thiSinhRepository.findByCuocThiId(cuocThiId);
        Map<String, ThiSinh> mapThiSinhs = new HashMap<>();
        for (ThiSinh ts : thiSinhs) {
            mapThiSinhs.put(ts.getId(), ts);
        }
        List<DoiThi> doiThis = doiThiRepository.findByCuocThiIdAndKhoiThiId(cuocThiId, khoiThiId);
        Map<String, DoiThi> mapDoiThis = new HashMap<>();
        for (DoiThi dt : doiThis) {
            mapDoiThis.put(dt.getId(), dt);
        }
        List<DoanThi> doanThis = doanThiRepository.findByCuocThiId(cuocThiId);
        Map<String, DoanThi> mapDoanThis = new HashMap<>();
        for (DoanThi dt : doanThis) {
            mapDoanThis.put(dt.getId(), dt);
        }
        if (cuocThiData.isPresent() && khoiThiData.isPresent()) {
            List<DanhSachThi> danhSachThis = danhSachThiRepository.getDanhSachThiTapThe(cuocThiData.get(), khoiThiId);
            for (DanhSachThi dst : danhSachThis) {
                if (dst.getDoiThiId() != null && !dst.getDoiThiId().isEmpty() && mapDoiThis.containsKey(dst.getDoiThiId())) {
                    KetQuaDongDoiResponse row = new KetQuaDongDoiResponse();
                    row.setThuTuXepHang(dst.getThuTuXepHang());
                    row.setHangGiaiThuong(dst.getHangGiaiThuong());
                    DoiThi dt = mapDoiThis.get(dst.getDoiThiId());
                    row.setTenDoiThi(dt.getTenGoi());
                    row.setMaDoiThi(dt.getId());
                    if (mapDoanThis.containsKey(dt.getDoanThiId())) {
                        row.setTenDoanThi(mapDoanThis.get(dt.getDoanThiId()).getTenGoi());
                        row.setMaDoanThi(dt.getDoanThiId());
                    }

                    if (dst.getHangGiaiThuong() != null && !dst.getHangGiaiThuong().isEmpty()) {
                        Optional<DanhMuc> giaiThuong = danhMucRepository.findById(new DanhMucId("C_HangGiaiThuong", dst.getHangGiaiThuong()));
                        if (giaiThuong.isPresent()) {
                            row.setTenGiaiThuong(giaiThuong.get().getGiaTri());
                        }
                        
                        List<DanhSachThi> thiSinhTrongDois = danhSachThiRepository.getDanhSachThiCuaDoiThi(cuocThiData.get(), khoiThiId, dst.getDoiThiId());
                        List<ThiSinh> lstThiSinh = new ArrayList<>();
                        for (DanhSachThi tstd : thiSinhTrongDois) {
                            if (mapThiSinhs.containsKey(tstd.getThiSinhId())) {
                                lstThiSinh.add(mapThiSinhs.get(tstd.getThiSinhId()));
                            }
                        }
                        row.setThiSinh(lstThiSinh);
                        lstKetquas.add(row);
                    }
                   

                    mapDoiThis.remove(dst.getDoiThiId());
                }
            }
        }
        return ResponseEntity.ok().body(lstKetquas);
    }

    @GetMapping("/cuocthis/{cuocThiId}/doanthis/{doanThiId}/thisinhs")
    public ResponseEntity<List<DanhSachThiSinhTrongDoanResponse>> danhSachThiSinhTrongDoan(@PathVariable(value = "cuocThiId") String cuocThiId, @PathVariable(value = "doanThiId") String doanThiId)
        throws ResourceNotFoundException {
        log.info("API GET /cuocthis/{cuocThiId}/doanthis/{doanThiId}/thisinhs");
        List<DanhSachThiSinhTrongDoanResponse> lstKetquas = new ArrayList<>();
        Optional<CuocThi> cuocThiData = cuocThiRepository.findById(cuocThiId);
        Optional<DoanThi> doanThiData = doanThiRepository.findById(doanThiId);
        List<ThiSinh> thiSinhs = thiSinhRepository.findByCuocThiIdAndDoanThiId(cuocThiId, doanThiId);
        Map<String, ThiSinh> mapThiSinhs = new HashMap<>();
        for (ThiSinh ts : thiSinhs) {
            mapThiSinhs.put(ts.getId(), ts);
        }
        if (cuocThiData.isPresent() && doanThiData.isPresent()) {
            for (ThiSinh thiSinh : thiSinhs) {
                List<DanhSachThi> danhSachThis = danhSachThiRepository.getDanhSachThamGiaCuaCaNhan(cuocThiData.get(), thiSinh.getId());
                DanhSachThiSinhTrongDoanResponse row = new DanhSachThiSinhTrongDoanResponse();
                row.setId(thiSinh.getId());
                row.setHoTen(thiSinh.getHoTen());
                row.setGioiTinh(thiSinh.getGioiTinh());
                row.setDoiTuongThi(thiSinh.getDoiTuongThi());
                row.setNganhDaoTao(thiSinh.getNganhDaoTao());
                row.setNgaySinh(thiSinh.getNgaySinh());
                row.setSoDienThoai(thiSinh.getSoDienThoai());
                row.setEmail(thiSinh.getEmail());
                row.setDatGiaiThuong(thiSinh.getDatGiaiThuong());
                
                List<String> khoiThiIds = new ArrayList<>();
                for (DanhSachThi dst : danhSachThis) {
                    if (!khoiThiIds.contains(dst.getKhoiThiId())) {
                        khoiThiIds.add(dst.getKhoiThiId());
                    }
                    
                    if(dst.getThiSinhId() == thiSinh.getId()) {
                    	row.setSoBaoDanh(dst.getSoBaoDanh());
                    }
                }
                List<KhoiThi> noiDungThi = khoiThiRepository.findByIdIn(khoiThiIds);
                row.setNoiDungThi(noiDungThi);

                if (noiDungThi.size() > 0) {
                    lstKetquas.add(row);
                }
            }
        }
        return ResponseEntity.ok().body(lstKetquas);
    }

    @GetMapping("/cuocthis/{cuocThiId}/doanthis/{doanThiId}/huanluyenviens")
    public ResponseEntity<List<DanhSachHLVTrongDoan>> danhSachHLVTrongDoan(@PathVariable(value = "cuocThiId") String cuocThiId, @PathVariable(value = "doanThiId") String doanThiId)
        throws ResourceNotFoundException {
        log.info("API GET /cuocthis/{cuocThiId}/doanthis/{doanThiId}/huanluyenviens");
        List<DanhSachHLVTrongDoan> lstKetquas = new ArrayList<>();
        Optional<CuocThi> cuocThiData = cuocThiRepository.findById(cuocThiId);
        Optional<DoanThi> doanThiData = doanThiRepository.findById(doanThiId);
        List<HuanLuyenVien> huanLuyenViens = huanLuyenVienRepository.findByCuocThiIdAndDoanThiId(cuocThiId, doanThiId);
        if (cuocThiData.isPresent() && doanThiData.isPresent()) {
            for (HuanLuyenVien huanLuyenVien : huanLuyenViens) {
                DanhSachHLVTrongDoan row = new DanhSachHLVTrongDoan();
                row.setHoTen(huanLuyenVien.getHoTen());
                row.setSoDienThoai(huanLuyenVien.getSoDienThoai());
                row.setEmail(huanLuyenVien.getEmail());
                row.setId(huanLuyenVien.getId());
                row.setTruongPhoDoan(huanLuyenVien.getTruongPhoDoan());
                
                List<String> khoiThiIds = Arrays.asList(huanLuyenVien.getKhoiThiId().split(","));
            
                List<KhoiThi> noiDungThi = khoiThiRepository.findByIdIn(khoiThiIds);
                row.setNoiDungThi(noiDungThi);
                
                //if (noiDungThi.size() > 0) {
                //    lstKetquas.add(row);
                //}
                
                lstKetquas.add(row);
            }
        }
        return ResponseEntity.ok().body(lstKetquas);
    }

    @PostMapping("/cuocthis/import")
    public ResponseEntity<ImportResponse> importDuLieuCuocThi(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileType") String fileType, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API POST /cuocthis/import");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();
        String message = "";

        if (ExcelHelper.hasExcelFormat(multipartFile)) {
            List<ToChucResponse> tochucs = fileService.importToChuc(multipartFile);

            List<CuocThi> cuocthis = fileService.importCuocThi(multipartFile);
            
            List<KhoiThi> khoithis = fileService.importKhoiThi(multipartFile, cuocthis);
            List<DoanThi> doanthis = new ArrayList<>();

            fileService.importDanhSachThi(multipartFile, cuocthis, khoithis);

            message = "Import contest data successfully: " + multipartFile.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ImportResponse(fileName, size, message));
        }

        message = "Please import an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImportResponse(fileName, size, message));
    }

    @PostMapping("/cuocthis/{id}/ketqua/import")
    public ResponseEntity<ImportResponse> importKetQuaCuocThi(@PathVariable(value = "id") String id, @RequestParam("file") MultipartFile multipartFile, @RequestParam("fileType") String fileType, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API POST /cuocthis/{id}/ketqua/import");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();
        String message = "";
        Optional<CuocThi> cuocThiData = cuocThiRepository.findById(id);
        if (!cuocThiData.isPresent()) {
            message = "Please correct CuocThi id!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImportResponse(fileName, size, message));
        }
        else {
            if (ExcelHelper.hasExcelFormat(multipartFile)) {
                fileService.importKetQuaCuocThi(multipartFile, cuocThiData.get());
                message = "Import contest result data successfully: " + multipartFile.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ImportResponse(fileName, size, message));
            }
        }   
        message = "Please import an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImportResponse(fileName, size, message));
    }
    
    @PostMapping("/cuocthis/{cuocThiId}/export")
	public ResponseEntity<?> exportThiSinh(@PathVariable("cuocThiId") String cuocThiId) {
		log.info("API POST /cuocthis/{cuocThiId}/export");

		File file = fileService.exportDanhSachDangKyMau2(cuocThiId);

		if (file == null) {
			return ResponseEntity.noContent().build();
		}

		Resource resource = null;
		try {
			resource = new UrlResource(file.toURI());
			return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		} catch (MalformedURLException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}
    
    @PostMapping("/cuocthis/{cuocThiId}/khoithis/{khoiThiId}/export")
  	public ResponseEntity<?> exportDanhSachThiTheoKhoiThi(@PathVariable("cuocThiId") String cuocThiId, @PathVariable("khoiThiId") String khoiThiId) {
  		log.info("API POST /cuocthis/{cuocThiId}/khoithis/{khoiThiId}/export");
  		
  		Optional<KhoiThi> khoiThiOpt = khoiThiRepository.findById(khoiThiId);
  		
  		if(!khoiThiOpt.isPresent()) {
  			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  		}
  		
  		KhoiThi khoiThi = khoiThiOpt.get();
  		
  		File file = null;
  		
  		if(khoiThi.getThiTapThe()) {
  			file = fileService.exportDanhSachDangKyKhoiThiTapTheMau1(cuocThiId, khoiThiId);
  		}else {
  			file = fileService.exportDanhSachDangKyKhoiThiCaNhanMau1(cuocThiId, khoiThiId);
  		}

  		if (file == null) {
  			return ResponseEntity.noContent().build();
  		}

  		Resource resource = null;
  		try {
  			resource = new UrlResource(file.toURI());
  			return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
  					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
  					.body(resource);
  		} catch (MalformedURLException e) {
  			log.error(e.getMessage());
  			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  		}
  	}
    
    
    @PostMapping("/cuocthis/khoithicanhan/import")
    public ResponseEntity<ImportResponse> importUpdateDuLieuCuocThiCaNhan(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileType") String fileType, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API POST /cuocthis/khoithicanhan/import");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        
        long size = multipartFile.getSize();
        
        String message = "";

        if (ExcelHelper.hasExcelFormat(multipartFile)) {
           
            fileService.importDanhSachDangKyKhoiThiCaNhanMau1(multipartFile);

            message = "Import contest data successfully: " + multipartFile.getOriginalFilename();
            
            return ResponseEntity.status(HttpStatus.OK).body(new ImportResponse(fileName, size, message));
        }

        message = "Please import an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImportResponse(fileName, size, message));
    }

    @PostMapping("/cuocthis/khoithitapthe/import")
    public ResponseEntity<ImportResponse> importUpdateDuLieuCuocThiTapThe(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileType") String fileType, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API POST /cuocthis/khoithitapthe/import");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        
        long size = multipartFile.getSize();
        
        String message = "";

        if (ExcelHelper.hasExcelFormat(multipartFile)) {
           
            fileService.importDanhSachDangKyKhoiThiTapTheMau1(multipartFile);

            message = "Import contest data successfully: " + multipartFile.getOriginalFilename();
            
            return ResponseEntity.status(HttpStatus.OK).body(new ImportResponse(fileName, size, message));
        }

        message = "Please import an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImportResponse(fileName, size, message));
    }
}
