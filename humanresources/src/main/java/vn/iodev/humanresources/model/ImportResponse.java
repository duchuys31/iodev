package vn.iodev.humanresources.model;

public class ImportResponse {
    private String fileName;
    private long size;
    private String message;

    public ImportResponse() {}

    public ImportResponse(String fileName, long size, String message) {
        this.fileName = fileName;
        this.size = size;
        this.message = message;
    }

    public ImportResponse(String message) {
        this.message = message;
    }
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
