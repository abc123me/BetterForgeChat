package com.jeremiahbl.bfcmod.config;

import java.lang.reflect.Field;
import java.util.UUID;

import com.jeremiahbl.bfcmod.BetterForgeChat;
import com.jeremiahbl.bfcmod.TextFormatter;

import net.minecraft.network.chat.TextComponent;
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
			ezyPermission("chat.colors", true, "Chat colors", "Enables/Disables colors in chat");
	public static PermissionNode<Boolean> styledChatNode = 
			ezyPermission("chat.styles", true, "Chat styles", "Enables/Disables styles in chat");
	public static PermissionNode<Boolean> markdownChatNode = 
			ezyPermission("chat.styles.md", true, "Chat markdown styling", "Enables/Disables markdown styling in chat");
	public static PermissionNode<Boolean> tabListNicknameNode = 
			ezyPermission("tablist.nickname", true, "Tab list nicknames", "Enables/Disables nicknames showing in the tab list");
	public static PermissionNode<Boolean> tabListMetadataNode = 
			ezyPermission("tablist.metadata", true, "Tab list metadata", "Enables/Disables prefixes&suffixes showing in the tab list");
	
	public static PermissionNode<Boolean> colorsCommand =
			ezyPermission("commands.colors", true, "Colors command", "Enables/Disables the \"/colors\" command");
	public static PermissionNode<Boolean> bfcModCommand = 
			ezyPermission("commands.bfc.allowed", true, "BetterForgeChat command", "Enables/Disables the \"/bfc\" command");
	public static PermissionNode<Boolean> bfcModCommandColorsSubCommand = 
			ezyPermission("commands.bfc.colors", true, "BetterForgeChat colors sub-command", "Enables/Disables the \"/bfc colors\" sub-command");
	public static PermissionNode<Boolean> bfcModCommandInfoSubCommand = 
			ezyPermission("commands.bfc.info", true, "BetterForgeChat info sub-command", "Enables/Disables the \"/bfc info\" sub-command");
	
	public static PermissionNode<Boolean> whoisCommand = 
			ezyPermission("commands.whois", true, "Nickname", "Enables/Disables the \"/whois <nickname>\" command");
	public static PermissionNode<Boolean> nickCommand = 
			ezyPermission("commands.nick", true, "Nickname", "Enables/Disables the \"/nick <nickname>\" command");
	public static PermissionNode<Boolean> nickOthersCommand = 
			ezyPermission("commands.nick.others", true, "Modify nicknames", "Enables/Disables the \"/nick <username> <nickname>\" command");
	
	@SubscribeEvent public void registerPermissionNodes(Nodes pge) {
		for(Field fld : PermissionsHandler.class.getDeclaredFields()) {
			if(fld.getType() == PermissionNode.class) {
				try { // Fuck adding all these nodes manually
					pge.addNodes((PermissionNode<?>) fld.get(PermissionNode.class));
				} catch (Exception e) {}
			}
		}
	}
	
	private static PermissionNode<Boolean> ezyPermission(String id, boolean defVal, String name, String desc) {
		PermissionNode<Boolean> node = new PermissionNode<Boolean>(BetterForgeChat.MODID, id, 
				PermissionTypes.BOOLEAN, (player, uuid, context) -> defVal);
		node.setInformation(new TextComponent(name), TextFormatter.stringToFormattedText(desc));
		return node;
	}

	public static boolean playerHasPermission(UUID uuid, PermissionNode<Boolean> node) {
		Boolean bool = false;
		try {
			bool = PermissionAPI.getOfflinePermission(uuid, node, new PermissionDynamicContext[0]);
			//bool = PermissionAPI.getPermission(player, node, new PermissionDynamicContext[0]);
		} catch(IllegalStateException ise) {
			BetterForgeChat.LOGGER.info("IllegalStateException when getting player tab list permissions, assuming false");
		}
		return bool == null ? false : bool.booleanValue();
	}
}
