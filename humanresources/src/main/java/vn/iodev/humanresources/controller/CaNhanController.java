package vn.iodev.humanresources.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import vn.iodev.humanresources.config.HumanResourcesConfiguration;
import vn.iodev.humanresources.exception.ResourceNotFoundException;
import vn.iodev.humanresources.helper.ExcelHelper;
import vn.iodev.humanresources.model.CaNhan;
import vn.iodev.humanresources.model.ImportResponse;
import vn.iodev.humanresources.repository.CaNhanRepository;
import vn.iodev.humanresources.service.ExcelService;
import vn.iodev.humanresources.utils.RandomUtil;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CaNhanController {
    @Autowired
    CaNhanRepository caNhanRepository;

    @Autowired
    ExcelService fileService;

    @Autowired
    HumanResourcesConfiguration configuration;

    @GetMapping("/canhans")
    public List<CaNhan> getAllCaNhans(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<CaNhan> caNhans;
        caNhans = caNhanRepository.findAll(paging);
        return caNhans.getContent();
    }

    @GetMapping("/canhans/{id}")
    public ResponseEntity<CaNhan> getCaNhanById(@PathVariable(value = "id") String caNhanId)
        throws ResourceNotFoundException {
        CaNhan caNhan = caNhanRepository.findById(caNhanId)
          .orElseThrow(() -> new ResourceNotFoundException("CaNhan not found for this id :: " + caNhanId));
        return ResponseEntity.ok().body(caNhan);
    }

    @GetMapping("/canhans/email/{email}")
    public ResponseEntity<CaNhan> getCaNhanByEmail(@PathVariable(value = "email") String email)
        throws ResourceNotFoundException {
        Optional<CaNhan> caNhanData = caNhanRepository.findByEmail(email);
        if (caNhanData.isPresent()) {
            return ResponseEntity.ok().body(caNhanData.get());
        }
        else {
            throw new ResourceNotFoundException("CaNhan not found for this email :: " + email);
        }
    }

    @PostMapping("/canhans")
    public ResponseEntity<CaNhan> createCaNhan(@Valid @RequestBody CaNhan caNhan) {
        try {
            Optional<CaNhan> _caNhanCuData = caNhanRepository.findByEmail(caNhan.getEmail());
            if (_caNhanCuData.isPresent()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            CaNhan pCaNhan = new CaNhan(caNhan.getHoTen(), caNhan.getGioiTinh(), caNhan.getNgaySinh(), caNhan.getEmail(), caNhan.getSoDienThoai(), caNhan.getLinkedIn(), caNhan.getGithub(), caNhan.getGoogle());
            pCaNhan.setId(RandomUtil.generateRandomEID(pCaNhan.getNgaySinh(), pCaNhan.getGioiTinh(), configuration.getCaNhanIdLength()));
            int nOfIdGeneratedRetry = configuration.getnOfIdGeneratedRetry();
            int i = 0;
            while (i < nOfIdGeneratedRetry) {
                _caNhanCuData = caNhanRepository.findById(pCaNhan.getId());
                if (!_caNhanCuData.isPresent()) {
                    break;
                }
                else {
                    pCaNhan.setId(RandomUtil.generateRandomEID(pCaNhan.getNgaySinh(), pCaNhan.getGioiTinh(), configuration.getCaNhanIdLength()));
                }
                i++;
            }
            CaNhan _caNhan = caNhanRepository.save(pCaNhan);
            return new ResponseEntity<>(_caNhan, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/canhans/import")
    public ResponseEntity<ImportResponse> importCaNhan(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileType") String fileType) {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();
        String message = "";
        if (ExcelHelper.hasExcelFormat(multipartFile)) {
            try {
                fileService.importCaNhan(multipartFile);
                message = "Import CaNhan successfully: " + multipartFile.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ImportResponse(fileName, size, message));
            }
            catch (Exception e) {
                e.printStackTrace();
                message = "Cound not import CaNhan: " + multipartFile.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ImportResponse(fileName, size, message));
            }
        }

        message = "Please import an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImportResponse(fileName, size, message));
    }

    @PutMapping("/canhans/{id}")
    public ResponseEntity<CaNhan> updateCaNhan(@PathVariable("id") String id, @Valid @RequestBody CaNhan caNhan) {
        Optional<CaNhan> caNhanData = caNhanRepository.findById(id);
        if (caNhanData.isPresent()) {
            CaNhan _caNhan = caNhanData.get();
            if (caNhan.getHoTen() != null) {
                _caNhan.setHoTen(caNhan.getHoTen());;
            }
            if (caNhan.getSoDienThoai() != null) {
                _caNhan.setSoDienThoai(caNhan.getSoDienThoai());;
            }
            if (caNhan.getEmail() != null) {
                _caNhan.setEmail(caNhan.getEmail());;
            }
            if (caNhan.getLinkedIn() != null) {
                _caNhan.setLinkedIn(caNhan.getLinkedIn());;
            }
            if (caNhan.getGithub() != null) {
                _caNhan.setGithub(caNhan.getGithub());
            }
            if (caNhan.getGoogle() != null) {
                _caNhan.setGoogle(caNhan.getGoogle());;
            }
            
            _caNhan.setThoiGianCapNhat(System.currentTimeMillis());
            
            return new ResponseEntity<>(caNhanRepository.save(_caNhan), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/canhans/{id}/avatar")
    public ResponseEntity<CaNhan> updateCaNhanAvatar(@PathVariable("id") String id, @RequestParam("avatar") MultipartFile avatarFile) {
        Optional<CaNhan> caNhanData = caNhanRepository.findById(id);
        String fileName = StringUtils.cleanPath(avatarFile.getOriginalFilename());

        if (caNhanData.isPresent()) {
            CaNhan caNhan = caNhanData.get();
            try {
                caNhan.setAvatar(avatarFile.getBytes());
                caNhan.setAvatarFileName(fileName);

                CaNhan saveCaNhan = caNhanRepository.save(caNhan);
                return new ResponseEntity<>(saveCaNhan, HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/files/canhans/{id}/avatar", produces = {
        MediaType.IMAGE_JPEG_VALUE,
        MediaType.IMAGE_PNG_VALUE,
        MediaType.IMAGE_GIF_VALUE
    })
    public ResponseEntity<byte[]> getCaNhanAvatar(@PathVariable String id) {
        if (!caNhanRepository.existsById(id)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<CaNhan> caNhanData = caNhanRepository.findById(id);
        if (caNhanData.isPresent()) {
            CaNhan caNhan = caNhanData.get();
            return ResponseEntity.ok()
                // .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + toChuc.getLogoFileName() + "\"")
                .body(caNhan.getAvatar());
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/canhans/{id}")
    public ResponseEntity<HttpStatus> deleteCaNhan(@PathVariable("id") String id) {
        try {
            caNhanRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/canhans/{id}/kichhoat")
    public ResponseEntity<HttpStatus> activeCaNhan(@PathVariable("id") String id) {
        try {
            Optional<CaNhan> _caNhanData = caNhanRepository.findById(id);
            if (_caNhanData.isPresent()) {
                CaNhan _caNhan = _caNhanData.get();
                _caNhan.setTinhTrang(1);
                caNhanRepository.save(_caNhan);
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
