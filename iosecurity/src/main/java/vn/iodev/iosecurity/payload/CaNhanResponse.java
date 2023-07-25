package vn.iodev.iosecurity.payload;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CaNhanResponse {
    private String id;
    private String hoTen;
    private int gioiTinh;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy", locale = "vi_VN", timezone = "Asia/Ho_Chi_Minh")
    private Date ngaySinh;
    private String email;
    private String soDienThoai;
    private String avatar;
    private String linkedIn;
    private String github;
    private String google;
    private int tinhTrang;
    private long thoiGianTao;
    private long thoiGianCapNhat;
}
