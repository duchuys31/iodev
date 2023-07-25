package vn.iodev.contestmanagementsystem.validator;

import javax.validation.ValidationException;

import vn.iodev.contestmanagementsystem.model.CuocThi;

public class CuocThiValidator {
    private static CuocThiValidator _validator = new CuocThiValidator();
    private CuocThiValidator() {}
    public static CuocThiValidator getInstance() {
        return _validator;
    }
    public void validate(CuocThi cuocThi) throws Exception {
        if (cuocThi.getNgayBatDau() != null && cuocThi.getNgayKetThuc() != null) {
            if (cuocThi.getNgayBatDau().compareTo(cuocThi.getNgayKetThuc()) > 0) {
                throw new ValidationException("NgayBatDau must lesser than NgayKetThuc");
            }
        }
    }
}
