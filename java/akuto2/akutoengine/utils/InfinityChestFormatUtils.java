package akuto2.akutoengine.utils;

import java.math.BigInteger;

import akuto2.akutoengine.proxies.ClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * InfinityChest用の数列フォーマットクラス
 */
@SideOnly(Side.CLIENT)
public class InfinityChestFormatUtils {
	private static final String jpLang = "ja_JP";
	private static final int maxJPFormatDigit = 73;
	private static final int maxUSFormatDigit = 66;
	private static final String[] formatJPString = new String[] { "無量大数", "不可思議", "那由他", "阿僧祇", "恒河沙", "極", "載", "正", "澗", "溝", "穣", "𥝱", "垓", "京", "兆", "億", "万" };
	private static final String[] formatUSString = new String[] { "Vigintillion", "Novemdecillion", "Octodecillion", "Septendecillion", "Sexdecillion", "Quindecillion", "Quattuordecillion", "Tredecillion", "Duodecillion", "Undecillion", "Decillion", "Nonillion", "Octillion", "Septillion", "Sextillion", "Quintillion", "Quadrillion", "Trillion", "Billion", "Million", "Thousand" };

	public static String formatStack(BigInteger size) {
		return formatStack(size, true);
	}

	public static String formatStack(BigInteger size, boolean flag) {
		return formatStack(size, flag, ClientProxy.mc.gameSettings.language);
	}

	public static String formatStack(BigInteger size, boolean flag, String lang) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(formatBigIntegerToString(size, lang));
		if(jpLang.equals(lang)) {
			if(flag) {
				stringBuilder.append("個");
			}
		}
		else {
			if(flag) {
				stringBuilder.append(" items");
			}
		}
		return stringBuilder.toString();
	}

	public static String formatBigIntegerToString(BigInteger size, String lang) {
		String sizeString = size.toString();
		String outString = null;
		if(jpLang.equals(lang)) {
			outString = getFrontSize(sizeString, true);
			int stringLength = sizeString.length();
			if(stringLength >= maxJPFormatDigit) {
				outString = "∞";
			}
			else if(stringLength <= 4) {}
			else {
				for(int i = 0; i < formatJPString.length; i++) {
					if(checkStringLength(stringLength, true, i + 1)) {
						outString += formatJPString[i];
					}
				}
			}
		}
		else {
			outString = getFrontSize(sizeString, false);
			int stringLength = sizeString.length();
			if(stringLength >= maxUSFormatDigit) {
				outString = "∞";
			}
			else if(stringLength <= 3) {}
			else {
				for(int i = 0; i < formatUSString.length; i++) {
					if(checkStringLength(stringLength, false, i + 1)) {
						outString += formatUSString[i];
					}
				}
			}
		}
		return outString;
	}

	private static boolean checkStringLength(int sizeLength, boolean isJP, int downValue) {
		return sizeLength >= (isJP ? maxJPFormatDigit : maxUSFormatDigit) - ((isJP ? 4 : 3) * downValue);
	}

	private static String getFrontSize(String size, boolean isJP) {
		if(isJP) {
			if(size.length() <= 4) {
				return size;
			}
		}
		else {
			if(size.length() <= 3) {
				return size;
			}
		}
		int i = size.length() % (isJP ? 4 : 3);
		return size.substring(0, i);
	}
}
