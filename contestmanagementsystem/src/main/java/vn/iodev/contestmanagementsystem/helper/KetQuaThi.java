package vn.iodev.contestmanagementsystem.helper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class KetQuaThi {
    private int STT;
    public int getSTT() {
        return STT;
    }
    public void setSTT(int sTT) {
        STT = sTT;
    }
    public String getDoiTuong() {
        return doiTuong;
    }
    public void setDoiTuong(String doiTuong) {
        this.doiTuong = doiTuong;
    }
    public String getTenTruong() {
        return tenTruong;
    }
    public void setTenTruong(String tenTruong) {
        this.tenTruong = tenTruong;
    }
    public String getGiaiThuong() {
        return giaiThuong;
    }
    public void setGiaiThuong(String giaiThuong) {
        this.giaiThuong = giaiThuong;
    }
    public String getKhoiThi() {
        return khoiThi;
    }
    public void setKhoiThi(String khoiThi) {
        this.khoiThi = khoiThi;
    }
    private String doiTuong;
    private String tenTruong;
    private String giaiThuong;
    private String khoiThi;
}
