package vn.iodev.iosecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ToChucRequest {
    private String email;
    private String loaiToChuc;
    private String tenGoi;
}
