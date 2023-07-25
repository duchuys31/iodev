package vn.iodev.iosecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.iodev.iosecurity.model.EVaiTro;
import vn.iodev.iosecurity.model.VaiTro;

public interface VaiTroRepository extends JpaRepository<VaiTro, Integer> {
    Optional<VaiTro> findByTen(EVaiTro ten);
}
