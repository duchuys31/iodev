package vn.iodev.humanresources.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "T_CaNhan")
public class CaNhan {
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

    public int getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(int gioiTinh) {
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

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
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

    @Id
    private String id;

    @Column(name = "hoTen", nullable = false)
    private String hoTen;

    @Column(name = "gioiTinh", nullable = false)
    private int gioiTinh;

    @Column(name = "ngaySinh", nullable = true)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy", locale = "vi_VN", timezone = "Asia/Ho_Chi_Minh")
    private Date ngaySinh;

    @Column(name = "email", nullable = false)
    @Email
    private String email;

    @Column(name = "soDienThoai", nullable = true)
    private String soDienThoai;

    @Column(name = "avatar", nullable = true)
    @Lob
    @JsonIgnore
    private byte[] avatar;

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    @Column(name = "avatarFileName", nullable = true)
    @JsonIgnore
    private String avatarFileName;
    
    public String getAvatarFileName() {
        return avatarFileName;
    }

    public void setAvatarFileName(String avatarFileName) {
        this.avatarFileName = avatarFileName;
    }

    @Column(name = "linkedIn", nullable = true)
    private String linkedIn;

    @Column(name = "github", nullable = true)
    private String github;

    @Column(name = "google", nullable = true)
    private String google;

    @Column(name = "tinhTrang", nullable = false)
    @Min(0)
    @Max(1)
    private int tinhTrang;

    @Column(name = "thoiGianTao", nullable = false)
    private long thoiGianTao;

    @Column(name = "thoiGianCapNhat", nullable = false)
    private long thoiGianCapNhat;

    public CaNhan() {}

    public CaNhan(String hoTen, int gioiTinh, Date ngaySinh, String email, String soDienThoai, String linkedIn, String github, String google) {
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.linkedIn = linkedIn;
        this.github = github;
        this.google = google;
        this.thoiGianTao = System.currentTimeMillis();
        this.thoiGianCapNhat = System.currentTimeMillis();
        this.tinhTrang = 0;
    }
}
