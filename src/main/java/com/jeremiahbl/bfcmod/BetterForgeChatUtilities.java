package com.jeremiahbl.bfcmod;

import dev.ftb.mods.ftbessentials.util.FTBEPlayerData;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class BetterForgeChatUtilities {
	public static String getRawPlayerName(ServerPlayer player) {
		if(BetterForgeChat.instance.perms == null)
			return player.getGameProfile().getName();
		FTBEPlayerData data = FTBEPlayerData.get(player);
		String nickname = null;
		if(data != null && data.nick != null && data.nick.length() > 0)
			nickname = data.nick;
		User usr = BetterForgeChat.instance.perms.getPlayerAdapter(ServerPlayer.class).getUser(player);;
		String name = nickname != null ? nickname : usr.getFriendlyName();
		CachedMetaData mdat = usr.getCachedData().getMetaData();
		String pfx = mdat.getPrefix(), sfx = mdat.getSuffix();
		if(pfx != null) name = pfx + name;
		if(sfx != null) name = name + sfx;
		return name;
	}
	public static TextComponent getFormattedPlayerName(ServerPlayer player) {
		return TextFormatter.stringToFormattedText(getRawPlayerName(player));
	}
}
