package com.jeremiahbl.bfcmod.utils;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.mojang.authlib.GameProfile;

public interface INicknameProvider {
	@NonNull public String getProviderName();
	public String getPlayerNickname(@NonNull GameProfile player);
	@NonNull public default String getPlayerChatName(@NonNull GameProfile player) {
		String nick = getPlayerNickname(player);
		if(nick == null || nick.length() < 1) return player.getName();
		else return nick;
	}
}