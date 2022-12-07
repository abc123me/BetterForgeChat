package com.jeremiahbl.bfcmod.utils.moddeps;

import com.jeremiahbl.bfcmod.utils.INicknameProvider;

import dev.ftb.mods.ftbessentials.FTBEssentials;
import dev.ftb.mods.ftbessentials.util.FTBEPlayerData;
import net.minecraft.server.level.ServerPlayer;

public class FTBNicknameProvider implements INicknameProvider {
	public FTBNicknameProvider() {
		// Literally here just to trigger an error! by loading the FTBEPlayerData class
		FTBEPlayerData.MAP.size();
	}
	@Override public String getPlayerNickname(ServerPlayer player) {
		FTBEPlayerData data = FTBEPlayerData.get(player);
		if(data != null && data.nick != null && data.nick.length() > 0)
			return data.nick;
		return null;
	}
}
