package vn.iodev.contestmanagementsystem.controller;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
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
import vn.iodev.contestmanagementsystem.exception.ResourceNotFoundException;
import vn.iodev.contestmanagementsystem.helper.ExcelHelper;
import vn.iodev.contestmanagementsystem.model.DoanThi;
import vn.iodev.contestmanagementsystem.model.ImportResponse;
import vn.iodev.contestmanagementsystem.repository.DoanThiRepository;
import vn.iodev.contestmanagementsystem.security.VaiTroChecker;
import vn.iodev.contestmanagementsystem.service.ExcelService;
import vn.iodev.contestmanagementsystem.service.ToChucService;
import vn.iodev.contestmanagementsystem.validator.DoanThiValidator;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Slf4j
public class DoanThiController {
    @Autowired
    DoanThiRepository doanThiRepository;

    @Autowired
    ExcelService fileService;

    @Autowired
    ToChucService toChucService;

    @Autowired
    VaiTroChecker vaiTroChecker;

    @GetMapping("/doanthis")
    public List<DoanThi> getAllDoanThis(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size, @RequestParam(required = false) String toChucId, @RequestParam(required = false) String cuocThiId) {
        log.info("API GET /doanthis");
        Pageable paging = PageRequest.of(page - 1, size);
        if (toChucId == null && cuocThiId != null) {
            return doanThiRepository.findByCuocThiId(cuocThiId, paging);
        }
        else if (toChucId != null && cuocThiId == null) {
            return doanThiRepository.findByToChucId(toChucId, paging);
        }
        else if (toChucId != null && cuocThiId != null) {
            return doanThiRepository.findByToChucIdAndCuocThiId(toChucId, cuocThiId, paging);
        }
        else {
            Page<DoanThi> doanThis;
            doanThis = doanThiRepository.findAll(paging);
            return doanThis.getContent();
        }
    }

    @GetMapping("/doanthis/{id}")
    public ResponseEntity<DoanThi> getDoanThiById(@PathVariable(value = "id") String doanThiId)
        throws ResourceNotFoundException {
        log.info("API GET /doanthis/{id}");

        DoanThi doanThi = doanThiRepository.findById(doanThiId)
          .orElseThrow(() -> new ResourceNotFoundException("DoanThi not found for this id :: " + doanThiId));
        return ResponseEntity.ok().body(doanThi);
    }

    @PostMapping("/doanthis")
    public ResponseEntity<DoanThi> createDoanThi(@RequestBody DoanThi doanThi, @RequestHeader("id") String toChucId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API POST /doanthis");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros) && !vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            DoanThiValidator.getInstance().validate(doanThi);
            if (doanThi.getToChucId() != null && doanThi.getToChucId().equals(toChucId)) {
                toChucService.getToChucById(doanThi.getToChucId());
            }
            else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            DoanThi _doanThi = doanThiRepository.save(new DoanThi(doanThi.getTenGoi(), doanThi.getTiengAnh(), doanThi.getDiaChiHoatDong(), doanThi.getEmail(), doanThi.getToChucId(), doanThi.getCuocThiId()));
            return new ResponseEntity<>(_doanThi, HttpStatus.CREATED);
        }
        catch (Exception e) {
            log.debug("API POST /doanthis", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/doanthis/import")
    public ResponseEntity<ImportResponse> importDoanThi(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileType") String fileType, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API POST /doanthis/import");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();
        String message = "";
        if (ExcelHelper.hasExcelFormat(multipartFile)) {
            try {
                fileService.importDoanThi(multipartFile);
                message = "Import DoanThi successfully: " + multipartFile.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ImportResponse(fileName, size, message));
            }
            catch (Exception e) {
                log.debug("API POST /doanthis/import", e);
                message = "Cound not import DoanThi: " + multipartFile.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ImportResponse(fileName, size, message));
            }
        }

        message = "Please import an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImportResponse(fileName, size, message));
    }

    @PutMapping("/doanthis/{id}")
    public ResponseEntity<DoanThi> updateDoanThi(@PathVariable("id") String id, @RequestBody DoanThi doanThi, @RequestHeader("id") String toChucId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API PUT /doanthis/{id}");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros) && !vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<DoanThi> doanThiData = doanThiRepository.findById(id);
        if (id != null && !id.equals(toChucId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (doanThiData.isPresent()) {
            try {
                DoanThiValidator.getInstance().validate(doanThi);
                DoanThi _doanThi = doanThiData.get();
                if (doanThi.getTenGoi() != null) {
                    _doanThi.setTenGoi(doanThi.getTenGoi());
                }
                if (doanThi.getTiengAnh() != null) {
                    _doanThi.setTiengAnh(doanThi.getTiengAnh());
                }
                if (doanThi.getDiaChiHoatDong() != null) {
                    _doanThi.setDiaChiHoatDong(doanThi.getDiaChiHoatDong());;
                }
                if (doanThi.getEmail() != null) {
                    _doanThi.setEmail(doanThi.getEmail());;
                }
                if (doanThi.getToChucId() != null) {
                    toChucService.getToChucById(doanThi.getToChucId());
                    _doanThi.setToChucId(doanThi.getToChucId());
                }
                if (doanThi.getCuocThiId() != null) {
                    _doanThi.setCuocThiId(doanThi.getCuocThiId());
                }
                if (doanThi.getThuTuXepHang() != null) {
                    _doanThi.setThuTuXepHang(doanThi.getThuTuXepHang());
                }
                _doanThi.setThoiGianCapNhat(System.currentTimeMillis());
                
                return new ResponseEntity<>(doanThiRepository.save(_doanThi), HttpStatus.OK);
            }
            catch (Exception e) {
                log.debug("API PUT /doanthis/{id}", e);
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/doanthis/{id}")
    public ResponseEntity<HttpStatus> deleteDoanThi(@PathVariable("id") String id, @RequestHeader("id") String toChucId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API DELETE /doanthis/{id}");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros) && !vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (id != null && !id.equals(toChucId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        try {
            doanThiRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            log.debug("API DELETE /doanthis/{id}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
	@PostMapping("/doanthis/{doanThiId}/export")
	public ResponseEntity<?> exportDoanThi(@PathVariable("doanThiId") String doanThiId) {
		log.info("API POST /doanthis/{doanThiId}/export");

		File file = fileService.exportDanhSachDangKyMau1(doanThiId);

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
}
