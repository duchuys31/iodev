package vn.iodev.contestmanagementsystem.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "T_DoanThi")
@NoArgsConstructor
@Setter
@Getter
public class DoanThi {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "tenGoi", length = 1024)
    private String tenGoi;

    @Column(name = "tiengAnh", nullable = true)
    private String tiengAnh;

    @Column(name = "diaChiHoatDong", nullable = true, columnDefinition = "TEXT")
    private String diaChiHoatDong;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "toChucId", nullable = true)
    private String toChucId;

    @Column(name = "cuocThiId", nullable = true)
    private String cuocThiId;

    @Column(name = "thuTuXepHang", nullable = true)
    private Integer thuTuXepHang;

    @Column(name = "thoiGianTao")
    private long thoiGianTao;

    @Column(name = "thoiGianCapNhat")
    private long thoiGianCapNhat;
    
    @Column(name = "maDoanThi")
    private String maDoanThi;

    public DoanThi(
        String tenGoi,
        String tiengAnh,
        String diaChiHoatDong,
        String email,
        String toChucId,
        String cuocThiId
    ) {
        this.tenGoi = tenGoi;
        this.tiengAnh = tiengAnh;
        this.diaChiHoatDong = diaChiHoatDong;
        this.email = email;
        this.toChucId = toChucId;
        this.cuocThiId = cuocThiId;
        this.thoiGianTao = System.currentTimeMillis();
        this.thoiGianCapNhat = System.currentTimeMillis();
    }
}
