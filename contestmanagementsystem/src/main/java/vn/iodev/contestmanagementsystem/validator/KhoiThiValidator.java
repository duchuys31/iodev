package vn.iodev.contestmanagementsystem.validator;

import javax.validation.ValidationException;

import vn.iodev.contestmanagementsystem.model.KhoiThi;

public class KhoiThiValidator {
    private static KhoiThiValidator _validator = new KhoiThiValidator();
    private KhoiThiValidator() {}
    public static KhoiThiValidator getInstance() {
        return _validator;
    }
    public void validate(KhoiThi khoiThi) throws Exception {
        if (khoiThi.getMaxDangKi() != null) {
            if (khoiThi.getMaxDangKi() < 0) {
                throw new ValidationException("MaxDangKi must greater than 0");
            }
        }
        if (khoiThi.getMaxThiSinh() != null) {
            if (khoiThi.getMaxThiSinh() < 0) {
                throw new ValidationException("MaxThiSinh must greater than 0");
            }
        }
    }
}
