package vn.iodev.contestmanagementsystem.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "T_DanhMuc")
@Table(name = "T_DanhMuc")
@IdClass(DanhMucId.class)
@Setter
@Getter
@NoArgsConstructor
public class DanhMuc {
    @Id
    @Column(name = "loaiDanhMuc", length = 512)
    private String loaiDanhMuc;

    @Id
    @Column(name = "maDanhMuc", length = 512)
    private String maDanhMuc;

    @Column(name = "giaTri", nullable = false, columnDefinition = "TEXT")
    private String giaTri;

    public DanhMuc(String loaiDanhMuc, String maDanhMuc, String giaTri) {
        this.loaiDanhMuc = loaiDanhMuc;
        this.maDanhMuc = maDanhMuc;
        this.giaTri = giaTri;
    }
}
