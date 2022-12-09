package com.jeremiahbl.bfcmod.config;

import java.util.ArrayList;

import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.config.ModConfigEvent.Reloading;

@EventBusSubscriber
public class ConfigurationEventHandler {
	private ArrayList<IReloadable> reloadables = new ArrayList<IReloadable>();
	
	public void registerReloadable(IReloadable rel) {
		reloadables.add(rel);
	}
	
	public void reloadConfigOptions() {
		for(IReloadable reloadable : reloadables)
			if(reloadable != null)
				reloadable.reloadConfigOptions();
	}
	
	@SubscribeEvent
	public void onModConfigReloadingEvent(Reloading e) {
		reloadConfigOptions();
	}
	@SubscribeEvent
	public void onServerStarted(ServerStartedEvent e) {
		reloadConfigOptions();
	}
}
