package vn.iodev.iosecurity.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.JoinColumn;

@Entity
@Table(name = "T_TaiKhoan", uniqueConstraints = { @UniqueConstraint(columnNames = "id"),
		@UniqueConstraint(columnNames = "email") })
public class TaiKhoan {
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public int getTinhTrang() {
		return tinhTrang;
	}

	public void setTinhTrang(int tinhTrang) {
		this.tinhTrang = tinhTrang;
	}

	public String getMaKichHoat() {
		return maKichHoat;
	}

	public void setMaKichHoat(String maKichHoat) {
		this.maKichHoat = maKichHoat;
	}

	public long getThoiHanKichHoat() {
		return thoiHanKichHoat;
	}

	public void setThoiHanKichHoat(long thoiHanKichHoat) {
		this.thoiHanKichHoat = thoiHanKichHoat;
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
	@Email
	private String email;

	@Column(name = "id")
	@NotBlank
	private String id;

	@Column(name = "matKhau")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String matKhau;

	@Column(name = "tinhTrang", nullable = false)
	@Min(0)
	@Max(2)
	private int tinhTrang;

	@Column(name = "maKichHoat")
	private String maKichHoat;

	@Column(name = "thoiHanKichHoat")
	private long thoiHanKichHoat;

	@Column(name = "loaiTaiKhoan")
	private Integer loaiTaiKhoan;

	public Integer getLoaiTaiKhoan() {
		return loaiTaiKhoan;
	}

	public void setLoaiTaiKhoan(Integer loaiTaiKhoan) {
		this.loaiTaiKhoan = loaiTaiKhoan;
	}

	@Column(name = "thoiGianTao")
	private long thoiGianTao;

	@Column(name = "thoiGianCapNhat")
	private long thoiGianCapNhat;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "T_TaiKhoan_VaiTro", joinColumns = @JoinColumn(name = "email"), inverseJoinColumns = @JoinColumn(name = "vaiTroId"))
	private Set<VaiTro> vaiTros = new HashSet<>();

	public Set<VaiTro> getVaiTros() {
		return vaiTros;
	}

	public void setVaiTros(Set<VaiTro> vaiTros) {
		this.vaiTros = vaiTros;
	}

	public void addVaiTro(VaiTro vaiTro) {
		this.vaiTros.add(vaiTro);
	}

	public void removeVaiTro(VaiTro vaiTro) {
		this.vaiTros.remove(vaiTro);
	}

	public TaiKhoan() {
	}

	public TaiKhoan(String email, String id, Integer loaiTaiKhoan, String matKhau) {
		this.email = email;
		this.id = id;
		this.loaiTaiKhoan = loaiTaiKhoan;
		this.matKhau = matKhau;
		this.tinhTrang = 0;
		this.thoiGianTao = System.currentTimeMillis();
		this.thoiGianCapNhat = System.currentTimeMillis();
	}

	public TaiKhoan(String email, String id, Integer loaiTaiKhoan, String matKhau, Integer tinhTrang) {
		this.email = email;
		this.id = id;
		this.loaiTaiKhoan = loaiTaiKhoan;
		this.matKhau = matKhau;
		this.tinhTrang = tinhTrang;
		this.thoiGianTao = System.currentTimeMillis();
		this.thoiGianCapNhat = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return "TaiKhoan [email = " + email + "]";
	}
}
