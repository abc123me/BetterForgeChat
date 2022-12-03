package com.jeremiahbl.bfcmod;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;

public final class TextFormatter {
	public static final TextComponent stringToFormattedText(String msg) {
		if(msg == null) return null;
		TextComponent newMsg = new TextComponent("");
		boolean nextIsStyle = false;
		String curStr = "";
		ChatFormatting curColor = ChatFormatting.WHITE;
		ChatFormatting curStyle = ChatFormatting.RESET;
		for(int i = 0; i < msg.length(); i++) {
			char c = msg.charAt(i);
			if(c == '&') {
				if(nextIsStyle) {
					nextIsStyle = false;
					curStr += "&";
				} else nextIsStyle = true;
			} else if(nextIsStyle) {
				TextComponent tmp = new TextComponent(curStr);
				tmp.withStyle(curStyle);
				tmp.withStyle(curColor);
				newMsg.append(tmp);
				curColor = getColor(c, curColor);
				curStyle = getStyle(c, curStyle);
				curStr = "";
				nextIsStyle = false;
			} else curStr += c;
		}
		if(curStr.length() > 0) {
			TextComponent tmp = new TextComponent(curStr);
			tmp.withStyle(curStyle);
			tmp.withStyle(curColor);
			newMsg.append(tmp);
		}
		return newMsg;
	}

	public static final String colorString() {
		return "&fLight:  &c&&c &e&&e &9&&9 &a&&a &b&&b &d&&d &f&&f &7&&7\n" + 
			   "&fDark:   &4&&4 &6&&6 &1&&1 &2&&2 &3&&3 &5&&5 &0&&0 &8&&8\n" + 
			   "&fStyles: &l&&l&r &n&&n&r &o&&o&r &m&&m&r &k&&k&r\n";
	}
    private static final ChatFormatting getColor(char c, ChatFormatting cur) {
    	switch(c) {
	    	case '0': return ChatFormatting.BLACK;
	    	case '1': return ChatFormatting.DARK_BLUE;
			case '2': return ChatFormatting.DARK_GREEN;
			case '3': return ChatFormatting.DARK_AQUA;
			case '4': return ChatFormatting.DARK_RED;
			case '5': return ChatFormatting.DARK_PURPLE;
			case '6': return ChatFormatting.GOLD;
			case '7': return ChatFormatting.GRAY;
			case '8': return ChatFormatting.DARK_GRAY;
			case '9': return ChatFormatting.BLUE;
			case 'a': return ChatFormatting.GREEN;
			case 'b': return ChatFormatting.AQUA;
			case 'c': return ChatFormatting.RED;
			case 'd': return ChatFormatting.LIGHT_PURPLE;
			case 'e': return ChatFormatting.YELLOW;
			case 'f': return ChatFormatting.WHITE;
			case 'r': return ChatFormatting.WHITE; // Reset
			default: return cur;
    	}
    }
    private static final ChatFormatting getStyle(char c, ChatFormatting cur) {
    	switch(c) {
	    	case 'l': return ChatFormatting.BOLD;
			case 'n': return ChatFormatting.UNDERLINE;
			case 'o': return ChatFormatting.ITALIC;
			case 'k': return ChatFormatting.OBFUSCATED;
			case 'm': return ChatFormatting.STRIKETHROUGH;
			case 'r': return ChatFormatting.RESET;
			default: return cur;
    	}
    }
}
