package vn.iodev.contestmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.iodev.contestmanagementsystem.model.CuocThi;
import vn.iodev.contestmanagementsystem.model.HuanLuyenVien;

public interface HuanLuyenVienRepository extends JpaRepository<HuanLuyenVien, String> {
    List<HuanLuyenVien> findByCuocThiId(String cuocThiId, Pageable pageable);
    List<HuanLuyenVien> findByCuocThiId(String cuocThiId);
    List<HuanLuyenVien> findByCuocThiIdAndDoanThiId(String cuocThiId, String doanThiId);
    Optional<HuanLuyenVien> findByHoTenAndEmailAndSoDienThoai(String hoTen, String email, String soDienThoai);
    Optional<HuanLuyenVien> findByHoTenAndCuocThiIdAndDoanThiIdAndEmailAndSoDienThoai(String hoTen, String cuocThiId, String doanThiId, String email, String soDienThoai);

    @Query("SELECT hlv FROM T_HuanLuyenVien hlv WHERE (:tuKhoa is null OR hlv.hoTen like %:tuKhoa%) AND (:cuocThi is null OR hlv.cuocThi = :cuocThi) AND (:khoiThiId is null OR hlv.khoiThiId = :khoiThiId) AND (:doanThiId is null OR hlv.doanThiId = :doanThiId)")
    List<HuanLuyenVien> findHuanLuyenVienByMultipleConditions(@Param("tuKhoa") String tuKhoa, @Param("cuocThi") CuocThi cuocThi, @Param("khoiThiId") String khoiThiId, @Param("doanThiId") String doanThiId, Pageable pageable);

    @Transactional
    void deleteByCuocThiId(String cuocThiId);

    long countByCuocThiIdAndDoanThiId(String cuocThiId, String doanThiId);
    
    //add by trungnt
    @Query("SELECT hlv FROM T_HuanLuyenVien hlv WHERE hlv.doanThiId IN (:doanThiIds)")
    List<HuanLuyenVien> findByDoanThiIds(List<String> doanThiIds, Pageable pageable);
}
