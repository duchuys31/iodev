package vn.iodev.contestmanagementsystem.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.iodev.contestmanagementsystem.converter.BangDiemThiConverter;

@Entity(name = "T_DanhSachThi")
@Table(name = "T_DanhSachThi")
@NoArgsConstructor
@Setter
@Getter
public class DanhSachThi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "danhSachThiId")
    private long danhSachThiId;

    @Column(name = "thiSinhId")
    private String thiSinhId;

    @Column(name = "khoiThiId", nullable = true)
    private String khoiThiId;

    @Column(name = "doiThiId", nullable = true)
    private String doiThiId;

    @Column(name = "chuDeSangTao", nullable = true)
    private String chuDeSangTao;

    @Column(name = "soBaoDanh", nullable = true)
    private String soBaoDanh;

    @Column(name = "ketQuaSoLoai", nullable = true)
    private Integer ketQuaSoLoai;

    @Column(name = "thuTuXepHang", nullable = true)
    private Integer thuTuXepHang;

    @Column(name = "bangDiemThi", nullable = true)
    @Convert(converter = BangDiemThiConverter.class)
    private Map<String, Object> bangDiemThi;

    @Column(name = "hangGiaiThuong", nullable = true)
    private String hangGiaiThuong;
    
    @Column(name = "tenGiaiThuong", nullable = true)
    private String tenGiaiThuong;

    @Column(name = "thoiGianTao")
    private long thoiGianTao;

    @Column(name = "thoiGianCapNhat")
    private long thoiGianCapNhat;
    
    @Column(name = "doanThiId")
    private String doanThiId;
    
    @Column(name = "soBaoDanhDoi")
    private String soBaoDanhDoi;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuocThiId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private CuocThi cuocThi;

    public DanhSachThi(
        String thiSinhId,
        CuocThi cuocThi,
        String khoiThiId,
        String doiThiId,
        String chuDeSangTao,
        String soBaoDanh,
        Integer ketQuaSoLoai,
        Integer thuTuXepHang,
        Map<String, Object> bangDiemThi,
        String hangGiaiThuong
    ) {
        this.thiSinhId = thiSinhId;
        this.cuocThi = cuocThi;
        this.khoiThiId = khoiThiId;
        this.doiThiId = doiThiId;
        this.chuDeSangTao = chuDeSangTao;
        this.soBaoDanh = soBaoDanh;
        this.ketQuaSoLoai = ketQuaSoLoai;
        this.thuTuXepHang = thuTuXepHang;
        this.bangDiemThi = bangDiemThi;
        this.hangGiaiThuong = hangGiaiThuong;
        this.thoiGianTao = System.currentTimeMillis();
        this.thoiGianCapNhat = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DanhSachThi) {
            DanhSachThi temp = (DanhSachThi)o;
            return danhSachThiId == temp.getDanhSachThiId();
        }
        else {
            return false;
        }
    }
}
