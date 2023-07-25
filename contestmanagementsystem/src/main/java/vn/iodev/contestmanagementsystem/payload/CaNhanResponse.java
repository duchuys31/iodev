package vn.iodev.contestmanagementsystem.payload;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CaNhanResponse {
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getHoTen() {
        return hoTen;
    }
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    public Integer getGioiTinh() {
        return gioiTinh;
    }
    public void setGioiTinh(Integer gioiTinh) {
        this.gioiTinh = gioiTinh;
    }
    public Date getNgaySinh() {
        return ngaySinh;
    }
    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
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
    public String getLinkedIn() {
        return linkedIn;
    }
    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }
    public String getGithub() {
        return github;
    }
    public void setGithub(String github) {
        this.github = github;
    }
    public String getGoogle() {
        return google;
    }
    public void setGoogle(String google) {
        this.google = google;
    }
    public Integer getTinhTrang() {
        return tinhTrang;
    }
    public void setTinhTrang(Integer tinhTrang) {
        this.tinhTrang = tinhTrang;
    }
    public long getThoiGianTao() {
        return thoiGianTao;
    }
    public void setThoiGianTao(long thoiGianTao) {
        this.thoiGianTao = thoiGianTao;
    }
    public long getThoiGianCapNhat() {
        return thoiGianCapNhat;
    }
    public void setThoiGianCapNhat(long thoiGianCapNhat) {
        this.thoiGianCapNhat = thoiGianCapNhat;
    }
    private String id;
    private String hoTen;
    private Integer gioiTinh;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy", locale = "vi_VN", timezone = "Asia/Ho_Chi_Minh")
    private Date ngaySinh;
    private String email;
    private String soDienThoai;
    private String linkedIn;
    private String github;
    private String google;
    private Integer tinhTrang;
    private long thoiGianTao;
    private long thoiGianCapNhat;
    public CaNhanResponse() {} 
}
