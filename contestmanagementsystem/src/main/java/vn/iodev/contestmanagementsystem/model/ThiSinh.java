package vn.iodev.contestmanagementsystem.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "T_ThiSinh")
@Table(name = "T_ThiSinh")
@NoArgsConstructor
@Setter
@Getter
public class ThiSinh {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)  
    private String id;

    @Column(name = "hoTen", nullable = false)
    private String hoTen;
    
    @Column(name = "gioiTinh", nullable = false)
    private Integer gioiTinh;

    @Column(name = "ngaySinh", nullable = true)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy", locale = "vi_VN", timezone = "Asia/Ho_Chi_Minh")
    private Date ngaySinh;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "soDienThoai", nullable = true)
    private String soDienThoai;

    @Column(name = "doiTuongThi", nullable = true)
    private String doiTuongThi;

    @Column(name = "nganhDaoTao", nullable = true)
    private String nganhDaoTao;

    @Column(name = "datGiaiThuong", nullable = true)
    private String datGiaiThuong;

    @Column(name = "caNhanId", nullable = true)
    private String caNhanId;
    
    @Column(name = "doanThiId", nullable = true)
    private String doanThiId;

    @Column(name = "thoiGianTao")
    private long thoiGianTao;

    @Column(name = "thoiGianCapNhat")
    private long thoiGianCapNhat;

    @JsonIgnore
    @Transient
    private String tenCuocThi;

    @JsonIgnore
    @Transient
    private String tenKhoiThi;

    @JsonIgnore
    @Transient
    private boolean thamGia;

    @JsonIgnore
    @Transient
    private String tenDoiThi;

    @JsonIgnore
    @Transient
    private String tenDoanThi;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuocThiId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private CuocThi cuocThi;

    public ThiSinh(
        String hoTen,
        Integer gioiTinh,
        Date ngaySinh,
        String email,
        String soDienThoai,
        String doiTuongThi,
        String nganhDaoTao,
        String datGiaiThuong,
        String caNhanId,
        CuocThi cuocThi,
        String doanThiId
    ) {
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.doiTuongThi = doiTuongThi;
        this.nganhDaoTao = nganhDaoTao;
        this.datGiaiThuong = datGiaiThuong;
        this.caNhanId = caNhanId;
        this.cuocThi = cuocThi;
        this.doanThiId = doanThiId;
        this.thoiGianTao = System.currentTimeMillis();
        this.thoiGianCapNhat = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ThiSinh) {
            ThiSinh temp = (ThiSinh)o;
            return (id != null && id.contentEquals(temp.getId()));
        }
        else {
            return false;
        }
    }
}
