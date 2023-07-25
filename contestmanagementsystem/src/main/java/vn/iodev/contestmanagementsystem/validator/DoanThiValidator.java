package vn.iodev.contestmanagementsystem.validator;

import javax.validation.ValidationException;

import vn.iodev.contestmanagementsystem.model.DoanThi;

public class DoanThiValidator {
    private static DoanThiValidator _validator = new DoanThiValidator();
    private DoanThiValidator() {}
    public static DoanThiValidator getInstance() {
        return _validator;
    }
    public void validate(DoanThi doanThi) throws Exception {
        if (doanThi.getThuTuXepHang() != null) {
            if (doanThi.getThuTuXepHang() < 0) {
                throw new ValidationException("ThuTuXepHang must greater than 0");
            }
        }
    }
}
