package vn.iodev.contestmanagementsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ImportResponse {
    private String fileName;
    private long size;
    private String message;

    public ImportResponse(String fileName, long size, String message) {
        this.fileName = fileName;
        this.size = size;
        this.message = message;
    }

    public ImportResponse(String message) {
        this.message = message;
    }   
}
