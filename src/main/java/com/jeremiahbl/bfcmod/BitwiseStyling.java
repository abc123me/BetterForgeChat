package com.jeremiahbl.bfcmod;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;

public class BitwiseStyling {
	public static final byte BOLD_BIT          = 1;
	public static final byte UNDERLINE_BIT     = 2;
	public static final byte ITALIC_BIT        = 4;
	public static final byte OBFUSCATED_BIT    = 8;
	public static final byte STRIKETHROUGH_BIT = 16;

	public static final byte NO_STYLE = 0;
	public static final byte ALL_STYLES =
			BOLD_BIT | UNDERLINE_BIT | ITALIC_BIT | OBFUSCATED_BIT | STRIKETHROUGH_BIT;
	
	public static final String styleString(byte mask) {
		String out = "";
		if((mask & BitwiseStyling.BOLD_BIT) != 0)          out += TextFormatter.BOLD_FORMAT;
		if((mask & BitwiseStyling.ITALIC_BIT) != 0)        out += TextFormatter.ITALIC_FORMAT;
		if((mask & BitwiseStyling.UNDERLINE_BIT) != 0)     out += TextFormatter.UNDERLINE_FORMAT;
		if((mask & BitwiseStyling.STRIKETHROUGH_BIT) != 0) out += TextFormatter.STRIKETHROUGH_FORMAT;
		if((mask & BitwiseStyling.OBFUSCATED_BIT) != 0)    out += TextFormatter.OBFUSCATED_FORMAT;
		return out;
	}
	public static final TextComponent makeEncapsulatingTextComponent(String msg, byte mask) {
		TextComponent out = new TextComponent(msg);
		if((mask & BitwiseStyling.BOLD_BIT) != 0)          out.withStyle(ChatFormatting.BOLD); 
		if((mask & BitwiseStyling.ITALIC_BIT) != 0)        out.withStyle(ChatFormatting.ITALIC); 
		if((mask & BitwiseStyling.UNDERLINE_BIT) != 0)     out.withStyle(ChatFormatting.UNDERLINE); 
		if((mask & BitwiseStyling.STRIKETHROUGH_BIT) != 0) out.withStyle(ChatFormatting.STRIKETHROUGH); 
		if((mask & BitwiseStyling.OBFUSCATED_BIT) != 0)    out.withStyle(ChatFormatting.OBFUSCATED); 
		return out;
	}
	public static final byte getStyleBit(char c) {
    	switch(c) {
	    	case 'l': return BOLD_BIT;
			case 'n': return UNDERLINE_BIT;
			case 'o': return ITALIC_BIT;
			case 'k': return OBFUSCATED_BIT;
			case 'm': return STRIKETHROUGH_BIT;
			default: return 0;
    	}
    }
}
