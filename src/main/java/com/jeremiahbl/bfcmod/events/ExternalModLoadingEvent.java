package com.jeremiahbl.bfcmod.events;

import com.jeremiahbl.bfcmod.BetterForgeChat;
import com.jeremiahbl.bfcmod.config.ConfigHandler;
import com.jeremiahbl.bfcmod.utils.FTBNicknameProvider;
import com.jeremiahbl.bfcmod.utils.LuckPermsProvider;

import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.permission.PermissionAPI;

@EventBusSubscriber
public class ExternalModLoadingEvent {
	@SubscribeEvent public void onServerStarted(ServerStartedEvent e) {
		loadLuckPerms();
		loadFtbEssentials();
	}
	private void loadLuckPerms() {
		if(!ConfigHandler.config.enableLuckPerms.get()) {
			BetterForgeChat.LOGGER.info("LuckPerms API was skipped by configuration file!");
			return;
		}
		BetterForgeChat.LOGGER.info("Attempting to load LuckPerms API!");
    	try {
    		BetterForgeChat.instance.metadataProvider = new LuckPermsProvider();
    		BetterForgeChat.LOGGER.info("LuckPerms API found and loaded sucessfully!");
    	} catch(Error e2) { // Could have a NoClassDefFoundError here!
    		BetterForgeChat.instance.metadataProvider = null;
    		BetterForgeChat.LOGGER.warn("WARNING - LuckPerms API wasn't found, we won't use it!");
    	}
	}
	private void loadFtbEssentials() {
		if(!ConfigHandler.config.enableFtbEssentials.get()) {
			BetterForgeChat.LOGGER.info("FTB Essentials was skipped by configuration file!");
			return;
		}
		BetterForgeChat.LOGGER.info("Attempting to load FTB Essentials!");
    	try {
    		BetterForgeChat.instance.nicknameProvider = new FTBNicknameProvider();
    		BetterForgeChat.LOGGER.info("FTB Essentials API found and loaded sucessfully!");
    	} catch(Error e2) { // Could have a NoClassDefFoundError here!
    		BetterForgeChat.instance.nicknameProvider = null;
    		BetterForgeChat.LOGGER.warn("WARNING - FTB Essentials wasn't found, we won't use it!");
    	}
	}
}
