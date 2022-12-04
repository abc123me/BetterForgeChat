package com.jeremiahbl.bfcmod.events;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.jeremiahbl.bfcmod.MarkdownFormatter;
import com.jeremiahbl.bfcmod.TextFormatter;
import com.jeremiahbl.bfcmod.config.ConfigHandler;
import com.jeremiahbl.bfcmod.config.PermissionsHandler;
import com.jeremiahbl.bfcmod.utils.BetterForgeChatUtilities;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

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
		boolean enableColor = PermissionsHandler.playerHasPermission(player, PermissionsHandler.coloredChatNode);
		boolean enableStyle = PermissionsHandler.playerHasPermission(player, PermissionsHandler.styledChatNode);
		String emsg = "";
		if(!enableColor && TextFormatter.messageContainsColorsOrStyles(msg, true))
			emsg = "You are not permitted to use colors";
		if(!enableStyle && TextFormatter.messageContainsColorsOrStyles(msg, false))
			emsg += emsg.length() > 0 ? " or styles" : "You are not permitted to use styles";
		if(emsg.length() > 0) {
			TextComponent ecmp = new TextComponent(emsg + "!");
			ecmp.withStyle(ChatFormatting.BOLD);
			ecmp.withStyle(ChatFormatting.RED);
			player.sendMessage(ecmp, ChatType.GAME_INFO, UUID.randomUUID());
		}
		if(enableStyle && PermissionsHandler.playerHasPermission(player, PermissionsHandler.markdownChatNode))
			msg = MarkdownFormatter.markdownStringToFormattedString(msg);
		TextComponent msgComp = TextFormatter.stringToFormattedText(msg, enableColor, enableStyle);
		e.setComponent(beforeMsg.append(msgComp.append(afterMsg)));
    }
}