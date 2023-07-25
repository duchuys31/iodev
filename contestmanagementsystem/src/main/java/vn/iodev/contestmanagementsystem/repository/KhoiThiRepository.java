package vn.iodev.contestmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.iodev.contestmanagementsystem.model.KhoiThi;

public interface KhoiThiRepository extends JpaRepository<KhoiThi, String> {
    List<KhoiThi> findByCuocThiId(String cuocThiId, Pageable pageable);
    List<KhoiThi> findByCuocThiId(String cuocThiId);
    List<KhoiThi> findByIdIn(List<String> khoiThiIds);
    
    Optional<KhoiThi> findByTenGoiAndCuocThiId(String tenGoi, String cuocThiId);

    @Transactional
    void deleteByCuocThiId(String cuocThiId);
}
