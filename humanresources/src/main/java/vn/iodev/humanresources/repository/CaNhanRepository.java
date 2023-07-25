package vn.iodev.humanresources.repository;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.iodev.humanresources.model.CaNhan;

public interface CaNhanRepository extends JpaRepository<CaNhan, String> {
    @Transactional
    Optional<CaNhan> findByEmail(String email);
    @Transactional
    Optional<CaNhan> findByHoTenAndGioiTinhAndNgaySinhAndEmailOrSoDienThoai(String hoTen, Integer gioiTinh, Date ngaySinh, String email, String soDienThoai);
}
