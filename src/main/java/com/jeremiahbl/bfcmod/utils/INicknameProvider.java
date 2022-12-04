package com.jeremiahbl.bfcmod.utils;

import org.checkerframework.checker.nullness.qual.NonNull;

import net.minecraft.server.level.ServerPlayer;

public interface INicknameProvider {
	public String getPlayerNickname(@NonNull ServerPlayer player);
	@NonNull public default String getPlayerChatName(@NonNull ServerPlayer player) {
		String nick = getPlayerNickname(player);
		if(nick == null || nick.length() < 1) return player.getGameProfile().getName();
		else return nick;
	}
}