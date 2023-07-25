package vn.iodev.contestmanagementsystem.controller;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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

import com.netflix.config.validation.ValidationException;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.contestmanagementsystem.exception.ResourceNotFoundException;
import vn.iodev.contestmanagementsystem.helper.ExcelHelper;
import vn.iodev.contestmanagementsystem.localservice.impl.ThiSinhLocalServiceImpl;
import vn.iodev.contestmanagementsystem.model.CuocThi;
import vn.iodev.contestmanagementsystem.model.DoanThi;
import vn.iodev.contestmanagementsystem.model.ImportResponse;
import vn.iodev.contestmanagementsystem.model.KhoiThi;
import vn.iodev.contestmanagementsystem.model.ThiSinh;
import vn.iodev.contestmanagementsystem.repository.CuocThiRepository;
import vn.iodev.contestmanagementsystem.repository.DoanThiRepository;
import vn.iodev.contestmanagementsystem.repository.ThiSinhRepository;
import vn.iodev.contestmanagementsystem.security.VaiTroChecker;
import vn.iodev.contestmanagementsystem.service.CaNhanService;
import vn.iodev.contestmanagementsystem.service.ExcelService;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Slf4j
public class ThiSinhController {
    @Autowired
    ThiSinhRepository thiSinhRepository;

    @Autowired
    CuocThiRepository cuocThiRepository;

    @Autowired
    DoanThiRepository doanThiRepository;

    @Autowired
    ExcelService fileService;

    @Autowired
    CaNhanService caNhanService;

    @Autowired
    VaiTroChecker vaiTroChecker;
    
    @Autowired
    ThiSinhLocalServiceImpl thiSinhLocalServiceImpl;

    @GetMapping("/thisinhs")
    public List<ThiSinh> getAllThiSinhs(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size, @RequestParam(name = "tuKhoa", defaultValue = "") String tuKhoa, @RequestParam(required = false) String cuocThiId, @RequestParam(required = false) String doanThiId) {
        log.info("API GET /thisinhs");
        Pageable paging = PageRequest.of(page - 1, size);
        Optional<CuocThi> cuocThiData = cuocThiRepository.findById(cuocThiId);
        if (cuocThiData.isPresent()) {
            return thiSinhRepository.findThiSinhByMultipleConditions(tuKhoa, cuocThiData.get(), doanThiId, paging);
        }
        else {
            return thiSinhRepository.findThiSinhByMultipleConditions(tuKhoa, null, doanThiId, paging);
        }
    }

    @GetMapping("/cuocthis/{cuocThiId}/thisinhs")
    public ResponseEntity<List<ThiSinh>> getAllThiSinhsByCuocThiId(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size, @PathVariable(value = "cuocThiId") String cuocThiId) {
        log.info("API GET /cuocthis/{cuocThiId}/thisinhs");
        List<ThiSinh> lstThiSinh = new ArrayList<>();

        if (!cuocThiRepository.existsById(cuocThiId)) {
            return new ResponseEntity<>(lstThiSinh, HttpStatus.OK);
        }

        Pageable paging = PageRequest.of(page - 1, size);

        List<ThiSinh> thiSinhs = thiSinhRepository.findByCuocThiId(cuocThiId, paging);
        lstThiSinh.addAll(thiSinhs);

        return new ResponseEntity<>(lstThiSinh, HttpStatus.OK);
    }
    
    @GetMapping("/thisinhs/{toChucId}/daghidanh")
    public ResponseEntity<List<ThiSinh>> getAllThiSinhsByToChucId(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size, @PathVariable(value = "toChucId") String toChucId) {
        log.info("API GET /thisinhs/{toChucId}/daghidanh");
        List<ThiSinh> lstThiSinh = new ArrayList<>();

    
        Pageable paging = PageRequest.of(page - 1, size);
        
        List<ThiSinh> thiSinhs = thiSinhLocalServiceImpl.findByToChucId(toChucId, paging);

        lstThiSinh.addAll(thiSinhs);

        return new ResponseEntity<>(lstThiSinh, HttpStatus.OK);
    }

    private void validateRelationConstraint(ThiSinh thiSinh) throws Exception {
        if (thiSinh.getCaNhanId() != null) {
            caNhanService.getCaNhanById(thiSinh.getCaNhanId());
        }
        if (thiSinh.getDoanThiId() != null) {
            Optional<DoanThi> doanThi = doanThiRepository.findById(thiSinh.getDoanThiId());
            if (!doanThi.isPresent()) {
                throw new ValidationException("DoanThi is not exists!");
            }
        }
    }
    
    private ThiSinh validateDuplicateContest(ThiSinh thiSinh, String cuocthiId, String doanThiId){
    	List<ThiSinh> thiSinhs = thiSinhRepository.findByCuocThiIdAndDoanThiId(cuocthiId, doanThiId);
    	System.out.println("ID ThiSinh: ============= " + thiSinh.getId());
    	System.out.println("HoTen ThiSinh: ============= " + thiSinh.getHoTen());
    	System.out.println("NgaySinh ThiSinh: ============= " + thiSinh.getNgaySinh());
    	System.out.println("GioiTinh ThiSinh: ============= " + thiSinh.getGioiTinh());
    	System.out.println("CuocThiID: ============= " + cuocthiId);
    	System.out.println("DoanThiId: ============= " + doanThiId);
    	if(thiSinhs != null) {
    		for(ThiSinh tmp : thiSinhs) {
    			System.out.println("tmp: ============= " + tmp.getHoTen() + "|" + tmp.getNgaySinh() + "|" + tmp.getGioiTinh() );
    			if(thiSinh.getHoTen().toLowerCase().equals(tmp.getHoTen().toLowerCase()) && thiSinh.getNgaySinh().equals(tmp.getNgaySinh()) && thiSinh.getGioiTinh() == tmp.getGioiTinh()) {
    				 return tmp;
    			}
    		}
    	}
    	
    	return null;
    }

    @PostMapping("/cuocthis/{cuocThiId}/thisinhs")
    public ResponseEntity<ThiSinh> createThiSinhOfCuocThi(@PathVariable(value = "cuocThiId") String cuocThiId, @Valid @RequestBody ThiSinh thiSinh, @RequestHeader("id") String toChucId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API POST /cuocthis/{cuocThiId}/thisinhs");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros) && !vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        if (vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros) && !vaiTroChecker.canAccessThiSinh(toChucId, thiSinh)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        try {
            validateRelationConstraint(thiSinh);
            
            ThiSinh tmp = validateDuplicateContest(thiSinh, cuocThiId, thiSinh.getDoanThiId());
            
            if(tmp != null) {
            	return new ResponseEntity<>(tmp, HttpStatus.CREATED);
            }
            
            ThiSinh thiSinhMoi = cuocThiRepository.findById(cuocThiId).map(cuocThi -> {
            	thiSinh.setCuocThi(cuocThi);
                return thiSinhRepository.save(thiSinh);
            }).orElseThrow(() -> new ResourceNotFoundException("Not found CuocThi with id = " + cuocThiId));

            return new ResponseEntity<>(thiSinhMoi, HttpStatus.CREATED);
            
        } catch (Exception e) {
            log.debug("API POST /cuocthis/{cuocThiId}/thisinhs", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }   
    }

    @PostMapping("/cuocthis/{cuocThiId}/thisinhs/import")
    public ResponseEntity<ImportResponse> importThiSinh(@PathVariable(value = "cuocThiId") String cuocThiId, @RequestParam("file") MultipartFile multipartFile, @RequestParam("fileType") String fileType, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API POST /cuocthis/{cuocThiId}/thisinhs/import");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros) && !vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
 
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();
        String message = "";
        if (!cuocThiRepository.existsById(cuocThiId)) {
            message = "Cound not import ThiSinh: " + multipartFile.getOriginalFilename() + ", CuocThi not found";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ImportResponse(fileName, size, message));
        }
        Optional<CuocThi> cuocThiData = cuocThiRepository.findById(cuocThiId);

        if (ExcelHelper.hasExcelFormat(multipartFile)) {
            try {
                if (cuocThiData.isPresent()) {
                    fileService.importThiSinh(multipartFile, cuocThiData.get());
                    message = "Import ThiSinh successfully: " + multipartFile.getOriginalFilename();
                    return ResponseEntity.status(HttpStatus.OK).body(new ImportResponse(fileName, size, message));
                }
                else {
                    message = "Cound not import ThiSinh: " + multipartFile.getOriginalFilename() + ", CuocThi not found";
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ImportResponse(fileName, size, message));
                }
            }
            catch (Exception e) {
                log.debug("API POST /cuocthis/{cuocThiId}/thisinhs/import", e);
                message = "Cound not import ThiSinh: " + multipartFile.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ImportResponse(fileName, size, message));
            }
        }

        message = "Please import an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImportResponse(fileName, size, message));
    }

    @GetMapping("/thisinhs/{id}")
    public ResponseEntity<ThiSinh> getThiSinhById(@PathVariable(value = "id") String thiSinhId)
        throws ResourceNotFoundException {
        log.info("API GET /thisinhs/{id}");
        ThiSinh thiSinh = thiSinhRepository.findById(thiSinhId)
          .orElseThrow(() -> new ResourceNotFoundException("ThiSinh not found for this id :: " + thiSinhId));
        return ResponseEntity.ok().body(thiSinh);
    }

    // @PostMapping("/thisinhs")
    // public ResponseEntity<ThiSinh> createThiSinh(@RequestBody ThiSinh thiSinh) {
    //     try {
    //         ThiSinh _thiSinh = thiSinhRepository.save(new ThiSinh(thiSinh.getHoTen(), thiSinh.getGioiTinh(), thiSinh.getNgaySinh(), thiSinh.getEmail(), thiSinh.getSoDienThoai(), thiSinh.getDoiTuongThi(), thiSinh.getNganhDaoTao(), thiSinh.getDatGiaiThuong(), thiSinh.getCaNhanId(), thiSinh.getCuocThi(), thiSinh.getDoanThiId()));
    //         return new ResponseEntity<>(_thiSinh, HttpStatus.CREATED);
    //     }
    //     catch (Exception e) {
    //         return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    @PutMapping("/thisinhs/{id}")
    public ResponseEntity<ThiSinh> updateThiSinh(@PathVariable("id") String id, @RequestBody ThiSinh thiSinh, @RequestHeader("id") String toChucId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API PUT /thisinhs/{id}");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros) && !vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        Optional<ThiSinh> thiSinhData = thiSinhRepository.findById(id);
        if (thiSinhData.isPresent()) {
            if (vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros) && !vaiTroChecker.canAccessThiSinh(toChucId, thiSinhData.get())) {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
    
            try {
                validateRelationConstraint(thiSinh);
                
                ThiSinh _thiSinh = thiSinhData.get();
                if (thiSinh.getHoTen() != null) {
                    _thiSinh.setHoTen(thiSinh.getHoTen());
                }
                if (thiSinh.getGioiTinh() != null) {
                    _thiSinh.setGioiTinh(thiSinh.getGioiTinh());
                }
                if (thiSinh.getNgaySinh() != null) {
                    _thiSinh.setNgaySinh(thiSinh.getNgaySinh());
                }
                if (thiSinh.getEmail() != null) {
                    _thiSinh.setEmail(thiSinh.getEmail());
                }
                if (thiSinh.getSoDienThoai() != null) {
                    _thiSinh.setSoDienThoai(thiSinh.getSoDienThoai());
                }
                if (thiSinh.getDoiTuongThi() != null) {
                    _thiSinh.setDoiTuongThi(thiSinh.getDoiTuongThi());
                }
                if (thiSinh.getNganhDaoTao() != null) {
                    _thiSinh.setNganhDaoTao(thiSinh.getNganhDaoTao());
                }
                if (thiSinh.getDatGiaiThuong() != null) {
                    _thiSinh.setDatGiaiThuong(thiSinh.getDatGiaiThuong());
                }
                if (thiSinh.getCaNhanId() != null) {
                    _thiSinh.setCaNhanId(thiSinh.getCaNhanId());
                }
                if (thiSinh.getDoanThiId() != null) {
                    _thiSinh.setDoanThiId(thiSinh.getDoanThiId());
                }
                _thiSinh.setThoiGianCapNhat(System.currentTimeMillis());
                
                return new ResponseEntity<>(thiSinhRepository.save(_thiSinh), HttpStatus.OK);
            }
            catch (Exception e) {
                log.debug("API PUT /thisinhs/{id}", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/thisinhs/{id}")
    public ResponseEntity<HttpStatus> deleteThiSinh(@PathVariable("id") String id, @RequestHeader("id") String toChucId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API DELETE /thisinhs/{id}");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros) && !vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        if (vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros) && !vaiTroChecker.canAccessThiSinh(toChucId, id)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        try {
            thiSinhRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            log.debug("API DELETE /thisinhs/{id}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/cuocthis/{cuocThiId}/thisinhs")
    public ResponseEntity<List<ThiSinh>> deleteAllThiSinhsOfCuocThi(@PathVariable(value = "cuocThiId") String cuocThiId, @RequestHeader("vaiTros") String vaiTros) {
        log.info("API DELETE /cuocthis/{cuocThiId}/thisinhs");
        if (!vaiTroChecker.hasVaiTroQuanTriHeThong(vaiTros) && !vaiTroChecker.hasVaiTroQuanTriToChuc(vaiTros)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!cuocThiRepository.existsById(cuocThiId)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        thiSinhRepository.deleteByCuocThiId(cuocThiId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PostMapping("/thisinhs/{cuocThiId}/doanthis/{doanThiId}/export")
  	public ResponseEntity<?> exportDanhSachThiTheoKhoiThi(@PathVariable("cuocThiId") String cuocThiId, @PathVariable("doanThiId") String doanThiId) {
  		log.info("API POST/thisinhs/{cuocThiId}/doanthis/{doanThiId}/export");
  		
  		File file = fileService.exportDanhSachThiSinhTrongDoanWithQRCode(cuocThiId, doanThiId);
  		  	
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
