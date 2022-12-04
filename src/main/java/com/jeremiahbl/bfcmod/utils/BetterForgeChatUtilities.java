package com.jeremiahbl.bfcmod.utils;

import com.jeremiahbl.bfcmod.BetterForgeChat;
import com.jeremiahbl.bfcmod.TextFormatter;
import com.jeremiahbl.bfcmod.config.ConfigHandler;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class BetterForgeChatUtilities {
	public static String getRawPreferredPlayerName(ServerPlayer player) {
		return getRawPreferredPlayerName(player, true, true);
	}
	public static String getRawPreferredPlayerName(ServerPlayer player, boolean enableNickname, boolean enableMetadata) {
		String name = BetterForgeChat.instance.nicknameProvider != null && enableNickname ? BetterForgeChat.instance.nicknameProvider.getPlayerChatName(player) : player.getGameProfile().getName();
		String pfx = "", sfx = "";
		if(enableMetadata && BetterForgeChat.instance.metadataProvider != null) {
			String[] dat = BetterForgeChat.instance.metadataProvider.getPlayerPrefixAndSuffix(player);
			pfx = dat[0];
			sfx = dat[1];
		}
		String fmat = ConfigHandler.config.playerNameFormat.get();
		return fmat.replace("$prefix", pfx).replace("$name", name).replace("$suffix", sfx);
	}
	public static TextComponent getFormattedPlayerName(ServerPlayer player) {
		return TextFormatter.stringToFormattedText(getRawPreferredPlayerName(player));
	}
	public static TextComponent getFormattedPlayerName(ServerPlayer player, boolean enableNickname, boolean enableMetadata) {
		return TextFormatter.stringToFormattedText(getRawPreferredPlayerName(player, enableNickname, enableMetadata));
	}
}
