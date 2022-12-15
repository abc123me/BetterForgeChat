package com.jeremiahbl.bfcmod.utils.moddeps;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.jeremiahbl.bfcmod.utils.INicknameProvider;
import com.mojang.authlib.GameProfile;

import dev.ftb.mods.ftbessentials.util.FTBEPlayerData;

public class FTBNicknameProvider implements INicknameProvider {
	public FTBNicknameProvider() {
		// Literally here just to trigger an error! by loading the FTBEPlayerData class
		FTBEPlayerData.MAP.size();
	}
	@Override public String getPlayerNickname(GameProfile player) {
		FTBEPlayerData data = FTBEPlayerData.MAP.get(player.getId());
		if(data != null && data.nick != null && data.nick.length() > 0)
			return data.nick;
		return null;
	}
	@Override public @NonNull String getProviderName() {
		return "FTB Essentials";
	}
	
}
