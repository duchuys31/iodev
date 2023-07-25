package vn.iodev.humanresources.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.iodev.humanresources.model.ToChuc;

public interface ToChucRepository extends JpaRepository<ToChuc, String> {
    @Transactional
    Optional<ToChuc> findByEmail(String email);
    @Transactional
    @Query("SELECT tc FROM T_ToChuc tc WHERE (:tuKhoa is null OR tc.tenGoi like %:tuKhoa%) AND (:tenGoi is null OR tc.tenGoi = :tenGoi) AND (:loaiToChuc is null OR tc.loaiToChuc = :loaiToChuc) AND (:email is null OR tc.email = :email) AND (:tinhTrang is null OR tc.tinhTrang = :tinhTrang)")
    List<ToChuc> findToChucByMultipleConditions(@Param("tuKhoa") String tuKhoa, @Param("tenGoi") String tenGoi, @Param("loaiToChuc") String loaiToChuc, @Param("email") String email, @Param("tinhTrang") Integer tinhTrang, Pageable pageable);

    @Transactional
    Optional<ToChuc> findByTenGoiOrTiengAnhOrTenVietTat(String tenGoi, String tiengAnh, String tenVietTat);
}
