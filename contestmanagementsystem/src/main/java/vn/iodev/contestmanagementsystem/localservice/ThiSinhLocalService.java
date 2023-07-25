package vn.iodev.contestmanagementsystem.localservice;

import java.util.List;

import org.springframework.data.domain.Pageable;

import vn.iodev.contestmanagementsystem.model.ThiSinh;

public interface ThiSinhLocalService {
	public List<ThiSinh> findByToChucId(String toChucId, Pageable pageable);
}
