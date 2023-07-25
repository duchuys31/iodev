package vn.iodev.iosecurity.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.iodev.iosecurity.repository.TaiKhoanRepository;

@Service
public class TaiKhoanService implements UserDetailsService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(username);
        if (taiKhoan == null) {
            throw new UsernameNotFoundException(username);    
        }
        else if (taiKhoan.getTinhTrang() == LoaiTinhTrang.CHO_KICH_HOAT || taiKhoan.getTinhTrang() == LoaiTinhTrang.DONG_TAI_KHOAN) {
            throw new UsernameNotFoundException(username);
        }
        return IOUserDetails.build(taiKhoan);
    }
    
}
