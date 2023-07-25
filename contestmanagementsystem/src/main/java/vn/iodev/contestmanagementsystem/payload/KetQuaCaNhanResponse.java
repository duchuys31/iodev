package vn.iodev.contestmanagementsystem.payload;

public class KetQuaCaNhanResponse {
    public Integer getThuTuXepHang() {
        return thuTuXepHang;
    }
    public void setThuTuXepHang(Integer thuTuXepHang) {
        this.thuTuXepHang = thuTuXepHang;
    }
    public String getTenThiSinh() {
        return tenThiSinh;
    }
    public void setTenThiSinh(String tenThiSinh) {
        this.tenThiSinh = tenThiSinh;
    }
    public String getMaThiSinh() {
        return maThiSinh;
    }
    public void setMaThiSinh(String maThiSinh) {
        this.maThiSinh = maThiSinh;
    }
    public String getSoBaoDanh() {
        return soBaoDanh;
    }
    public void setSoBaoDanh(String soBaoDanh) {
        this.soBaoDanh = soBaoDanh;
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
    private Integer thuTuXepHang;
    private String tenThiSinh;
    private String maThiSinh;
    private String soBaoDanh;
    private String tenDoanThi;
    private String maDoanThi;
    private String hangGiaiThuong;
    private String tenGiaiThuong;
    public KetQuaCaNhanResponse() {}
}
