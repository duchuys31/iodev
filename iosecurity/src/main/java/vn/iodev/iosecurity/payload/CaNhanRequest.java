package vn.iodev.iosecurity.payload;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CaNhanRequest {
    private Date ngaySinh;
    private int gioiTinh;
    private String email;
    private String hoTen;
}
