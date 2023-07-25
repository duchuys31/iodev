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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "T_DoiThi")
@Table(name = "T_DoiThi")
@NoArgsConstructor
@Setter
@Getter
public class DoiThi {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "tenGoi", nullable = false)
    private String tenGoi;

    @Column(name = "doanThiId", nullable = true)
    private String doanThiId;

    @Column(name = "khoiThiId", nullable = true)
    private String khoiThiId;

    @Column(name = "chuDeSangTao", nullable = true)
    private String chuDeSangTao;

    @Column(name = "huanLuyenVienId", nullable = true)
    private String huanLuyenVienId;

    @Column(name = "ketQuaSoLoai", nullable = true)
    private Integer ketQuaSoLoai;

    @Column(name = "thuTuXepHang")
    private Integer thuTuXepHang;

    @Column(name = "bangDiemThi", nullable = true)
    private String bangDiemThi;

    @Column(name = "hangGiaiThuong", nullable = true)
    private String hangGiaiThuong;

    @Column(name = "thoiGianTao")
    private long thoiGianTao;

    @Column(name = "thoiGianCapNhat")
    private long thoiGianCapNhat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuocThiId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private CuocThi cuocThi;
    
    public DoiThi(
        String tenGoi,
        CuocThi cuocThi,
        String doanThiId,
        String khoiThiId,
        String chuDeSangTao,
        String huanLuyenVienId,
        Integer ketQuaSoLoai,
        Integer thuTuXepHang,
        String bangDiemThi,
        String hangGiaiThuong
    ) {
        this.tenGoi = tenGoi;
        this.cuocThi = cuocThi;
        this.doanThiId = doanThiId;
        this.khoiThiId = khoiThiId;
        this.chuDeSangTao = chuDeSangTao;
        this.huanLuyenVienId = huanLuyenVienId;
        this.ketQuaSoLoai = ketQuaSoLoai;
        this.thuTuXepHang = thuTuXepHang;
        this.bangDiemThi = bangDiemThi;
        this.hangGiaiThuong = hangGiaiThuong;
        this.thoiGianTao = System.currentTimeMillis();
        this.thoiGianCapNhat = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DoiThi) {
            DoiThi temp = (DoiThi)o;
            return (id != null && id.contentEquals(temp.getId()));
        }
        else {
            return false;
        }
    }
}
