package vn.iodev.contestmanagementsystem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.contestmanagementsystem.model.DanhMuc;
import vn.iodev.contestmanagementsystem.model.DanhMucId;
import vn.iodev.contestmanagementsystem.repository.DanhMucRepository;
import vn.iodev.contestmanagementsystem.security.VaiTroChecker;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Slf4j
public class DanhMucController {
    @Autowired
    DanhMucRepository danhMucRepository;

    @Autowired
    VaiTroChecker vaiTroChecker;

    @GetMapping("/danhmucs")
    public List<DanhMuc> getAllDanhMucs(@RequestParam(defaultValue = "1") int page, 
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String loaiDanhMuc, 
            @RequestParam(required = false) String maDanhMuc) {
        log.info("API GET /danhmucs");
        Pageable pageable = PageRequest.of(page - 1, size);
        List<DanhMuc> danhMucs;
        if (loaiDanhMuc != null) {
            danhMucs = danhMucRepository.findByLoaiDanhMuc(loaiDanhMuc, pageable);
        }
        else {
            danhMucs = danhMucRepository.findAll(pageable).getContent();
        }
        return danhMucs;
    }
    
    @GetMapping("/danhmucs/{loaiDanhMuc}/{maDanhMuc}")
    public ResponseEntity<DanhMuc> getDanhMuc(
        @PathVariable(value = "loaiDanhMuc") String loaiDanhMuc,
        @PathVariable(value = "maDanhMuc") String maDanhMuc,
        @RequestHeader("id") String id,
        @RequestHeader("email") String email,
        @RequestHeader("vaiTros") String vaiTros) {
        log.info("API GET /danhmucs/{loaiDanhMuc}/{maDanhMuc}");
            try {
            if (vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
                if (!danhMucRepository.existsById(new DanhMucId(loaiDanhMuc, maDanhMuc))) {
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }
                else {
                    Optional<DanhMuc> dmData = danhMucRepository.findById(new DanhMucId(loaiDanhMuc, maDanhMuc));
                    if (dmData.isPresent()) {                       
                        return new ResponseEntity<>(dmData.get(), HttpStatus.OK);
                    }
                    else {
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                    }
                }
            }
            else {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/danhmucs")
    public ResponseEntity<DanhMuc> createDanhMuc(
        @RequestBody DanhMuc danhMuc,
        @RequestHeader("id") String id,
        @RequestHeader("email") String email,
        @RequestHeader("vaiTros") String vaiTros) {
        log.info("API POST /danhmucs");
        try {
            if (vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
                DanhMuc _danhMuc = danhMucRepository.save(new DanhMuc(danhMuc.getLoaiDanhMuc(), danhMuc.getMaDanhMuc(), danhMuc.getGiaTri()));
                return new ResponseEntity<>(_danhMuc, HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/danhmucs/{loaiDanhMuc}/{maDanhMuc}")
    public ResponseEntity<DanhMuc> updateDanhMuc(
        @PathVariable(value = "loaiDanhMuc") String loaiDanhMuc,
        @PathVariable(value = "maDanhMuc") String maDanhMuc,
        @RequestBody DanhMuc danhMuc,
        @RequestHeader("id") String id,
        @RequestHeader("email") String email,
        @RequestHeader("vaiTros") String vaiTros) {
        log.info("API PUT /danhmucs/{loaiDanhMuc}/{maDanhMuc}");
        try {
            if (vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
                if (!danhMucRepository.existsById(new DanhMucId(loaiDanhMuc, maDanhMuc))) {
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }
                else {
                    Optional<DanhMuc> dmData = danhMucRepository.findById(new DanhMucId(loaiDanhMuc, maDanhMuc));
                    if (dmData.isPresent()) {
                        DanhMuc _danhMuc = dmData.get();
                        if (danhMuc.getGiaTri() != null) {
                            _danhMuc.setGiaTri(danhMuc.getGiaTri());
                        }
                        danhMucRepository.save(_danhMuc);
                        return new ResponseEntity<>(_danhMuc, HttpStatus.OK);
                    }
                    else {
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                    }
                }
            }
            else {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/danhmucs")
    public ResponseEntity<DanhMuc> updateDanhMuc(
        @RequestBody DanhMuc danhMuc,
        @RequestHeader("id") String id,
        @RequestHeader("email") String email,
        @RequestHeader("vaiTros") String vaiTros) {
        log.info("API PUT /danhmucs");
        try {
            if (vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
                if (!danhMucRepository.existsById(new DanhMucId(danhMuc.getLoaiDanhMuc(), danhMuc.getMaDanhMuc()))) {
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }
                else {
                    Optional<DanhMuc> dmData = danhMucRepository.findById(new DanhMucId(danhMuc.getLoaiDanhMuc(), danhMuc.getMaDanhMuc()));
                    if (dmData.isPresent()) {
                        DanhMuc _danhMuc = dmData.get();
                        if (danhMuc.getGiaTri() != null) {
                            _danhMuc.setGiaTri(danhMuc.getGiaTri());
                        }
                        danhMucRepository.save(_danhMuc);
                        return new ResponseEntity<>(_danhMuc, HttpStatus.NO_CONTENT);
                    }
                    else {
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                    }
                }
            }
            else {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/danhmucs/{loaiDanhMuc}/{maDanhMuc}")
    public ResponseEntity<DanhMuc> deleteDanhMuc(
        @PathVariable(value = "loaiDanhMuc") String loaiDanhMuc,
        @PathVariable(value = "maDanhMuc") String maDanhMuc,
        @RequestHeader("id") String id,
        @RequestHeader("email") String email,
        @RequestHeader("vaiTros") String vaiTros) {
        log.info("API DELETE /danhmucs/{loaiDanhMuc}/{maDanhMuc}");
        try {
            if (vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros)) {
                if (!danhMucRepository.existsById(new DanhMucId(loaiDanhMuc, maDanhMuc))) {
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }
                else {
                    danhMucRepository.deleteById(new DanhMucId(loaiDanhMuc, maDanhMuc));
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }
            else {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
