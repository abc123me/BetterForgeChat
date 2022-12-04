package com.jeremiahbl.bfcmod.events;

import com.jeremiahbl.bfcmod.config.ConfigHandler;
import com.jeremiahbl.bfcmod.config.PermissionsHandler;
import com.jeremiahbl.bfcmod.utils.BetterForgeChatUtilities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PermissionsChangedEvent;
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
			if(player instanceof ServerPlayer) {
				ServerPlayer sexyPlayer = (ServerPlayer) player;
				boolean allowNickname = ConfigHandler.config.enableNicknamesInTabList.get();
				boolean allowMetadata = ConfigHandler.config.enableMetadataInTabList.get();
				if(allowNickname) allowNickname = PermissionsHandler.playerHasPermission(sexyPlayer, PermissionsHandler.tabListNicknameNode);
				if(allowMetadata) allowMetadata = PermissionsHandler.playerHasPermission(sexyPlayer, PermissionsHandler.tabListMetadataNode);
				e.setDisplayName(BetterForgeChatUtilities.getFormattedPlayerName(sexyPlayer, allowNickname, allowMetadata));
			}
		}
	}
	@SubscribeEvent
	public void onNameFormatEvent(NameFormat e) {
		Player player = e.getPlayer();
		if(player instanceof ServerPlayer) 
			e.setDisplayname(BetterForgeChatUtilities.getFormattedPlayerName((ServerPlayer) player));
	}
	@SubscribeEvent
	public void onPermissionsChanged(PermissionsChangedEvent e) {
		Player player = e.getPlayer();
		if(player instanceof ServerPlayer) {
			// Update tab list name so if the players group changes or there tab
			// list permissions are revoked the tab list name gets updated
			ServerPlayer sexyPlayer = (ServerPlayer) player;
			sexyPlayer.refreshTabListName();
		}
	}
}
