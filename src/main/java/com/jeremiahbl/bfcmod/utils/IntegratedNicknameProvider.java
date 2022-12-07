package com.jeremiahbl.bfcmod.utils;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.jeremiahbl.bfcmod.config.PlayerData;

import net.minecraft.server.level.ServerPlayer;

public class IntegratedNicknameProvider implements INicknameProvider {
	@Override public String getPlayerNickname(@NonNull ServerPlayer player) {
		return PlayerData.getNickname(player.getGameProfile().getId());
	}
}
