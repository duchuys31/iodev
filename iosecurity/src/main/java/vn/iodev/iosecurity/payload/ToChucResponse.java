package vn.iodev.iosecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ToChucResponse {
    private String id;
    private String tenGoi;
    private String tiengAnh;
    private String loaiToChuc;
    private String diaChiHoatDong;
    private String viTriDiaLy;
    private String email;
    private String web;
    private String logo;
    private Integer tinhTrang;
    private long thoiGianTao;
    private long thoiGianCapNhat;
}
