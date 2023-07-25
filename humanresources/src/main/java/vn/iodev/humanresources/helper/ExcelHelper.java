package vn.iodev.humanresources.helper;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import vn.iodev.humanresources.model.CaNhan;
import vn.iodev.humanresources.model.LoaiGioiTinh;
import vn.iodev.humanresources.model.LoaiTinhTrang;
import vn.iodev.humanresources.model.LoaiTinhTrangToChuc;
import vn.iodev.humanresources.model.ToChuc;

import java.text.ParseException;;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs_TOCHUC = { "Tên gọi", "Tiếng Anh", "Tên viết tắt", "Loại tổ chức", "Địa chỉ hoạt động", "Vị trí địa lý", "Email", "Địa chỉ trang web" };
    static String SHEET_TOCHUC = "Tổ chức";

    static String[] HEADERs_CANHAN = { "Họ tên", "Giới tính", "Ngày sinh", "Email", "Số điện thoại", "Avatar", "LinkedIn", "Github", "Google" };
    static String SHEET_CANHAN = "Cá nhân";
    
    private static final String GIOITINH_NAM = "Nam";
    private static final String GIOITINH_NU = "Nữ";

    private static final String VN_DATE_FORMAT = "dd/MM/yyyy";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<ToChuc> excelXlsxToToChucs(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
    
            Sheet sheet = workbook.getSheet(SHEET_TOCHUC);
            Iterator<Row> rows = sheet.iterator();
    
            List<ToChuc> tochucs = new ArrayList<ToChuc>();
    
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
    
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
    
                ToChuc toChuc = new ToChuc();
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
          throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static List<CaNhan> excelXlsxToCaNhans(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
    
            Sheet sheet = workbook.getSheet(SHEET_CANHAN);
            Iterator<Row> rows = sheet.iterator();
    
            List<CaNhan> canhans = new ArrayList<CaNhan>();
            SimpleDateFormat format = new SimpleDateFormat(VN_DATE_FORMAT);
            
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
    
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
    
                CaNhan caNhan = new CaNhan();
                caNhan.setTinhTrang(LoaiTinhTrang.DA_CHINH_THUC);

                int cellIdx = 0;
                for (int i = 0; i <= 7; i++) {
                    Cell currentCell = currentRow.getCell(i);
                    cellIdx = i;
                    
                    switch (cellIdx) {
                        case 0:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                caNhan.setHoTen(currentCell.getStringCellValue());
                            }
                            break;
    
                        case 1:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String gioiTinhStr = currentCell.getStringCellValue();
                                if (GIOITINH_NAM.equals(gioiTinhStr.trim())) {
                                    caNhan.setGioiTinh(LoaiGioiTinh.NAM);
                                }
                                else if (GIOITINH_NU.equals(gioiTinhStr.trim())) {
                                    caNhan.setGioiTinh(LoaiGioiTinh.NU);
                                }
                            }
                            break;
    
                        case 2:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String ngaySinhStr = currentCell.getStringCellValue();
                                Date parsed;
                                try {
                                    parsed = format.parse(ngaySinhStr);
                                    caNhan.setNgaySinh(new java.sql.Date(parsed.getTime()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
    
                        case 3:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                String email = currentCell.getStringCellValue();
                                if (email != null && !email.isEmpty()) {
                                    caNhan.setEmail(email);
                                }
                            }
                            break;
                        case 4:
                            if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                                caNhan.setSoDienThoai(currentCell.getStringCellValue());
                            }
                            break;
                        
                        case 5:
                            break;
                        
                        case 6:
                            break;
                        
                        case 7:
                            break;

                        case 8:
                            break;

                        default:
                            break;
                    }
                }
    
                canhans.add(caNhan);
            }
    
            workbook.close();
    
            return canhans;
        } catch (IOException e) {
          throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
