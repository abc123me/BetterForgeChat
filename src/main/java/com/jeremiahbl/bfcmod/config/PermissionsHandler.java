package com.jeremiahbl.bfcmod.config;

import com.jeremiahbl.bfcmod.BetterForgeChat;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.permission.events.PermissionGatherEvent.Nodes;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

@EventBusSubscriber
public class PermissionsHandler {
	public static PermissionNode<Boolean> coloredChatNode = 
			ezyPermission(ConfigHandler.config.coloredChatPermissionName.get(), true);
	public static PermissionNode<Boolean> styledChatNode = 
			ezyPermission(ConfigHandler.config.styledChatPermissionName.get(), true);
	
	@SubscribeEvent
	public void registerPermissionNodes(Nodes pge) {
		pge.addNodes(styledChatNode, coloredChatNode);
	}
	
	private static PermissionNode<Boolean> ezyPermission(String id, boolean defVal) {
		return new PermissionNode<Boolean>(BetterForgeChat.MODID, id, 
				PermissionTypes.BOOLEAN, (player, uuid, context) -> defVal);
	}
}
