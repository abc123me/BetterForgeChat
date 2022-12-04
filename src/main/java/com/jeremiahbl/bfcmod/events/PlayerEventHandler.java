package com.jeremiahbl.bfcmod.events;

import com.jeremiahbl.bfcmod.config.ConfigHandler;
import com.jeremiahbl.bfcmod.utils.BetterForgeChatUtilities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat;
import net.minecraftforge.event.entity.player.PlayerEvent.TabListNameFormat;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PlayerEventHandler {
	@SubscribeEvent
	public void onTabListNameFormatEvent(TabListNameFormat e) {
		if(ConfigHandler.config.enableTabListIntegration.get()) {
			Player player = e.getPlayer();
			if(player instanceof ServerPlayer)
				e.setDisplayName(BetterForgeChatUtilities.getFormattedPlayerName((ServerPlayer) player,
						ConfigHandler.config.enableNicknamesInTabList.get(),
						ConfigHandler.config.enableMetadataInTabList.get()));
		}
	}
	@SubscribeEvent
	public void onNameFormatEvent(NameFormat e) {
		Player player = e.getPlayer();
		if(player instanceof ServerPlayer) 
			e.setDisplayname(BetterForgeChatUtilities.getFormattedPlayerName((ServerPlayer) player));
	}
}
