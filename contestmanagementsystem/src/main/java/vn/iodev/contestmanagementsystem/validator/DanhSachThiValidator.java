package vn.iodev.contestmanagementsystem.validator;

import javax.validation.ValidationException;

import vn.iodev.contestmanagementsystem.model.DanhSachThi;

public class DanhSachThiValidator {
    private static DanhSachThiValidator _validator = new DanhSachThiValidator();
    private DanhSachThiValidator() {}
    public static DanhSachThiValidator getInstance() {
        return _validator;
    }
    public void validate(DanhSachThi danhSachThi) throws Exception {
        if (danhSachThi.getKetQuaSoLoai() != null) {
            if (danhSachThi.getKetQuaSoLoai() < 0 || danhSachThi.getKetQuaSoLoai() > 2) {
                throw new ValidationException("KetQuaSoLoai must be 0 or 1");
            }
        }
        if (danhSachThi.getThuTuXepHang() != null) {
            if (danhSachThi.getThuTuXepHang() < 0) {
                throw new ValidationException("ThuTuXepHang must greater than 0");
            }
        }
    }    
}
