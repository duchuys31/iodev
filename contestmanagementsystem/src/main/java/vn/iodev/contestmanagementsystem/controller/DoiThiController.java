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
import org.springframework.web.bind.annotation.*;

import com.netflix.config.validation.ValidationException;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.contestmanagementsystem.exception.ResourceNotFoundException;
import vn.iodev.contestmanagementsystem.model.CuocThi;
import vn.iodev.contestmanagementsystem.model.DoanThi;
import vn.iodev.contestmanagementsystem.model.DoiThi;
import vn.iodev.contestmanagementsystem.model.HuanLuyenVien;
import vn.iodev.contestmanagementsystem.model.KhoiThi;
import vn.iodev.contestmanagementsystem.repository.CuocThiRepository;
import vn.iodev.contestmanagementsystem.repository.DoanThiRepository;
import vn.iodev.contestmanagementsystem.repository.DoiThiRepository;
import vn.iodev.contestmanagementsystem.repository.HuanLuyenVienRepository;
import vn.iodev.contestmanagementsystem.repository.KhoiThiRepository;
import vn.iodev.contestmanagementsystem.security.VaiTroChecker;
import vn.iodev.contestmanagementsystem.validator.DoiThiValidator;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Slf4j
public class DoiThiController {
    @Autowired
    DoiThiRepository doiThiRepository;

    @Autowired
    CuocThiRepository cuocThiRepository;

    @Autowired
    DoanThiRepository doanThiRepository;

    @Autowired
    KhoiThiRepository khoiThiRepository;

    @Autowired
    HuanLuyenVienRepository huanLuyenVienRepository;

    @Autowired
    VaiTroChecker vaiTroChecker;

    @GetMapping("/doithis")
    public List<DoiThi> getAllDoiThis(@RequestParam(defaultValue = "1") int page,@RequestParam(name = "tuKhoa", defaultValue="") String tuKhoa, @RequestParam(defaultValue = "15") int size, @RequestParam(required = false) String cuocThiId, @RequestParam(required = false) String khoiThiId, @RequestParam(required = false) String doanThiId) {
        log.info("API GET /doithis");
        
        Pageable paging = PageRequest.of(page - 1, size);
        Optional<CuocThi> cuocThiData = cuocThiRepository.findById(cuocThiId);
        return doiThiRepository.findDoiThiByMultipleConditions(tuKhoa, cuocThiData.get(), khoiThiId, doanThiId, paging);
    }

    @GetMapping("/cuocthis/{cuocThiId}/doithis")
    public ResponseEntity<List<DoiThi>> getAllDoiThisByCuocThiId(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size, @PathVariable(value = "cuocThiId") String cuocThiId) {
        log.info("API GET /cuocthis/{cuocThiId}/doithis");

        List<DoiThi> lstDoiThi = new ArrayList<>();

        if (!cuocThiRepository.existsById(cuocThiId)) {
            return new ResponseEntity<>(lstDoiThi, HttpStatus.OK);
        }

        Pageable paging = PageRequest.of(page - 1, size);

        List<DoiThi> doiThis = doiThiRepository.findByCuocThiId(cuocThiId, paging);
        lstDoiThi.addAll(doiThis);

        return new ResponseEntity<>(lstDoiThi, HttpStatus.OK);
    }

    @GetMapping("/doithis/{id}")
    public ResponseEntity<DoiThi> getDoiThiById(@PathVariable(value = "id") String doiThiId)
        throws ResourceNotFoundException {
        log.info("API GET /doithis/{id}");

        DoiThi doiThi = doiThiRepository.findById(doiThiId)
          .orElseThrow(() -> new ResourceNotFoundException("DoiThi not found for this id :: " + doiThiId));
        return ResponseEntity.ok().body(doiThi);
    }

    private void validateRelationConstraint(DoiThi doiThi) throws Exception {
        if (doiThi.getDoanThiId() != null) {
            Optional<DoanThi> doanThi = doanThiRepository.findById(doiThi.getDoanThiId());
            if (!doanThi.isPresent()) {
                throw new ValidationException("DoanThi is not exists!");
            }
        }
        if (doiThi.getKhoiThiId() != null) {
            Optional<KhoiThi> khoiThi = khoiThiRepository.findById(doiThi.getKhoiThiId());
            if (!khoiThi.isPresent()) {
                throw new ValidationException("KhoiThi is not exists!");
            }
        }
        if (doiThi.getHuanLuyenVienId() != null) {
            Optional<HuanLuyenVien> hlv = huanLuyenVienRepository.findById(doiThi.getHuanLuyenVienId());
            if (!hlv.isPresent()) {
                throw new ValidationException("HuanLuyenVien is not exists!");
            }
        }
    }

    @PostMapping("/cuocthis/{cuocThiId}/doithis")
    public ResponseEntity<DoiThi> createThiSinhOfCuocThi(@PathVariable(value = "cuocThiId") String cuocThiId, @Valid @RequestBody DoiThi doiThi, @RequestHeader("id") String toChucId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API POST /cuocthis/{cuocThiId}/doithis");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros) && !vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros) && !vaiTroChecker.canAccessDoiThi(toChucId, doiThi)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            DoiThiValidator.getInstance().validate(doiThi);
            validateRelationConstraint(doiThi);
            DoiThi doiThiMoi = cuocThiRepository.findById(cuocThiId).map(cuocThi -> {
                doiThi.setCuocThi(cuocThi);
                return doiThiRepository.save(doiThi);
            }).orElseThrow(() -> new ResourceNotFoundException("Not found CuocThi with id = " + cuocThiId));

            return new ResponseEntity<>(doiThiMoi, HttpStatus.CREATED);
        } catch (Exception e) {
            log.debug("API POST /cuocthis/{cuocThiId}/doithis", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }   
    }

    // @PostMapping("/doithis")
    // public ResponseEntity<DoiThi> createDoiThi(@RequestBody DoiThi doiThi) {
    //     try {
    //         DoiThi _doiThi = doiThiRepository.save(new DoiThi(doiThi.getTenGoi(), doiThi.getCuocThi(), doiThi.getDoanThiId(), doiThi.getKhoiThiId(), doiThi.getChuDeSangTao(), doiThi.getHuanLuyenVienId(), doiThi.getKetQuaSoLoai(), doiThi.getThuTuXepHang(), doiThi.getBangDiemThi(), doiThi.getHangGiaiThuong()));
    //         return new ResponseEntity<>(_doiThi, HttpStatus.CREATED);
    //     }
    //     catch (Exception e) {
    //         return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    @PutMapping("/doithis/{id}")
    public ResponseEntity<DoiThi> updateDoiThi(@PathVariable("id") String id, @RequestBody DoiThi doiThi, @RequestHeader("id") String toChucId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API PUT /doithis/{id}");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros) && !vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<DoiThi> doiThiData = doiThiRepository.findById(id);
        if (doiThiData.isPresent()) {
            if (vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros) && !vaiTroChecker.canAccessDoiThi(toChucId, doiThi)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
    
            try {
                DoiThiValidator.getInstance().validate(doiThi);
                validateRelationConstraint(doiThi);

                DoiThi _doiThi = doiThiData.get();
                if (doiThi.getTenGoi() != null) {
                    _doiThi.setTenGoi(doiThi.getTenGoi());
                }
                if (doiThi.getDoanThiId() != null) {
                    _doiThi.setDoanThiId(doiThi.getDoanThiId());
                }
                if (doiThi.getKhoiThiId() != null) {
                    _doiThi.setKhoiThiId(doiThi.getKhoiThiId());
                }
                if (doiThi.getChuDeSangTao() != null) {
                    _doiThi.setChuDeSangTao(doiThi.getChuDeSangTao());
                }
                if (doiThi.getHuanLuyenVienId() != null) {
                    _doiThi.setHuanLuyenVienId(doiThi.getHuanLuyenVienId());
                }
                if (doiThi.getKetQuaSoLoai() != null) {
                    _doiThi.setKetQuaSoLoai(doiThi.getKetQuaSoLoai());
                }
                if (doiThi.getThuTuXepHang() != null) {
                    _doiThi.setThuTuXepHang(doiThi.getThuTuXepHang());
                }
                if (doiThi.getBangDiemThi() != null) {
                    _doiThi.setBangDiemThi(doiThi.getBangDiemThi());
                }
                if (doiThi.getHangGiaiThuong() != null) {
                    _doiThi.setHangGiaiThuong(doiThi.getHangGiaiThuong());
                }
                _doiThi.setThoiGianCapNhat(System.currentTimeMillis());
                
                return new ResponseEntity<>(doiThiRepository.save(_doiThi), HttpStatus.OK);
            }
            catch (Exception e) {
                log.debug("API PUT /doithis/{id}", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/doithis/{id}")
    public ResponseEntity<HttpStatus> deleteDoiThi(@PathVariable("id") String id, @RequestHeader("id") String toChucId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API DELETE /doithis/{id}");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros) && !vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros) && !vaiTroChecker.canAccessDoiThi(toChucId, id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            doiThiRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            log.debug("API DELETE /doithis/{id}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/cuocthis/{cuocThiId}/doithis")
    public ResponseEntity<List<DoiThi>> deleteAllDoiThisOfCuocThi(@PathVariable(value = "cuocThiId") String cuocThiId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API DELETE /cuocthis/{cuocThiId}/doithis");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (!cuocThiRepository.existsById(cuocThiId)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        doiThiRepository.deleteByCuocThiId(cuocThiId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
