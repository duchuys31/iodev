package vn.iodev.contestmanagementsystem.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.contestmanagementsystem.exception.ResourceNotFoundException;
import vn.iodev.contestmanagementsystem.model.KhoiThi;
import vn.iodev.contestmanagementsystem.repository.CuocThiRepository;
import vn.iodev.contestmanagementsystem.repository.KhoiThiRepository;
import vn.iodev.contestmanagementsystem.security.VaiTroChecker;
import vn.iodev.contestmanagementsystem.validator.KhoiThiValidator;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Slf4j
public class KhoiThiController {
    @Autowired
    KhoiThiRepository khoiThiRepository;

    @Autowired
    CuocThiRepository cuocThiRepository;

    @Autowired
    VaiTroChecker vaiTroChecker;

    @GetMapping("/cuocthis/{cuocThiId}/khoithis")
    public ResponseEntity<List<KhoiThi>> getAllKhoiThisByCuocThiId(@PathVariable(value = "cuocThiId") String cuocThiId) {
        log.info("API /cuocthis/{cuocThiId}/khoithis");
        List<KhoiThi> lstKhoiThi = new ArrayList<>();

        if (!cuocThiRepository.existsById(cuocThiId)) {
            return new ResponseEntity<>(lstKhoiThi, HttpStatus.OK);
        }
        List<KhoiThi> khoiThis = khoiThiRepository.findByCuocThiId(cuocThiId);
        lstKhoiThi.addAll(khoiThis);

        return new ResponseEntity<>(lstKhoiThi, HttpStatus.OK);
    }
        
    @PostMapping("/cuocthis/{cuocThiId}/khoithis")
    public ResponseEntity<KhoiThi> createKhoiThiOfCuocThi(@PathVariable(value = "cuocThiId") String cuocThiId, @Valid @RequestBody KhoiThi khoiThi, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API POST /cuocthis/{cuocThiId}/khoithis");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        try {
            KhoiThiValidator.getInstance().validate(khoiThi);
            KhoiThi khoiThiMoi = cuocThiRepository.findById(cuocThiId).map(cuocThi -> {
                khoiThi.setCuocThi(cuocThi);
                return khoiThiRepository.save(khoiThi);
            }).orElseThrow(() -> new ResourceNotFoundException("Not found CuocThi with id = " + cuocThiId));

            return new ResponseEntity<>(khoiThiMoi, HttpStatus.CREATED);
        } catch (Exception e) {
            log.debug("API POST /cuocthis/{cuocThiId}/khoithis", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }   
    }

    @GetMapping("/khoithis")
    public List<KhoiThi> getAllKhoiThis(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size, @RequestParam(required = false) String cuocThiId) {
        log.info("API GET /khoithis");
        Pageable paging = PageRequest.of(page - 1, size);
        if (cuocThiId == null) {
            Page<KhoiThi> khoiThis;
            khoiThis = khoiThiRepository.findAll(paging);
            return khoiThis.getContent();
        }
        else {
            return khoiThiRepository.findByCuocThiId(cuocThiId, paging);
        }
    }

    @GetMapping("/khoithis/{id}")
    public ResponseEntity<KhoiThi> getKhoiThiById(@PathVariable(value = "id") String khoiThiId)
        throws ResourceNotFoundException {
        log.info("API GET /khoithis/{id}");
        KhoiThi khoiThi = khoiThiRepository.findById(khoiThiId)
          .orElseThrow(() -> new ResourceNotFoundException("KhoiThi not found for this id :: " + khoiThiId));
        return ResponseEntity.ok().body(khoiThi);
    }

    // @PostMapping("/khoithis")
    // public ResponseEntity<KhoiThi> createKhoiThi(@Valid @RequestBody KhoiThi khoiThi, @RequestHeader("vaiTros") String vaiTros) {
    //     if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
    //         return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    //     }
    //     try {
    //         KhoiThi _khoiThi = khoiThiRepository.save(new KhoiThi(khoiThi.getTenGoi(), khoiThi.getCuocThi(), khoiThi.getNoiDungThi(), khoiThi.getVongSoLoai(), khoiThi.getThiTapThe(), khoiThi.getThiSangTao(), khoiThi.getMaxDangKi(), khoiThi.getMaxThiSinh()));
    //         return new ResponseEntity<>(_khoiThi, HttpStatus.CREATED);
    //     }
    //     catch (Exception e) {
    //         return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    @PutMapping("/khoithis/{id}")
    public ResponseEntity<KhoiThi> updateKhoiThi(@PathVariable("id") String id, @Valid @RequestBody KhoiThi khoiThi, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API PUT /khoithis/{id}");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Optional<KhoiThi> khoiThiData = khoiThiRepository.findById(id);
        if (khoiThiData.isPresent()) {
            try {
                KhoiThiValidator.getInstance().validate(khoiThi);
                KhoiThi _khoiThi = khoiThiData.get();
                if (khoiThi.getTenGoi() != null) {
                    _khoiThi.setTenGoi(khoiThi.getTenGoi());
                }
                if (khoiThi.getNoiDungThi() != null) {
                    _khoiThi.setNoiDungThi(khoiThi.getNoiDungThi());
                }
                if (khoiThi.getVongSoLoai() != null) {
                    _khoiThi.setVongSoLoai(khoiThi.getVongSoLoai());
                }
                if (khoiThi.getThiTapThe() != null) {
                    _khoiThi.setThiTapThe(khoiThi.getThiTapThe());
                }
                if (khoiThi.getThiSangTao() != null) {
                    _khoiThi.setThiSangTao(khoiThi.getThiSangTao());
                }
                if (khoiThi.getMaxDangKi() != null) {
                    _khoiThi.setMaxDangKi(khoiThi.getMaxDangKi());
                }
                if (khoiThi.getMaxThiSinh() != null) {
                    _khoiThi.setMaxThiSinh(khoiThi.getMaxThiSinh());
                }
                if (khoiThi.getTinhTrang() != null) {
                    _khoiThi.setTinhTrang(khoiThi.getTinhTrang());
                }
                _khoiThi.setThoiGianCapNhat(System.currentTimeMillis());
                
                return new ResponseEntity<>(khoiThiRepository.save(_khoiThi), HttpStatus.OK);
            }
            catch (Exception e) {
                log.info("API PUT /khoithis/{id}");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/khoithis/{id}")
    public ResponseEntity<HttpStatus> deleteKhoiThi(@PathVariable("id") String id, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API DELETE /khoithis/{id}");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<HttpStatus>(HttpStatus.FORBIDDEN);
        }
        try {
            khoiThiRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            log.debug("API DELETE /khoithis/{id}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/cuocthis/{cuocThiId}/khoithis")
    public ResponseEntity<List<KhoiThi>> deleteAllKhoiThisOfCuocThi(@PathVariable(value = "cuocThiId") String cuocThiId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API DELETE /cuocthis/{cuocThiId}/khoithis");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!cuocThiRepository.existsById(cuocThiId)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        khoiThiRepository.deleteByCuocThiId(cuocThiId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
