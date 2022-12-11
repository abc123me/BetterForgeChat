package com.jeremiahbl.bfcmod.utils.moddeps;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.jeremiahbl.bfcmod.utils.IMetadataProvider;
import com.mojang.authlib.GameProfile;

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
	
	@Override public String[] getPlayerPrefixAndSuffix(GameProfile player) {
		try {
			User usr = perms.getUserManager().getUser(player.getId());
			if(usr == null) return null;
			CachedMetaData mdat = usr.getCachedData().getMetaData();
			return new String[] { mdat.getPrefix(), mdat.getSuffix() };
		} catch(IllegalStateException ise) {
			return null;
		}
	}
	@Override public @NonNull String getProviderName() {
		return "LuckPerms";
	}
}
