package vn.iodev.iosecurity.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.iosecurity.model.VaiTro;
import vn.iodev.iosecurity.repository.VaiTroRepository;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Slf4j
public class VaiTroController {
    @Autowired
    VaiTroRepository vaiTroRepository;

    @GetMapping("/vaitros")
    public ResponseEntity<List<VaiTro>> getAllVaiTros() {
        log.info("API GET /vaitros");
        try {
            List<VaiTro> vaiTros = vaiTroRepository.findAll();

            return new ResponseEntity<>(vaiTros, HttpStatus.OK);
        } catch (Exception e) {
            log.debug("API GET /vaitros", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/vaitros/{id}")
    public ResponseEntity<VaiTro> getVaiTroById(@PathVariable("id") Integer id) {
        log.info("API GET /vaitros/{id}");
        Optional<VaiTro> vaiTroData = vaiTroRepository.findById(id);

        if (vaiTroData.isPresent()) {
            return new ResponseEntity<>(vaiTroData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/vaitros")
    public ResponseEntity<VaiTro> createVaiTro(@RequestBody VaiTro vaiTro) {
        log.info("API POST /vaitros");
        try {
            VaiTro vaiTroMoi = new VaiTro(vaiTro.getTen());

            VaiTro _vaiTro = vaiTroRepository
                .save(vaiTroMoi);
            return new ResponseEntity<>(_vaiTro, HttpStatus.CREATED);
        } catch (Exception e) {
            log.debug("API POST /vaitros", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/vaitros/{id}")
    public ResponseEntity<VaiTro> updateVaiTro(@PathVariable("id") Integer id, @RequestBody VaiTro vaiTro) {
        log.info("API PUT /vaitros/{id}");
        Optional<VaiTro> vaiTroData = vaiTroRepository.findById(id);

        if (vaiTroData.isPresent()) {
            VaiTro _vaiTro = vaiTroData.get();
            if (vaiTro.getTen() != null) {
                _vaiTro.setTen(vaiTro.getTen());
            }
            return new ResponseEntity<>(vaiTroRepository.save(_vaiTro), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/vaitros/{id}")
    public ResponseEntity<HttpStatus> deleteVaiTro(@PathVariable("id") Integer id) {
        log.info("API DELETE /vaitros/{id}");
        try {
            vaiTroRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
