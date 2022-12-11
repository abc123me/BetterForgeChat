package com.jeremiahbl.bfcmod;

public class MarkdownFormatter {
	public static final String markdownStringToFormattedString(String md) {
		return markdownStringToFormattedString(md, BitwiseStyling.ALL_STYLES);
	}
	public static final String markdownStringToFormattedString(String md, byte allowedMask) {
		String newStr = "";
		md += " ";
		byte mask = 0; int tmpPos = 0;
		char[] tmpMd = new char[2];
		boolean escapeNext = false;
		for(int pos = 0; pos < md.length(); pos++) {
			char c = md.charAt(pos);
			if(c == '\\') {
				if(escapeNext) {
					escapeNext = false;
					newStr += c;
				} else escapeNext = true;
			} else if(c == '*' || c == '_' || c == '~') {
				if(escapeNext) {
					escapeNext = false;
					newStr += c;
				} else {
					tmpMd[tmpPos++] = c;
					if(tmpPos >= 2) {
						byte nmask = convertMD(mask, tmpMd, tmpPos);
						newStr += maskDiff(nmask, mask, allowedMask);
						mask = nmask;
						tmpPos = 0;
					}
				}
			} else {
				if(tmpPos > 0) {
					byte nmask = convertMD(mask, tmpMd, tmpPos);
					newStr += maskDiff(nmask, mask, allowedMask);
					mask = nmask;
					tmpPos = 0;
				}
				newStr += c;
			}
		}
		return newStr.trim();
	}
	private static byte convertMD(byte mask, char[] chrs, int len) {
		char c1 = len > 0 ? chrs[0] : ' ';
		char c2 = len > 1 ? chrs[1] : ' ';
		if((c1 == '*' && c2 == ' ') || (c1 == '_' && c2 == ' '))
			return bitToggle(mask, BitwiseStyling.ITALIC_BIT);
		else if((c1 == '~' && c2 == ' '))
			return bitToggle(mask, BitwiseStyling.OBFUSCATED_BIT);
		else if((c1 == '*' && c2 == '*'))
			return bitToggle(mask, BitwiseStyling.BOLD_BIT);
		else if((c1 == '~' && c2 == '~'))
			return bitToggle(mask, BitwiseStyling.STRIKETHROUGH_BIT);
		else if((c1 == '_' && c2 == '_'))
			return bitToggle(mask, BitwiseStyling.UNDERLINE_BIT);
		else return mask;
	}
	private static String maskDiff(byte newMask, byte oldMask, byte allowedMask) {
		if(newMask == 0) return TextFormatter.RESET_ALL_FORMAT;
		if(newMask == oldMask) return "";
		byte stylesNew = bitCount(newMask);
		byte stylesOld = bitCount(oldMask);
		if(stylesNew > stylesOld) // Styles were added
			return BitwiseStyling.styleString((byte) (newMask & (~oldMask) & allowedMask));
		else if(stylesNew < stylesOld) { // Styles were removed
			return TextFormatter.RESET_ALL_FORMAT + BitwiseStyling.styleString((byte)(newMask & allowedMask));
		} else { // Styles shifted
			return "MARKDOWN FORMATTER INVALID STATE - REPORT TO DEVELOPER";
		}
	}
	private static byte bitCount(byte mask) {
		byte cnt = 0;
		for(byte i = 1; i != 0; i <<= 1)
			if((mask & i) != 0) cnt++;
		return cnt;
	}
	private static byte bitToggle(byte mask, byte bit) {
		if((mask & bit) != 0) mask &= ~bit;
		else mask |= bit;
		return mask;
	}
}

// Debugging porpoises only
/*public static void main(String[] args) {
	System.out.println(markdownStringToFormattedString("***test**123*"));
}
private static String maskStr(byte mask) {
	String out = "";
	if((mask & MARKDOWN_BOLD_BIT) != 0)   out += "BLD ";
	else out += "--- ";
	if((mask & MARKDOWN_ITALIC_BIT) != 0) out += "ITA ";
	else out += "--- ";
	if((mask & MARKDOWN_UNLINE_BIT) != 0) out += "ULN ";
	else out += "--- ";
	if((mask & MARKDOWN_STRIKE_BIT) != 0) out += "STR ";
	else out += "--- ";
	if((mask & MARKDOWN_MAGIC_BIT) != 0)  out += "MAG";
	else out += "---";
	return out;
}*/
