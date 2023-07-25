package vn.iodev.contestmanagementsystem.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.contestmanagementsystem.helper.ExcelHelper;
import vn.iodev.contestmanagementsystem.helper.KetQuaThi;
import vn.iodev.contestmanagementsystem.localservice.impl.BaoCaoLocalServiceImpl;
import vn.iodev.contestmanagementsystem.model.CuocThi;
import vn.iodev.contestmanagementsystem.model.DanhSachThi;
import vn.iodev.contestmanagementsystem.model.DoanThi;
import vn.iodev.contestmanagementsystem.model.DoiThi;
import vn.iodev.contestmanagementsystem.model.HuanLuyenVien;
import vn.iodev.contestmanagementsystem.model.KhoiThi;
import vn.iodev.contestmanagementsystem.model.LoaiTinhTrangToChuc;
import vn.iodev.contestmanagementsystem.model.ThiSinh;
import vn.iodev.contestmanagementsystem.payload.ToChucResponse;
import vn.iodev.contestmanagementsystem.repository.CuocThiRepository;
import vn.iodev.contestmanagementsystem.repository.DanhSachThiRepository;
import vn.iodev.contestmanagementsystem.repository.DoanThiRepository;
import vn.iodev.contestmanagementsystem.repository.DoiThiRepository;
import vn.iodev.contestmanagementsystem.repository.HuanLuyenVienRepository;
import vn.iodev.contestmanagementsystem.repository.KhoiThiRepository;
import vn.iodev.contestmanagementsystem.repository.ThiSinhRepository;
import vn.iodev.contestmanagementsystem.utils.RandomUtil;

@Service
@Slf4j
public class ExcelService {
    @Autowired
    DoanThiRepository doanThiRepository;

    @Autowired
    ThiSinhRepository thiSinhRepository;

    @Autowired
    HuanLuyenVienRepository huanLuyenVienRepository;

    @Autowired
    CuocThiRepository cuocThiRepository;

    @Autowired
    ToChucService toChucService;

    @Autowired
    KhoiThiRepository khoiThiRepository;

    @Autowired
    DanhSachThiRepository danhSachThiRepository;

    @Autowired
    DoiThiRepository doiThiRepository;
    
    @Autowired
    BaoCaoLocalServiceImpl baoCaoLocalServiceImpl;
    
    @Value("${io.report.template.folder}")
	private String templateFolderName;
    
    @Value("${io.report.folder}")
	private String exportFolderName;
    
    @Value("${io.report.danhsachdangkymau1.name}")
	private String danhSachDangKyMau1Name;
    
    @Value("${io.report.danhsachdangkymau1.fc}")
   	private Integer fc;
    @Value("${io.report.danhsachdangkymau1.lc}")
   	private Integer lc;
    @Value("${io.report.danhsachdangkymau1.fr}")
   	private Integer fr;
    @Value("${io.report.danhsachdangkymau1.lr}")
   	private Integer lr;
 
    @Value("${io.report.danhsachdangkymau1.looprow}")
   	private Integer looprow;
    
    
	@Value("${io.report.danhsachdangkymau2.name}")
	private String danhsachdangkymau2Name;
	@Value("${io.report.danhsachdangkymau2.fc}")
	private Integer danhsachdangkymau2_fc;
	@Value("${io.report.danhsachdangkymau2.lc}")
	private Integer danhsachdangkymau2_lc;
	@Value("${io.report.danhsachdangkymau2.fr}")
	private Integer danhsachdangkymau2_fr;
	@Value("${io.report.danhsachdangkymau2.lr}")
	private Integer danhsachdangkymau2_lr;
	@Value("${io.report.danhsachdangkymau2.looprow}")
	
	private Integer danhsachdangkymau2_looprow;
	
	
	@Value("${io.report.danhsachdangkykhoithicanhanmau1.name}")
	private String danhsachdangkykhoithicanhanmau1Name;
	@Value("${io.report.danhsachdangkykhoithicanhanmau1.fc}")
	private Integer danhsachdangkykhoithicanhanmau1_fc;
	@Value("${io.report.danhsachdangkykhoithicanhanmau1.lc}")
	private Integer danhsachdangkykhoithicanhanmau1_lc;
	@Value("${io.report.danhsachdangkykhoithicanhanmau1.fr}")
	private Integer danhsachdangkykhoithicanhanmau1_fr;
	@Value("${io.report.danhsachdangkykhoithicanhanmau1.lr}")
	private Integer danhsachdangkykhoithicanhanmau1_lr;
	@Value("${io.report.danhsachdangkykhoithicanhanmau1.looprow}")
	private Integer danhsachdangkykhoithicanhanmau1_looprow;
	
	
	@Value("${io.report.danhsachdangkykhoithitapthemau1.name}")
	private String danhsachdangkykhoithitapthemau1Name;
	@Value("${io.report.danhsachdangkykhoithitapthemau1.fc}")
	private Integer danhsachdangkykhoithitapthemau1_fc;
	@Value("${io.report.danhsachdangkykhoithitapthemau1.lc}")
	private Integer danhsachdangkykhoithitapthemau1_lc;
	@Value("${io.report.danhsachdangkykhoithitapthemau1.fr}")
	private Integer danhsachdangkykhoithitapthemau1_fr;
	@Value("${io.report.danhsachdangkykhoithitapthemau1.lr}")
	private Integer danhsachdangkykhoithitapthemau1_lr;
	@Value("${io.report.danhsachdangkykhoithitapthemau1.looprow}")
	private Integer danhsachdangkykhoithitapthemau1_looprow;

	@Value("${io.report.thisinh.qrcode.datalink}")
	private String dataLink;

    public void importDoanThi(MultipartFile file) {
        try {
            List<DoanThi> doanthis = ExcelHelper.excelXlsxToDoanThis(file.getInputStream());
            for (DoanThi dt : doanthis) {
                try {
                	Optional<DoanThi> doanThiData = doanThiRepository.findByTenGoiOrTiengAnh(dt.getTenGoi(), dt.getTiengAnh());
                    if (doanThiData.isPresent()) {
                        dt.setId(doanThiData.get().getId());
                    }
                    else {
                        dt.setThoiGianTao(System.currentTimeMillis());
                    }
                    dt.setThoiGianCapNhat(System.currentTimeMillis());
				} catch (Exception e) {
					log.warn(e.getMessage());
					continue;
				}
            }
            doanThiRepository.saveAll(doanthis);
        }
        catch (IOException e) {
            log.debug("importDoanThi", e);
            
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }  
    
	public void importThiSinh(MultipartFile file, CuocThi cuocThi) {
		try {
			List<ThiSinh> thisinhs = ExcelHelper.excelXlsxToThiSinhs(file.getInputStream());
			for (ThiSinh ts : thisinhs) {

				try {
					ts.setCuocThi(cuocThi);
					Optional<ThiSinh> thiSinhData = thiSinhRepository
							.findByHoTenAndGioiTinhAndNgaySinhAndNganhDaoTaoAndCuocThiId(ts.getHoTen(),
									ts.getGioiTinh(), ts.getNgaySinh(), ts.getNganhDaoTao(), cuocThi.getId());
					if (thiSinhData.isPresent()) {
						ts.setId(thiSinhData.get().getId());
					} else {
						ts.setThoiGianTao(System.currentTimeMillis());
					}
					ts.setThoiGianCapNhat(System.currentTimeMillis());

				} catch (Exception e) {
					log.warn(e.getMessage());
					continue;
				}
			}
			thiSinhRepository.saveAll(thisinhs);
		} catch (IOException e) {
			log.debug("importThiSinh", e);

			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}

	public void importHuanLuyenVien(MultipartFile file, CuocThi cuocThi) {
		try {
			List<HuanLuyenVien> huanluyenviens = ExcelHelper.excelXlsxToHuanLuyenViens(file.getInputStream());
			for (HuanLuyenVien hlv : huanluyenviens) {

				try {
					hlv.setCuocThi(cuocThi);
					Optional<HuanLuyenVien> huanLuyenVienData = huanLuyenVienRepository
							.findByHoTenAndEmailAndSoDienThoai(hlv.getHoTen(), hlv.getEmail(), hlv.getSoDienThoai());
					if (huanLuyenVienData.isPresent()) {
						hlv.setId(huanLuyenVienData.get().getId());
					} else {
						hlv.setThoiGianTao(System.currentTimeMillis());
					}
					hlv.setThoiGianCapNhat(System.currentTimeMillis());
				} catch (Exception e) {
					log.warn(e.getMessage());
					continue;
				}
			}
			huanLuyenVienRepository.saveAll(huanluyenviens);
		} catch (IOException e) {
			log.debug("importHuanLuyenVien", e);

			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}
    
    public List<ToChucResponse> importToChuc(MultipartFile file) {
        List<ToChucResponse> lstToChucs = new ArrayList<>();

        try {
            List<ToChucResponse> tochucs = ExcelHelper.excelXlsxToToChucs(file.getInputStream());
            for (ToChucResponse tcr : tochucs) {
                try {
                	tcr.setTinhTrang(LoaiTinhTrangToChuc.DA_CHINH_THUC);
                    ToChucResponse[] findTC = toChucService.getToChucByTenGoi(tcr.getTenGoi());
                    if (findTC.length > 0) {
                        lstToChucs.add(findTC[0]);
                    }
                    else {
                        ToChucResponse result = toChucService.createToChuc(tcr);
                        lstToChucs.add(result);
                    }
				} catch (Exception e) {
					log.warn(e.getMessage());
					continue;
				}
            }
        }
        catch (IOException e) {
            log.debug("importToChuc", e);
            
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }

        return lstToChucs;
    }

	public List<CuocThi> importCuocThi(MultipartFile file) {
		try {
			List<CuocThi> cuocthis = ExcelHelper.excelXlsxToCuocThis(file.getInputStream());

			for (CuocThi cuocThi : cuocthis) {
				try {
					CuocThi oldCuocThi = cuocThiRepository.findByTenGoiAndSerieCuocThiAndLanToChucAndDonViToChuc(
							cuocThi.getTenGoi(), cuocThi.getSerieCuocThi(), cuocThi.getLanToChuc(),
							cuocThi.getDonViToChuc());
					if (oldCuocThi != null) {
						cuocThi.setId(oldCuocThi.getId());
					}
					if (cuocThi.getDonViToChuc() != null && !cuocThi.getDonViToChuc().isEmpty()) {
						ToChucResponse[] toChucs = toChucService.getToChucByTenGoi(cuocThi.getDonViToChuc());
						if (toChucs.length > 0) {
							cuocThi.setToChucId(toChucs[0].getId());
						}
					}
				} catch (Exception e) {
					log.warn(e.getMessage());
					continue;
				}
			}

			return cuocThiRepository.saveAll(cuocthis);
		} catch (IOException e) {
			log.debug("importCuocThi", e);

			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}

    public List<KhoiThi> importKhoiThi(MultipartFile file, List<CuocThi> cuocthis) {
        try {
            List<KhoiThi> khoithis = ExcelHelper.excelXlsxToKhoiThis(file.getInputStream());
            for (KhoiThi khoiThi : khoithis) {
                if (khoiThi.getTenCuocThi() != null && !khoiThi.getTenCuocThi().isEmpty()) {
                    for (CuocThi ct : cuocthis) {
                    	try {
                    		if (ct.getTenGoi().equals(khoiThi.getTenCuocThi())) {
                                khoiThi.setCuocThi(ct);
                                Optional<KhoiThi> oldKhoiThi = khoiThiRepository.findByTenGoiAndCuocThiId(khoiThi.getTenGoi(), khoiThi.getCuocThi().getId());
                                if (oldKhoiThi.isPresent()) {
                                    khoiThi.setId(oldKhoiThi.get().getId());
                                }
                            }
						} catch (Exception e) {
							log.warn(e.getMessage());
							continue;
						}
                        
                    }
                }
            }

            return khoiThiRepository.saveAll(khoithis);
        }
        catch (IOException e) {
            log.debug("importKhoiThi", e);
            
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public List<HuanLuyenVien> importHuanLuyenVien(MultipartFile file, List<ToChucResponse> tochucs, List<KhoiThi> khoithis, List<DoanThi> doanthis, CuocThi cuocThi) {
        try {
            List<HuanLuyenVien> huanLuyenViens = ExcelHelper.excelXlsxToHuanLuyenViens(file.getInputStream());
            StringBuilder khoiThiId = new StringBuilder();
            for (KhoiThi khoiThi : khoithis) {
                if (!khoiThiId.toString().equals("")) {
                    khoiThiId.append(",");
                }
                khoiThiId.append(khoiThi.getId());
            }
            for (HuanLuyenVien huanLuyenVien : huanLuyenViens) {
                huanLuyenVien.setKhoiThiId(khoiThiId.toString());
                if (huanLuyenVien.getTenToChuc() != null && !huanLuyenVien.getTenToChuc().isEmpty()) {
                    for (ToChucResponse tc : tochucs) {
                        if (tc.getTenGoi().equals(huanLuyenVien.getTenToChuc())) {
                            DoanThi doanThi = new DoanThi(huanLuyenVien.getTenDoanThi(), "", tc.getDiaChiHoatDong(), tc.getEmail(), tc.getId(), cuocThi.getId());
                            Optional<DoanThi> oldDoanThi = doanThiRepository.findByTenGoiAndToChucIdAndCuocThiId(doanThi.getTenGoi(), doanThi.getToChucId(), cuocThi.getId());
                            if (oldDoanThi.isPresent()) {
                                doanThi.setId(oldDoanThi.get().getId());
                            }
                            DoanThi dtKQ = doanThiRepository.save(doanThi);
                            doanthis.add(dtKQ);

                            huanLuyenVien.setDoanThiId(dtKQ.getId());
                            Optional<HuanLuyenVien> oldHuanLuyenVien = huanLuyenVienRepository.findByHoTenAndCuocThiIdAndDoanThiIdAndEmailAndSoDienThoai(huanLuyenVien.getHoTen(), cuocThi.getId(), huanLuyenVien.getDoanThiId(), huanLuyenVien.getEmail(), huanLuyenVien.getSoDienThoai());
                            
                            if (oldHuanLuyenVien.isPresent()) {
                                huanLuyenVien.setId(oldHuanLuyenVien.get().getId());
                            }
            
                            break;
                        }
                    }
                }
                huanLuyenVien.setCuocThi(cuocThi);
            }

            return huanLuyenVienRepository.saveAll(huanLuyenViens);
        }
        catch (IOException e) {
            log.debug("importHuanLuyenVien", e);
            
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

	public void importDanhSachThi(MultipartFile file, List<CuocThi> cuocthis, List<KhoiThi> khoithis,
			List<DoanThi> doanthis) {
		try {
			List<ThiSinh> thisinhs = ExcelHelper.excelXlsxToDanhSachThis(file.getInputStream(),
					ExcelHelper.SHEET_DANHSACHTHI_OLPCHUYENTIN);
			thisinhs.addAll(ExcelHelper.excelXlsxToDanhSachThis(file.getInputStream(),
					ExcelHelper.SHEET_DANHSACHTHI_OLPKHONGCHUYENTIN));
			thisinhs.addAll(ExcelHelper.excelXlsxToDanhSachThis(file.getInputStream(),
					ExcelHelper.SHEET_DANHSACHTHI_ICPCCHUYENTIN));
			thisinhs.addAll(ExcelHelper.excelXlsxToDanhSachThis(file.getInputStream(),
					ExcelHelper.SHEET_DANHSACHTHI_ICPCKHONGCHUYENTIN));
			thisinhs.addAll(ExcelHelper.excelXlsxToDanhSachThis(file.getInputStream(),
					ExcelHelper.SHEET_DANHSACHTHI_OLPCAODANG));
			thisinhs.addAll(ExcelHelper.excelXlsxToDanhSachThis(file.getInputStream(),
					ExcelHelper.SHEET_DANHSACHTHI_OLPPROCON));
			thisinhs.addAll(
					ExcelHelper.excelXlsxToDanhSachThis(file.getInputStream(), ExcelHelper.SHEET_DANHSACHTHI_OLPPMNM));
			thisinhs.addAll(ExcelHelper.excelXlsxToDanhSachThis(file.getInputStream(),
					ExcelHelper.SHEET_DANHSACHTHI_OLPSIEUCUP));

			for (ThiSinh ts : thisinhs) {
				for (CuocThi cuocThi : cuocthis) {
					if (cuocThi.getTenGoi().equals(ts.getTenCuocThi())) {
						ts.setCuocThi(cuocThi);
						for (DoanThi doanThi : doanthis) {
							if (doanThi.getTenGoi().equals(ts.getTenDoanThi())
									&& doanThi.getCuocThiId().equals(cuocThi.getId())) {
								ts.setDoanThiId(doanThi.getId());
							}
						}
						break;
					}
				}
				KhoiThi khoiThi = null;
				for (KhoiThi kt : khoithis) {
					if (kt.getTenGoi().equals(ts.getTenKhoiThi())) {
						khoiThi = kt;
						break;
					}
				}
				try {
					if (khoiThi != null && !khoiThi.getThiTapThe()) {
						// DoanThi doanThi = null;
						// for (DoanThi dt : doanthis) {
						// if (dt.getTenGoi().equals(ts.getTenDoanThi())) {
						// doanThi = dt;
						// break;
						// }
						// }
						Optional<ThiSinh> thiSinhData = thiSinhRepository
								.findByHoTenAndGioiTinhAndNgaySinhAndNganhDaoTaoAndCuocThiId(ts.getHoTen(),
										ts.getGioiTinh(), ts.getNgaySinh(), ts.getNganhDaoTao(),
										ts.getCuocThi().getId());
						if (thiSinhData.isPresent()) {
							ThiSinh tempTs = thiSinhData.get();
							if (tempTs.getDoanThiId() != null && tempTs.getDoanThiId().equals(ts.getDoanThiId())
									&& tempTs.getCuocThi().getId().equals(ts.getCuocThi().getId()))
								ts.setId(thiSinhData.get().getId());
						} else {
							ts.setThoiGianTao(System.currentTimeMillis());
						}
						ts.setThoiGianCapNhat(System.currentTimeMillis());

						ThiSinh thiSinhMoi = thiSinhRepository.save(ts);
						if (ts.isThamGia()) {
							DanhSachThi danhSachThi = new DanhSachThi(thiSinhMoi.getId(), ts.getCuocThi(),
									khoiThi != null ? khoiThi.getId() : null, null, null, null, null, null, null, null);
							Optional<DanhSachThi> oldDstData = danhSachThiRepository
									.findByThiSinhIdAndCuocThiIdAndKhoiThiIdAndDoiThiId(thiSinhMoi.getId(),
											thiSinhMoi.getCuocThi().getId(), khoiThi != null ? khoiThi.getId() : null,
											null);
							if (oldDstData.isPresent()) {
								danhSachThi.setDanhSachThiId(oldDstData.get().getDanhSachThiId());
							}
							
							danhSachThi.setDoanThiId(thiSinhMoi.getDoanThiId());

							danhSachThiRepository.save(danhSachThi);
						}
					} else if (khoiThi != null && khoiThi.getThiTapThe()) {
						DoanThi doanThi = null;
						for (DoanThi dt : doanthis) {
							if (dt.getTenGoi().equals(ts.getTenDoanThi())) {
								doanThi = dt;
								break;
							}
						}
						Optional<DoiThi> doiThiData = doiThiRepository.findByTenGoiAndCuocThiIdAndDoanThiIdAndKhoiThiId(
								ts.getTenDoiThi(), ts.getCuocThi() != null ? ts.getCuocThi().getId() : null,
								doanThi != null ? doanThi.getId() : null, khoiThi != null ? khoiThi.getId() : null);
						Optional<ThiSinh> thiSinhData = thiSinhRepository
								.findByHoTenAndGioiTinhAndNgaySinhAndNganhDaoTaoAndCuocThiId(ts.getHoTen(),
										ts.getGioiTinh(), ts.getNgaySinh(), ts.getNganhDaoTao(),
										ts.getCuocThi().getId());
						if (thiSinhData.isPresent()) {
							ThiSinh tempTs = thiSinhData.get();
							if (tempTs.getDoanThiId() != null && tempTs.getDoanThiId().equals(ts.getDoanThiId())
									&& tempTs.getCuocThi().getId().equals(ts.getCuocThi().getId()))
								ts.setId(thiSinhData.get().getId());
						} else {
							ts.setThoiGianTao(System.currentTimeMillis());
						}
						ts.setThoiGianCapNhat(System.currentTimeMillis());

						ThiSinh thiSinhMoi = thiSinhRepository.save(ts);
						if (ts.isThamGia()) {
							DoiThi doiThi = new DoiThi(ts.getTenDoiThi(), ts.getCuocThi(),
									doanThi != null ? doanThi.getId() : null, khoiThi != null ? khoiThi.getId() : null,
									null, null, null, null, null, null);
							if (doiThiData.isPresent()) {
								doiThi.setId(doiThiData.get().getId());
							}
							if (doiThi.getTenGoi() == null || doiThi.getTenGoi().isEmpty()) {
								doiThi.setTenGoi(RandomUtil.generateRandomAlphanumeric(16));
							}
							DoiThi doiThiMoi = doiThiRepository.save(doiThi);

							DanhSachThi danhSachThi = new DanhSachThi(thiSinhMoi.getId(), ts.getCuocThi(),
									khoiThi != null ? khoiThi.getId() : null, doiThiMoi.getId(), null, null, null, null,
									null, null);
							Optional<DanhSachThi> oldDstData = danhSachThiRepository
									.findByThiSinhIdAndCuocThiIdAndKhoiThiIdAndDoiThiId(thiSinhMoi.getId(),
											thiSinhMoi.getCuocThi().getId(), khoiThi != null ? khoiThi.getId() : null,
											doiThiData.isPresent() ? doiThiData.get().getId() : null);
							if (oldDstData.isPresent()) {
								danhSachThi.setDanhSachThiId(oldDstData.get().getDanhSachThiId());
							}
							
							danhSachThi.setDoanThiId(thiSinhMoi.getDoanThiId());

							danhSachThiRepository.save(danhSachThi);
						}
					}
				} catch (Exception e) {
					log.warn(e.getMessage());
					continue;

				}
			}

		} catch (IOException e) {
			log.debug("importDanhSachThi", e);

			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}

    public void importDanhSachThi(MultipartFile file, List<CuocThi> cuocthis, List<KhoiThi> khoithis) {
        try {
            ExcelHelper.excelXlsxToTongHopThiSinh(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 

    public void importKetQuaCuocThi(MultipartFile file, CuocThi cuocThi) {
        try {
            List<KetQuaThi> ketQuaThis = ExcelHelper.excelToKetQuaDanhSachThis(file.getInputStream(), ExcelHelper.SHEET_KETQUATHI_OLPSIEUCUP);
            ketQuaThis.addAll(ExcelHelper.excelToKetQuaDanhSachThis(file.getInputStream(), ExcelHelper.SHEET_KETQUATHI_OLPCHUYENTIN));
            ketQuaThis.addAll(ExcelHelper.excelToKetQuaDanhSachThis(file.getInputStream(), ExcelHelper.SHEET_KETQUATHI_OLPKHONGCHUYENTIN));
            ketQuaThis.addAll(ExcelHelper.excelToKetQuaDanhSachThis(file.getInputStream(), ExcelHelper.SHEET_KETQUATHI_OLPCAODANG));
            ketQuaThis.addAll(ExcelHelper.excelToKetQuaDanhSachThis(file.getInputStream(), ExcelHelper.SHEET_KETQUATHI_OLPPROCON));
            ketQuaThis.addAll(ExcelHelper.excelToKetQuaDanhSachThis(file.getInputStream(), ExcelHelper.SHEET_KETQUATHI_OLPPMNM));

            for (KetQuaThi ketQuaThi : ketQuaThis) {
                String tenKhoiThi = ketQuaThi.getKhoiThi();
                Optional<KhoiThi> khoiThiData = khoiThiRepository.findByTenGoiAndCuocThiId(tenKhoiThi, cuocThi.getId());
                
                ToChucResponse[] toChucResponse = toChucService.getToChucByTenGoi(ketQuaThi.getTenTruong());
                String toChucId = toChucResponse[0].getId();
                List<DoanThi> doanThis = doanThiRepository.findByToChucIdAndCuocThiId(toChucId, cuocThi.getId());
                if (khoiThiData.isPresent() && doanThis.size() > 0) {
                    KhoiThi khoiThi = khoiThiData.get();
                    if (khoiThi.getThiTapThe()) {
                        Optional<DoiThi> doiThiData = doiThiRepository.findByTenGoiAndCuocThiIdAndDoanThiIdAndKhoiThiId(ketQuaThi.getDoiTuong(), cuocThi.getId(), doanThis.get(0).getId(), khoiThi.getId());
                        
                        if (doiThiData.isPresent()) {
                            DoiThi doiThi = doiThiData.get();
                            List<DanhSachThi> danhSachThis = danhSachThiRepository.findByCuocThiIdAndKhoiThiIdAndDoiThiId(cuocThi.getId(), khoiThi.getId(), doiThiData.get().getId());
                            for (DanhSachThi danhSachThi : danhSachThis) {
                                danhSachThi.setThuTuXepHang(ketQuaThi.getSTT());
                                danhSachThi.setHangGiaiThuong(ketQuaThi.getGiaiThuong());
                            }
                            danhSachThiRepository.saveAll(danhSachThis);
                            doiThi.setThuTuXepHang(ketQuaThi.getSTT());
                            doiThi.setHangGiaiThuong(ketQuaThi.getGiaiThuong());
                            doiThiRepository.save(doiThi);
                        }
                    }
                    else {
                        log.info("Find " + ketQuaThi.getDoiTuong() + "," + cuocThi.getId() + ", " + doanThis.get(0).getId());

                        Optional<ThiSinh> thiSinhData = thiSinhRepository.findByHoTenAndCuocThiIdAndDoanThiId(ketQuaThi.getDoiTuong(), cuocThi.getId(), doanThis.get(0).getId());
                        if (thiSinhData.isPresent()) {
                            ThiSinh thiSinh = thiSinhData.get();
                            log.info("Found " + thiSinh.getId());

                            List<DanhSachThi> danhSachThis = danhSachThiRepository.findByCuocThiIdAndKhoiThiIdAndThiSinhId(cuocThi.getId(), khoiThi.getId(), thiSinh.getId());
                            for (DanhSachThi danhSachThi : danhSachThis) {
                                danhSachThi.setThuTuXepHang(ketQuaThi.getSTT());
                                danhSachThi.setHangGiaiThuong(ketQuaThi.getGiaiThuong());
                            }
                            danhSachThiRepository.saveAll(danhSachThis);
                        }
                    }
                }
            }
            
        }
        catch (IOException e) {
            log.debug("importKetQuaCuocThi", e);
            
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    } 
    
    public File exportDanhSachDangKyMau1(String doanThiId) {
    	
    	String homeDir = System.getProperty("user.dir");
    	
    	String templateFilePath = homeDir + "/" + templateFolderName + "/" + danhSachDangKyMau1Name;
    	
    	String outputPath = homeDir + "/" + exportFolderName + "/" + System.currentTimeMillis() + "_danhsachdangky.xlsx";
    	
    	log.info("homeDir:{}", homeDir);
    	
    	log.info("templateFilePath:{}", templateFilePath);
    	
    	log.info("outputPath:{}", outputPath);
    	
    	HashMap<String, Object> data = baoCaoLocalServiceImpl.getDanhSachDangKyMau1(doanThiId);
    	
    	if(data == null) {
    		return null;
    	}
    	
    	log.info("data size:{}", data.size());
    	
    	return ExcelHelper.exportDanhSachDangKyMau1(templateFilePath, outputPath, fc, lc, fr, lr, looprow,  data);
    }
    
    public File exportDanhSachDangKyMau2(String cuocThiId) {
    	
    	String homeDir = System.getProperty("user.dir");
    	
    	String templateFilePath = homeDir + "/" + templateFolderName + "/" + danhsachdangkymau2Name;
    	
    	String outputPath = homeDir + "/" + exportFolderName + "/" + System.currentTimeMillis() + "_danhsachdangkymau2.xlsx";
    	
    	log.info("homeDir:{}", homeDir);
    	
    	log.info("templateFilePath:{}", templateFilePath);
    	
    	log.info("outputPath:{}", outputPath);
    	
    	HashMap<String, Object> data = baoCaoLocalServiceImpl.getDanhSachDangKyMau2(cuocThiId);
    	
    	if(data == null) {
    		return null;
    	}
    	
    	log.info("data size:{}", data.size());
    	
    	return ExcelHelper.exportDanhSachDangKyMau2(templateFilePath, outputPath, danhsachdangkymau2_fc, danhsachdangkymau2_lc, danhsachdangkymau2_fr, danhsachdangkymau2_lr, danhsachdangkymau2_looprow,  data);
    }
    
    public File exportDanhSachDangKyKhoiThiCaNhanMau1(String cuocThiId, String khoiThiId) {
    	
    	String homeDir = System.getProperty("user.dir");
    	
    	String templateFilePath = homeDir + "/" + templateFolderName + "/" + danhsachdangkykhoithicanhanmau1Name;
    	
    	String outputPath = homeDir + "/" + exportFolderName + "/" + System.currentTimeMillis() + "_danhsachdangkykhoithicanhanmau1.xlsx";
    	
    	log.info("homeDir:{}", homeDir);
    	
    	log.info("templateFilePath:{}", templateFilePath);
    	
    	log.info("outputPath:{}", outputPath);
    	
    	HashMap<String, Object> data = baoCaoLocalServiceImpl.getDanhSachDangKyKhoiThiCaNhanMau1(cuocThiId, khoiThiId);
    	
    	if(data == null) {
    		return null;
    	}
    	
    	log.info("data size:{}", data.size());
    	
    	return ExcelHelper.exportDanhSachDangKyKhoiThiCaNhanMau1(templateFilePath, outputPath, danhsachdangkykhoithicanhanmau1_fc, danhsachdangkykhoithicanhanmau1_lc, danhsachdangkykhoithicanhanmau1_fr, danhsachdangkykhoithicanhanmau1_lr, danhsachdangkykhoithicanhanmau1_looprow,  data);
    }
    
    public File exportDanhSachDangKyKhoiThiTapTheMau1(String cuocThiId, String khoiThiId) {
    	
    	String homeDir = System.getProperty("user.dir");
    	
    	String templateFilePath = homeDir + "/" + templateFolderName + "/" + danhsachdangkykhoithitapthemau1Name;
    	
    	String outputPath = homeDir + "/" + exportFolderName + "/" + System.currentTimeMillis() + "_danhsachdangkykhoithitapthemau1.xlsx";
    	
    	log.info("homeDir:{}", homeDir);
    	
    	log.info("templateFilePath:{}", templateFilePath);
    	
    	log.info("outputPath:{}", outputPath);
    	
    	HashMap<String, Object> data = baoCaoLocalServiceImpl.getDanhSachDangKyKhoiThiTapTheMau1(cuocThiId, khoiThiId);
    	
    	if(data == null) {
    		return null;
    	}
    	
    	log.info("data size:{}", data.size());
    	
    	return ExcelHelper.exportDanhSachDangKyKhoiThiTapTheMau1(templateFilePath, outputPath, danhsachdangkykhoithitapthemau1_fc, danhsachdangkykhoithitapthemau1_lc, danhsachdangkykhoithitapthemau1_fr, danhsachdangkykhoithitapthemau1_lr, danhsachdangkykhoithitapthemau1_looprow,  data);
    }
    
    public void importDanhSachDangKyKhoiThiCaNhanMau1(MultipartFile multipartFile) {
		try {
			List<HashMap<String, Object>> data = ExcelHelper.getDanhSachThiKhoiThiCaNhanMau1Data(
					multipartFile.getInputStream(), danhsachdangkykhoithicanhanmau1_fc, danhsachdangkykhoithicanhanmau1_lc,
					danhsachdangkykhoithicanhanmau1_looprow);
			System.out.println("data size" + data.size());
			baoCaoLocalServiceImpl.updateDanhSachDangKyKhoiThiCaNhanMau1(data);
			
		} catch (IOException e) {

			e.printStackTrace();

			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}
    
	public void importDanhSachDangKyKhoiThiTapTheMau1(MultipartFile multipartFile) {
		try {
			List<HashMap<String, Object>> data = ExcelHelper.getDanhSachDangKyKhoiThiTapTheMau1Data(
					multipartFile.getInputStream(), danhsachdangkykhoithitapthemau1_fc, danhsachdangkykhoithitapthemau1_lc,
					danhsachdangkykhoithitapthemau1_looprow);
			System.out.println("data size" + data.size());
			baoCaoLocalServiceImpl.updateDanhSachDangKyKhoiThiTapTheMau1(data);
			
		} catch (IOException e) {

			e.printStackTrace();

			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}
	
	public File exportDanhSachThiSinhTrongDoanWithQRCode(String cuocThiId, String doanThiId) {
    	
    	String homeDir = System.getProperty("user.dir");
    	
    	String outputPath = homeDir + "/" + exportFolderName + "/" + System.currentTimeMillis() + "danhsanhthisinhtrongdoan.xlsx";
    	
    	log.info("homeDir:{}", homeDir);

    	log.info("outputPath:{}", outputPath);
    	
    	List<HashMap<String, Object>> data = baoCaoLocalServiceImpl.getDanhSachThiSinhTrongDoan(cuocThiId, doanThiId, dataLink);
    	
    	if(data == null) {
    		return null;
    	}
    	
    	log.info("data size:{}", data.size());
    	
    	return ExcelHelper.exportDanhSachThiSinhQRCode(outputPath, data);
    }
}
