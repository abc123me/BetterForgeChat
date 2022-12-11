package com.jeremiahbl.bfcmod.events;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.jeremiahbl.bfcmod.MarkdownFormatter;
import com.jeremiahbl.bfcmod.TextFormatter;
import com.jeremiahbl.bfcmod.config.ConfigHandler;
import com.jeremiahbl.bfcmod.config.IReloadable;
import com.jeremiahbl.bfcmod.config.PermissionsHandler;
import com.jeremiahbl.bfcmod.utils.BetterForgeChatUtilities;
import com.mojang.authlib.GameProfile;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ChatEventHandler implements IReloadable {
	private SimpleDateFormat timestampFormat = null;
	private boolean markdownEnabled = false;
	private String chatMessageFormat = "";
	private boolean loaded = false;
	
	public void reloadConfigOptions() {
		loaded = false;
		timestampFormat = ConfigHandler.config.enableTimestamp.get().booleanValue() ? new SimpleDateFormat(ConfigHandler.config.timestampFormat.get()) : null;
		markdownEnabled = ConfigHandler.config.enableMarkdown.get().booleanValue();
		chatMessageFormat = ConfigHandler.config.chatMessageFormat.get();
		loaded = true;
	}
	
	public Style getHoverClickEventStyle(Component old) {
		if(old != null && old instanceof TranslatableComponent) {
			TranslatableComponent tcmp = (TranslatableComponent) old;
			Object[] args = tcmp.getArgs();
			for(Object arg : args) {
				if(arg != null && arg instanceof TextComponent) {
					TextComponent tc = (TextComponent) arg;
					if(tc.getStyle() != null && tc.getStyle().getClickEvent() != null)
						return ((TextComponent) arg).getStyle();
				}
			}
		}
		return null;
	}
	
	@SubscribeEvent
    public void onServerChat(ServerChatEvent e) {
		if(!loaded) return; // Just do nothing until everything's ready to go!
    	ServerPlayer player = e.getPlayer();
    	GameProfile profile = player.getGameProfile();
    	UUID uuid = profile.getId();
    	if(e == null || player == null) return;
		String msg = e.getMessage();
		if(msg == null || msg.length() <= 0) return;
    	String tstamp = timestampFormat == null ? "" : timestampFormat.format(new Date());
		String name = BetterForgeChatUtilities.getRawPreferredPlayerName(profile);
		String fmat = chatMessageFormat.replace("$time", tstamp).replace("$name", name);
		TextComponent beforeMsg = TextFormatter.stringToFormattedText(fmat.substring(0, fmat.indexOf("$msg")));
		TextComponent afterMsg = TextFormatter.stringToFormattedText(fmat.substring(fmat.indexOf("$msg") + 4, fmat.length()));
		boolean enableColor = PermissionsHandler.playerHasPermission(uuid, PermissionsHandler.coloredChatNode);
		boolean enableStyle = PermissionsHandler.playerHasPermission(uuid, PermissionsHandler.styledChatNode);
		// Create an error message if the plyer isn't allowed to use styles/colors
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
		// Convert markdown to normal essentials formatting
		if(markdownEnabled && enableStyle && PermissionsHandler.playerHasPermission(uuid, PermissionsHandler.markdownChatNode))
			msg = MarkdownFormatter.markdownStringToFormattedString(msg);
		// Start generating the main TextComponent
		TextComponent msgComp = TextFormatter.stringToFormattedText(msg, enableColor, enableStyle);
		// Append the hover and click event crap
		Style sty = getHoverClickEventStyle(e.getComponent());
		TextComponent ecmp = new TextComponent("");
		if(sty != null && sty.getHoverEvent() != null)
			ecmp.setStyle(sty);
		e.setComponent(beforeMsg.append(msgComp.append(afterMsg)));
    }
}