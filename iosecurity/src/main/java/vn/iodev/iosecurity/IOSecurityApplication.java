package vn.iodev.iosecurity;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.iosecurity.model.EVaiTro;
import vn.iodev.iosecurity.model.LoaiTinhTrang;
import vn.iodev.iosecurity.model.TaiKhoan;
import vn.iodev.iosecurity.model.VaiTro;
import vn.iodev.iosecurity.repository.TaiKhoanRepository;
import vn.iodev.iosecurity.repository.VaiTroRepository;

@SpringBootApplication
@EnableEurekaClient
@EnableScheduling
@Slf4j
public class IOSecurityApplication implements CommandLineRunner {

	@Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Autowired
    VaiTroRepository vaiTroRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
	
    @Value("${io.app.admin.email:}")
    private String adminEmail;
    
    @Value("${io.app.admin.password:}")
    private String adminPassword;

    @Value("${io.app.admin.role:}")
    private String adminRole;

	public static void main(String[] args) {
		SpringApplication.run(IOSecurityApplication.class, args);
	}

	@Override
    public void run(String... args) throws Exception {
        Optional<VaiTro> vtNdData = vaiTroRepository.findByTen(EVaiTro.VAITRO_NGUOIDUNG);

        if (!vtNdData.isPresent()) {
            VaiTro vaiTro = new VaiTro(EVaiTro.VAITRO_NGUOIDUNG);
            vaiTroRepository.save(vaiTro);
        }

        vtNdData = vaiTroRepository.findByTen(EVaiTro.VAITRO_QUANTRICUOCTHI);
        if (!vtNdData.isPresent()) {
            VaiTro vaiTro = new VaiTro(EVaiTro.VAITRO_QUANTRICUOCTHI);
            vaiTroRepository.save(vaiTro);
        }

        vtNdData = vaiTroRepository.findByTen(EVaiTro.VAITRO_QUANTRIHETHONG);
        if (!vtNdData.isPresent()) {
            VaiTro vaiTro = new VaiTro(EVaiTro.VAITRO_QUANTRIHETHONG);
            vaiTroRepository.save(vaiTro);
        }

        vtNdData = vaiTroRepository.findByTen(EVaiTro.VAITRO_QUANTRITOCHUC);

        if (!vtNdData.isPresent()) {
            VaiTro vaiTro = new VaiTro(EVaiTro.VAITRO_QUANTRITOCHUC);
            vaiTroRepository.save(vaiTro);
        }

        log.info("Initialize admin data: " + adminEmail + ", " + adminPassword + ", " + adminRole);
        if (!adminEmail.isEmpty() && !adminPassword.isEmpty() && !adminRole.isEmpty()) {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setEmail(adminEmail);
            taiKhoan.setMatKhau(passwordEncoder.encode(adminPassword));
            taiKhoan.setId("SUPERADMIN");
            taiKhoan.setLoaiTaiKhoan(1);
            taiKhoan.setTinhTrang(LoaiTinhTrang.DA_KICH_HOAT);
    
            String[] vaiTroStrs = adminRole.split(",");
            Set<VaiTro> vaiTros = new HashSet<>();

            for (String tenVaiTro : vaiTroStrs) {
                if (tenVaiTro.contentEquals(EVaiTro.VAITRO_QUANTRIHETHONG.toString())) {
                    vtNdData = vaiTroRepository.findByTen(EVaiTro.VAITRO_QUANTRIHETHONG);
                } 
                else if (tenVaiTro.contentEquals(EVaiTro.VAITRO_QUANTRICUOCTHI.toString())) {
                    vtNdData = vaiTroRepository.findByTen(EVaiTro.VAITRO_QUANTRICUOCTHI);
                }
                else if (tenVaiTro.contentEquals(EVaiTro.VAITRO_QUANTRITOCHUC.toString())) {
                    vtNdData = vaiTroRepository.findByTen(EVaiTro.VAITRO_QUANTRITOCHUC);
                }
                else if (tenVaiTro.contentEquals(EVaiTro.VAITRO_NGUOIDUNG.toString())) {
                    vtNdData = vaiTroRepository.findByTen(EVaiTro.VAITRO_NGUOIDUNG);
                } 
                if (vtNdData.isPresent()) {
                    vaiTros.add(vtNdData.get());
                }    
            }
            taiKhoan.setVaiTros(vaiTros);
                
            taiKhoanRepository.save(taiKhoan);    
        }
    }
}
