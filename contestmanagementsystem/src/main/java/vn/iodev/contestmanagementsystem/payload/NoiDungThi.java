package vn.iodev.contestmanagementsystem.payload;

public class NoiDungThi {
    public String getTenNoiDung() {
        return tenNoiDung;
    }
    public void setTenNoiDung(String tenNoiDung) {
        this.tenNoiDung = tenNoiDung;
    }
    public Integer getSoDoi() {
        return soDoi;
    }
    public void setSoDoi(Integer soDoi) {
        this.soDoi = soDoi;
    }
    public Integer getSoThiSinh() {
        return soThiSinh;
    }
    public void setSoThiSinh(Integer soThiSinh) {
        this.soThiSinh = soThiSinh;
    }
    private String tenNoiDung;
    private String khoiThiId;
    private Boolean thiTapThe;
    private Boolean thiSangTao;
    public Boolean getThiTapThe() {
        return thiTapThe;
    }
    public void setThiTapThe(Boolean thiTapThe) {
        this.thiTapThe = thiTapThe;
    }
    public Boolean getThiSangTao() {
        return thiSangTao;
    }
    public void setThiSangTao(Boolean thiSangTao) {
        this.thiSangTao = thiSangTao;
    }
    public String getKhoiThiId() {
        return khoiThiId;
    }
    public void setKhoiThiId(String khoiThiId) {
        this.khoiThiId = khoiThiId;
    }
    
    private Integer soDoi;
    private Integer soThiSinh;
    public NoiDungThi() {}
}
