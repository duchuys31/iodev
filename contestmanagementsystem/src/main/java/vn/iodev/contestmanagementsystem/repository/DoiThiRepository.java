package vn.iodev.contestmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.iodev.contestmanagementsystem.model.CuocThi;
import vn.iodev.contestmanagementsystem.model.DoiThi;

public interface DoiThiRepository extends JpaRepository<DoiThi, String> {
    List<DoiThi> findByCuocThiId(String cuocThiId, Pageable pageable);
    List<DoiThi> findByCuocThiId(String cuocThiId);

    @Query("SELECT dt FROM T_DoiThi dt WHERE (:tuKhoa is null OR dt.tenGoi like %:tuKhoa%) AND (:cuocThi is null OR dt.cuocThi = :cuocThi) AND (:khoiThiId is null OR dt.khoiThiId = :khoiThiId) AND (:doanThiId is null OR dt.doanThiId = :doanThiId)")
    List<DoiThi> findDoiThiByMultipleConditions(@Param("tuKhoa") String tuKhoa, @Param("cuocThi") CuocThi cuocThi, @Param("khoiThiId") String khoiThiId, @Param("doanThiId") String doanThiId, Pageable pageable);

    Optional<DoiThi> findByTenGoiAndCuocThiIdAndDoanThiId(String tenGoi, String cuocThiId, String doanThiId);
    Optional<DoiThi> findByTenGoiAndCuocThiIdAndDoanThiIdAndKhoiThiId(String tenGoi, String cuocThiId, String doanThiId, String khoiThiId);
    @Transactional
    void deleteByCuocThiId(String cuocThiId);
    List<DoiThi> findByCuocThiIdAndDoanThiId(String cuocThiId, String doanThiId);
    List<DoiThi> findByCuocThiIdAndKhoiThiId(String cuocThiId, String khoiThiId);
    List<DoiThi> findByCuocThiIdAndKhoiThiIdAndDoanThiId(String cuocThiId, String khoiThiId, String doanThiId);
}
