package vn.iodev.contestmanagementsystem.exportdata;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class DanhSachDangKyKhoiThiCaNhanMau1 {
	public static final Pattern dataPattern = Pattern.compile("\\$([a-z0-9\\_]+|[A-Z0-9\\_]+)\\$",
			Pattern.CASE_INSENSITIVE);
	public static final String id = "id";
	public static final String tenKhoi = "$ten_khoi$";
	public static final String tenCuocThi = "$ten_cuoc_thi$";
	public static final String ngayBatDau = "$ngay_bat_dau$";
	public static final String ngayKetThuc = "$ngay_ket_thuc$";
	public static final String diaDiemToChuc = "$dia_diem_to_chuc$";
	public static final String tenDoan = "$ten_doan$";
	public static final String thuTu = "$thu_tu$";
	public static final String tenDoanThi = "$ten_doan_thi$";
	public static final String hoTen = "$ho_ten$";
	public static final String soBaoDanh = "$so_bao_danh$";
	public static final String ngayThangNamSinh = "$ngay_thang_nam_sinh$";
	public static final String gioiTinhNam = "$gt_nam$";
	public static final String gioiTinhNu = "$gt_nu$";
	public static final String thuHang = "$thu_hang$";
	public static final String giaiThuong = "$giai_thuong$";

	public static final String loop = "$loop$";

	public static final List<String> colMap = Arrays.asList(thuTu, hoTen, soBaoDanh, ngayThangNamSinh, gioiTinhNam,
			gioiTinhNu, tenDoanThi, thuHang, giaiThuong);
}
