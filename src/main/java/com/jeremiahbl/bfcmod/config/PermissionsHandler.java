package com.jeremiahbl.bfcmod.config;

import com.jeremiahbl.bfcmod.BetterForgeChat;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.events.PermissionGatherEvent.Nodes;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContext;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

@EventBusSubscriber
public class PermissionsHandler {	
	public static PermissionNode<Boolean> coloredChatNode = 
			ezyPermission("chat.colors", true);
	public static PermissionNode<Boolean> styledChatNode = 
			ezyPermission("chat.styles", true);
	public static PermissionNode<Boolean> markdownChatNode = 
			ezyPermission("chat.styles.md", true);
	public static PermissionNode<Boolean> tabListNicknameNode = 
			ezyPermission("tablist.nickname", true);
	public static PermissionNode<Boolean> tabListMetadataNode = 
			ezyPermission("tablist.metadata", true);
	
	public static PermissionNode<Boolean> colorsCommand = 
			ezyPermission("commands.colors", true);
	public static PermissionNode<Boolean> bfcModCommand = 
			ezyPermission("commands.bfcmod", true);
	public static PermissionNode<Boolean> bfcModCommandColorsSubCommand = 
			ezyPermission("commands.bfcmod.colors", true);
	public static PermissionNode<Boolean> bfcModCommandInfoSubCommand = 
			ezyPermission("commands.bfcmod.info", true);
	
	@SubscribeEvent public void registerPermissionNodes(Nodes pge) {
		pge.addNodes(styledChatNode, coloredChatNode, markdownChatNode, tabListMetadataNode, tabListNicknameNode);
	}
	
	private static PermissionNode<Boolean> ezyPermission(String id, boolean defVal) {
		return new PermissionNode<Boolean>(BetterForgeChat.MODID, id, 
				PermissionTypes.BOOLEAN, (player, uuid, context) -> defVal);
	}

	public static boolean playerHasPermission(ServerPlayer player, PermissionNode<Boolean> node) {
		Boolean bool = PermissionAPI.getPermission(player, node, new PermissionDynamicContext[0]);
		return bool == null ? false : bool.booleanValue();
	}
}
