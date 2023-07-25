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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "T_KhoiThi")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class KhoiThi {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)    private String id;

    @Column(name = "tenGoi", nullable = false)
    private String tenGoi;

    @Column(name = "noiDungThi", nullable = false, columnDefinition = "TEXT")
    private String noiDungThi;

    @Column(name = "vongSoLoai")
    private Boolean vongSoLoai;

    @Column(name = "thiTapThe")
    private Boolean thiTapThe;

    @Column(name = "thiSangTao")
    private Boolean thiSangTao;

    @Column(name = "maxDangKi")
    private Integer maxDangKi;

    @Column(name = "maxThiSinh")
    private Integer maxThiSinh;

    @Column(name = "tinhTrang")
    private Integer tinhTrang;

    @Column(name = "thoiGianTao")
    private long thoiGianTao;

    @Column(name = "thoiGianCapNhat")
    private long thoiGianCapNhat;
    
    @Column(name = "khoiThiTruocId")
    private String khoiThiTruocId;
    
    @Column(name = "cuocThiTruocId")
    private String cuocThiTruocId;
    
    @Column(name = "maKhoi")
    private String maKhoi;

    @JsonIgnore
    @Transient
    private String tenCuocThi;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuocThiId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private CuocThi cuocThi;

    public KhoiThi(
        String tenGoi,
        CuocThi cuocThi,
        String noiDungThi,
        Boolean vongSoLoai,
        Boolean thiTapThe,
        Boolean thiSangTao,
        Integer maxDangKi,
        Integer maxThiSinh
    ) {
        this.tenGoi = tenGoi;
        this.cuocThi = cuocThi;
        this.noiDungThi = noiDungThi;
        this.vongSoLoai = vongSoLoai;
        this.thiTapThe = thiTapThe;
        this.thiSangTao = thiSangTao;
        this.maxDangKi = maxDangKi;
        this.maxThiSinh = maxThiSinh;
        this.thoiGianTao = System.currentTimeMillis();
        this.thoiGianCapNhat = System.currentTimeMillis();
    }
}
