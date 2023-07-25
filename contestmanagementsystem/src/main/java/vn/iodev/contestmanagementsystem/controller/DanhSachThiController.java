package vn.iodev.contestmanagementsystem.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.netflix.config.validation.ValidationException;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.contestmanagementsystem.exception.ResourceNotFoundException;
import vn.iodev.contestmanagementsystem.helper.ExcelHelper;
import vn.iodev.contestmanagementsystem.model.CuocThi;
import vn.iodev.contestmanagementsystem.model.DanhSachThi;
import vn.iodev.contestmanagementsystem.model.DoanThi;
import vn.iodev.contestmanagementsystem.model.DoiThi;
import vn.iodev.contestmanagementsystem.model.ImportResponse;
import vn.iodev.contestmanagementsystem.model.KhoiThi;
import vn.iodev.contestmanagementsystem.model.ThiSinh;
import vn.iodev.contestmanagementsystem.payload.ToChucResponse;
import vn.iodev.contestmanagementsystem.repository.CuocThiRepository;
import vn.iodev.contestmanagementsystem.repository.DanhSachThiRepository;
import vn.iodev.contestmanagementsystem.repository.DoiThiRepository;
import vn.iodev.contestmanagementsystem.repository.KhoiThiRepository;
import vn.iodev.contestmanagementsystem.repository.ThiSinhRepository;
import vn.iodev.contestmanagementsystem.security.VaiTroChecker;
import vn.iodev.contestmanagementsystem.service.ExcelService;
import vn.iodev.contestmanagementsystem.validator.DanhSachThiValidator;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Slf4j
public class DanhSachThiController {
    @Autowired
    DanhSachThiRepository danhSachThiRepository;

    @Autowired
    CuocThiRepository cuocThiRepository;

    @Autowired
    KhoiThiRepository khoiThiRepository;

    @Autowired
    DoiThiRepository doiThiRepository;

    @Autowired
    ThiSinhRepository thiSinhRepository;

    @Autowired
    ExcelService fileService;

    @Autowired
    VaiTroChecker vaiTroChecker;

    @GetMapping("/danhsachthis")
    public List<DanhSachThi> getAllDanhSachThis(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size, @RequestParam(required = false) String cuocThiId, @RequestParam(required = false) String khoiThiId, @RequestParam(required = false) String doiThiId,  @RequestParam(required = false) String doanThiId) {
        log.info("API GET /danhsachthis");

        Pageable paging = PageRequest.of(page - 1, size);
        Optional<CuocThi> cuocThiData = cuocThiRepository.findById(cuocThiId);
        if (cuocThiData.isPresent()) {
            return danhSachThiRepository.findDanhSachThiByMultipleConditions(cuocThiData.get(), khoiThiId, doiThiId, doanThiId, paging);
        }
        else {
            return danhSachThiRepository.findDanhSachThiByMultipleConditions(null, khoiThiId, doiThiId, doanThiId, paging);
        }
    }

    @GetMapping("/cuocthis/{cuocThiId}/danhsachthis")
    public ResponseEntity<List<DanhSachThi>> getAllDanhSachThisByCuocThiId(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size, @PathVariable(value = "cuocThiId") String cuocThiId) {
        log.info("API GET /cuocthis/{cuocThiId}/danhsachthis");

        List<DanhSachThi> lstDanhSachThi = new ArrayList<>();

        if (!cuocThiRepository.existsById(cuocThiId)) {
            return new ResponseEntity<>(lstDanhSachThi, HttpStatus.OK);
        }

        Pageable paging = PageRequest.of(page - 1, size);

        List<DanhSachThi> danhSachThis = danhSachThiRepository.findByCuocThiId(cuocThiId, paging);
        lstDanhSachThi.addAll(danhSachThis);

        return new ResponseEntity<>(lstDanhSachThi, HttpStatus.OK);
    }

    @GetMapping("/danhsachthis/{danhSachId}")
    public ResponseEntity<DanhSachThi> getDanhSachThiById(@PathVariable(value = "danhSachId", required = true) long danhSachId)
        throws ResourceNotFoundException {
        log.info("API GET /danhsachthis/{danhSachId}");

        DanhSachThi danhSachThi = danhSachThiRepository.findById(danhSachId)
          .orElseThrow(() -> new ResourceNotFoundException("DanhSachThi not found for this id::" + danhSachId));
        return ResponseEntity.ok().body(danhSachThi);
    }

    private void validateLimited(DanhSachThi danhSachThi, KhoiThi khoiThi) throws Exception {
        if (khoiThi.getThiTapThe()) {
            List<DoiThi> lstDoiThis = doiThiRepository.findByCuocThiIdAndKhoiThiId(danhSachThi.getCuocThi().getId(), khoiThi.getId());

            int maxDoi = khoiThi.getMaxDangKi();
            if (maxDoi <= lstDoiThis.size()) {
                throw new ValidationException("Number of DoiThi limited!");
            }
        }
        else if (khoiThi.getThiSangTao()) {

        }
        else {
            //int maxThiSinh = khoiThi.getMaxThiSinh();
        	//update by trungnt
        	int maxThiSinh = khoiThi.getMaxDangKi();
            List<DanhSachThi> lstDst = danhSachThiRepository.findByCuocThiIdAndKhoiThiId(danhSachThi.getCuocThi().getId(), danhSachThi.getKhoiThiId());
            if (maxThiSinh <= lstDst.size()) {
                throw new ValidationException("Number of ThiSinh limited!");
            }
        }
    }

    private void validateRelationConstraint(DanhSachThi danhSachThi) throws Exception {
        Optional<ThiSinh> thiSinh = thiSinhRepository.findById(danhSachThi.getThiSinhId());
        if (!thiSinh.isPresent()) {
            throw new ValidationException("ThiSinh is not exists!");
        }
        if (danhSachThi.getKhoiThiId() != null) {
            Optional<KhoiThi> khoiThi = khoiThiRepository.findById(danhSachThi.getKhoiThiId());
            if (!khoiThi.isPresent()) {
                throw new ValidationException("KhoiThi is not exists!");
            }
            else {
            	//block by trungnt. 
                //validateLimited(danhSachThi, khoiThi.get());
            }
        }
        if (danhSachThi.getDoiThiId() != null) {
            Optional<DoiThi> doiThi = doiThiRepository.findById(danhSachThi.getDoiThiId());
            if (!doiThi.isPresent()) {
                throw new ValidationException("DoiThi is not exists!");
            }
        }
    }

    @PostMapping("/cuocthis/{cuocThiId}/danhsachthis")
    public ResponseEntity<DanhSachThi> createDanhSachThiOfCuocThi(@PathVariable(value = "cuocThiId") String cuocThiId, @Valid @RequestBody DanhSachThi danhSachThi, @RequestHeader("id") String toChucId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API POST /cuocthis/{cuocThiId}/danhsachthis");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros) && !vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        if (vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros) && !vaiTroChecker.canAccessDanhSachThi(toChucId, danhSachThi)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        try {
            validateRelationConstraint(danhSachThi);
            DanhSachThiValidator.getInstance().validate(danhSachThi);

            /*
            DanhSachThi danhSachThiMoi = cuocThiRepository.findById(cuocThiId).map(cuocThi -> {
                danhSachThi.setCuocThi(cuocThi);
                Optional<ThiSinh> thiSinhOpt = thiSinhRepository.findById(danhSachThi.getThiSinhId());
                
                if(thiSinhOpt.isPresent()) {
                	danhSachThi.setDoanThiId(thiSinhOpt.get().getDoanThiId());
                }
                
                return danhSachThiRepository.save(danhSachThi);
            }).orElseThrow(() -> new ResourceNotFoundException("Not found CuocThi with id = " + cuocThiId));

            return new ResponseEntity<>(danhSachThiMoi, HttpStatus.CREATED);
            */
            
            Optional<CuocThi> cuocThiOpt = cuocThiRepository.findById(cuocThiId);
            
            if(!cuocThiOpt.isPresent()) {
            	throw new ResourceNotFoundException("Not found CuocThi with id = " + cuocThiId);
            }
            
            danhSachThi.setCuocThi(cuocThiOpt.get());
            
            
            Optional<ThiSinh> thiSinhOpt = thiSinhRepository.findById(danhSachThi.getThiSinhId());
            
            if(thiSinhOpt.isPresent()) {
            	danhSachThi.setDoanThiId(thiSinhOpt.get().getDoanThiId());
            }
            
            danhSachThi =  danhSachThiRepository.save(danhSachThi);
            
            return new ResponseEntity<>(danhSachThi, HttpStatus.CREATED);
            
        } catch (Exception e) {
            log.debug("API POST /cuocthis/{cuocThiId}/danhsachthis", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }   
    }

    // @PostMapping("/danhsachthis")
    // public ResponseEntity<DanhSachThi> createDanhSachThi(@RequestBody DanhSachThi danhSachThi) {
    //     try {
    //         DanhSachThi _danhSachThi = danhSachThiRepository.save(new DanhSachThi(
    //             danhSachThi.getThiSinhId(),
    //             danhSachThi.getCuocThi(),
    //             danhSachThi.getKhoiThiId(),
    //             danhSachThi.getDoiThiId(),
    //             danhSachThi.getChuDeSangTao(),
    //             danhSachThi.getSoBaoDanh(),
    //             danhSachThi.getKetQuaSoLoai(),
    //             danhSachThi.getThuTuXepHang(),
    //             danhSachThi.getBangDiemThi(),
    //             danhSachThi.getHangGiaiThuong()    
    //         ));
    //         return new ResponseEntity<>(_danhSachThi, HttpStatus.CREATED);
    //     }
    //     catch (Exception e) {
    //         return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    @PutMapping("/danhsachthis/{id}")
    public ResponseEntity<DanhSachThi> updateDanhSachThi(@PathVariable("id") long id, @RequestBody DanhSachThi danhSachThi, @RequestHeader("id") String toChucId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API PUT /danhsachthis/{id}");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros) && !vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        if (vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros) && !vaiTroChecker.canAccessDanhSachThi(toChucId, id)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        Optional<DanhSachThi> danhSachThiData = danhSachThiRepository.findById(id);
        if (danhSachThiData.isPresent()) {
            try {
                validateRelationConstraint(danhSachThi);
                DanhSachThiValidator.getInstance().validate(danhSachThi);

                DanhSachThi _danhSachThi = danhSachThiData.get();
                if (danhSachThi.getThiSinhId() != null) {
                    _danhSachThi.setThiSinhId(danhSachThi.getThiSinhId());
                }
                if (danhSachThi.getKhoiThiId() != null) {
                    _danhSachThi.setKhoiThiId(danhSachThi.getKhoiThiId());
                }
                if (danhSachThi.getDoiThiId() != null) {
                    _danhSachThi.setDoiThiId(danhSachThi.getDoiThiId());
                }
                if (danhSachThi.getChuDeSangTao() != null) {
                    _danhSachThi.setChuDeSangTao(danhSachThi.getChuDeSangTao());
                }
                if (danhSachThi.getSoBaoDanh() != null) {
                    _danhSachThi.setSoBaoDanh(danhSachThi.getSoBaoDanh());
                }
                if (danhSachThi.getKetQuaSoLoai() != null) {
                    _danhSachThi.setKetQuaSoLoai(danhSachThi.getKetQuaSoLoai());
                }
                if (danhSachThi.getThuTuXepHang() != null) {
                    _danhSachThi.setThuTuXepHang(danhSachThi.getThuTuXepHang());
                }
                if (danhSachThi.getBangDiemThi() != null) {
                    _danhSachThi.setBangDiemThi(danhSachThi.getBangDiemThi());
                }
                if (danhSachThi.getHangGiaiThuong() != null) {
                    _danhSachThi.setHangGiaiThuong(danhSachThi.getHangGiaiThuong());
                }
                
                Optional<ThiSinh> thiSinhOpt = thiSinhRepository.findById(_danhSachThi.getThiSinhId());
                
                if(thiSinhOpt.isPresent()) {
                	_danhSachThi.setDoanThiId(thiSinhOpt.get().getDoanThiId());
                }
                
                
                _danhSachThi.setThoiGianCapNhat(System.currentTimeMillis());
                
                return new ResponseEntity<>(danhSachThiRepository.save(_danhSachThi), HttpStatus.OK);
            }
            catch (Exception e) {
                log.debug("API PUT /danhsachthis/{id}", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/danhsachthis/{id}")
    public ResponseEntity<HttpStatus> deleteDanhSachThi(@PathVariable("id") long id, @RequestHeader("id") String toChucId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API DELETE /danhsachthis/{id}");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros) && !vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        if (vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros) && !vaiTroChecker.canAccessDanhSachThi(toChucId, id)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        try {
            danhSachThiRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            log.debug("API DELETE /danhsachthis/{id}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/cuocthis/{cuocThiId}/danhsachthis")
    public ResponseEntity<List<DanhSachThi>> deleteAllDanhSachThisOfCuocThi(@PathVariable(value = "cuocThiId") String cuocThiId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API DELETE /cuocthis/{cuocThiId}/danhsachthis");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        if (!cuocThiRepository.existsById(cuocThiId)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        danhSachThiRepository.deleteByCuocThiId(cuocThiId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/danhsachthis/import")
    public ResponseEntity<ImportResponse> importDanhSachThi(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileType") String fileType, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API POST /danhsachthis/import");
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

            for (CuocThi ct : cuocthis) {
                fileService.importHuanLuyenVien(multipartFile, tochucs, khoithis, doanthis, ct);
            }

            fileService.importDanhSachThi(multipartFile, cuocthis, khoithis, doanthis);

            message = "Import DanhSachThi successfully: " + multipartFile.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ImportResponse(fileName, size, message));
        }

        message = "Please import an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImportResponse(fileName, size, message));
    }
}
