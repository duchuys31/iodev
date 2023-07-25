package vn.iodev.contestmanagementsystem.converter;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BangDiemThiConverter implements AttributeConverter<Map<String, Object>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public String convertToDatabaseColumn(Map<String, Object> bangDiemThi) {

        String bangDiemThiJson = null;
        try {
            bangDiemThiJson = objectMapper.writeValueAsString(bangDiemThi);
        } catch (final JsonProcessingException e) {
        }

        return bangDiemThiJson;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String bangDiemThiJSON) {
        Map<String, Object> bangDiemThi = null;
        try {
            bangDiemThi = objectMapper.readValue(bangDiemThiJSON, Map.class);
        } catch (final IOException e) {
        }

        return bangDiemThi;
    }
}
