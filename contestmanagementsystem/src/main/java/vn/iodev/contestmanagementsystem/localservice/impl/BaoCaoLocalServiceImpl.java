package vn.iodev.contestmanagementsystem.localservice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.contestmanagementsystem.exportdata.DanhSachDangKyKhoiThiCaNhanMau1;
import vn.iodev.contestmanagementsystem.exportdata.DanhSachDangKyKhoiThiTapTheMau1;
import vn.iodev.contestmanagementsystem.exportdata.DanhSachDangKyMau1;
import vn.iodev.contestmanagementsystem.exportdata.DanhSachDangKyMau2;
import vn.iodev.contestmanagementsystem.model.CuocThi;
import vn.iodev.contestmanagementsystem.model.DanhMuc;
import vn.iodev.contestmanagementsystem.model.DanhMucId;
import vn.iodev.contestmanagementsystem.model.DanhSachThi;
import vn.iodev.contestmanagementsystem.model.DoanThi;
import vn.iodev.contestmanagementsystem.model.DoiThi;
import vn.iodev.contestmanagementsystem.model.HuanLuyenVien;
import vn.iodev.contestmanagementsystem.model.KhoiThi;
import vn.iodev.contestmanagementsystem.model.ThiSinh;
import vn.iodev.contestmanagementsystem.payload.ThongKeResponse;
import vn.iodev.contestmanagementsystem.repository.CuocThiRepository;
import vn.iodev.contestmanagementsystem.repository.DanhMucRepository;
import vn.iodev.contestmanagementsystem.repository.DanhSachThiRepository;
import vn.iodev.contestmanagementsystem.repository.DoanThiRepository;
import vn.iodev.contestmanagementsystem.repository.DoiThiRepository;
import vn.iodev.contestmanagementsystem.repository.HuanLuyenVienRepository;
import vn.iodev.contestmanagementsystem.repository.KhoiThiRepository;
import vn.iodev.contestmanagementsystem.repository.ThiSinhRepository;
import vn.iodev.contestmanagementsystem.utils.DatetimeUtil;

@Service
@Slf4j
public class BaoCaoLocalServiceImpl {
	@Autowired
	private DoanThiRepository doanThiRepository;

	@Autowired
	private CuocThiRepository cuocThiRepository;

	@Autowired
	private HuanLuyenVienRepository huanLuyenVienRepository;

	@Autowired
	private DanhSachThiRepository danhSachThiRepository;

	@Autowired
	private ThiSinhRepository thiSinhRepository;

	@Autowired
	private KhoiThiRepository khoiThiRepository;

	@Autowired
	private DoiThiRepository doiThiRepository;

	@Autowired
	private DanhMucRepository danhMucRepository;

	public HashMap<String, Object> getDanhSachDangKyMau1(String doanThiId) {

		HashMap<String, Object> data = new HashMap<>();

		Optional<DoanThi> doanThiOpt = doanThiRepository.findById(doanThiId);

		if (!doanThiOpt.isPresent()) {
			return null;
		}

		DoanThi doanThi = doanThiOpt.get();

		if (ObjectUtils.isEmpty(doanThi.getCuocThiId())) {
			return null;
		}

		Optional<CuocThi> cuocThiOpt = cuocThiRepository.findById(doanThi.getCuocThiId());

		if (!cuocThiOpt.isPresent()) {
			return null;
		}

		CuocThi cuocThi = cuocThiOpt.get();

		List<HuanLuyenVien> huanLuyenViens = huanLuyenVienRepository.findByCuocThiIdAndDoanThiId(cuocThi.getId(),
				doanThiId);

		data.put(DanhSachDangKyMau1.tenCuocThi, cuocThi.getTenGoi());

		data.put(DanhSachDangKyMau1.ngayBatDau,
				DatetimeUtil.dateToString(cuocThi.getNgayBatDau(), DatetimeUtil._VN_DATE_FORMAT));

		data.put(DanhSachDangKyMau1.ngayKetThuc,
				DatetimeUtil.dateToString(cuocThi.getNgayKetThuc(), DatetimeUtil._VN_DATE_FORMAT));

		data.put(DanhSachDangKyMau1.diaDiemToChuc, cuocThi.getDiaDiemToChuc());

		data.put(DanhSachDangKyMau1.tenDoan, doanThi.getTenGoi());

		data.put(DanhSachDangKyMau1.diaChiDoan, doanThi.getDiaChiHoatDong());

		// danhSachDangKyMau1.setDiaChiDoan(doanThi.getDiaChiHoatDong());

		if (huanLuyenViens != null) {
			for (HuanLuyenVien huanLuyenVien : huanLuyenViens) {
				if (huanLuyenVien.getTruongPhoDoan() == 1) {
					data.put(DanhSachDangKyMau1.tenTruongDoan, huanLuyenVien.getHoTen());
					data.put(DanhSachDangKyMau1.emailTruongDoan, huanLuyenVien.getEmail());
					data.put(DanhSachDangKyMau1.soDienThoaiTruongDoan, huanLuyenVien.getSoDienThoai());
				} else if (huanLuyenVien.getTruongPhoDoan() == 2) {
					data.put(DanhSachDangKyMau1.tenPhoDoan, huanLuyenVien.getHoTen());
					data.put(DanhSachDangKyMau1.emailPhoDoan, huanLuyenVien.getEmail());
					data.put(DanhSachDangKyMau1.soDienThoaiPhoDoan, huanLuyenVien.getSoDienThoai());
				}
			}
		}

		List<DanhSachThi> danhSachThis = danhSachThiRepository.findByCuocThiIdAndDoanThiId(cuocThi.getId(), doanThiId);

		List<HashMap<String, Object>> danhSachDangKy = new ArrayList<HashMap<String, Object>>();

		List<HashMap<String, Object>> danhSachDangKyOLP = new ArrayList<HashMap<String, Object>>();

		danhSachDangKyOLP.add(createDataRowDanhSachDangKyMau1(null, "OLP", null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null));

		List<HashMap<String, Object>> danhSachDangKyICPC = new ArrayList<HashMap<String, Object>>();

		danhSachDangKyICPC.add(createDataRowDanhSachDangKyMau1(null, "ICPC", null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null));

		LinkedHashMap<String, List<HashMap<String, Object>>> doiThiMap = new LinkedHashMap<>();

		int olpCount = 1;

		if (danhSachThis != null) {
			for (DanhSachThi danhSachThi : danhSachThis) {

				if (ObjectUtils.isEmpty(danhSachThi.getThiSinhId())) {
					log.info("thiSinhId is null at danhSachThi:{}", danhSachThi.getDanhSachThiId());
					continue;
				}

				Optional<ThiSinh> thiSinhOpt = thiSinhRepository.findById(danhSachThi.getThiSinhId());

				if (!thiSinhOpt.isPresent()) {
					continue;
				}

				ThiSinh thiSinh = thiSinhOpt.get();

				if (ObjectUtils.isEmpty(danhSachThi.getKhoiThiId())) {
					log.info("khoiThiId is null at danhSachThi:{}", danhSachThi.getDanhSachThiId());
					continue;
				}

				Optional<KhoiThi> khoiThiOpt = khoiThiRepository.findById(danhSachThi.getKhoiThiId());

				/*
				 * if (ObjectUtils.isEmpty(danhSachThi.getDoiThiId())) {
				 * log.info("doiThiId is null at danhSachThi:{}",
				 * danhSachThi.getDanhSachThiId()); continue; }
				 */

				DoiThi doiThi = null;

				if (!ObjectUtils.isEmpty(danhSachThi.getDoiThiId())) {
					Optional<DoiThi> doiThiOpt = doiThiRepository.findById(danhSachThi.getDoiThiId());
					doiThi = doiThiOpt.isPresent() ? doiThiOpt.get() : null;
				}

				KhoiThi khoiThi = khoiThiOpt.isPresent() ? khoiThiOpt.get() : null;

				String lienHe = thiSinh.getEmail();

				if (!ObjectUtils.isEmpty(thiSinh.getSoDienThoai())) {
					lienHe += ObjectUtils.isEmpty(lienHe) ? thiSinh.getSoDienThoai()
							: (", " + thiSinh.getSoDienThoai());
				}

				String chuyenTin = "";
				String khongChuyenTin = "";
				String icpcChuyenTin = "";
				String icpcKhongChuyen = "";
				String caoDang = "";
				String procon = "";
				String pmnm = "";

				String tenKhoi = khoiThi != null ? khoiThi.getTenGoi().toLowerCase() : "";

				String maKhoi = (khoiThi != null && !ObjectUtils.isEmpty(khoiThi.getMaKhoi())) ? khoiThi.getMaKhoi()
						: "";

				switch (maKhoi) {
				case "PROCON":
					procon = "X";
					break;
				case "PMNM":
					pmnm = "X";
					break;
				case "ICPC-CT":
					icpcChuyenTin = "X";
					break;
				case "ICPC-KC":
					icpcKhongChuyen = "X";
					break;
				case "OLP-CUP":

					break;
				case "OLP-CT":
					chuyenTin = "X";
					break;
				case "OLP-KC":
					khongChuyenTin = "X";
					break;
				case "OLP-CĐ":
					caoDang = "X";
					break;
				default:
					break;
				}

				if (doiThi == null) {
					// add to olp

					HashMap<String, Object> dataRow = createDataRowDanhSachDangKyMau1(olpCount, thiSinh.getHoTen(),
							thiSinh.getDoiTuongThi(), lienHe, thiSinh.getNganhDaoTao(), null,
							DatetimeUtil.dateToString(thiSinh.getNgaySinh(), DatetimeUtil._VN_DATE_FORMAT),
							thiSinh.getGioiTinh() == 0 ? "X" : null, thiSinh.getGioiTinh() == 1 ? "X" : null, chuyenTin,
							khongChuyenTin, icpcChuyenTin, icpcKhongChuyen, caoDang, procon, pmnm,
							thiSinh.getDatGiaiThuong(), null);

					danhSachDangKyOLP.add(dataRow);
					olpCount++;
				} else {
					String tenDoi = doiThi.getTenGoi();
					List<HashMap<String, Object>> dataRows = new ArrayList<HashMap<String, Object>>();
					if (!doiThiMap.containsKey(tenDoi)) {
						dataRows.add(createDataRowDanhSachDangKyMau1(null, tenDoi, null, null, null, null, null, null,
								null, null, null, null, null, null, null, null, null, null));

						doiThiMap.put(tenDoi, dataRows);
					} else {
						dataRows = doiThiMap.get(tenDoi);
					}

					HashMap<String, Object> dataRow = createDataRowDanhSachDangKyMau1(dataRows.size(),
							thiSinh.getHoTen(), thiSinh.getDoiTuongThi(), lienHe, thiSinh.getNganhDaoTao(), null,
							DatetimeUtil.dateToString(thiSinh.getNgaySinh(), DatetimeUtil._VN_DATE_FORMAT),
							thiSinh.getGioiTinh() == 0 ? "X" : null, thiSinh.getGioiTinh() == 1 ? "X" : null, chuyenTin,
							khongChuyenTin, icpcChuyenTin, icpcKhongChuyen, caoDang, procon, pmnm,
							thiSinh.getDatGiaiThuong(), null);

					dataRows.add(dataRow);

					doiThiMap.put(tenDoi, dataRows);

				}
			}

			doiThiMap.forEach((k, v) -> {
				danhSachDangKyICPC.addAll(v);
			});

			danhSachDangKy.addAll(danhSachDangKyOLP);
			danhSachDangKy.addAll(danhSachDangKyICPC);

			data.put(DanhSachDangKyMau1.loop, danhSachDangKy);
		}

		return data;

	}

	public HashMap<String, Object> getDanhSachDangKyMau2(String cuocThiId) {
		Optional<CuocThi> cuocThiOpt = cuocThiRepository.findById(cuocThiId);
		if (!cuocThiOpt.isPresent()) {
			return null;
		}

		CuocThi cuocThi = cuocThiOpt.get();

		List<DoanThi> doanThis = doanThiRepository.findByCuocThiId(cuocThiId);

		List<KhoiThi> khoiThis = khoiThiRepository.findByCuocThiId(cuocThiId);

		HashMap<String, KhoiThi> khoiThiMap = new HashMap<>();

		List<HashMap<String, Object>> dataRows = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> data = new HashMap<String, Object>();

		data.put(DanhSachDangKyMau2.tenCuocThi, cuocThi.getTenGoi());

		data.put(DanhSachDangKyMau2.ngayBatDau,
				DatetimeUtil.dateToString(cuocThi.getNgayBatDau(), DatetimeUtil._VN_DATE_FORMAT));

		data.put(DanhSachDangKyMau2.ngayKetThuc,
				DatetimeUtil.dateToString(cuocThi.getNgayKetThuc(), DatetimeUtil._VN_DATE_FORMAT));

		data.put(DanhSachDangKyMau2.diaDiemToChuc, cuocThi.getDiaDiemToChuc());

		if (khoiThis != null) {
			for (KhoiThi khoiThi : khoiThis) {
				khoiThiMap.put(khoiThi.getId(), khoiThi);
			}
		}
		int doanThiCount = 1;

		if (doanThis != null) {
			for (DoanThi doanThi : doanThis) {
				List<ThiSinh> thiSinhs = thiSinhRepository.findByCuocThiIdAndDoanThiId(cuocThiId, doanThi.getId());
				List<DoiThi> doiThis = doiThiRepository.findByCuocThiIdAndDoanThiId(cuocThiId, doanThi.getId());

				HashMap<String, DoiThi> doiThiMap = new HashMap<>();

				HashMap<String, Object> dataRow = createDataRowDanhSachDangKyMau2(doanThiCount, doanThi.getTenGoi(),
						null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
						null);

				dataRows.add(dataRow);
				doanThiCount++;

				if (doiThis != null) {
					for (DoiThi doiThi : doiThis) {
						doiThiMap.put(doiThi.getId(), doiThi);
					}
				}

				int thiSinhCount = 1;
				if (thiSinhs != null) {
					for (ThiSinh thiSinh : thiSinhs) {
						String hoTen = thiSinh.getHoTen();
						String email = thiSinh.getEmail();
						String soDienThoai = thiSinh.getSoDienThoai();
						String ngayThangNamSinh = DatetimeUtil.dateToString(thiSinh.getNgaySinh(),
								DatetimeUtil._VN_DATE_FORMAT);
						String khoaDaoTao = thiSinh.getNganhDaoTao();
						String maDoanThi = doanThi.getMaDoanThi();
						String gioiTinhNam = (thiSinh.getGioiTinh() != null && thiSinh.getGioiTinh() == 0) ? "X" : "";
						String gioiTinhNu = (thiSinh.getGioiTinh() != null && thiSinh.getGioiTinh() == 1) ? "X" : "";
						List<DanhSachThi> danhSachThis = danhSachThiRepository.findByCuocThiIdAndThiSinhId(cuocThiId,
								thiSinh.getId());
						String chuyenTin = "";
						String khongChuyenTin = "";
						String icpcChuyenTin = "";
						String icpcKhongChuyenTin = "";
						String caoDang = "";
						String procon = "";
						String pmnm = "";
						String khoiSieuCup_DangKy = "";
						String khoiSieuCup_GiaiDaDat = "";
						String namThu = thiSinh.getDoiTuongThi();

						for (DanhSachThi danhSachThi : danhSachThis) {
							String khoiThiId = danhSachThi.getKhoiThiId();

							DoiThi doiThi = null;
							if (doiThiMap.containsKey(danhSachThi.getDoiThiId())) {
								doiThi = doiThiMap.get(danhSachThi.getDoiThiId());
							}

							String tenDoi = doiThi != null ? doiThi.getTenGoi() : "";

							if (khoiThiMap.containsKey(khoiThiId)) {
								KhoiThi khoiThi = khoiThiMap.get(khoiThiId);
								if (khoiThi.getMaKhoi() != null) {
									switch (khoiThi.getMaKhoi()) {
									case "PROCON":
										procon = tenDoi;
										break;
									case "PMNM":
										pmnm = tenDoi;
										break;
									case "ICPC-CT":
										icpcChuyenTin = tenDoi;
										break;
									case "ICPC-KC":
										icpcKhongChuyenTin = tenDoi;
										break;
									case "OLP-CUP":
										khoiSieuCup_DangKy = "X";
										khoiSieuCup_GiaiDaDat = thiSinh.getDatGiaiThuong();
										break;
									case "OLP-CT":
										chuyenTin = "X";
										break;
									case "OLP-KC":
										khongChuyenTin = "X";
										break;
									case "OLP-CĐ":
										caoDang = "X";
										break;
									default:
										break;
									}
								}

							}

						}

						dataRow = createDataRowDanhSachDangKyMau2(thiSinhCount, hoTen, ngayThangNamSinh, gioiTinhNam,
								gioiTinhNu, maDoanThi, namThu, khoaDaoTao, email, soDienThoai, chuyenTin,
								khongChuyenTin, icpcChuyenTin, icpcKhongChuyenTin, caoDang, procon, pmnm,
								khoiSieuCup_DangKy, khoiSieuCup_GiaiDaDat);

						dataRows.add(dataRow);
						thiSinhCount++;
					}
				}

				data.put(DanhSachDangKyMau2.loop, dataRows);
			}
		}

		return data;

	}

	public HashMap<String, Object> getDanhSachDangKyKhoiThiCaNhanMau1(String cuocThiId, String khoiThiId) {
		Optional<CuocThi> cuocThiOpt = cuocThiRepository.findById(cuocThiId);
		if (!cuocThiOpt.isPresent()) {
			return null;
		}

		Optional<KhoiThi> khoiThiOpt = khoiThiRepository.findById(khoiThiId);

		if (!khoiThiOpt.isPresent() || khoiThiOpt.get().getThiTapThe() == true) {
			return null;
		}

		CuocThi cuocThi = cuocThiOpt.get();

		List<DoanThi> doanThis = doanThiRepository.findByCuocThiId(cuocThiId);

		List<HashMap<String, Object>> dataRows = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> data = new HashMap<String, Object>();

		data.put(DanhSachDangKyKhoiThiCaNhanMau1.tenCuocThi, cuocThi.getTenGoi());
		
		data.put(DanhSachDangKyKhoiThiTapTheMau1.tenKhoi, khoiThiOpt.get().getTenGoi());

		data.put(DanhSachDangKyKhoiThiCaNhanMau1.ngayBatDau,
				DatetimeUtil.dateToString(cuocThi.getNgayBatDau(), DatetimeUtil._VN_DATE_FORMAT));

		data.put(DanhSachDangKyKhoiThiCaNhanMau1.ngayKetThuc,
				DatetimeUtil.dateToString(cuocThi.getNgayKetThuc(), DatetimeUtil._VN_DATE_FORMAT));

		data.put(DanhSachDangKyKhoiThiCaNhanMau1.diaDiemToChuc, cuocThi.getDiaDiemToChuc());

		if (doanThis != null) {
			int thiSinhCount = 1;
			for (DoanThi doanThi : doanThis) {
				List<DanhSachThi> danhSachThis = danhSachThiRepository
						.findByCuocThiIdAndDoanThiIdAndKhoiThiId(cuocThiId, doanThi.getId(), khoiThiId);

				
				if (danhSachThis != null) {
					for (DanhSachThi danhSachThi : danhSachThis) {
						if (ObjectUtils.isEmpty(danhSachThi.getThiSinhId())) {
							log.info("thiSinhId is null at danhSachThi:{}", danhSachThi.getDanhSachThiId());
							continue;
						}

						Optional<ThiSinh> thiSinhOpt = thiSinhRepository.findById(danhSachThi.getThiSinhId());

						if (!thiSinhOpt.isPresent()) {
							log.info("not found ThiSinh with thiSinhId:{}", danhSachThi.getThiSinhId());
							continue;
						}

						ThiSinh thiSinh = thiSinhOpt.get();
						String soBaoDanh = danhSachThi.getSoBaoDanh();
						String hoTen = thiSinh.getHoTen();

						String ngayThangNamSinh = DatetimeUtil.dateToString(thiSinh.getNgaySinh(),
								DatetimeUtil._VN_DATE_FORMAT);

						String tenDoanThi = doanThi.getTenGoi();
						String gioiTinhNam = (thiSinh.getGioiTinh() != null && thiSinh.getGioiTinh() == 0) ? "X" : "";
						String gioiTinhNu = (thiSinh.getGioiTinh() != null && thiSinh.getGioiTinh() == 1) ? "X" : "";
						Integer thuHang = danhSachThi.getThuTuXepHang();
						// String hangGiaiThuong = danhSachThi.getHangGiaiThuong();
						String tenGiaiThuong = danhSachThi.getTenGiaiThuong();

						HashMap<String, Object> dataRow = createDanhSachDangKyKhoiThiCaNhanMau1(danhSachThi.getDanhSachThiId(), thiSinhCount, hoTen,
								soBaoDanh, ngayThangNamSinh, gioiTinhNam, gioiTinhNu, tenDoanThi, thuHang,
								tenGiaiThuong);

						dataRows.add(dataRow);
						
						thiSinhCount++;
					}
				}
			}
			data.put(DanhSachDangKyKhoiThiCaNhanMau1.loop, dataRows);
		}

		return data;

	}

	public HashMap<String, Object> getDanhSachDangKyKhoiThiTapTheMau1(String cuocThiId, String khoiThiId) {
		Optional<CuocThi> cuocThiOpt = cuocThiRepository.findById(cuocThiId);
		if (!cuocThiOpt.isPresent()) {
			return null;
		}

		Optional<KhoiThi> khoiThiOpt = khoiThiRepository.findById(khoiThiId);

		if (!khoiThiOpt.isPresent() || !khoiThiOpt.get().getThiTapThe() == true) {
			return null;
		}

		CuocThi cuocThi = cuocThiOpt.get();

		List<DoanThi> doanThis = doanThiRepository.findByCuocThiId(cuocThiId);

		List<HashMap<String, Object>> dataRows = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> data = new HashMap<String, Object>();

		data.put(DanhSachDangKyKhoiThiTapTheMau1.tenCuocThi, cuocThi.getTenGoi());
		
		data.put(DanhSachDangKyKhoiThiTapTheMau1.tenKhoi, khoiThiOpt.get().getTenGoi());

		data.put(DanhSachDangKyKhoiThiTapTheMau1.ngayBatDau,
				DatetimeUtil.dateToString(cuocThi.getNgayBatDau(), DatetimeUtil._VN_DATE_FORMAT));

		data.put(DanhSachDangKyKhoiThiTapTheMau1.ngayKetThuc,
				DatetimeUtil.dateToString(cuocThi.getNgayKetThuc(), DatetimeUtil._VN_DATE_FORMAT));

		data.put(DanhSachDangKyKhoiThiTapTheMau1.diaDiemToChuc, cuocThi.getDiaDiemToChuc());

		if (doanThis != null) {
			int doanThiCount = 1;
			for (DoanThi doanThi : doanThis) {
				
				String tenDoanThi = doanThi.getTenGoi();
				
				List<DoiThi> doiThis = doiThiRepository.findByCuocThiIdAndKhoiThiIdAndDoanThiId(cuocThiId, khoiThiId,
						doanThi.getId());
				boolean hasDanhSachThi = false;
				if (doiThis != null) {
					for (DoiThi doiThi : doiThis) {
						
						String tenDoi = doiThi.getTenGoi();
						
						List<DanhSachThi> danhSachThis = danhSachThiRepository
								.findByCuocThiIdAndKhoiThiIdAndDoiThiId(cuocThiId, khoiThiId, doiThi.getId());

						if (danhSachThis != null) {
							for (DanhSachThi danhSachThi : danhSachThis) {
								if (ObjectUtils.isEmpty(danhSachThi.getThiSinhId())) {
									log.info("thiSinhId is null at danhSachThi:{}", danhSachThi.getDanhSachThiId());
									continue;
								}

								Optional<ThiSinh> thiSinhOpt = thiSinhRepository.findById(danhSachThi.getThiSinhId());

								if (!thiSinhOpt.isPresent()) {
									log.info("not found ThiSinh with thiSinhId:{}", danhSachThi.getThiSinhId());
									continue;
								}

								ThiSinh thiSinh = thiSinhOpt.get();
								
								String soBaoDanh = danhSachThi.getSoBaoDanh();
								
								String hoTen = thiSinh.getHoTen();

								String ngayThangNamSinh = DatetimeUtil.dateToString(thiSinh.getNgaySinh(),
										DatetimeUtil._VN_DATE_FORMAT);

								String gioiTinhNam = (thiSinh.getGioiTinh() != null && thiSinh.getGioiTinh() == 0) ? "X"
										: "";
								String gioiTinhNu = (thiSinh.getGioiTinh() != null && thiSinh.getGioiTinh() == 1) ? "X"
										: "";
								Integer thuHang = danhSachThi.getThuTuXepHang();
								
								String giaiThuong = danhSachThi.getHangGiaiThuong();

								HashMap<String, Object> dataRow = createDanhSachDangKyKhoiThiTapTheMau1(danhSachThi.getDanhSachThiId(), doanThiCount,
										tenDoi, soBaoDanh, hoTen, ngayThangNamSinh, gioiTinhNam, gioiTinhNu, tenDoanThi,
										thuHang, giaiThuong);

								dataRows.add(dataRow);
								
								hasDanhSachThi = true;
							}
						}

						
					}
					if(hasDanhSachThi) {
						doanThiCount++;
					}
					
				}
			}
			
			data.put(DanhSachDangKyKhoiThiTapTheMau1.loop, dataRows);
		}
		
		return data;
	}
	
	public List<HashMap<String, Object>> getDanhSachThiSinhTrongDoan(String cuocThiId, String doanThiId, String dataLink){
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		List<ThiSinh> thiSinhs = thiSinhRepository.findByCuocThiIdAndDoanThiId(cuocThiId, doanThiId);
		Optional<DoanThi> doanThiOpt = doanThiRepository.findById(doanThiId);
		
		if(!doanThiOpt.isPresent()) {
			return data;
		}
		
		String maDoanThi = doanThiOpt.get().getMaDoanThi();
		
		if(thiSinhs != null) {
			for(ThiSinh thiSinh : thiSinhs) {
				String id = thiSinh.getId();
				String hoTen = thiSinh.getHoTen();
				String content = dataLink.replace("{id}", id);
				HashMap<String, Object> dataRow = new HashMap<String, Object>();
				dataRow.put("content", content);
				dataRow.put("info", hoTen + "\n" + maDoanThi);
				data.add(dataRow);
			}
		}
		return data;
	}
	
	
	public void updateDanhSachDangKyKhoiThiCaNhanMau1(List<HashMap<String, Object>> data) {
		if (data != null) {
			List<DanhSachThi> danhSachThis = new ArrayList<DanhSachThi>();

			for (HashMap<String, Object> dataRow : data) {
				Long id = (Long)dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.id);
				String hoTen = (String) dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.hoTen);
				String soBaoDanh = (String) dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.soBaoDanh);
				Date ngayThangNamSinh = (Date) dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.ngayThangNamSinh);
				String tenDoan = (String) dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.tenDoan);
				Integer thuHang = (Integer) dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.thuHang);
				String giaiThuong = (String) dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.giaiThuong);
				
				if (!ObjectUtils.isEmpty(id)) {
					Optional<DanhSachThi> danhSachThiOpt = danhSachThiRepository.findById(id);
					if(danhSachThiOpt.isPresent()) {
						DanhSachThi danhSachThi = danhSachThiOpt.get();
						//TODO validate
						
						danhSachThi.setSoBaoDanh(soBaoDanh);
						danhSachThi.setThuTuXepHang(thuHang);
						danhSachThi.setHangGiaiThuong(giaiThuong);
						danhSachThis.add(danhSachThi);
						
						if(!ObjectUtils.isEmpty(giaiThuong)) {
							Optional<DanhMuc> danhMucOpt = danhMucRepository.findById(new DanhMucId("C_HangGiaiThuong", giaiThuong));
							if(danhMucOpt.isPresent()) {
								danhSachThi.setTenGiaiThuong(danhMucOpt.get().getGiaTri());
							}
						}
						
						
					}
				}
			}
			
			if(danhSachThis != null && !danhSachThis.isEmpty()) {
				danhSachThiRepository.saveAll(danhSachThis);
			}
		}
	}
	
	public void updateDanhSachDangKyKhoiThiTapTheMau1(List<HashMap<String, Object>> data) {
		if (data != null) {
			List<DanhSachThi> danhSachThis = new ArrayList<DanhSachThi>();
			List<DoiThi> doiThis = new ArrayList<DoiThi>();
			
			for (HashMap<String, Object> dataRow : data) {
				Long id = (Long)dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.id);
				String tenDoi = (String) dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.tenDoi);
				String hoTen = (String) dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.hoTen);
				String soBaoDanh = (String) dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.soBaoDanh);
				Date ngayThangNamSinh = (Date) dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.ngayThangNamSinh);
				String tenDoan = (String) dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.tenDoan);
				Integer thuHang = (Integer) dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.thuHang);
				String giaiThuong = (String) dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.giaiThuong);
				
				if (!ObjectUtils.isEmpty(id)) {
					Optional<DanhSachThi> danhSachThiOpt = danhSachThiRepository.findById(id);
					if(danhSachThiOpt.isPresent()) {
						DanhSachThi danhSachThi = danhSachThiOpt.get();
						//TODO validate
						
						danhSachThi.setSoBaoDanhDoi(soBaoDanh);
						danhSachThi.setThuTuXepHang(thuHang);
						danhSachThi.setHangGiaiThuong(giaiThuong);
						danhSachThis.add(danhSachThi);
						if(!ObjectUtils.isEmpty(giaiThuong)) {
							Optional<DanhMuc> danhMucOpt = danhMucRepository.findById(new DanhMucId("C_HangGiaiThuong", giaiThuong));
							if(danhMucOpt.isPresent()) {
								danhSachThi.setTenGiaiThuong(danhMucOpt.get().getGiaTri());
							}
						}
						
						Optional<DoiThi> doiThiOpt = doiThiRepository.findById(danhSachThi.getDoiThiId());
						
						if(doiThiOpt.isPresent()) {
							DoiThi doiThi = doiThiOpt.get();
							doiThi.setHangGiaiThuong(giaiThuong);
							doiThi.setThuTuXepHang(thuHang);
							if(!doiThis.contains(doiThi)) {
								doiThis.add(doiThi);
							}
						}
					}
				}
			}
			
			if(danhSachThis != null && !danhSachThis.isEmpty()) {
				danhSachThiRepository.saveAll(danhSachThis);
			}
			
			if(doiThis != null && !doiThis.isEmpty()) {
				doiThiRepository.saveAll(doiThis);
			}
		}
	}

	private HashMap<String, Object> createDanhSachDangKyKhoiThiTapTheMau1(Long id, Integer thuTu, String tenDoi, 
			String soBaoDanh, String hoTen, String ngayThangNamSinh, String gioiTinhNam, String gioiTinhNu, String tenDoanThi,
			Integer thuHang, String giaiThuong) {
		HashMap<String, Object> map = new HashMap<>();
		
		map.put(DanhSachDangKyKhoiThiTapTheMau1.id, id);

		if (!ObjectUtils.isEmpty(thuTu)) {
			map.put(DanhSachDangKyKhoiThiTapTheMau1.thuTu, thuTu);
		}
		if (!ObjectUtils.isEmpty(tenDoi)) {
			map.put(DanhSachDangKyKhoiThiTapTheMau1.tenDoi, tenDoi);
		}
		if (!ObjectUtils.isEmpty(soBaoDanh)) {
			map.put(DanhSachDangKyKhoiThiTapTheMau1.soBaoDanh, soBaoDanh);
		}
		if (!ObjectUtils.isEmpty(hoTen)) {
			map.put(DanhSachDangKyKhoiThiTapTheMau1.hoTen, hoTen);
		}
		if (!ObjectUtils.isEmpty(ngayThangNamSinh)) {
			map.put(DanhSachDangKyKhoiThiTapTheMau1.ngayThangNamSinh, ngayThangNamSinh);
		}
		if (!ObjectUtils.isEmpty(gioiTinhNam)) {
			map.put(DanhSachDangKyKhoiThiTapTheMau1.gioiTinhNam, gioiTinhNam);
		}
		if (!ObjectUtils.isEmpty(gioiTinhNu)) {
			map.put(DanhSachDangKyKhoiThiTapTheMau1.gioiTinhNu, gioiTinhNu);
		}
		if (!ObjectUtils.isEmpty(tenDoanThi)) {
			map.put(DanhSachDangKyKhoiThiTapTheMau1.tenDoanThi, tenDoanThi);
		}
		if (!ObjectUtils.isEmpty(thuHang)) {
			map.put(DanhSachDangKyKhoiThiTapTheMau1.thuHang, thuHang);
		}
		if (!ObjectUtils.isEmpty(giaiThuong)) {
			map.put(DanhSachDangKyKhoiThiTapTheMau1.giaiThuong, giaiThuong);
		}
		
		return map;
	}

	private HashMap<String, Object> createDanhSachDangKyKhoiThiCaNhanMau1(Long id, Integer thuTu, String hoTen, String soBaoDanh,
			String ngayThangNamSinh, String gioiTinhNam, String gioiTinhNu, String tenDoanThi, Integer thuHang,
			String giaiThuong) {
		HashMap<String, Object> map = new HashMap<>();
		map.put(DanhSachDangKyKhoiThiCaNhanMau1.id, id);
		if (!ObjectUtils.isEmpty(thuTu)) {
			map.put(DanhSachDangKyKhoiThiCaNhanMau1.thuTu, thuTu);
		}

		if (!ObjectUtils.isEmpty(hoTen)) {
			map.put(DanhSachDangKyKhoiThiCaNhanMau1.hoTen, hoTen);
		}
		if (!ObjectUtils.isEmpty(soBaoDanh)) {
			map.put(DanhSachDangKyKhoiThiCaNhanMau1.soBaoDanh, soBaoDanh);
		}
		if (!ObjectUtils.isEmpty(ngayThangNamSinh)) {
			map.put(DanhSachDangKyKhoiThiCaNhanMau1.ngayThangNamSinh, ngayThangNamSinh);
		}
		if (!ObjectUtils.isEmpty(gioiTinhNam)) {
			map.put(DanhSachDangKyKhoiThiCaNhanMau1.gioiTinhNam, gioiTinhNam);
		}
		if (!ObjectUtils.isEmpty(gioiTinhNu)) {
			map.put(DanhSachDangKyKhoiThiCaNhanMau1.gioiTinhNu, gioiTinhNu);
		}
		if (!ObjectUtils.isEmpty(tenDoanThi)) {
			map.put(DanhSachDangKyKhoiThiCaNhanMau1.tenDoanThi, tenDoanThi);
		}
		if (!ObjectUtils.isEmpty(thuHang)) {
			map.put(DanhSachDangKyKhoiThiCaNhanMau1.thuHang, thuHang);
		}
		if (!ObjectUtils.isEmpty(giaiThuong)) {
			map.put(DanhSachDangKyKhoiThiCaNhanMau1.giaiThuong, giaiThuong);
		}

		return map;
	}

	private HashMap<String, Object> createDataRowDanhSachDangKyMau1(Integer thuTu, String hoTen, String namThu,
			String lienHe, String khoaDaoTao, String soMuiTiem, String ngayThangNamSinh, String gioiTinhNam,
			String gioiTinhNu, String chuyenTin, String khongChuyenTin, String icpcChuyenTin, String icpcKhongChuyen,
			String caoDang, String procon, String pmnm, String giai, Integer namThamDu) {
		HashMap<String, Object> map = new HashMap<>();

		if (!ObjectUtils.isEmpty(thuTu)) {
			map.put(DanhSachDangKyMau1.thuTu, thuTu);
		}

		if (!ObjectUtils.isEmpty(hoTen)) {
			map.put(DanhSachDangKyMau1.hoTen, hoTen);
		}
		if (!ObjectUtils.isEmpty(namThu)) {
			map.put(DanhSachDangKyMau1.namThu, namThu);
		}
		if (!ObjectUtils.isEmpty(lienHe)) {
			map.put(DanhSachDangKyMau1.lienHe, lienHe);
		}
		if (!ObjectUtils.isEmpty(khoaDaoTao)) {
			map.put(DanhSachDangKyMau1.khoaDaoTao, khoaDaoTao);
		}
		if (!ObjectUtils.isEmpty(soMuiTiem)) {
			map.put(DanhSachDangKyMau1.soMuiTiem, soMuiTiem);
		}
		if (!ObjectUtils.isEmpty(ngayThangNamSinh)) {
			map.put(DanhSachDangKyMau1.ngayThangNamSinh, ngayThangNamSinh);
		}
		if (!ObjectUtils.isEmpty(gioiTinhNam)) {
			map.put(DanhSachDangKyMau1.gioiTinhNam, gioiTinhNam);
		}
		if (!ObjectUtils.isEmpty(gioiTinhNu)) {
			map.put(DanhSachDangKyMau1.gioiTinhNu, gioiTinhNu);
		}
		if (!ObjectUtils.isEmpty(chuyenTin)) {
			map.put(DanhSachDangKyMau1.chuyenTin, chuyenTin);
		}
		if (!ObjectUtils.isEmpty(khongChuyenTin)) {
			map.put(DanhSachDangKyMau1.khongChuyenTin, khongChuyenTin);
		}
		if (!ObjectUtils.isEmpty(icpcChuyenTin)) {
			map.put(DanhSachDangKyMau1.icpcChuyenTin, icpcChuyenTin);
		}
		if (!ObjectUtils.isEmpty(icpcKhongChuyen)) {
			map.put(DanhSachDangKyMau1.icpcKhongChuyen, icpcKhongChuyen);
		}
		if (!ObjectUtils.isEmpty(caoDang)) {
			map.put(DanhSachDangKyMau1.caoDang, caoDang);
		}
		if (!ObjectUtils.isEmpty(procon)) {
			map.put(DanhSachDangKyMau1.procon, procon);
		}
		if (!ObjectUtils.isEmpty(pmnm)) {
			map.put(DanhSachDangKyMau1.pmnm, pmnm);
		}
		if (!ObjectUtils.isEmpty(giai)) {
			map.put(DanhSachDangKyMau1.giai, giai);
		}
		if (!ObjectUtils.isEmpty(namThamDu)) {
			map.put(DanhSachDangKyMau1.namThamDu, namThamDu);
		}

		return map;
	}

	private HashMap<String, Object> createDataRowDanhSachDangKyMau2(Integer thuTu, String hoTen,
			String ngayThangNamSinh, String gioiTinhNam, String gioiTinhNu, String maDoanThi, String namThu,
			String khoaDaoTao, String email, String soDienThoai, String chuyenTin, String khongChuyenTin,
			String icpcChuyenTin, String icpcKhongChuyenTin, String caoDang, String procon, String pmnm,
			String khoiSieuCup_DangKy, String khoiSieuCup_GiaiDaDat) {
		HashMap<String, Object> map = new HashMap<>();

		if (!ObjectUtils.isEmpty(thuTu)) {
			map.put(DanhSachDangKyMau2.thuTu, thuTu);
		}

		if (!ObjectUtils.isEmpty(hoTen)) {
			map.put(DanhSachDangKyMau2.hoTen, hoTen);
		}
		if (!ObjectUtils.isEmpty(ngayThangNamSinh)) {
			map.put(DanhSachDangKyMau2.ngayThangNamSinh, ngayThangNamSinh);
		}
		if (!ObjectUtils.isEmpty(gioiTinhNam)) {
			map.put(DanhSachDangKyMau2.gioiTinhNam, gioiTinhNam);
		}
		if (!ObjectUtils.isEmpty(gioiTinhNu)) {
			map.put(DanhSachDangKyMau2.gioiTinhNu, gioiTinhNu);
		}
		if (!ObjectUtils.isEmpty(maDoanThi)) {
			map.put(DanhSachDangKyMau2.maDoanThi, maDoanThi);
		}
		if (!ObjectUtils.isEmpty(namThu)) {
			map.put(DanhSachDangKyMau2.namThu, namThu);
		}
		if (!ObjectUtils.isEmpty(khoaDaoTao)) {
			map.put(DanhSachDangKyMau2.khoaDaoTao, khoaDaoTao);
		}
		if (!ObjectUtils.isEmpty(email)) {
			map.put(DanhSachDangKyMau2.email, email);
		}
		if (!ObjectUtils.isEmpty(soDienThoai)) {
			map.put(DanhSachDangKyMau2.soDienThoai, soDienThoai);
		}
		if (!ObjectUtils.isEmpty(chuyenTin)) {
			map.put(DanhSachDangKyMau2.chuyenTin, chuyenTin);
		}
		if (!ObjectUtils.isEmpty(khongChuyenTin)) {
			map.put(DanhSachDangKyMau2.khongChuyenTin, khongChuyenTin);
		}
		if (!ObjectUtils.isEmpty(icpcChuyenTin)) {
			map.put(DanhSachDangKyMau2.icpcChuyenTin, icpcChuyenTin);
		}
		if (!ObjectUtils.isEmpty(icpcKhongChuyenTin)) {
			map.put(DanhSachDangKyMau2.icpcKhongChuyenTin, icpcKhongChuyenTin);
		}
		if (!ObjectUtils.isEmpty(caoDang)) {
			map.put(DanhSachDangKyMau2.caoDang, caoDang);
		}
		if (!ObjectUtils.isEmpty(procon)) {
			map.put(DanhSachDangKyMau2.procon, procon);
		}
		if (!ObjectUtils.isEmpty(pmnm)) {
			map.put(DanhSachDangKyMau2.pmnm, pmnm);
		}
		if (!ObjectUtils.isEmpty(khoiSieuCup_DangKy)) {
			map.put(DanhSachDangKyMau2.khoiSieuCup_DangKy, khoiSieuCup_DangKy);
		}
		if (!ObjectUtils.isEmpty(khoiSieuCup_GiaiDaDat)) {
			map.put(DanhSachDangKyMau2.khoiSieuCup_GiaiDaDat, khoiSieuCup_GiaiDaDat);
		}

		return map;
	}

}
