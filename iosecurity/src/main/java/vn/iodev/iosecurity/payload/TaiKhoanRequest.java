package vn.iodev.iosecurity.payload;

import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TaiKhoanRequest {
    @Email
    private String email;
    private Integer loaiTaiKhoan;
    private String matKhau;
}
