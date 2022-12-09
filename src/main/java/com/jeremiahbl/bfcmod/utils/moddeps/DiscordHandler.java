package com.jeremiahbl.bfcmod.utils.moddeps;

import java.time.Duration;
import java.util.ArrayList;

import com.jeremiahbl.bfcmod.BetterForgeChat;
import com.jeremiahbl.bfcmod.MarkdownFormatter;
import com.jeremiahbl.bfcmod.config.ConfigHandler;
import com.jeremiahbl.bfcmod.config.IReloadable;
import com.jeremiahbl.bfcmod.utils.IDiscordInterface;
import com.jeremiahbl.bfcmod.utils.IDiscordListener;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.rest.entity.RestChannel;

public class DiscordHandler implements IDiscordInterface {
	private GatewayDiscordClient client = null;
	private String token = null, channel = null;
	private RestChannel msgChannel = null;
	private ArrayList<IDiscordListener> lists = null;
	
	public DiscordHandler() {
		lists = new ArrayList<IDiscordListener>();
	}
	
	@Override public void reloadConfigOptions() {
		String oldToken = token, oldChannel = channel;
		token = ConfigHandler.config.discordBotToken.get();
		channel = ConfigHandler.config.discordBotChannelID.get();
		if(!token.equals(oldToken)) {
			disconnectAPI();
			connectAPI();
		}
		if(!channel.equals(oldChannel))
			msgChannel = client.rest().getChannelById(Snowflake.of(channel));
	}

	@Override public void sendMessage(String str) {
		sendDiscordMessage(MarkdownFormatter.formattedStringToMarkdownString(str));
	}
	public void sendDiscordMessage(String msg) {
		if(msgChannel != null)
			msgChannel.createMessage(msg);
	} 
	private void onReadyEvent(ReadyEvent e) {
		msgChannel = client.rest().getChannelById(Snowflake.of(channel));
		sendDiscordMessage("***BetterForgeChat Server started***");
		for(IDiscordListener list : lists)
			if(list != null)
				list.onDiscordReady();
	}
	private void onMessageCreateEvent(MessageCreateEvent e) {
		for(IDiscordListener list : lists)
			if(list != null)
				list.onMessageReceived(e.getMessage().getContent(), "");;
	}
	
	@Override public void connectAPI() {
		BetterForgeChat.LOGGER.info("Discord API Client connecting!");
		DiscordClient cli = DiscordClient.create(token);
		client = cli.login().block(Duration.ofSeconds(3));
		client.getEventDispatcher().on(ReadyEvent.class).subscribe((e) -> onReadyEvent(e));
		client.getEventDispatcher().on(MessageCreateEvent.class).subscribe((e) -> onMessageCreateEvent(e));
		BetterForgeChat.LOGGER.info("Discord API Client connected!");
	}
	@Override public void disconnectAPI() {
		if(client != null) {
			BetterForgeChat.LOGGER.info("Discord API Client disconnecting!");
			sendDiscordMessage("***BetterForgeChat Server stopping***");
			client.logout().block(Duration.ofSeconds(3));
			client = null;
			BetterForgeChat.LOGGER.info("Discord API Client disconnected!");
		} else BetterForgeChat.LOGGER.info("Discord API Client already disconnected!");
	}

	public static DiscordHandler bfcFactory() {
		DiscordHandler handler = new DiscordHandler();
		handler.connectAPI();
		BetterForgeChat.instance.registerReloadable(handler);
		return handler;
	}

	@Override public void registerDiscordListener(IDiscordListener list) {
		lists.add(list);
	}
}
