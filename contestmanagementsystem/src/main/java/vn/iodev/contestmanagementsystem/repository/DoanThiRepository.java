package vn.iodev.contestmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.iodev.contestmanagementsystem.model.DoanThi;

public interface DoanThiRepository extends JpaRepository<DoanThi, String> {
    List<DoanThi> findByToChucId(String toChucId, Pageable pageable);
    List<DoanThi> findByToChucId(String toChucId);
    List<DoanThi> findByCuocThiId(String cuocThiId, Pageable pageable);
    List<DoanThi> findByCuocThiId(String cuocThiId);
    List<DoanThi> findByToChucIdAndCuocThiId(String toChucId, String cuocThiId, Pageable pageable);
    List<DoanThi> findByToChucIdAndCuocThiId(String toChucId, String cuocThiId);
    Optional<DoanThi> findByTenGoiOrTiengAnh(String tenGoi, String tiengAnh);
    Optional<DoanThi> findByTenGoiAndToChucId(String tenGoi, String toChucId);
    Optional<DoanThi> findByTenGoiAndToChucIdAndCuocThiId(String tenGoi, String toChucId, String cuocThiId);
}
