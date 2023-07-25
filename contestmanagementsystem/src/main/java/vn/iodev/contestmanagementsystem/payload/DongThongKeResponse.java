package vn.iodev.contestmanagementsystem.payload;

import java.util.List;

import vn.iodev.contestmanagementsystem.model.DoanThi;

public class DongThongKeResponse {
    public DoanThi getDoanThi() {
        return doanThi;
    }

    public void setDoanThi(DoanThi doanThi) {
        this.doanThi = doanThi;
    }

    public Long getSoThiSinh() {
        return soThiSinh;
    }

    public void setSoThiSinh(Long soThiSinh) {
        this.soThiSinh = soThiSinh;
    }

    public Long getSoHuanLuyenVien() {
        return soHuanLuyenVien;
    }

    public void setSoHuanLuyenVien(Long soHuanLuyenVien) {
        this.soHuanLuyenVien = soHuanLuyenVien;
    }

    private DoanThi doanThi;
    private Long soThiSinh;
    private Long soHuanLuyenVien;
    private List<NoiDungThi> noiDungThi;

    public List<NoiDungThi> getNoiDungThi() {
        return noiDungThi;
    }

    public void setNoiDungThi(List<NoiDungThi> noiDungThi) {
        this.noiDungThi = noiDungThi;
    }

    public DongThongKeResponse() {
        
    }
}
