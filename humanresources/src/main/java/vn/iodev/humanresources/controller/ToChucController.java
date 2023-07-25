package vn.iodev.humanresources.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.humanresources.config.HumanResourcesConfiguration;
import vn.iodev.humanresources.exception.ResourceNotFoundException;
import vn.iodev.humanresources.helper.ExcelHelper;
import vn.iodev.humanresources.model.ToChuc;
import vn.iodev.humanresources.model.ImportResponse;
import vn.iodev.humanresources.repository.ToChucRepository;
import vn.iodev.humanresources.service.ExcelService;
import vn.iodev.humanresources.utils.RandomUtil;

import org.springframework.util.StringUtils;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Slf4j
public class ToChucController {
    @Autowired
    private ToChucRepository toChucRepository;

    @Autowired
    ExcelService fileService;

    @Autowired
    HumanResourcesConfiguration configuration;

    @GetMapping("/tochucs")
    public List<ToChuc> getAllToChucs(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size, @RequestParam(required = false, defaultValue = "") String tuKhoa, @RequestParam(required = false) String tenGoi, @RequestParam(required = false) String loaiToChuc, @RequestParam(required = false) String email, @RequestParam(required = false) Integer tinhTrang) {
        Pageable paging = PageRequest.of(page - 1, size);
        List<ToChuc> toChucs = new ArrayList<>();
        try {
            toChucs = toChucRepository.findToChucByMultipleConditions(tuKhoa, tenGoi, loaiToChuc, email, tinhTrang, paging);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return toChucs;
    }

    @GetMapping("/tochucs/{id}")
    public ResponseEntity<ToChuc> getToChucById(@PathVariable(value = "id") String toChucId)
        throws ResourceNotFoundException {
        ToChuc toChuc = toChucRepository.findById(toChucId)
          .orElseThrow(() -> new ResourceNotFoundException("ToChuc not found for this id :: " + toChucId));
        return ResponseEntity.ok().body(toChuc);
    }

    @GetMapping("/tochucs/email/{email}")
    public ResponseEntity<ToChuc> getToChucByEmail(@PathVariable(value = "email") String email)
        throws ResourceNotFoundException {
        Optional<ToChuc> toChucData = toChucRepository.findByEmail(email);
        if (toChucData.isPresent()) {
            return ResponseEntity.ok().body(toChucData.get());
        }
        else {
            throw new ResourceNotFoundException("ToChuc not found for this email :: " + email);
        }
    }

    @PostMapping("/tochucs")
    public ResponseEntity<ToChuc> createToChuc(@RequestBody ToChuc toChuc) {
        try {
            Optional<ToChuc> _toChucCuData = toChucRepository.findByEmail(toChuc.getEmail());
            if (_toChucCuData.isPresent()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            ToChuc pToChuc = new ToChuc(toChuc.getTenGoi(), toChuc.getTiengAnh(), toChuc.getTenVietTat(), toChuc.getLoaiToChuc(), toChuc.getDiaChiHoatDong(), toChuc.getViTriDiaLy(), toChuc.getEmail(), toChuc.getWeb());
            int nOfIdGeneratedRetry = configuration.getnOfIdGeneratedRetry();
            pToChuc.setId(RandomUtil.generateRandomNumeric(configuration.getToChucIdLength()));

            int i = 0;
            while (i < nOfIdGeneratedRetry) {
                _toChucCuData = toChucRepository.findById(pToChuc.getId());
                if (_toChucCuData.isPresent()) {
                    pToChuc.setId(RandomUtil.generateRandomNumeric(configuration.getToChucIdLength()));
                }
                else {
                    break;
                }
                i++;
            }
            ToChuc _toChuc = toChucRepository.save(pToChuc);
            return new ResponseEntity<>(_toChuc, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tochucs/import")
    public ResponseEntity<ImportResponse> importToChuc(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileType") String fileType) {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();
        String message = "";
        if (ExcelHelper.hasExcelFormat(multipartFile)) {
            try {
                fileService.importToChuc(multipartFile);
                message = "Import ToChuc successfully: " + multipartFile.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ImportResponse(fileName, size, message));
            }
            catch (Exception e) {
                e.printStackTrace();
                message = "Cound not import ToChuc: " + multipartFile.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ImportResponse(fileName, size, message));
            }
        }

        message = "Please import an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImportResponse(fileName, size, message));
    }

    @PutMapping("/tochucs/{id}")
    public ResponseEntity<ToChuc> updateToChuc(@PathVariable("id") String id, @RequestBody ToChuc toChuc) {
        Optional<ToChuc> toChucData = toChucRepository.findById(id);
        if (toChucData.isPresent()) {
            ToChuc _toChuc = toChucData.get();
            if (toChuc.getTenGoi() != null) {
                _toChuc.setTenGoi(toChuc.getTenGoi());
            }
            if (toChuc.getTiengAnh() != null) {
                _toChuc.setTiengAnh(toChuc.getTiengAnh());
            }
            if (toChuc.getTenVietTat() != null) {
                _toChuc.setTenVietTat(toChuc.getTenVietTat());
            }
            if (toChuc.getDiaChiHoatDong() != null) {
                _toChuc.setDiaChiHoatDong(toChuc.getDiaChiHoatDong());
            }
            if (toChuc.getLoaiToChuc() != null) {
                _toChuc.setLoaiToChuc(toChuc.getLoaiToChuc());
            }
            if (toChuc.getViTriDiaLy() != null) {
                _toChuc.setViTriDiaLy(toChuc.getViTriDiaLy());
            }
            if (toChuc.getEmail() != null) {
                _toChuc.setEmail(toChuc.getEmail());
            }
            if (toChuc.getWeb() != null) {
                _toChuc.setWeb(toChuc.getWeb());
            }
            if (toChuc.getLogo() != null) {
                _toChuc.setLogo(toChuc.getLogo());
            }
            _toChuc.setThoiGianCapNhat(System.currentTimeMillis());
            
            return new ResponseEntity<>(toChucRepository.save(_toChuc), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tochucs/{id}/logo")
    public ResponseEntity<ToChuc> updateToChucLogo(@PathVariable("id") String id, @RequestParam("logo") MultipartFile logoFile) {
        Optional<ToChuc> toChucData = toChucRepository.findById(id);
        String fileName = StringUtils.cleanPath(logoFile.getOriginalFilename());
        if (toChucData.isPresent()) {
            ToChuc toChuc = toChucData.get();
            try {
                toChuc.setLogo(logoFile.getBytes());
                toChuc.setLogoFileName(fileName);

                ToChuc saveToChuc = toChucRepository.save(toChuc);
                return new ResponseEntity<>(saveToChuc, HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/files/tochucs/{id}/logo", produces = {
        MediaType.IMAGE_JPEG_VALUE,
        MediaType.IMAGE_PNG_VALUE,
        MediaType.IMAGE_GIF_VALUE
    })
    public ResponseEntity<byte[]> getToChucLogo(@PathVariable String id) {
        if (!toChucRepository.existsById(id)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<ToChuc> toChucData = toChucRepository.findById(id);
        if (toChucData.isPresent()) {
            ToChuc toChuc = toChucData.get();
            return ResponseEntity.ok()
                // .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + toChuc.getLogoFileName() + "\"")
                .body(toChuc.getLogo());
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/tochucs/{id}")
    public ResponseEntity<HttpStatus> deleteToChuc(@PathVariable("id") String id) {
        try {
            toChucRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tochucs/{id}/kichhoat")
    public ResponseEntity<HttpStatus> activeToChuc(@PathVariable("id") String id) {
        try {
            Optional<ToChuc> _toChucData = toChucRepository.findById(id);
            if (_toChucData.isPresent()) {
                ToChuc _toChuc = _toChucData.get();
                _toChuc.setTinhTrang(1);
                toChucRepository.save(_toChuc);
                return new ResponseEntity<>(HttpStatus.OK);    
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
