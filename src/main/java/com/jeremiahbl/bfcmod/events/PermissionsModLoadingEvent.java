package com.jeremiahbl.bfcmod.events;

import com.jeremiahbl.bfcmod.BetterForgeChat;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PermissionsModLoadingEvent {
	@SubscribeEvent
	public void onServerStarted(ServerStartedEvent e) {
		BetterForgeChat.LOGGER.info("Attempting to load LuckPerms API!");
    	boolean failed = true;
    	try {
    		LuckPerms perms = null;
    		perms = LuckPermsProvider.get();
    		if(perms != null) {
    			BetterForgeChat.instance.perms = perms;
	    		failed = false;
    		}
    	} catch(Exception e2) { }
    	//
    	BetterForgeChat.LOGGER.info((failed ? "LuckPerms API not found, is LuckPerms installed?" : "LuckPerms API found!"));
    	if(failed) BetterForgeChat.LOGGER.warn("WARNING - LuckPerms API wasn't found, mod won't use it!");
	}
}
