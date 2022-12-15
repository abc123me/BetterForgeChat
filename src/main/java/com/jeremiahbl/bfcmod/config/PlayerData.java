package com.jeremiahbl.bfcmod.config;

import java.io.*;
import java.util.*;

import com.jeremiahbl.bfcmod.BetterForgeChat;

public class PlayerData {
	public static final Map<UUID, PlayerData> map = new HashMap<>();
	public static final String playerDataFileName = "bfcmod.playerdata";
	
	public final UUID uuid;
	public String nickname = null;
	public Long discordID = null;
	
	private PlayerData(UUID uuid) {
		this.uuid = uuid;
	}
	
	public boolean hasDiscord() { return discordID != null; }
	public boolean hasNickname() { return nickname != null; }
	public void setNickname(String nickname) { this.nickname = nickname; }
	public String getNickname() { return nickname; }
	
	public static PlayerData lookupOrCreatePlayerdata(UUID id) {
		PlayerData dat = map.get(id);
		if(dat == null) {
			dat = new PlayerData(id);
			dat.nickname = null;
			dat.discordID = null;
			map.put(id, dat);
		}
		return dat;
	}
	public static void setNickname(UUID uuid, String nickName) {
		lookupOrCreatePlayerdata(uuid).setNickname(nickName);
	}
	public static String getNickname(UUID id) {
		PlayerData dat = map.get(id);
		return dat == null ? null : dat.nickname;
	}
	
	@Override public String toString() {
		String out = "";
		out += "[PlayerDataEntry]\n";
		out += "\tUUID: " + encodeStr(uuid.toString()) + '\n';
		out += "\tNickname: " + encodeStr(nickname) + '\n';
		return out;
	}
	public static PlayerData fromString(String str) {
		if(str == null) return null;
		String[] strs = str.trim().split("\n");
		for(int i = 0; i < strs.length; i++)
			strs[i] = strs[i].trim();
		if(strs.length > 2 && strs[0].contentEquals("[PlayerDataEntry]")) {
			UUID uuid = null;
			String nick = null;
			for(int i = 1; i < strs.length; i++) {
				String[] prs = strs[i].split(":");
				for(int j = 0; j < prs.length; j++)
					prs[j] = prs[j].trim();
				try {
					if(prs[0].contentEquals("UUID"))
						uuid = UUID.fromString(decodeStr(prs[1]));
					else if(prs[0].contentEquals("Nickname"))
						nick = decodeStr(prs[1]);
				} catch(NullPointerException npe) {
					BetterForgeChat.LOGGER.error("Failed to parse PlayerData: \"" + strs[i] + "\"");
				}
			}
			if(uuid != null) {
				PlayerData out = new PlayerData(uuid);
				out.nickname = nick;
				return out;
			} else return null;
		} else return null;
	}
	public static void loadFromDir(File playerDirectory) {
		File dataFile = new File(playerDirectory, playerDataFileName);
		try {
			FileInputStream fis = new FileInputStream(dataFile);
			Scanner scn = new Scanner(fis).useDelimiter("[PlayerDataEntry]\n");
			while(scn.hasNext()) {
				PlayerData pdat = PlayerData.fromString(scn.next());
				if(pdat != null) map.put(pdat.uuid, pdat);
			}
			scn.close();
			fis.close();
		} catch(IOException ioe) {
			BetterForgeChat.LOGGER.error("Failed to save " + dataFile.getAbsolutePath());
		}
	}
	public static void saveToDir(File playerDirectory) {
		File dataFile = new File(playerDirectory, playerDataFileName);
		try {
			FileOutputStream fos = new FileOutputStream(dataFile);
			for(PlayerData dat : map.values())
				if(dat != null)
					fos.write(dat.toString().getBytes());
			fos.close();
		} catch(IOException ioe) {
			BetterForgeChat.LOGGER.error("Failed to save " + dataFile.getAbsolutePath());
		}
	}
	public static String encodeStr(String str) {
		if(str == null) return "null";
		String newStr = "";
		for(int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if((c >= ' ' && c <= '~') && c != '\"' && c != '\\') newStr += c;
			else {
				String hex = Integer.toHexString(c);
				if(hex.length() < 4) hex = "0".repeat(4 - hex.length()) + hex;
				newStr += "\\u" + hex;
			}
		}
		return "\"" + newStr + "\"";
	}
	public static String decodeStr(String str) {
		if(str == null) return null;
		str = str.trim();
		if(str.startsWith("\"") && str.endsWith("\"") && str.length() > 1) {
			String out = "";
			for(int i = 1; i < str.length() - 1; i++) {
				char c = str.charAt(i);
				if(c == '\\' && i < str.length() - 6 && str.charAt(i + 1) == 'u') {
					String hex = str.substring(i + 2, i + 6);
					out += (char) Integer.parseUnsignedInt(hex, 16);
					i += 5;
				} else out += c;
			}
			return out;
		} else return null;
	}
}
