package com.jeremiahbl.bfcmod.utils;

import org.checkerframework.checker.nullness.qual.NonNull;

import net.minecraft.server.level.ServerPlayer;

public interface IMetadataProvider {
	@NonNull public default String getPlayerPrefix(@NonNull ServerPlayer player) { return getPlayerPrefixAndSuffix(player)[0]; }
	@NonNull public default String getPlayerSuffix(@NonNull ServerPlayer player) { return getPlayerPrefixAndSuffix(player)[1]; }
	@NonNull public String[] getPlayerPrefixAndSuffix(@NonNull ServerPlayer player);
}
