package com.jeremiahbl.bfcmod.events;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jeremiahbl.bfcmod.BetterForgeChat;
import com.jeremiahbl.bfcmod.BetterForgeChatUtilities;
import com.jeremiahbl.bfcmod.TextFormatter;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ChatEventHandler {
	@SubscribeEvent
    public void onServerChat(ServerChatEvent e) {
    	if(e == null) return;
    	String tstamp = new SimpleDateFormat("hh:mm").format(new Date());
		String user = e.getUsername();
		String msg = e.getMessage();
		if(user == null || msg == null) return;
		if(msg.length() <= 0) return;
		
		if(BetterForgeChat.instance.perms != null)
			user = BetterForgeChatUtilities.getRawPlayerName(e.getPlayer());
		
		msg = String.format("%s | %s: %s", tstamp, user, msg);
		e.setComponent(TextFormatter.stringToFormattedText(msg));
    }
}