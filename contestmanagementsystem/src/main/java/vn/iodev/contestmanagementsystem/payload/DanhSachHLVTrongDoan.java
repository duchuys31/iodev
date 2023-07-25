package vn.iodev.contestmanagementsystem.payload;

import java.util.List;

import vn.iodev.contestmanagementsystem.model.KhoiThi;

public class DanhSachHLVTrongDoan {
    public String getHoTen() {
        return hoTen;
    }
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    public Integer getTruongPhoDoan() {
        return truongPhoDoan;
    }
    public void setTruongPhoDoan(Integer truongPhoDoan) {
        this.truongPhoDoan = truongPhoDoan;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSoDienThoai() {
        return soDienThoai;
    }
    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
    public List<KhoiThi> getNoiDungThi() {
        return noiDungThi;
    }
    public void setNoiDungThi(List<KhoiThi> noiDungThi) {
        this.noiDungThi = noiDungThi;
    }
    private String hoTen;
    private Integer truongPhoDoan;
    private String email;
    private String soDienThoai;
    private String id;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    private List<KhoiThi> noiDungThi;
    public DanhSachHLVTrongDoan() {}
}
