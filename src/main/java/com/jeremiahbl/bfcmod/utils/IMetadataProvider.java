package com.jeremiahbl.bfcmod.utils;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.mojang.authlib.GameProfile;

public interface IMetadataProvider {
	@NonNull public String getProviderName();
	@NonNull public default String getPlayerPrefix(@NonNull GameProfile player) { return getPlayerPrefixAndSuffix(player)[0]; }
	@NonNull public default String getPlayerSuffix(@NonNull GameProfile player) { return getPlayerPrefixAndSuffix(player)[1]; }
	@NonNull public String[] getPlayerPrefixAndSuffix(@NonNull GameProfile player);
}
