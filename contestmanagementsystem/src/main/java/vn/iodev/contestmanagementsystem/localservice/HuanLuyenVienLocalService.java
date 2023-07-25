package vn.iodev.contestmanagementsystem.localservice;

import java.util.List;

import org.springframework.data.domain.Pageable;

import vn.iodev.contestmanagementsystem.model.HuanLuyenVien;

public interface HuanLuyenVienLocalService {
	public List<HuanLuyenVien> findByToChucId(String toChucId, Pageable pageable);
}
