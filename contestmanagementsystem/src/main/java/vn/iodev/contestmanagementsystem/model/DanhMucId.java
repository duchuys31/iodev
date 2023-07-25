package vn.iodev.contestmanagementsystem.model;

import java.io.Serializable;

public class DanhMucId implements Serializable {
    private String maDanhMuc;
    private String loaiDanhMuc;

    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getLoaiDanhMuc() {
        return loaiDanhMuc;
    }

    public void setLoaiDanhMuc(String loaiDanhMuc) {
        this.loaiDanhMuc = loaiDanhMuc;
    }

    public DanhMucId() {}

    public DanhMucId(String loaiDanhMuc, String maDanhMuc) {
        this.loaiDanhMuc = loaiDanhMuc;
        this.maDanhMuc = maDanhMuc;
    }
    
    @Override
    public boolean equals(Object dm) {
        if (dm instanceof DanhMucId) {
            DanhMucId otherDanhMuc = (DanhMucId)dm;
            return (this.loaiDanhMuc.equals(otherDanhMuc.getLoaiDanhMuc()) && this.getMaDanhMuc().equals(otherDanhMuc.getMaDanhMuc()));
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return loaiDanhMuc.hashCode() + maDanhMuc.hashCode();
    }
}
