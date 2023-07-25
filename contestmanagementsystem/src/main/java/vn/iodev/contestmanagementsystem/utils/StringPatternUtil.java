package vn.iodev.contestmanagementsystem.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.ObjectUtils;

public class StringPatternUtil {
	public static boolean isMatcher(String txt, Pattern pattern) {

		String template = "";

		Matcher matcher = pattern.matcher(txt);

		while (matcher.find()) {
			template = matcher.group();
			if (!ObjectUtils.isEmpty(template)) {
				break;
			}
		}

		return ObjectUtils.isEmpty(template);
	}

	public static List<String> getMatcher(String txt, Pattern pattern) {

		List<String> txtPatterns = new ArrayList<String>();

		Matcher matcher = pattern.matcher(txt);

		while (matcher.find()) {
			String tmp = matcher.group();
			if (!ObjectUtils.isEmpty(tmp) && !txtPatterns.contains(tmp)) {
				txtPatterns.add(tmp);
			}
		}

		return txtPatterns;
	}

}
