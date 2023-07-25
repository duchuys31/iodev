package vn.iodev.contestmanagementsystem.helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.contestmanagementsystem.exportdata.DanhSachDangKyKhoiThiCaNhanMau1;
import vn.iodev.contestmanagementsystem.exportdata.DanhSachDangKyKhoiThiTapTheMau1;
import vn.iodev.contestmanagementsystem.exportdata.DanhSachDangKyMau1;
import vn.iodev.contestmanagementsystem.exportdata.DanhSachDangKyMau2;
import vn.iodev.contestmanagementsystem.model.CuocThi;
import vn.iodev.contestmanagementsystem.model.DanhSachThi;
import vn.iodev.contestmanagementsystem.model.DoanThi;
import vn.iodev.contestmanagementsystem.model.HuanLuyenVien;
import vn.iodev.contestmanagementsystem.model.KhoiThi;
import vn.iodev.contestmanagementsystem.model.LoaiChucVu;
import vn.iodev.contestmanagementsystem.model.LoaiGioiTinh;
import vn.iodev.contestmanagementsystem.model.LoaiTinhTrangCuocThi;
import vn.iodev.contestmanagementsystem.model.LoaiTinhTrangToChuc;
import vn.iodev.contestmanagementsystem.model.ThiSinh;
import vn.iodev.contestmanagementsystem.payload.ToChucResponse;
import vn.iodev.contestmanagementsystem.utils.DatetimeUtil;
import vn.iodev.contestmanagementsystem.utils.ImageUtil;
import vn.iodev.contestmanagementsystem.utils.StringPatternUtil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Shape;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;

@Slf4j
public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] DOANTHI_HEADERs = { "Tên gọi", "Tiếng Anh", "Địa chỉ hoạt động", "Email", "ToChuc_ID", "CuocThi_ID", "Thứ tự xếp hạng" };
    static String SHEET_DOANTHI = "Đoàn thi";

    static String[] THISINH_HEADERs = { "Họ tên", "Giới tính", "Ngày sinh", "Email", "Số điện thoại", "Đối tượng thi", "Ngành đào tạo", "Đạt giải thưởng", "CaNhan_ID", "DoanThi_ID" };
    static String SHEET_THISINH = "Thí sinh";

    static String[] HUANLUYENVIEN_HEADERs = { "Họ tên", "Email", "Số điện thoại", "Tổ chức", "Đoàn thi", "Trưởng phó đoàn" };
    static String SHEET_HUANLUYENVIEN = "Huấn luyện viên";
    
    static String[] HEADERs_TOCHUC = { "Tên gọi", "Tiếng Anh", "Tên viết tắt", "Loại tổ chức", "Địa chỉ hoạt động", "Vị trí địa lý", "Email", "Địa chỉ trang web" };
    static String SHEET_TOCHUC = "Tổ chức";

    static String[] HEADERs_CUOCTHI = { "Tên gọi", "Tiếng Anh", "Serie cuộc thi", "Lần tổ chức", "Đơn vị tổ chức", "Ngày bắt đầu", "Ngày kết thúc", "Thông tin mô tả", "Website", "Tình trạng" };
    static String SHEET_CUOCTHI = "Cuộc thi";

    static String[] HEADERs_KHOITHI = { "Tên gọi", "Cuộc thi", "Nội dung thi", "Vòng sơ loại", "Thi tập thể", "Thi sáng tạo", "Số đăng ký tối đa", "Số thí sinh tối đa" };
    static String SHEET_KHOITHI = "Khối thi";

    static String SHEET_DANHSACHTHI = "Danh sách thi";

    static String[] DANHSACHTHI_HEADERs = { "Họ tên", "Giới tính", "Ngày sinh", "Email", "Số điện thoại", "Đối tượng thi", "Ngành đào tạo", "Đạt giải thưởng", "Đoàn thi", "Cuộc thi", "Tham gia", "Đội thi" };
    public static final String SHEET_DANHSACHTHI_OLPCHUYENTIN = "OLP chuyên tin";
    public static final String SHEET_DANHSACHTHI_OLPKHONGCHUYENTIN = "OLP không chuyên tin";
    public static final String SHEET_DANHSACHTHI_ICPCCHUYENTIN = "ICPC chuyên tin";
    public static final String SHEET_DANHSACHTHI_ICPCKHONGCHUYENTIN = "ICPC không chuyên tin";
    public static final String SHEET_DANHSACHTHI_OLPCAODANG = "OLP cao đẳng";
    public static final String SHEET_DANHSACHTHI_OLPPROCON = "OLP PROCON";
    public static final String SHEET_DANHSACHTHI_OLPPMNM = "OLP PMNM";
    public static final String SHEET_DANHSACHTHI_OLPSIEUCUP = "OLP siêu cúp";

    private static final String GIOITINH_NAM = "Nam";
    private static final String GIOITINH_NU = "Nữ";

    private static final String CHUC_VU_TRUONG_DOAN = "Trưởng đoàn";
    private static final String CHUC_VU_PHO_DOAN = "Phó đoàn";

    private static final String TINHTRANG_CUOCTHI_MODANGKI = "Mở đăng ký";
    private static final String TINHTRANG_CUOCTHI_DONGDANGKI = "Đóng đăng ký";
    private static final String TINHTRANG_CUOCTHI_DAKETTHUC = "Đã kết thúc";

    private static final String TRUE_STR = "Có";
    private static final String FALSE_STR = "Không";

    private static final String VN_DATE_FORMAT = "dd/MM/yyyy";

    public static final String SHEET_KETQUATHI_OLPCHUYENTIN = "OLP chuyên tin";
    public static final String SHEET_KETQUATHI_OLPKHONGCHUYENTIN = "OLP không chuyên tin";
    public static final String SHEET_KETQUATHI_ICPCCHUYENTIN = "ICPC chuyên tin";
    public static final String SHEET_KETQUATHI_ICPCKHONGCHUYENTIN = "ICPC không chuyên tin";
    public static final String SHEET_KETQUATHI_OLPCAODANG = "OLP cao đẳng";
    public static final String SHEET_KETQUATHI_OLPPROCON = "OLP PROCON";
    public static final String SHEET_KETQUATHI_OLPPMNM = "OLP PMNM";
    public static final String SHEET_KETQUATHI_OLPSIEUCUP = "OLP siêu cúp";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<ToChucResponse> excelXlsxToToChucs(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
    
            Sheet sheet = workbook.getSheet(SHEET_TOCHUC);
            Iterator<Row> rows = sheet.iterator();
    
            List<ToChucResponse> tochucs = new ArrayList<ToChucResponse>();
    
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
    
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
    
                ToChucResponse toChuc = new ToChucResponse();
                toChuc.setTinhTrang(LoaiTinhTrangToChuc.DA_CHINH_THUC);

                int cellIdx = 0;
                for (int i = 0; i <= 7; i++) {
                    Cell currentCell = currentRow.getCell(i);
                    cellIdx = i;
                    
                    switch (cellIdx) {
                        case 0:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                toChuc.setTenGoi(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 1:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                toChuc.setTiengAnh(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 2:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                toChuc.setTenVietTat(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 3:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                toChuc.setLoaiToChuc(currentCell.getStringCellValue());
                            }
                            break;
                        case 4:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                toChuc.setDiaChiHoatDong(currentCell.getStringCellValue());
                            }
                            break;
                        
                        case 5:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                toChuc.setViTriDiaLy(currentCell.getStringCellValue());
                            }
                            break;
                        
                        case 6:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                toChuc.setEmail(currentCell.getStringCellValue());
                            }
                            break;
                        
                        case 7:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                toChuc.setWeb(currentCell.getStringCellValue());
                            }
                            break;

                        default:
                            break;
                    }
                }
    
                tochucs.add(toChuc);
            }
    
            workbook.close();
    
            return tochucs;
        } catch (IOException e) {
            log.debug("excelXlsxToToChucs", e);
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static List<DoanThi> excelXlsxToDoanThis(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
    
            Sheet sheet = workbook.getSheet(SHEET_DOANTHI);
            Iterator<Row> rows = sheet.iterator();
    
            List<DoanThi> doanthis = new ArrayList<DoanThi>();
    
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
    
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
    
                DoanThi doanThi = new DoanThi();

                int cellIdx = 0;
                for (int i = 0; i <= 6; i++) {
                    Cell currentCell = currentRow.getCell(i);
                    cellIdx = i;
                    
                    switch (cellIdx) {
                        case 0:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                doanThi.setTenGoi(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 1:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                doanThi.setTiengAnh(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 2:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                doanThi.setDiaChiHoatDong(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 3:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                doanThi.setEmail(currentCell.getStringCellValue());
                            }
                            break;
                        case 4:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                doanThi.setToChucId(currentCell.getStringCellValue());
                            }
                            break;
                        
                        case 5:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                doanThi.setCuocThiId(currentCell.getStringCellValue());
                            }
                            break;
                        
                        case 6:
                            if (currentCell != null && currentCell.getCellType() == CellType.NUMERIC) {
                                doanThi.setThuTuXepHang((int)currentCell.getNumericCellValue());
                            }
                            break;

                        default:
                            break;
                    }
                }
    
                doanthis.add(doanThi);
            }
    
            workbook.close();
    
            return doanthis;
        } catch (IOException e) {
            log.debug("excelXlsxToDoanThis", e);
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    } 
    
    public static List<ThiSinh> excelXlsxToThiSinhs(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
    
            Sheet sheet = workbook.getSheet(SHEET_THISINH);
            Iterator<Row> rows = sheet.iterator();
    
            List<ThiSinh> thisinhs = new ArrayList<ThiSinh>();
            SimpleDateFormat format = new SimpleDateFormat(VN_DATE_FORMAT);

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
    
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
    
                ThiSinh thiSinh = new ThiSinh();

                int cellIdx = 0;
                for (int i = 0; i <= 6; i++) {
                    Cell currentCell = currentRow.getCell(i);
                    cellIdx = i;
                    
                    switch (cellIdx) {
                        case 0:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                thiSinh.setHoTen(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 1:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String gioiTinhStr = currentCell.getStringCellValue();
                                if (GIOITINH_NAM.equals(gioiTinhStr.trim())) {
                                    thiSinh.setGioiTinh(LoaiGioiTinh.NAM);
                                }
                                else if (GIOITINH_NU.equals(gioiTinhStr.trim())) {
                                    thiSinh.setGioiTinh(LoaiGioiTinh.NU);
                                }
                            }
                            break;
    
                        case 2:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String ngaySinhStr = currentCell.getStringCellValue();
                                Date parsed;
                                try {
                                    parsed = format.parse(ngaySinhStr);
                                    thiSinh.setNgaySinh(new java.sql.Date(parsed.getTime()));
                                } catch (ParseException e) {
                                    log.debug("excelXlsxToThiSinhs", e);
                                }
                            }
                            break;
    
                        case 3:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String email = currentCell.getStringCellValue();
                                if (email != null && !email.isEmpty()) {
                                    thiSinh.setEmail(email);
                                }
                            }
                            break;
                        case 4:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                thiSinh.setSoDienThoai(currentCell.getStringCellValue());
                            }
                            break;
                        
                        case 5:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                thiSinh.setDoiTuongThi(currentCell.getStringCellValue());
                            }
                            break;
                        
                        case 6:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                thiSinh.setNganhDaoTao(currentCell.getStringCellValue());
                            }
                            break;
                        
                        case 7:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                thiSinh.setDatGiaiThuong(currentCell.getStringCellValue());
                            }
                            break;

                        default:
                            break;
                    }
                }
    
                thisinhs.add(thiSinh);
            }
    
            workbook.close();
    
            return thisinhs;
        } catch (IOException e) {
            log.debug("excelXlsxToThiSinhs", e);
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    } 

    public static List<HuanLuyenVien> excelXlsxToHuanLuyenViens(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
    
            Sheet sheet = workbook.getSheet(SHEET_HUANLUYENVIEN);
            Iterator<Row> rows = sheet.iterator();
    
            List<HuanLuyenVien> huanluyenviens = new ArrayList<HuanLuyenVien>();
    
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
    
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
    
                HuanLuyenVien huanLuyenVien = new HuanLuyenVien();

                int cellIdx = 0;
                for (int i = 0; i <= 6; i++) {
                    Cell currentCell = currentRow.getCell(i);
                    cellIdx = i;
                    
                    switch (cellIdx) {
                        case 0:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                huanLuyenVien.setHoTen(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 1:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                huanLuyenVien.setEmail(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 2:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                huanLuyenVien.setSoDienThoai(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 3:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                huanLuyenVien.setTenToChuc(currentCell.getStringCellValue());
                            }
                            break;
                        case 4:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                huanLuyenVien.setTenDoanThi(currentCell.getStringCellValue());
                            }
                            break;
                        
                        case 5:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String truongPhoDoanStr = currentCell.getStringCellValue();
                                if (CHUC_VU_TRUONG_DOAN.equals(truongPhoDoanStr.trim())) {
                                    huanLuyenVien.setTruongPhoDoan(LoaiChucVu.TRUONG_DOAN);
                                }
                                else if (CHUC_VU_PHO_DOAN.equals(truongPhoDoanStr.trim())) {
                                    huanLuyenVien.setTruongPhoDoan(LoaiChucVu.PHO_DOAN);
                                }
                            }
                            break;

                        default:
                            break;
                    }
                }
    
                huanluyenviens.add(huanLuyenVien);
            }
    
            workbook.close();
    
            return huanluyenviens;
        } catch (IOException e) {
            log.debug("excelXlsxToHuanLuyenViens", e);
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static List<CuocThi> excelXlsxToCuocThis(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
    
            Sheet sheet = workbook.getSheet(SHEET_CUOCTHI);
            Iterator<Row> rows = sheet.iterator();
    
            List<CuocThi> cuocthis = new ArrayList<CuocThi>();
            SimpleDateFormat format = new SimpleDateFormat(VN_DATE_FORMAT);

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
    
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
    
                CuocThi cuocThi = new CuocThi();

                int cellIdx = 0;
                for (int i = 0; i <= 11; i++) {
                    Cell currentCell = currentRow.getCell(i);
                    cellIdx = i;
                    
                    switch (cellIdx) {
                        case 0:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                cuocThi.setId(currentCell.getStringCellValue());
                            }
                            break;

                        case 1:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                cuocThi.setTenGoi(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 2:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                cuocThi.setTiengAnh(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 3:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                cuocThi.setSerieCuocThi(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 4:
                            if (currentCell != null && currentCell.getCellType() == CellType.NUMERIC) {
                                cuocThi.setLanToChuc((int)currentCell.getNumericCellValue());
                            }
                            break;
                        case 5:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String tenDonViToChuc = currentCell.getStringCellValue();
                                cuocThi.setDonViToChuc(tenDonViToChuc);
                            }
                            break;

                        case 6:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String diaDiemToChuc = currentCell.getStringCellValue();
                                cuocThi.setDiaDiemToChuc(diaDiemToChuc);
                            }
                            break;

                        case 7:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {  
                                String ngayBatDauStr = currentCell.getStringCellValue();
                                Date parsed;
                                try {
                                    parsed = format.parse(ngayBatDauStr);
                                    cuocThi.setNgayBatDau(new java.sql.Date(parsed.getTime()));
                                } catch (ParseException e) {
                                    log.debug("excelXlsxToCuocThis", e);
                                }
                            }
                            break;
                        
                        case 8:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String ngayKetThucStr = currentCell.getStringCellValue();
                                Date parsed;
                                try {
                                    parsed = format.parse(ngayKetThucStr);
                                    cuocThi.setNgayKetThuc(new java.sql.Date(parsed.getTime()));
                                } catch (ParseException e) {
                                    log.debug("excelXlsxToCuocThis", e);
                                }
                            }
                            break;
                        
                        case 9:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                cuocThi.setThongTinMoTa(currentCell.getStringCellValue());
                            }
                            break;

                        case 10:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                cuocThi.setWebsite(currentCell.getStringCellValue());
                            }
                            break;

                        case 11:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String tinhTrangStr = currentCell.getStringCellValue();
                                if (TINHTRANG_CUOCTHI_MODANGKI.equals(tinhTrangStr.trim())) {
                                    cuocThi.setTinhTrang(LoaiTinhTrangCuocThi.MODANGKY);
                                }
                                else if (TINHTRANG_CUOCTHI_DONGDANGKI.equals(tinhTrangStr.trim())) {
                                    cuocThi.setTinhTrang(LoaiTinhTrangCuocThi.DONGDANGKY);
                                }
                                else if (TINHTRANG_CUOCTHI_DAKETTHUC.equals(tinhTrangStr.trim())) {
                                    cuocThi.setTinhTrang(LoaiTinhTrangCuocThi.DAKETTHUC);
                                }
                                else {
                                    cuocThi.setTinhTrang(-1);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
    
                cuocthis.add(cuocThi);
            }
    
            workbook.close();
    
            return cuocthis;
        } catch (IOException e) {
            log.debug("excelXlsxToCuocThis", e);
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static List<KhoiThi> excelXlsxToKhoiThis(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
    
            Sheet sheet = workbook.getSheet(SHEET_KHOITHI);
            Iterator<Row> rows = sheet.iterator();
    
            List<KhoiThi> khoithis = new ArrayList<KhoiThi>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
    
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
    
                KhoiThi khoiThi = new KhoiThi();

                int cellIdx = 0;
                for (int i = 0; i <= 7; i++) {
                    Cell currentCell = currentRow.getCell(i);
                    cellIdx = i;
                    
                    switch (cellIdx) {
                        case 0:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                khoiThi.setTenGoi(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 1:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String tenCuocThi = currentCell.getStringCellValue();
                                khoiThi.setTenCuocThi(tenCuocThi);
                            }
                            break;
    
                        case 2:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                khoiThi.setNoiDungThi(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 3:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String vongSoLoaiStr = currentCell.getStringCellValue();
                                if (TRUE_STR.equals(vongSoLoaiStr.trim())) {
                                    khoiThi.setVongSoLoai(true);
                                }
                                else if (FALSE_STR.equals(vongSoLoaiStr.trim())) {
                                    khoiThi.setVongSoLoai(false);
                                }
                            }
                            break;
                        case 4:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String thiTapTheStr = currentCell.getStringCellValue();
                                if (TRUE_STR.equals(thiTapTheStr.trim())) {
                                    khoiThi.setThiTapThe(true);
                                }
                                else if (FALSE_STR.equals(thiTapTheStr.trim())) {
                                    khoiThi.setThiTapThe(false);
                                }
                            }
                            break;
                        
                        case 5:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {  
                                String thiSangTaoStr = currentCell.getStringCellValue();
                                if (TRUE_STR.equals(thiSangTaoStr.trim())) {
                                    khoiThi.setThiSangTao(true);
                                }
                                else if (FALSE_STR.equals(thiSangTaoStr.trim())) {
                                    khoiThi.setThiSangTao(false);
                                }
                            }
                            break;
                        
                        case 6:
                            if (currentCell != null && currentCell.getCellType() == CellType.NUMERIC) {
                                khoiThi.setMaxDangKi((int)currentCell.getNumericCellValue());
                            }
                            break;
                        
                        case 7:
                            if (currentCell != null && currentCell.getCellType() == CellType.NUMERIC) {
                                khoiThi.setMaxThiSinh((int)currentCell.getNumericCellValue());
                            }
                            break;
                        default:
                            break;
                    }
                }
    
                khoithis.add(khoiThi);
            }
    
            workbook.close();
    
            return khoithis;
        } catch (IOException e) {
            log.debug("excelXlsxToKhoiThis", e);
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static List<ThiSinh> excelXlsxToDanhSachThis(InputStream is, String sheetName) {
        try {
            List<ThiSinh> thisinhs = new ArrayList<ThiSinh>();

            Workbook workbook = new XSSFWorkbook(is);
    
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                workbook.close();
                return thisinhs;
            }
            Iterator<Row> rows = sheet.iterator();
    
            SimpleDateFormat format = new SimpleDateFormat(VN_DATE_FORMAT);

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
    
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
    
                ThiSinh thiSinh = new ThiSinh();

                int cellIdx = 0;
                for (int i = 0; i <= 12; i++) {
                    Cell currentCell = currentRow.getCell(i);
                    cellIdx = i;
                    
                    switch (cellIdx) {
                        case 0:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                thiSinh.setHoTen(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 1:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String gioiTinhStr = currentCell.getStringCellValue();
                                if (GIOITINH_NAM.equals(gioiTinhStr.trim())) {
                                    thiSinh.setGioiTinh(LoaiGioiTinh.NAM);
                                }
                                else if (GIOITINH_NU.equals(gioiTinhStr.trim())) {
                                    thiSinh.setGioiTinh(LoaiGioiTinh.NU);
                                }
                            }
                            break;
    
                        case 2:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String ngaySinhStr = currentCell.getStringCellValue();
                                Date parsed;
                                try {
                                    parsed = format.parse(ngaySinhStr);
                                    thiSinh.setNgaySinh(new java.sql.Date(parsed.getTime()));
                                } catch (ParseException e) {
                                    log.debug("excelXlsxToDanhSachThis", e);
                                }
                            }
                            break;
    
                        case 3:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String email = currentCell.getStringCellValue();
                                if (email != null && !email.isEmpty()) {
                                    thiSinh.setEmail(email);
                                }
                            }
                            break;
                        case 4:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                thiSinh.setSoDienThoai(currentCell.getStringCellValue());
                            }
                            break;
                        
                        case 5:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                thiSinh.setDoiTuongThi(currentCell.getStringCellValue());
                            }
                            break;
                        
                        case 6:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                thiSinh.setNganhDaoTao(currentCell.getStringCellValue());
                            }
                            break;
                        
                        case 7:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                thiSinh.setDatGiaiThuong(currentCell.getStringCellValue());
                            }
                            break;
                        
                        case 8:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                thiSinh.setTenDoanThi(currentCell.getStringCellValue());
                            }
                            break;

                        case 9:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                thiSinh.setTenCuocThi(currentCell.getStringCellValue());
                            }
                            break;

                        case 10:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                thiSinh.setTenKhoiThi(currentCell.getStringCellValue());
                            }
                            break;

                        case 11:
                            if (currentCell != null && currentCell.getCellType() == CellType.NUMERIC) {
                                int thamGia = (int)currentCell.getNumericCellValue();
                                if (thamGia == 1) {
                                    thiSinh.setThamGia(true);
                                }
                                else {
                                    thiSinh.setThamGia(false);
                                }
                            }
                            else {
                                thiSinh.setThamGia(false);
                            }
                            break;
                        case 12:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                thiSinh.setTenDoiThi(currentCell.getStringCellValue());
                            }
                            break;
                        default:
                            break;
                    }
                }
    
                if (thiSinh.isThamGia()) {
                    thisinhs.add(thiSinh);
                }
            }
    
            workbook.close();
    
            return thisinhs;
        } catch (IOException e) {
            log.debug("excelXlsxToDanhSachThis", e);
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    } 

    public static List<ThiSinh> excelXlsxToTongHopThiSinh(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
    
            Sheet sheet = workbook.getSheet(SHEET_DANHSACHTHI);
            Iterator<Row> rows = sheet.iterator();
    
            List<ThiSinh> thisinhs = new ArrayList<ThiSinh>();
            SimpleDateFormat format = new SimpleDateFormat(VN_DATE_FORMAT);

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
    
                if (rowNumber < 9) {
                    rowNumber++;
                    continue;
                }
    
                ThiSinh thiSinh = new ThiSinh();

                int cellIdx = 0;

                Cell thuTuCell = currentRow.getCell(3);
                if (thuTuCell != null && thuTuCell.getCellType() == CellType.NUMERIC) {
                    log.info("Cell: " + thuTuCell.getNumericCellValue());
                }
    
                thisinhs.add(thiSinh);
            }
    
            workbook.close();
    
            return thisinhs;
        } catch (IOException e) {
            log.debug("excelXlsxToTongHopThiSinh", e);
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    } 

    public static List<KetQuaThi> excelToKetQuaDanhSachThis(InputStream is, String sheetName) {
        try {
            List<KetQuaThi> ketQuaThis = new ArrayList<KetQuaThi>();

            Workbook workbook = new XSSFWorkbook(is);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator(); 
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                workbook.close();
                return ketQuaThis;
            }
            Iterator<Row> rows = sheet.iterator();
    
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
    
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
    
                KetQuaThi ketQuaThi = new KetQuaThi();

                int cellIdx = 0;
                for (int i = 0; i <= 4; i++) {
                    Cell currentCell = currentRow.getCell(i);
                    cellIdx = i;
                    
                    switch (cellIdx) {
                        case 0:
                            if (currentCell != null && (currentCell.getCellType() == CellType.NUMERIC || currentCell.getCellType() == CellType.FORMULA)) {
                                if (currentCell.getCellType() == CellType.NUMERIC) {
                                    log.info("STT: " + currentCell.getNumericCellValue());
                                    ketQuaThi.setSTT((int)currentCell.getNumericCellValue());
                                }
                                else {
                                    if (evaluator.evaluateFormulaCell(currentCell) == CellType.NUMERIC) {
                                        log.info("STT: " + currentCell.getNumericCellValue());
                                        ketQuaThi.setSTT((int)currentCell.getNumericCellValue());
                                    }
                                }
                            }
                            break;
    
                        case 1:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                ketQuaThi.setDoiTuong(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 2:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                ketQuaThi.setTenTruong(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 3:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                ketQuaThi.setGiaiThuong(currentCell.getStringCellValue());
                            }
                            break;

                        case 4:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                ketQuaThi.setKhoiThi(currentCell.getStringCellValue());
                            }
                            break;
                        default:
                            break;
                    }
                }
    
                ketQuaThis.add(ketQuaThi);
            }
    
            workbook.close();
    
            return ketQuaThis;
        } catch (IOException e) {
            log.debug("excelToKetQuaDanhSachThis", e);
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
    
    
	public static File exportDanhSachDangKyMau1(String templateFilePath, String outputPath, int fc, int lc, int fr,
			int lr, int looprow, HashMap<String, Object> data) {

		Workbook workbook = null;

		FileInputStream fis = null;

		FileOutputStream fos = null;

		Path targetPath = null;

		try {

			Path sourcePath = Paths.get(templateFilePath);

			// init target report
			targetPath = Paths.get(outputPath);

			// copy source to target
			targetPath = Files.copy(sourcePath, targetPath);

			fis = new FileInputStream(targetPath.toFile());

			workbook = new XSSFWorkbook(fis);

			// close fis after init workbook
			fis.close();

			Sheet sheet = workbook.getSheetAt(0);

			Row row = null;

			// int dataSize = 0;

			for (int r = fr; r < lr; r++) {

				row = sheet.getRow(r);

				if (row != null) {

					for (int c = fc; c < lc; c++) {

						Cell cell = row.getCell(c);

						if (cell != null) {
							if (cell.getCellComment() != null) {
								cell.removeCellComment();
							}

							String cellValue = cell.getStringCellValue();

							List<String> cellPatterns = new ArrayList<>();

							if (!ObjectUtils.isEmpty(cellValue)) {
								cellPatterns = StringPatternUtil.getMatcher(cellValue, DanhSachDangKyMau1.dataPattern);
							}

							for (String cellPattern : cellPatterns) {
								if (data.containsKey(cellPattern)) {
									cellValue = cellValue.replace(cellPattern, String.valueOf(data.get(cellPattern)));
								} else {
									cellValue = cellValue.replace(cellPattern, "");
								}
							}
							
							cell.setCellValue(cellValue);

						}
					}
				}
			}
			
			row = sheet.getRow(looprow);
			
			List<HashMap<String, Object>> dataRows = (List<HashMap<String, Object>>) data.get("$loop$");
			
			sheet.shiftRows(looprow, lr, dataRows.size() - 1);
			
			Cell tmpCell = row.getCell(fc);

			CellStyle tmpCellStyle = tmpCell.getCellStyle();
			
			if (row != null && dataRows != null && !dataRows.isEmpty()) {
			
				Row newRow = null;

				int dataRowCount = 0;

				for (int i = looprow; i <= looprow + (dataRows.size() - 1); i++) {

					HashMap<String, Object> dataRow = dataRows.get(dataRowCount);

					// new row insert
					
					newRow = sheet.createRow(i);
					
					newRow.setHeight(row.getHeight()); // create cell end set style
					
					CellStyle cellStyle = workbook.createCellStyle();
					
					cellStyle.cloneStyleFrom(tmpCellStyle);
					
					Font font = workbook.createFont();
					
					Cell newCell = null;
					
					if(dataRow.size() == 1) {
						font.setBold(true);
						cellStyle.setFont(font);
					}else {
						font.setBold(false);
						cellStyle.setFont(font);
					}
					
					for (int column = fc; column <= lc; column++) {
						
						newCell = newRow.createCell(column);
						
						newCell.setCellStyle(cellStyle);

						String cellPattern = DanhSachDangKyMau1.colMap.get(column);
						
						String value = "";

						if (dataRow.containsKey(cellPattern)) {
							value = String.valueOf(dataRow.get(cellPattern));
						}

						newCell.setCellValue(value);
					}
				
					dataRowCount++;

				}

			}

			fos = new FileOutputStream(targetPath.toFile());

			workbook.write(fos);

			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			try {

				if (workbook != null) {
					workbook.close();
				}
				if (fis != null) {
					fis.close();
				}

				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

		return targetPath.toFile();

	}
	
	
	public static File exportDanhSachDangKyMau2(String templateFilePath, String outputPath, int fc, int lc, int fr,
			int lr, int looprow, HashMap<String, Object> data) {

		Workbook workbook = null;

		FileInputStream fis = null;

		FileOutputStream fos = null;

		Path targetPath = null;

		try {

			Path sourcePath = Paths.get(templateFilePath);

			// init target report
			targetPath = Paths.get(outputPath);

			// copy source to target
			targetPath = Files.copy(sourcePath, targetPath);

			fis = new FileInputStream(targetPath.toFile());

			workbook = new XSSFWorkbook(fis);

			// close fis after init workbook
			fis.close();

			Sheet sheet = workbook.getSheetAt(0);

			Row row = null;

			// int dataSize = 0;

			for (int r = fr; r < lr; r++) {

				row = sheet.getRow(r);

				if (row != null) {

					for (int c = fc; c < lc; c++) {

						Cell cell = row.getCell(c);

						if (cell != null) {
							if (cell.getCellComment() != null) {
								cell.removeCellComment();
							}

							String cellValue = cell.getStringCellValue();

							List<String> cellPatterns = new ArrayList<>();

							if (!ObjectUtils.isEmpty(cellValue)) {
								cellPatterns = StringPatternUtil.getMatcher(cellValue, DanhSachDangKyMau2.dataPattern);
							}

							for (String cellPattern : cellPatterns) {
								if (data.containsKey(cellPattern)) {
									cellValue = cellValue.replace(cellPattern, String.valueOf(data.get(cellPattern)));
								} else {
									cellValue = cellValue.replace(cellPattern, "");
								}
							}
							
							cell.setCellValue(cellValue);

						}
					}
				}
			}
			
			row = sheet.getRow(looprow);
			
			List<HashMap<String, Object>> dataRows = (List<HashMap<String, Object>>) data.get("$loop$");
			
			sheet.shiftRows(looprow, lr, dataRows.size() - 1);
			
			Cell tmpCell = row.getCell(fc);

			CellStyle tmpCellStyle = tmpCell.getCellStyle();
			
			if (row != null && dataRows != null && !dataRows.isEmpty()) {
			
				Row newRow = null;

				int dataRowCount = 0;

				for (int i = looprow; i <= looprow + (dataRows.size() - 1); i++) {

					HashMap<String, Object> dataRow = dataRows.get(dataRowCount);

					// new row insert
					
					newRow = sheet.createRow(i);
					
					newRow.setHeight(row.getHeight()); // create cell end set style
					
					CellStyle cellStyle = workbook.createCellStyle();
					
					cellStyle.cloneStyleFrom(tmpCellStyle);
					
					Font font = workbook.createFont();
					
					Cell newCell = null;
					
					if(dataRow.size() == 2) {
						font.setBold(true);
						cellStyle.setFont(font);
					}else {
						font.setBold(false);
						cellStyle.setFont(font);
					}
					
					for (int column = fc; column <= lc; column++) {
						
						newCell = newRow.createCell(column);
						
						newCell.setCellStyle(cellStyle);

						String cellPattern = DanhSachDangKyMau2.colMap.get(column);
						
						String value = "";

						if (dataRow.containsKey(cellPattern)) {
							value = String.valueOf(dataRow.get(cellPattern));
						}

						newCell.setCellValue(value);
					}
				
					dataRowCount++;

				}

			}

			fos = new FileOutputStream(targetPath.toFile());

			workbook.write(fos);

			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			try {

				if (workbook != null) {
					workbook.close();
				}
				if (fis != null) {
					fis.close();
				}

				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

		return targetPath.toFile();

	}
	
	
	public static File exportDanhSachDangKyKhoiThiCaNhanMau1(String templateFilePath, String outputPath, int fc, int lc, int fr,
			int lr, int looprow, HashMap<String, Object> data) {

		Workbook workbook = null;

		FileInputStream fis = null;

		FileOutputStream fos = null;

		Path targetPath = null;

		try {

			Path sourcePath = Paths.get(templateFilePath);

			// init target report
			targetPath = Paths.get(outputPath);

			// copy source to target
			targetPath = Files.copy(sourcePath, targetPath);

			fis = new FileInputStream(targetPath.toFile());

			workbook = new XSSFWorkbook(fis);

			// close fis after init workbook
			fis.close();

			Sheet sheet = workbook.getSheetAt(0);

			Row row = null;

			// int dataSize = 0;

			for (int r = fr; r < lr; r++) {

				row = sheet.getRow(r);

				if (row != null) {

					for (int c = fc; c < lc; c++) {

						Cell cell = row.getCell(c);

						if (cell != null) {
							if (cell.getCellComment() != null) {
								cell.removeCellComment();
							}

							String cellValue = cell.getStringCellValue();

							List<String> cellPatterns = new ArrayList<>();

							if (!ObjectUtils.isEmpty(cellValue)) {
								cellPatterns = StringPatternUtil.getMatcher(cellValue, DanhSachDangKyKhoiThiCaNhanMau1.dataPattern);
							}

							for (String cellPattern : cellPatterns) {
								if (data.containsKey(cellPattern)) {
									cellValue = cellValue.replace(cellPattern, String.valueOf(data.get(cellPattern)));
								} else {
									cellValue = cellValue.replace(cellPattern, "");
								}
							}
							
							cell.setCellValue(cellValue);

						}
					}
				}
			}
			
			row = sheet.getRow(looprow);
			
			List<HashMap<String, Object>> dataRows = (List<HashMap<String, Object>>) data.get("$loop$");
			
			sheet.shiftRows(looprow, lr, dataRows.size() - 1);
			
			List<CellStyle> cellStyles = new ArrayList<CellStyle>();
			
			for(int c = fc; c <= lc; c++) {
				Cell tmpCell = row.getCell(c);
				CellStyle tmpCellStyle = tmpCell.getCellStyle();
				cellStyles.add(tmpCellStyle);
			}
			
			if (row != null && dataRows != null && !dataRows.isEmpty()) {
			
				Row newRow = null;

				int dataRowCount = 0;

				for (int i = looprow; i <= looprow + (dataRows.size() - 1); i++) {

					HashMap<String, Object> dataRow = dataRows.get(dataRowCount);

					// new row insert
					
					newRow = sheet.createRow(i);
					
					newRow.setHeight(row.getHeight()); // create cell end set style
					
					//CellStyle cellStyle = workbook.createCellStyle();
					
					Cell newCell = null;

					for (int column = fc; column <= lc; column++) {
						
						//cellStyle.cloneStyleFrom(cellStyles.get(column));
						
						newCell = newRow.createCell(column);
						
						newCell.setCellStyle(cellStyles.get(column));	

						if (column == fc) {
							XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
							//XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, column, i, column, i);
							ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, column, i, column, i);
							XSSFComment comment = drawing.createCellComment(anchor);
							comment.setString(String.valueOf(dataRow.get(DanhSachDangKyKhoiThiCaNhanMau1.id)));
							newCell.setCellComment(comment);
						}
						
						String cellPattern = DanhSachDangKyKhoiThiCaNhanMau1.colMap.get(column);
						
						String value = "";

						if (dataRow.containsKey(cellPattern)) {
							value = String.valueOf(dataRow.get(cellPattern));
						}

						newCell.setCellValue(value);
					}
					
					dataRowCount++;
				}
				
			}

			fos = new FileOutputStream(targetPath.toFile());

			workbook.write(fos);

			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			try {

				if (workbook != null) {
					workbook.close();
				}
				if (fis != null) {
					fis.close();
				}

				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

		return targetPath.toFile();

	}
	
	public static File exportDanhSachDangKyKhoiThiTapTheMau1(String templateFilePath, String outputPath, int fc, int lc, int fr,
			int lr, int looprow, HashMap<String, Object> data) {

		Workbook workbook = null;

		FileInputStream fis = null;

		FileOutputStream fos = null;

		Path targetPath = null;

		try {

			Path sourcePath = Paths.get(templateFilePath);

			// init target report
			targetPath = Paths.get(outputPath);

			// copy source to target
			targetPath = Files.copy(sourcePath, targetPath);

			fis = new FileInputStream(targetPath.toFile());

			workbook = new XSSFWorkbook(fis);

			// close fis after init workbook
			fis.close();

			Sheet sheet = workbook.getSheetAt(0);

			Row row = null;

			// int dataSize = 0;

			for (int r = fr; r < lr; r++) {

				row = sheet.getRow(r);

				if (row != null) {

					for (int c = fc; c < lc; c++) {

						Cell cell = row.getCell(c);

						if (cell != null) {
							if (cell.getCellComment() != null) {
								cell.removeCellComment();
							}

							String cellValue = cell.getStringCellValue();

							List<String> cellPatterns = new ArrayList<>();

							if (!ObjectUtils.isEmpty(cellValue)) {
								cellPatterns = StringPatternUtil.getMatcher(cellValue, DanhSachDangKyKhoiThiTapTheMau1.dataPattern);
							}

							for (String cellPattern : cellPatterns) {
								if (data.containsKey(cellPattern)) {
									cellValue = cellValue.replace(cellPattern, String.valueOf(data.get(cellPattern)));
								} else {
									cellValue = cellValue.replace(cellPattern, "");
								}
							}
							
							cell.setCellValue(cellValue);

						}
					}
				}
			}
			
			row = sheet.getRow(looprow);
			
			List<HashMap<String, Object>> dataRows = (List<HashMap<String, Object>>) data.get("$loop$");
			
			sheet.shiftRows(looprow, lr, dataRows.size() - 1);
			
			List<CellStyle> cellStyles = new ArrayList<CellStyle>();
			
			for(int c = fc; c <= lc; c++) {
				Cell tmpCell = row.getCell(c);
				CellStyle tmpCellStyle = tmpCell.getCellStyle();
				cellStyles.add(tmpCellStyle);
			}
			
			if (row != null && dataRows != null && !dataRows.isEmpty()) {
			
				Row newRow = null;

				int dataRowCount = 0;
				
				for (int i = looprow; i <= looprow + (dataRows.size() - 1); i++) {

					HashMap<String, Object> dataRow = dataRows.get(dataRowCount);

					// new row insert
					
					newRow = sheet.createRow(i);
					
					newRow.setHeight(row.getHeight()); // create cell end set style

					Cell newCell = null;

					for (int column = fc; column <= lc; column++) {
						
						newCell = newRow.createCell(column);
						
						newCell.setCellStyle(cellStyles.get(column));	
						
						if (column == fc) {
							XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
							
							//XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, column, i, column, i);
							ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, column, i, column, i);
							XSSFComment comment = drawing.createCellComment(anchor);
							comment.setString(String.valueOf(dataRow.get(DanhSachDangKyKhoiThiTapTheMau1.id)));
							newCell.setCellComment(comment);
						}
						
						String cellPattern = DanhSachDangKyKhoiThiTapTheMau1.colMap.get(column);
						
						String value = "";

						if (dataRow.containsKey(cellPattern)) {
							value = String.valueOf(dataRow.get(cellPattern));
						}

						newCell.setCellValue(value);
					}
					dataRowCount++;
				}
			}

			fos = new FileOutputStream(targetPath.toFile());

			workbook.write(fos);

			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			try {

				if (workbook != null) {
					workbook.close();
				}
				if (fis != null) {
					fis.close();
				}

				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

		return targetPath.toFile();

	}
	
	public static List<HashMap<String, Object>> getDanhSachThiKhoiThiCaNhanMau1Data(InputStream is, Integer fc,
			Integer lc, Integer fr) {
		try {
			Workbook workbook = new XSSFWorkbook(is);

			Sheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rows = sheet.iterator();

			List<HashMap<String, Object>> data = new ArrayList<>();

			int rowNumber = 0;

			while (rows.hasNext()) {
				Row currentRow = rows.next();

				if (rowNumber < fr) {
					rowNumber++;
					continue;
				}
				Long id = null;
				String hoTen = "";
				String soBaoDanh = "";
				String ngayThangNamSinh = "";
				String tenDoan = "";
				Integer thuHang = null;
				String giaiThuong = "";
				for (int i = fc; i <= lc; i++) {

					Cell currentCell = currentRow.getCell(i);
					switch (i) {
					case 0:
						if (currentCell != null) {
							Comment comment = currentCell.getCellComment();
							if(comment != null && !ObjectUtils.isEmpty(comment.getString())) {
								String tmp = comment.getString().getString();
								if(!ObjectUtils.isEmpty(tmp)) {
									id = Long.parseLong(tmp);
								}
							}
						}
						break;

					case 1:
						if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
							hoTen = currentCell.getStringCellValue();
						}
						break;

					case 2:
						if (currentCell != null) {
							if(currentCell.getCellType().compareTo(CellType.NUMERIC)  == 0) {
								Integer tmp = (int)currentCell.getNumericCellValue();
								soBaoDanh = String.valueOf(tmp);
							}else {
								soBaoDanh = currentCell.getStringCellValue();
							}
						
						}
						break;

					case 3:
						if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
							ngayThangNamSinh = currentCell.getStringCellValue();
						}
						break;
					case 4:

						break;

					case 5:

						break;

					case 6:
						if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
							tenDoan = currentCell.getStringCellValue();
						}
						break;
					case 7:
						if (currentCell != null) {
							if(currentCell.getCellType().compareTo(CellType.NUMERIC)  == 0) {
								Integer tmp = (int)currentCell.getNumericCellValue();
								thuHang = tmp;
							}else {
								String tmp = currentCell.getStringCellValue();
								if (!ObjectUtils.isEmpty(tmp)) {
									thuHang = Integer.parseInt(tmp);
								}
							}
						}

						break;

					case 8:
						if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
							giaiThuong = currentCell.getStringCellValue();
						}
						break;
					default:
						break;
					}
				}

				HashMap<String, Object> dataRow = new HashMap<String, Object>();
				dataRow.put(DanhSachDangKyKhoiThiTapTheMau1.id, id);
				dataRow.put(DanhSachDangKyKhoiThiCaNhanMau1.hoTen, hoTen);
				dataRow.put(DanhSachDangKyKhoiThiCaNhanMau1.soBaoDanh, soBaoDanh);
				dataRow.put(DanhSachDangKyKhoiThiCaNhanMau1.ngayThangNamSinh,
						DatetimeUtil.stringToDate(ngayThangNamSinh, DatetimeUtil._VN_DATE_FORMAT));
				dataRow.put(DanhSachDangKyKhoiThiCaNhanMau1.tenDoan, tenDoan);
				dataRow.put(DanhSachDangKyKhoiThiCaNhanMau1.thuHang, thuHang);
				dataRow.put(DanhSachDangKyKhoiThiCaNhanMau1.giaiThuong, giaiThuong);

				data.add(dataRow);
			}

			workbook.close();

			return data;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}
	
	/*
	public static void main(String[] args) {
		
		File file = new File("");
		try {
			InputStream targetStream = new FileInputStream(file);
			getDanhSachThiKhoiThiCaNhanMau1Data(targetStream, 0, 9, 8);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		for(int i = 0; i < 11; i++) {
			HashMap<String, Object>  dataRow = new HashMap<String, Object>();
			dataRow.put("content", "http//dangky.olp.vn/thisinh/" + i);
			dataRow.put("hoTen", "Nguyen Van A" + i);
			dataRow.put("maTruong", "ABC" + i);
			data.add(dataRow);
		}
		exportDanhSachThiSinhQRCode("D:\\test.xlsx", data);
	}

	*/
	
	public static List<HashMap<String, Object>> getDanhSachDangKyKhoiThiTapTheMau1Data(InputStream is, Integer fc,
			Integer lc, Integer fr) {
		
		try {
			Workbook workbook = new XSSFWorkbook(is);

			Sheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rows = sheet.iterator();

			List<HashMap<String, Object>> data = new ArrayList<>();

			int rowNumber = 0;

			while (rows.hasNext()) {
				Row currentRow = rows.next();
				
				if (rowNumber < fr) {
					rowNumber++;
					continue;
				}
				Long id = null;
				String tenDoi = "";
				String hoTen = "";
				String soBaoDanh = "";
				String ngayThangNamSinh = "";
				String tenDoan = "";
				Integer thuHang = null;
				String giaiThuong = "";
				for (int i = fc; i <= lc; i++) {

					Cell currentCell = currentRow.getCell(i);

					switch (i) {
					case 0:
						if (currentCell != null) {
							Comment comment = currentCell.getCellComment();
							if(comment != null && !ObjectUtils.isEmpty(comment.getString())) {
								String tmp = comment.getString().getString();
								if(!ObjectUtils.isEmpty(tmp)) {
									id = Long.parseLong(tmp);
								}
							}
						}
						
						break;

					case 1:
						if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
							tenDoi = currentCell.getStringCellValue();
						}
						break;

					case 2:
						if (currentCell != null) {
							if(currentCell.getCellType().compareTo(CellType.NUMERIC)  == 0) {
								Integer tmp = (int)currentCell.getNumericCellValue();
								soBaoDanh = String.valueOf(tmp);
							}else {
								soBaoDanh = currentCell.getStringCellValue();
							}
						
						}
						break;

					case 3:
						if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
							hoTen = currentCell.getStringCellValue();
						}
						break;
					case 4:
						if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
							ngayThangNamSinh = currentCell.getStringCellValue();
						}
						break;

					case 5:

						break;

					case 6:

						break;
					case 7:
						if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
							tenDoan = currentCell.getStringCellValue();
						}

						break;

					case 8:
						if (currentCell != null) {
							if(currentCell.getCellType().compareTo(CellType.NUMERIC)  == 0) {
								Integer tmp = (int)currentCell.getNumericCellValue();
								thuHang = tmp;
							}else {
								String tmp = currentCell.getStringCellValue();
								if (!ObjectUtils.isEmpty(tmp)) {
									thuHang = Integer.parseInt(tmp);
								}
							}
						}

						break;
					case 9:
						if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
							giaiThuong = currentCell.getStringCellValue();
						}
						break;
					default:
						break;
					}
				}

				HashMap<String, Object> dataRow = new HashMap<String, Object>();
				dataRow.put(DanhSachDangKyKhoiThiTapTheMau1.id, id);
				dataRow.put(DanhSachDangKyKhoiThiTapTheMau1.tenDoi, tenDoi);
				dataRow.put(DanhSachDangKyKhoiThiTapTheMau1.hoTen, hoTen);
				dataRow.put(DanhSachDangKyKhoiThiTapTheMau1.soBaoDanh, soBaoDanh);
				dataRow.put(DanhSachDangKyKhoiThiTapTheMau1.ngayThangNamSinh,
						DatetimeUtil.stringToDate(ngayThangNamSinh, DatetimeUtil._VN_DATE_FORMAT));
				dataRow.put(DanhSachDangKyKhoiThiTapTheMau1.tenDoan, tenDoan);
				dataRow.put(DanhSachDangKyKhoiThiTapTheMau1.thuHang, thuHang);
				dataRow.put(DanhSachDangKyKhoiThiTapTheMau1.giaiThuong, giaiThuong);

				data.add(dataRow);
			}

			workbook.close();

			return data;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}
	
	
	
	public static File exportDanhSachThiSinhQRCode(String outputPath, List<HashMap<String, Object>> data) {

		Workbook workbook = null;

		FileOutputStream fos = null;

		try {
			workbook = new XSSFWorkbook();

			Sheet sheet = workbook.createSheet("QRCode");
			sheet.setColumnWidth(0, 19 * 256);
			sheet.setColumnWidth(1, 50 * 256);
			sheet.setColumnWidth(2, 19 * 256);
			sheet.setColumnWidth(3, 50 * 256);
			XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
			
			CellStyle cellStyle = workbook.createCellStyle();

			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 26);
			cellStyle.setFont(font);
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cellStyle.setWrapText(true);
			
			if (data != null) {
				int size = data.size() % 2 == 0 ? data.size()/2 : data.size()/2 + 1;
				
				for(int i = 0; i < size; i++) {
					int idx1 = i * 2;
					int idx2 = i * 2 + 1;
					
					HashMap<String, Object> dataRow1 = data.get(idx1);
					
					HashMap<String, Object> dataRow2 = null;
					
					if(idx2 <= data.size() - 1) {
						dataRow2 = data.get(idx2);
					}
					
					Row row = sheet.createRow(i);
					row.setHeight((short) (8 * 256));
					Cell cell0 = row.createCell(0);
					Cell cell1 = row.createCell(1);
					cell1.setCellStyle(cellStyle);
					
					Cell cell2 = row.createCell(2);
					Cell cell3 = row.createCell(3);
					cell3.setCellStyle(cellStyle);
					
					ClientAnchor anchor = drawing.createAnchor(0, 0, 128, 128, 0, i, 0, i);
					anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);

					String content = (String) dataRow1.get("content");
					String info = (String) dataRow1.get("info");

					QRCodeWriter barcodeWriter = new QRCodeWriter();
					BitMatrix bitMatrix = barcodeWriter.encode(content, BarcodeFormat.QR_CODE, 128, 128);
					BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
					byte[] bytes = ImageUtil.toByteArray(bufferedImage, "jpeg");

					int pictureIndex = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
					Picture pict = drawing.createPicture(anchor, pictureIndex);
					pict.resize();
					cell1.setCellValue(info.toUpperCase());
					
					if(dataRow2 != null) {
						anchor = drawing.createAnchor(0, 0, 128, 128, 2, i, 2, i);
						anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);

						content = (String) dataRow2.get("content");
						info = (String) dataRow2.get("info");
	
						barcodeWriter = new QRCodeWriter();
						bitMatrix = barcodeWriter.encode(content, BarcodeFormat.QR_CODE, 128, 128);
						bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
						bytes = ImageUtil.toByteArray(bufferedImage, "jpeg");

						pictureIndex = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
						pict = drawing.createPicture(anchor, pictureIndex);
						pict.resize();
						cell3.setCellValue(info.toUpperCase());
					}
					
				}
				
			}

			File file = new File(outputPath);

			fos = new FileOutputStream(new File(outputPath));

			workbook.write(fos);

			fos.close();

			return file;

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		} finally {
			try {

				if (workbook != null) {
					workbook.close();
				}

				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

	}
}
