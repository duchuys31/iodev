package vn.iodev.contestmanagementsystem.utils;

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
}
