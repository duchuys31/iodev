package vn.iodev.iosecurity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "T_VaiTro",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "ten")
    }
)
public class VaiTro {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EVaiTro getTen() {
        return ten;
    }

    public void setTen(EVaiTro ten) {
        this.ten = ten;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private EVaiTro ten;

    public VaiTro() {}

    public VaiTro(EVaiTro ten) {
        this.ten = ten;
    }

    @Override
    public String toString() {
        return this.ten.toString();
    }
}
