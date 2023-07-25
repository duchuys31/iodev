package vn.iodev.contestmanagementsystem.payload;

import java.util.List;

import vn.iodev.contestmanagementsystem.model.ThiSinh;

public class KetQuaDongDoiResponse {
    public Integer getThuTuXepHang() {
        return thuTuXepHang;
    }
    public void setThuTuXepHang(Integer thuTuXepHang) {
        this.thuTuXepHang = thuTuXepHang;
    }
    public String getTenDoiThi() {
        return tenDoiThi;
    }
    public void setTenDoiThi(String tenDoiThi) {
        this.tenDoiThi = tenDoiThi;
    }
    public String getMaDoiThi() {
        return maDoiThi;
    }
    public void setMaDoiThi(String maDoiThi) {
        this.maDoiThi = maDoiThi;
    }
    public String getTenDoanThi() {
        return tenDoanThi;
    }
    public void setTenDoanThi(String tenDoanThi) {
        this.tenDoanThi = tenDoanThi;
    }
    public String getMaDoanThi() {
        return maDoanThi;
    }
    public void setMaDoanThi(String maDoanThi) {
        this.maDoanThi = maDoanThi;
    }
    public String getHangGiaiThuong() {
        return hangGiaiThuong;
    }
    public void setHangGiaiThuong(String hangGiaiThuong) {
        this.hangGiaiThuong = hangGiaiThuong;
    }
    public String getTenGiaiThuong() {
        return tenGiaiThuong;
    }
    public void setTenGiaiThuong(String tenGiaiThuong) {
        this.tenGiaiThuong = tenGiaiThuong;
    }
    public List<ThiSinh> getThiSinh() {
        return thiSinh;
    }
    public void setThiSinh(List<ThiSinh> thiSinh) {
        this.thiSinh = thiSinh;
    }
    private Integer thuTuXepHang;
    private String tenDoiThi;
    private String maDoiThi;
    private String tenDoanThi;
    private String maDoanThi;
    private String hangGiaiThuong;
    private String tenGiaiThuong;
    private List<ThiSinh> thiSinh;
    public KetQuaDongDoiResponse() {}
}
