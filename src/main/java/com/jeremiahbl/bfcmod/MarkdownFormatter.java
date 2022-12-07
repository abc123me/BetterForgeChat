package com.jeremiahbl.bfcmod;

public class MarkdownFormatter {
	public static final byte MARKDOWN_BOLD_BIT   = 1;
	public static final byte MARKDOWN_ITALIC_BIT = 2;
	public static final byte MARKDOWN_UNLINE_BIT = 4;
	public static final byte MARKDOWN_STRIKE_BIT = 8;
	public static final byte MARKDOWN_MAGIC_BIT  = 16;
	public static final byte MARKDOWN_ALL_FEATURES 
		= MARKDOWN_BOLD_BIT | MARKDOWN_ITALIC_BIT | MARKDOWN_UNLINE_BIT | MARKDOWN_STRIKE_BIT | MARKDOWN_MAGIC_BIT;
	
	public static final String markdownStringToFormattedString(String md) {
		return markdownStringToFormattedString(md, MARKDOWN_ALL_FEATURES);
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
		return newStr + TextFormatter.RESET_ALL_FORMAT;
	}
	private static byte convertMD(byte mask, char[] chrs, int len) {
		char c1 = len > 0 ? chrs[0] : ' ';
		char c2 = len > 1 ? chrs[1] : ' ';
		if((c1 == '*' && c2 == ' ') || (c1 == '_' && c2 == ' '))
			return bitToggle(mask, MARKDOWN_ITALIC_BIT);
		else if((c1 == '~' && c2 == ' '))
			return bitToggle(mask, MARKDOWN_MAGIC_BIT);
		else if((c1 == '*' && c2 == '*'))
			return bitToggle(mask, MARKDOWN_BOLD_BIT);
		else if((c1 == '~' && c2 == '~'))
			return bitToggle(mask, MARKDOWN_STRIKE_BIT);
		else if((c1 == '_' && c2 == '_'))
			return bitToggle(mask, MARKDOWN_UNLINE_BIT);
		else return mask;
	}
	private static String maskDiff(byte newMask, byte oldMask, byte allowedMask) {
		if(newMask == 0) return TextFormatter.RESET_ALL_FORMAT;
		if(newMask == oldMask) return "";
		byte stylesNew = bitCount(newMask);
		byte stylesOld = bitCount(oldMask);
		if(stylesNew > stylesOld) // Styles were added
			return styleString((byte) (newMask & (~oldMask) & allowedMask));
		else if(stylesNew < stylesOld) { // Styles were removed
			return TextFormatter.RESET_ALL_FORMAT + styleString((byte)(newMask & allowedMask));
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
	private static String styleString(byte mask) {
		String out = "";
		if((mask & MARKDOWN_BOLD_BIT) != 0)   out += TextFormatter.BOLD_FORMAT;
		if((mask & MARKDOWN_ITALIC_BIT) != 0) out += TextFormatter.ITALIC_FORMAT;
		if((mask & MARKDOWN_UNLINE_BIT) != 0) out += TextFormatter.UNDERLINE_FORMAT;
		if((mask & MARKDOWN_STRIKE_BIT) != 0) out += TextFormatter.STRIKETHROUGH_FORMAT;
		if((mask & MARKDOWN_MAGIC_BIT) != 0)  out += TextFormatter.OBFUSCATED_FORMAT;
		return out;
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
