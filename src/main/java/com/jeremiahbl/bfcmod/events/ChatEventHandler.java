package com.jeremiahbl.bfcmod.events;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jeremiahbl.bfcmod.TextFormatter;
import com.jeremiahbl.bfcmod.config.ConfigHandler;
import com.jeremiahbl.bfcmod.config.PermissionsHandler;
import com.jeremiahbl.bfcmod.utils.BetterForgeChatUtilities;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.permission.*;
import net.minecraftforge.server.permission.nodes.*;

@EventBusSubscriber
public class ChatEventHandler {
	private SimpleDateFormat timestampFormat = null;
	
	@SubscribeEvent
	public void onServerLoad(ServerStartedEvent e) {
		timestampFormat = ConfigHandler.config.enableTimestamp.get().booleanValue() ? new SimpleDateFormat(ConfigHandler.config.timestampFormat.get()) : null;
	}
	@SubscribeEvent
    public void onServerChat(ServerChatEvent e) {
    	ServerPlayer player = e.getPlayer();
    	if(e == null || player == null) return;
		String msg = e.getMessage();
		if(msg == null || msg.length() <= 0) return;
    	String tstamp = timestampFormat == null ? "" : timestampFormat.format(new Date());
		String name = BetterForgeChatUtilities.getRawPreferredPlayerName(player);
		String fmat = ConfigHandler.config.chatMessageFormat.get().replace("$time", tstamp).replace("$name", name);
		TextComponent beforeMsg = TextFormatter.stringToFormattedText(fmat.substring(0, fmat.indexOf("$msg")));
		TextComponent afterMsg = TextFormatter.stringToFormattedText(fmat.substring(fmat.indexOf("$msg") + 4, fmat.length()));
		Boolean enableColor = PermissionAPI.getPermission(player, PermissionsHandler.coloredChatNode, new PermissionDynamicContext[0]);
		Boolean enableStyle = PermissionAPI.getPermission(player, PermissionsHandler.styledChatNode, new PermissionDynamicContext[0]);
		if(enableColor == null) enableColor = true;
		if(enableStyle == null) enableStyle = true;
		String emsg = "";
		if(!enableColor && TextFormatter.messageContainsColorsOrStyles(msg, true))
			emsg = "&4&lYou are not permitted to use colors";
		if(!enableStyle && TextFormatter.messageContainsColorsOrStyles(msg, false))
			emsg = emsg.length() > 0 ? " or styles" : "&4&lYou are not permitted to use styles";
		if(emsg.length() > 0)
			player.sendMessage(new TextComponent(emsg + "!"), ChatType.GAME_INFO, player.getUUID());
		TextComponent msgComp = TextFormatter.stringToFormattedText(msg, enableColor, enableStyle);
		e.setComponent(beforeMsg.append(msgComp.append(afterMsg)));
    }
}