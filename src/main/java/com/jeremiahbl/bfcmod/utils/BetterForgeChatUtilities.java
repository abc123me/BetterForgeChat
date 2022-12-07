package com.jeremiahbl.bfcmod.utils;

import com.jeremiahbl.bfcmod.BetterForgeChat;
import com.jeremiahbl.bfcmod.TextFormatter;
import com.jeremiahbl.bfcmod.config.ConfigHandler;
import com.mojang.authlib.GameProfile;

import net.minecraft.network.chat.TextComponent;

public class BetterForgeChatUtilities {
	private static String playerNameFormat = "";
	
	public static void reloadConfig() {
		playerNameFormat = ConfigHandler.config.playerNameFormat.get();
	}
	
	public static String getRawPreferredPlayerName(GameProfile player) {
		return getRawPreferredPlayerName(player, true, true);
	}
	public static String getRawPreferredPlayerName(GameProfile player, boolean enableNickname, boolean enableMetadata) {
		String name = BetterForgeChat.instance.nicknameProvider != null && enableNickname ? BetterForgeChat.instance.nicknameProvider.getPlayerChatName(player) : player.getName();
		if(name == null) name = player.getName(); /* No nickname (or null-nickname) provided */
		String pfx = "", sfx = "";
		if(enableMetadata && BetterForgeChat.instance.metadataProvider != null) {
			String[] dat = BetterForgeChat.instance.metadataProvider.getPlayerPrefixAndSuffix(player);
			if(dat != null) {
				pfx = dat[0] != null ? dat[0] : "";
				sfx = dat[1] != null ? dat[1] : "";
			}
		}
		String fmat = playerNameFormat;
		if(fmat == null) {
			BetterForgeChat.LOGGER.warn("Could not get playerNameFormat from configuration file, please post issue on GitHub!");
			return player.getName();
		} else return fmat.replace("$prefix", pfx).replace("$name", name).replace("$suffix", sfx);
	}
	public static TextComponent getFormattedPlayerName(GameProfile player) {
		return TextFormatter.stringToFormattedText(getRawPreferredPlayerName(player));
	}
	public static TextComponent getFormattedPlayerName(GameProfile player, boolean enableNickname, boolean enableMetadata) {
		return TextFormatter.stringToFormattedText(getRawPreferredPlayerName(player, enableNickname, enableMetadata));
	}
}
