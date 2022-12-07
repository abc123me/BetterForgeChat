package com.jeremiahbl.bfcmod.utils.moddeps;

import com.jeremiahbl.bfcmod.utils.IMetadataProvider;

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
	
	@Override public String[] getPlayerPrefixAndSuffix(ServerPlayer player) {
		User usr = perms.getUserManager().getUser(player.getGameProfile().getId());
		if(usr == null) return null;
		CachedMetaData mdat = usr.getCachedData().getMetaData();
		return new String[] { mdat.getPrefix(), mdat.getSuffix() };
	}
}
