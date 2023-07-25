package vn.iodev.contestmanagementsystem.exportdata;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class DanhSachDangKyMau2 {

	public static final Pattern dataPattern = Pattern.compile("\\$([a-z0-9\\_]+|[A-Z0-9\\_]+)\\$",
			Pattern.CASE_INSENSITIVE);

	public static final String tenCuocThi = "$ten_cuoc_thi$";
	public static final String ngayBatDau = "$ngay_bat_dau$";
	public static final String ngayKetThuc = "$ngay_ket_thuc$";
	public static final String diaDiemToChuc = "$dia_diem_to_chuc$";
	public static final String tenDoan = "$ten_doan$";
	public static final String thuTu = "$thu_tu$";
	public static final String maDoanThi = "$ma_doan_thi$";
	public static final String hoTen = "$ho_ten$";
	public static final String email = "$email$";
	public static final String soDienThoai = "$so_dien_thoai$";
	public static final String namThu = "$nam_thu$";
	public static final String khoaDaoTao = "$khoa_dao_tao$";
	public static final String ngayThangNamSinh = "$ngay_thang_nam_sinh$";
	public static final String gioiTinhNam = "$gt_nam$";
	public static final String gioiTinhNu = "$gt_nu$";
	public static final String chuyenTin = "$chuyen_tin$";
	public static final String khongChuyenTin = "$khong_chuyen_tin$";
	public static final String icpcChuyenTin = "$icpc_chuyen_tin$";
	public static final String icpcKhongChuyenTin = "$icpc_khong_chuyen$";
	public static final String caoDang = "$cao_dang$";
	public static final String procon = "$procon$";
	public static final String pmnm = "$pmnm$";
	public static final String khoiSieuCup_DangKy = "$khoi_sieu_cup_dang_ky$";
	public static final String khoiSieuCup_GiaiDaDat = "$khoi_sieu_cup_giai_da_dat$";

	public static final String loop = "$loop$";
	
	public static final List<String> colMap = Arrays.asList(thuTu, hoTen,
			 ngayThangNamSinh,  gioiTinhNam,  gioiTinhNu,  maDoanThi,  namThu,
			 khoaDaoTao,  email,  soDienThoai,  chuyenTin,  khongChuyenTin,
			 icpcChuyenTin,  icpcKhongChuyenTin,  caoDang,  procon,  pmnm,
			 khoiSieuCup_DangKy,  khoiSieuCup_GiaiDaDat);
}
