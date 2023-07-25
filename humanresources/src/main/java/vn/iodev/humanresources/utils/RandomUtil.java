package vn.iodev.humanresources.utils;


import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class RandomUtil {
    public static String generateRandomAlphanumeric(int length) {
        int leftLimit = 48;
        int rightLimit = 122;
        Random random = new Random();
    
        String generatedString = random.ints(leftLimit, rightLimit + 1)
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
          .limit(length)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
    
        return generatedString;
    }

    public static String generateRandomNumeric(int length) {
      int leftLimit = 48;
      int rightLimit = 57;
      Random random = new Random();
  
      String generatedString = random.ints(leftLimit, rightLimit + 1)
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  
      return generatedString;
    }

    public static String generateRandomEID(Date ngaySinh, int gioiTinh, int length) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(ngaySinh);
      int year = cal.get(Calendar.YEAR);
      String maGioiTinh = "";
      String maNamSinh = String.valueOf(year % 100);
      if (1900 <= year && year <= 1999) {
        if (gioiTinh == 0) {
          maGioiTinh = "0";
        }
        else {
          maGioiTinh = "1";
        }
      }
      else if (2000 <= year && year <= 2099) {
        if (gioiTinh == 0) {
          maGioiTinh = "2";
        }
        else {
          maGioiTinh = "3";
        }
      }
      else if (2100 <= year && year <= 2199) {
        if (gioiTinh == 0) {
          maGioiTinh = "4";
        }
        else {
          maGioiTinh = "5";
        }
      }
      else if (2200 <= year && year <= 2299) {
        if (gioiTinh == 0) {
          maGioiTinh = "6";
        }
        else {
          maGioiTinh = "7";
        }
      }
      else if (2300 <= year && year <= 2399) {
        if (gioiTinh == 0) {
          maGioiTinh = "8";
        }
        else {
          maGioiTinh = "9";
        }
      }

      return maGioiTinh + maNamSinh + generateRandomNumeric(length);
    }
}
