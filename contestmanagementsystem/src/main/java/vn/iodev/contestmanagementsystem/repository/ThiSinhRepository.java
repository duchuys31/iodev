package vn.iodev.contestmanagementsystem.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.iodev.contestmanagementsystem.model.CuocThi;
import vn.iodev.contestmanagementsystem.model.ThiSinh;

public interface ThiSinhRepository extends JpaRepository<ThiSinh, String> {
    List<ThiSinh> findByCuocThiId(String cuocThiId, Pageable pageable);
    List<ThiSinh> findByCuocThiId(String cuocThiId);
    List<ThiSinh> findByHoTenAndGioiTinhAndNgaySinhAndNganhDaoTao(String hoTen, Integer gioiTinh, Date ngaySinh, String nganhDaoTao);
    Optional<ThiSinh> findByHoTenAndGioiTinhAndNgaySinhAndNganhDaoTaoAndCuocThiId(String hoTen, Integer gioiTinh, Date ngaySinh, String nganhDaoTao, String cuocThiId);
    
    @Query("SELECT ts FROM T_ThiSinh ts WHERE (:tuKhoa is null OR ts.hoTen like %:tuKhoa%) AND (:cuocThi is null OR ts.cuocThi = :cuocThi) AND (:doanThiId is null OR ts.doanThiId = :doanThiId)")
    List<ThiSinh> findThiSinhByMultipleConditions(@Param("tuKhoa") String tuKhoa, @Param("cuocThi") CuocThi cuocThi, @Param("doanThiId") String doanThiId, Pageable pageable);

    @Transactional
    void deleteByCuocThiId(String cuocThiId);

    long countByCuocThiIdAndDoanThiId(String cuocThiId, String doanThiId);
    List<ThiSinh> findByCuocThiIdAndDoanThiId(String cuocThiId, String doanThiId);
    Optional<ThiSinh> findByHoTenAndCuocThiIdAndDoanThiId(String hoTen, String cuocThiId, String doanThiId);
    Optional<ThiSinh> findByHoTenAndDoanThiId(String hoTen, String doanThiId);
    //add by trungnt
    @Query("SELECT ts FROM T_ThiSinh ts WHERE ts.doanThiId IN (:doanThiIds)")
    List<ThiSinh> findByDoanThiIds(List<String> doanThiIds, Pageable pageable);
}
