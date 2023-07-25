package vn.iodev.contestmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.iodev.contestmanagementsystem.model.CuocThi;

public interface CuocThiRepository extends JpaRepository<CuocThi, String> {
    @Query("SELECT ct FROM T_CuocThi ct WHERE (:tuKhoa is null OR ct.tenGoi like %:tuKhoa%) AND (:serieCuocThi is null OR ct.serieCuocThi = :serieCuocThi) AND (:lanToChuc is null OR ct.lanToChuc = :lanToChuc) AND (:toChucId is null OR ct.toChucId = :toChucId) AND (ct.tinhTrang IN :tinhTrang)")
    List<CuocThi> findToChucByMultipleConditions(@Param("tuKhoa") String tuKhoa, @Param("serieCuocThi") String serieCuocThi, @Param("lanToChuc") Integer lanToChuc, @Param("toChucId") String toChucId, @Param("tinhTrang") List<Integer> tinhTrangs, Pageable pageable);
    
    CuocThi findByTenGoiAndSerieCuocThiAndLanToChucAndDonViToChuc(String tenGoi, String serieCuocThi, Integer lanToChuc, String donViToChuc);
    List<CuocThi> findByTenGoi(String tenGoi);
    
    Optional<CuocThi> findByMaCuocThi(String maCuocThi);
}
