package com.jeremiahbl.bfcmod.utils;

import org.checkerframework.checker.nullness.qual.NonNull;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.minecraft.server.level.ServerPlayer;

public class LuckPermsProvider implements IMetadataProvider {
	public final LuckPerms perms;
	
	public LuckPermsProvider() { // This one just throws a mean exception if it fails
		this(net.luckperms.api.LuckPermsProvider.get());
	}
	public LuckPermsProvider(LuckPerms perms) {
		if(perms == null)
			throw new NullPointerException("LuckPerms object cannot be null!");
		this.perms = perms;
	}

	private @NonNull User playerToUser(@NonNull ServerPlayer player) {
		return perms.getPlayerAdapter(ServerPlayer.class).getUser(player);
	}
	
	@Override public String[] getPlayerPrefixAndSuffix(ServerPlayer player) {
		User usr = playerToUser(player);
		CachedMetaData mdat = usr.getCachedData().getMetaData();
		return new String[] { mdat.getPrefix(), mdat.getSuffix() };
	}
}
