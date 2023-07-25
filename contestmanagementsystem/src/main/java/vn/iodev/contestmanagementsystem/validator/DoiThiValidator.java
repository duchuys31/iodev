package vn.iodev.contestmanagementsystem.validator;

import com.netflix.config.validation.ValidationException;

import vn.iodev.contestmanagementsystem.model.DoiThi;

public class DoiThiValidator {
    private static DoiThiValidator _validator = new DoiThiValidator();
    private DoiThiValidator() {}
    public static DoiThiValidator getInstance() {
        return _validator;
    }
    public void validate(DoiThi doiThi) throws Exception {
        if (doiThi.getKetQuaSoLoai() != null) {
            if (doiThi.getKetQuaSoLoai() < 0 || doiThi.getKetQuaSoLoai() > 1) {
                throw new ValidationException("KetQuaSoLoai must be 0 or 1");
            }
        }
        if (doiThi.getThuTuXepHang() != null) {
            if (doiThi.getThuTuXepHang() < 0) {
                throw new ValidationException("ThuTuXepHang must greater than 0");
            }
        }
    }
}
