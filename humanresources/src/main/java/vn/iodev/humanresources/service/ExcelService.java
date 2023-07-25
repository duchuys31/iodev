package vn.iodev.humanresources.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vn.iodev.humanresources.config.HumanResourcesConfiguration;
import vn.iodev.humanresources.helper.ExcelHelper;
import vn.iodev.humanresources.model.CaNhan;
import vn.iodev.humanresources.model.ToChuc;
import vn.iodev.humanresources.repository.CaNhanRepository;
import vn.iodev.humanresources.repository.ToChucRepository;
import vn.iodev.humanresources.utils.RandomUtil;

@Service
public class ExcelService {
    @Autowired
    ToChucRepository toChucRepository;

    @Autowired
    CaNhanRepository caNhanRepository;

    @Autowired
    HumanResourcesConfiguration configuration;
    
    public void importToChuc(MultipartFile file) {
        try {
            List<ToChuc> tochucs = ExcelHelper.excelXlsxToToChucs(file.getInputStream());
            for (ToChuc tc : tochucs) {
                Optional<ToChuc> toChucData = toChucRepository.findByTenGoiOrTiengAnhOrTenVietTat(tc.getTenGoi(), tc.getTiengAnh(), tc.getTenVietTat());
                if (toChucData.isPresent()) {
                    tc.setId(toChucData.get().getId());
                }
                else {
                    tc.setId(RandomUtil.generateRandomNumeric(configuration.getToChucIdLength()));
                    int nOfIdGeneratedRetry = configuration.getnOfIdGeneratedRetry();
                    int i = 0;
                    while (i < nOfIdGeneratedRetry) {
                        toChucData = toChucRepository.findById(tc.getId());
                        if (toChucData.isPresent()) {
                            tc.setId(RandomUtil.generateRandomNumeric(configuration.getToChucIdLength()));
                        }
                        else {
                            break;
                        }
                        i++;
                    }
                    tc.setThoiGianTao(System.currentTimeMillis());
                }
                tc.setThoiGianCapNhat(System.currentTimeMillis());
            }
            toChucRepository.saveAll(tochucs);
        }
        catch (IOException e) {
            e.printStackTrace();
            
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public void importCaNhan(MultipartFile file) {
        try {
            List<CaNhan> canhans = ExcelHelper.excelXlsxToCaNhans(file.getInputStream());
            List<CaNhan> lstFilterSaved = new ArrayList<>();
            for (CaNhan cn : canhans) {
                Optional<CaNhan> caNhanData = caNhanRepository.findByHoTenAndGioiTinhAndNgaySinhAndEmailOrSoDienThoai(cn.getHoTen(), cn.getGioiTinh(), cn.getNgaySinh(), cn.getEmail(), cn.getSoDienThoai());
                if (caNhanData.isPresent()) {
                    cn.setId(caNhanData.get().getId());
                }
                else if (cn.getNgaySinh() != null) {
                    int i = 0;
                    int nOfIdGeneratedRetry = configuration.getnOfIdGeneratedRetry();
                    while (i < nOfIdGeneratedRetry) {
                        cn.setId(RandomUtil.generateRandomEID(cn.getNgaySinh(), cn.getGioiTinh(), configuration.getCaNhanIdLength()));
                        caNhanData = caNhanRepository.findById(cn.getId());
                        if (caNhanData.isPresent()) {
                            cn.setId(RandomUtil.generateRandomEID(cn.getNgaySinh(), cn.getGioiTinh(), configuration.getCaNhanIdLength()));
                        }
                        else {
                            break;
                        }
                        i++;
                    }
                    cn.setThoiGianTao(System.currentTimeMillis());
                }
                else {
                    continue;
                }
                if (cn.getEmail() == null || !EmailValidator.getInstance().isValid(cn.getEmail())) {
                    continue;
                }
                cn.setThoiGianCapNhat(System.currentTimeMillis());
                lstFilterSaved.add(cn);
            }
            caNhanRepository.saveAll(lstFilterSaved);
        }
        catch (IOException e) {
            e.printStackTrace();
            
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}
