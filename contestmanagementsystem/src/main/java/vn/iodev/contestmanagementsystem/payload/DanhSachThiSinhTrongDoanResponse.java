package vn.iodev.contestmanagementsystem.payload;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import vn.iodev.contestmanagementsystem.model.KhoiThi;

public class DanhSachThiSinhTrongDoanResponse {
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
    public String getDoiTuongThi() {
        return doiTuongThi;
    }
    public void setDoiTuongThi(String doiTuongThi) {
        this.doiTuongThi = doiTuongThi;
    }
    public String getNganhDaoTao() {
        return nganhDaoTao;
    }
    public void setNganhDaoTao(String nganhDaoTao) {
        this.nganhDaoTao = nganhDaoTao;
    }
    public String getDatGiaiThuong() {
        return datGiaiThuong;
    }
    public void setDatGiaiThuong(String datGiaiThuong) {
        this.datGiaiThuong = datGiaiThuong;
    }
    public List<KhoiThi> getNoiDungThi() {
        return noiDungThi;
    }
    public void setNoiDungThi(List<KhoiThi> noiDungThi) {
        this.noiDungThi = noiDungThi;
    }
    public String getSoBaoDanh() {
		return soBaoDanh;
	}
	public void setSoBaoDanh(String soBaoDanh) {
		this.soBaoDanh = soBaoDanh;
	}

	private String id;
    private String hoTen;
    private Integer gioiTinh;

    @JsonFormat(pattern = "dd-MM-yyyy", locale = "vi_VN", timezone = "Asia/Ho_Chi_Minh")
    private Date ngaySinh;
    private String email;
    private String soDienThoai;
    private String doiTuongThi;
    private String nganhDaoTao;
    private String datGiaiThuong;
    private List<KhoiThi> noiDungThi;
    private String soBaoDanh;
    public DanhSachThiSinhTrongDoanResponse() {}
}
