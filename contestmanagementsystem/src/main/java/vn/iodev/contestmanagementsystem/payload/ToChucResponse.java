package vn.iodev.contestmanagementsystem.payload;

public class ToChucResponse {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenGoi() {
        return tenGoi;
    }

    public void setTenGoi(String tenGoi) {
        this.tenGoi = tenGoi;
    }

    public String getTiengAnh() {
        return tiengAnh;
    }

    public void setTiengAnh(String tiengAnh) {
        this.tiengAnh = tiengAnh;
    }

    public String getLoaiToChuc() {
        return loaiToChuc;
    }

    public void setLoaiToChuc(String loaiToChuc) {
        this.loaiToChuc = loaiToChuc;
    }

    public String getDiaChiHoatDong() {
        return diaChiHoatDong;
    }

    public void setDiaChiHoatDong(String diaChiHoatDong) {
        this.diaChiHoatDong = diaChiHoatDong;
    }

    public String getViTriDiaLy() {
        return viTriDiaLy;
    }

    public void setViTriDiaLy(String viTriDiaLy) {
        this.viTriDiaLy = viTriDiaLy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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
    private String tenGoi;
    private String tiengAnh;
    private String tenVietTat;
    public String getTenVietTat() {
        return tenVietTat;
    }

    public void setTenVietTat(String tenVietTat) {
        this.tenVietTat = tenVietTat;
    }

    private String loaiToChuc;
    private String diaChiHoatDong;
    private String viTriDiaLy;
    private String email;
    private String web;
    private String logo;
    private Integer tinhTrang;
    private long thoiGianTao;
    private long thoiGianCapNhat;

    public ToChucResponse() {}    
}
