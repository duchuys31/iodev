package vn.iodev.contestmanagementsystem.model;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "T_HuanLuyenVien")
@Table(name = "T_HuanLuyenVien")
@NoArgsConstructor
@Setter
@Getter
public class HuanLuyenVien {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)    
    private String id;

    @Column(name = "hoTen", length = 1024)
    private String hoTen;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "soDienThoai", nullable = true)
    private String soDienThoai;

    @Column(name = "caNhanId", nullable = true)
    private String caNhanId;

    @Column(name = "doanThiId", nullable = true)
    private String doanThiId;

    @Column(name = "khoiThiId", nullable = true, columnDefinition = "TEXT")
    private String khoiThiId;

    @Column(name = "truongPhoDoan", nullable = true)
    private Integer truongPhoDoan;

    @Column(name = "thoiGianTao")
    private long thoiGianTao;

    @Column(name = "thoiGianCapNhat")
    private long thoiGianCapNhat;

    @JsonIgnore
    @Transient
    private String tenToChuc;

    @JsonIgnore
    @Transient
    private String tenDoanThi;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuocThiId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private CuocThi cuocThi;

    public HuanLuyenVien(
        String hoTen,
        String email,
        String soDienThoai,
        String caNhanId,
        CuocThi cuocThi,
        String doanThiId,
        String khoiThiId,
        Integer truongPhoDoan
    ) {
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.caNhanId = caNhanId;
        this.cuocThi = cuocThi;
        this.khoiThiId = khoiThiId;
        this.truongPhoDoan = truongPhoDoan;
        this.thoiGianTao = System.currentTimeMillis();
        this.thoiGianCapNhat = System.currentTimeMillis();
    }

    @Override 
    public boolean equals(Object o) {
        if (o instanceof HuanLuyenVien) {
            HuanLuyenVien temp = (HuanLuyenVien)o;
            return (id != null && id.contentEquals(temp.getId()));
        }
        else {
            return false;
        }
    }
}
