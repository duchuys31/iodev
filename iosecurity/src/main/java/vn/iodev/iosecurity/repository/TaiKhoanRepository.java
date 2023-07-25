package vn.iodev.iosecurity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.iodev.iosecurity.model.TaiKhoan;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {
    TaiKhoan findByEmail(String email);
    Optional<TaiKhoan> findById(String id);
    List<TaiKhoan> findByEmailContaining(String email);
    List<TaiKhoan> findByTinhTrang(Integer tinhTrang);
    List<TaiKhoan> findByEmailContainingAndTinhTrang(String email, Integer tinhTrang);
    Boolean existsByEmail(String email);
    boolean existsById(String id);
}
