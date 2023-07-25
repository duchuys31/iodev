package vn.iodev.contestmanagementsystem.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iodev.contestmanagementsystem.model.DanhSachThi;
import vn.iodev.contestmanagementsystem.model.DoanThi;
import vn.iodev.contestmanagementsystem.model.DoiThi;
import vn.iodev.contestmanagementsystem.model.HuanLuyenVien;
import vn.iodev.contestmanagementsystem.model.ThiSinh;
import vn.iodev.contestmanagementsystem.repository.DanhSachThiRepository;
import vn.iodev.contestmanagementsystem.repository.DoanThiRepository;
import vn.iodev.contestmanagementsystem.repository.DoiThiRepository;
import vn.iodev.contestmanagementsystem.repository.HuanLuyenVienRepository;
import vn.iodev.contestmanagementsystem.repository.ThiSinhRepository;

@Service
public class VaiTroChecker {
    @Autowired
    DoanThiRepository doanThiRepository;

    @Autowired
    DoiThiRepository doiThiRepository;

    @Autowired
    HuanLuyenVienRepository huanLuyenVienRepository;

    @Autowired
    ThiSinhRepository thiSinhRepository;

    @Autowired
    DanhSachThiRepository danhSachThiRepository;

    public static final String VAITRO_QUANTRIHETHONG = "VAITRO_QUANTRIHETHONG";
    public static final String VAITRO_QUANTRITOCHUC = "VAITRO_QUANTRITOCHUC";
    public static final String VAITRO_NGUOIDUNG = "VAITRO_NGUOIDUNG";

    public boolean hasVaiTroQuanTriHeThong(String vaiTro) {
        if (vaiTro == null || vaiTro.isEmpty()) return false;
        List<String> lstVaiTro = Arrays.asList(vaiTro.split(","));
        
        return lstVaiTro.contains(VAITRO_QUANTRIHETHONG);
    }
    public boolean hasVaiTroQuanTriToChuc(String vaiTro) {
        if (vaiTro == null || vaiTro.isEmpty()) return false;
        List<String> lstVaiTro = Arrays.asList(vaiTro.split(","));
        
        return lstVaiTro.contains(VAITRO_QUANTRITOCHUC);
    }

    public boolean hasVaiTroNguoiDung(String vaiTro) {
        if (vaiTro == null || vaiTro.isEmpty()) return false;
        List<String> lstVaiTro = Arrays.asList(vaiTro.split(","));
        
        return lstVaiTro.contains(VAITRO_NGUOIDUNG);
    }

    public boolean hasVaiTro(String maVaiTro, String vaiTro) {
        if (vaiTro == null || vaiTro.isEmpty()) return false;
        List<String> lstVaiTro = Arrays.asList(vaiTro.split(","));
        
        return lstVaiTro.contains(maVaiTro);
    }

    public boolean canAccessHuanLuyenVien(String toChucId, HuanLuyenVien hlv) {
        List<DoanThi> lstDoanThi = doanThiRepository.findByToChucId(toChucId);
        List<String> doanThiIds = new ArrayList<>();
        for (DoanThi dt : lstDoanThi) {
            doanThiIds.add(dt.getId());
        }
        if (doanThiIds.contains(hlv.getDoanThiId())) {
            return true;
        }

        return false;
    }

    public boolean canAccessHuanLuyenVien(String toChucId, String huanLuyenVienId) {
        List<DoanThi> lstDoanThi = doanThiRepository.findByToChucId(toChucId);
        List<String> doanThiIds = new ArrayList<>();
        for (DoanThi dt : lstDoanThi) {
            doanThiIds.add(dt.getId());
        }
        Optional<HuanLuyenVien> hlvData = huanLuyenVienRepository.findById(huanLuyenVienId);
        if (!hlvData.isPresent()) {
            return false;
        }
        HuanLuyenVien hlv = hlvData.get();
        if (doanThiIds.contains(hlv.getDoanThiId())) {
            return true;
        }

        return false;
    }

    public boolean canAccessThiSinh(String toChucId, ThiSinh thiSinh) {
        List<DoanThi> lstDoanThi = doanThiRepository.findByToChucId(toChucId);
        List<String> doanThiIds = new ArrayList<>();
        for (DoanThi dt : lstDoanThi) {
            doanThiIds.add(dt.getId());
        }
        if (doanThiIds.contains(thiSinh.getDoanThiId())) {
            return true;
        }

        return false;
    }

    public boolean canAccessThiSinh(String toChucId, String thiSinhId) {
        List<DoanThi> lstDoanThi = doanThiRepository.findByToChucId(toChucId);
        List<String> doanThiIds = new ArrayList<>();
        for (DoanThi dt : lstDoanThi) {
            doanThiIds.add(dt.getId());
        }
        Optional<ThiSinh> thiSinhData = thiSinhRepository.findById(thiSinhId);
        if (!thiSinhData.isPresent()) {
            return false;
        }
        ThiSinh thiSinh = thiSinhData.get();

        if (doanThiIds.contains(thiSinh.getDoanThiId())) {
            return true;
        }

        return false;
    }

    public boolean canAccessDoiThi(String toChucId, DoiThi doiThi) {
        List<DoanThi> lstDoanThi = doanThiRepository.findByToChucId(toChucId);
        List<String> doanThiIds = new ArrayList<>();
        for (DoanThi dt : lstDoanThi) {
            doanThiIds.add(dt.getId());
        }
        if (doanThiIds.contains(doiThi.getDoanThiId())) {
            return true;
        }

        return false;
    }

    public boolean canAccessDoiThi(String toChucId, String doiThiId) {
        List<DoanThi> lstDoanThi = doanThiRepository.findByToChucId(toChucId);
        List<String> doanThiIds = new ArrayList<>();
        for (DoanThi dt : lstDoanThi) {
            doanThiIds.add(dt.getId());
        }
        Optional<DoiThi> doiThiData = doiThiRepository.findById(doiThiId);
        if (!doiThiData.isPresent()) {
            return false;
        }
        DoiThi doiThi = doiThiData.get();
        
        if (doanThiIds.contains(doiThi.getDoanThiId())) {
            return true;
        }

        return false;
    }

    public boolean canAccessDanhSachThi(String toChucId, DanhSachThi danhSachThi) {
        List<DoanThi> lstDoanThi = doanThiRepository.findByToChucId(toChucId);
        List<String> doanThiIds = new ArrayList<>();
        for (DoanThi dt : lstDoanThi) {
            doanThiIds.add(dt.getId());
        }
        if (danhSachThi.getDoiThiId() != null) {
            Optional<DoiThi> doiThiData = doiThiRepository.findById(danhSachThi.getDoiThiId());
            if (!doiThiData.isPresent()) {
                return false;
            }
            DoiThi doiThi = doiThiData.get();
            
            if (doanThiIds.contains(doiThi.getDoanThiId())) {
                return true;
            }
        }
        if (danhSachThi.getThiSinhId() != null) {
            Optional<ThiSinh> thiSinhData = thiSinhRepository.findById(danhSachThi.getThiSinhId());
            if (!thiSinhData.isPresent()) {
                return false;
            }
            ThiSinh thiSinh = thiSinhData.get();
            
            if (doanThiIds.contains(thiSinh.getDoanThiId())) {
                return true;
            }
        }

        return false;
    }

    public boolean canAccessDanhSachThi(String toChucId, Long id) {
        List<DoanThi> lstDoanThi = doanThiRepository.findByToChucId(toChucId);
        List<String> doanThiIds = new ArrayList<>();
        for (DoanThi dt : lstDoanThi) {
            doanThiIds.add(dt.getId());
        }
        Optional<DanhSachThi> danhSachThiData = danhSachThiRepository.findById(id);
        if (!danhSachThiData.isPresent()) {
            return false;
        }
        DanhSachThi danhSachThi = danhSachThiData.get();
        
        if (danhSachThi.getDoiThiId() != null) {
            Optional<DoiThi> doiThiData = doiThiRepository.findById(danhSachThi.getDoiThiId());
            if (!doiThiData.isPresent()) {
                return false;
            }
            DoiThi doiThi = doiThiData.get();
            
            if (doanThiIds.contains(doiThi.getDoanThiId())) {
                return true;
            }
        }
        if (danhSachThi.getThiSinhId() != null) {
            Optional<ThiSinh> thiSinhData = thiSinhRepository.findById(danhSachThi.getThiSinhId());
            if (!thiSinhData.isPresent()) {
                return false;
            }
            ThiSinh thiSinh = thiSinhData.get();
            
            if (doanThiIds.contains(thiSinh.getDoanThiId())) {
                return true;
            }
        }

        return false;
    }
}
