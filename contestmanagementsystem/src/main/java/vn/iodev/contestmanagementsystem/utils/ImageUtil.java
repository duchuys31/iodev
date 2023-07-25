package vn.iodev.contestmanagementsystem.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {
	public static byte[] toByteArray(BufferedImage bufferedImage, String format) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, format, baos);
		byte[] bytes = baos.toByteArray();
		return bytes;

	}
}
