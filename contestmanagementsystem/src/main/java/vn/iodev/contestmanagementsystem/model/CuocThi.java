package vn.iodev.contestmanagementsystem.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "T_CuocThi")
@Table(name = "T_CuocThi")
@Setter
@Getter
@NoArgsConstructor
public class CuocThi {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "tenGoi")
    private String tenGoi;

    @Column(name = "tiengAnh", nullable = true)
    private String tiengAnh;

    @Column(name = "serieCuocThi", nullable = true)
    private String serieCuocThi;

    @Column(name = "lanToChuc", nullable = true)
    private Integer lanToChuc;

    @Column(name = "donViToChuc", nullable = false)
    private String donViToChuc;

    @Column(name = "toChucId")
    private String toChucId;

    @Column(name = "ngayBatDau")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy", locale = "vi_VN", timezone = "Asia/Ho_Chi_Minh")
    private Date ngayBatDau;

    @Column(name = "ngayKetThuc")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy", locale = "vi_VN", timezone = "Asia/Ho_Chi_Minh")
    private Date ngayKetThuc;

    @Column(name = "diaDiemToChuc", length = 1024)
    private String diaDiemToChuc;

    @Column(name = "thongTinMoTa", columnDefinition = "TEXT")
    private String thongTinMoTa;

    @Column(name = "website", nullable = true)
    private String website;
    
    @Column(name = "maCuocThi")
    private String maCuocThi;

    @Transient
    private String hinhAnh;

    @Column(name = "tinhTrang")
    private Integer tinhTrang;
    
    public CuocThi(
        String id,
        String tenGoi,
        String tiengAnh,
        String serieCuocThi,
        Integer lanToChuc,
        String donViToChuc,
        String diaDiemToChuc,
        String toChucId,
        Date ngayBatDau,
        Date ngayKetThuc,
        String thongTinMoTa,
        String website,
        Integer tinhTrang
        ) {
            this.id = id;
            this.tenGoi = tenGoi;
            this.tiengAnh = tiengAnh;
            this.serieCuocThi = serieCuocThi;
            this.lanToChuc = lanToChuc;
            this.donViToChuc = donViToChuc;
            this.diaDiemToChuc = diaDiemToChuc;
            this.toChucId = toChucId;
            this.ngayBatDau = ngayBatDau;
            this.ngayKetThuc = ngayKetThuc;
            this.thongTinMoTa = thongTinMoTa;
            this.website = website;
            this.tinhTrang = tinhTrang;
    }

    public CuocThi(String tenGoi,
        String tiengAnh,
        String serieCuocThi,
        Integer lanToChuc,
        String donViToChuc,
        String diaDiemToChuc,
        String toChucId,
        Date ngayBatDau,
        Date ngayKetThuc,
        String thongTinMoTa,
        String website,
        Integer tinhTrang
        ) {
            this.tenGoi = tenGoi;
            this.tiengAnh = tiengAnh;
            this.serieCuocThi = serieCuocThi;
            this.lanToChuc = lanToChuc;
            this.donViToChuc = donViToChuc;
            this.diaDiemToChuc = diaDiemToChuc;
            this.toChucId = toChucId;
            this.ngayBatDau = ngayBatDau;
            this.ngayKetThuc = ngayKetThuc;
            this.thongTinMoTa = thongTinMoTa;
            this.website = website;
            this.tinhTrang = tinhTrang;
    }
}
