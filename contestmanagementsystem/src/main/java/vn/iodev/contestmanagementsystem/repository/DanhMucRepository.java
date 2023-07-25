package vn.iodev.contestmanagementsystem.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.iodev.contestmanagementsystem.model.DanhMuc;
import vn.iodev.contestmanagementsystem.model.DanhMucId;

public interface DanhMucRepository extends JpaRepository<DanhMuc, DanhMucId> {
    public List<DanhMuc> findByLoaiDanhMuc(String loaiDanhMuc, Pageable pageable);
}
