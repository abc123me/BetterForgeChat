package com.jeremiahbl.bfcmod.utils.moddeps;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.jeremiahbl.bfcmod.BetterForgeChat;
import com.jeremiahbl.bfcmod.TextFormatter;
import com.jeremiahbl.bfcmod.config.ConfigHandler;
import com.jeremiahbl.bfcmod.utils.BetterForgeChatUtilities;
import com.jeremiahbl.bfcmod.utils.IDiscordInterface;
import com.jeremiahbl.bfcmod.utils.IDiscordListener;
import com.mojang.authlib.GameProfile;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.rest.entity.RestChannel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraftforge.server.ServerLifecycleHooks;

public class DiscordHandler implements IDiscordInterface {
	private GatewayDiscordClient client = null;
	private String token = null, channel = null;
	private RestChannel msgChannel = null;
	private CopyOnWriteArrayList<IDiscordListener> lists = null;
	private String serverStartMsg = null, serverStopMsg = null;
	private String playerLeftFormat = null, playerJoinFormat = null;
	private String playerChatFormat = null;
	private boolean allowMentions = false;
	
	public DiscordHandler() {
		lists = new CopyOnWriteArrayList<IDiscordListener>();
	}
	
	@Override public void reloadConfigOptions() {
		loadConfigOptions(true);
	}
	public void loadConfigOptions(boolean handleStuff) {
		String oldToken = token, oldChannel = channel;
		token = ConfigHandler.config.discordBotToken.get();
		channel = ConfigHandler.config.discordBotChannelID.get();
		if(ConfigHandler.config.enableDiscordServerMessages.get()) {
			serverStopMsg = ConfigHandler.config.discordServerStopMessage.get();
			serverStartMsg = ConfigHandler.config.discordServerStartMessage.get();
		} else serverStartMsg = serverStopMsg = null;
		if(ConfigHandler.config.enableDiscordPlayerMessages.get()) {
			playerLeftFormat = ConfigHandler.config.discordPlayerLeftMessage.get();
			playerJoinFormat = ConfigHandler.config.discordPlayerJoinMessage.get();
		} else playerJoinFormat = playerLeftFormat = null;
		playerChatFormat = ConfigHandler.config.discordPlayerChatMessage.get();
		allowMentions = ConfigHandler.config.enableDiscordMentions.get();
		if(handleStuff) {
			if(!token.equals(oldToken)) {
				disconnectAPI();
				connectAPI();
			}
			if(!channel.equals(oldChannel))
				msgChannel = client.rest().getChannelById(Snowflake.of(channel));
		}
	}

	@Override public void sendPlayerMessage(GameProfile profile, String name, boolean left) {
		String usr = TextFormatter.removeTextFormatting(profile.getName());
		String nme = TextFormatter.removeTextFormatting(name);
		if(left) {
			if(playerLeftFormat != null)
				sendDiscordMessage(playerLeftFormat.replace("$user", usr).replace("$name", nme));
		} else {
			if(playerJoinFormat != null)
				sendDiscordMessage(playerJoinFormat.replace("$user", usr).replace("$name", nme));
		}
	}
	@Override public void sendChatMessage(GameProfile profile, String name, String rawMsg) {
		String usr = TextFormatter.removeTextFormatting(profile.getName());
		String nme = TextFormatter.removeTextFormatting(name);
		String msg = TextFormatter.removeTextFormatting(rawMsg);
		if(playerChatFormat != null)
			sendDiscordMessage(playerChatFormat.replace("$user", usr).replace("$name", nme).replace("$msg", msg));
	} 
	@Override public void sendDeathMessage(GameProfile playerProfile, String playerChatName, String reason) {
		// TODO: Not yet implemented!		
	}
	public void sendDiscordMessage(String msg) {
		if(msgChannel != null)
			msgChannel.createMessage(msg).subscribe();
		else BetterForgeChat.LOGGER.error("msgChannel is null, ignoring error!");
	} 
	private void onReadyEvent(ReadyEvent e) {
		msgChannel = client.rest().getChannelById(Snowflake.of(channel));
		if(serverStartMsg != null && serverStartMsg.length() > 0)
			sendDiscordMessage(serverStartMsg);
		for(IDiscordListener list : lists)
			if(list != null)
				list.onDiscordReady();
	}
	@SuppressWarnings("resource")
	private void runDiscordCommand(String cmd) {
		if(cmd.contentEquals("help")) {
			sendDiscordMessage("!help - Shows this message");
			sendDiscordMessage("!list - List players on server");
		} else if(cmd.contentEquals("list")) {
			PlayerList plist = ServerLifecycleHooks.getCurrentServer().getPlayerList();
			int maxPlayers = plist.getMaxPlayers();
			int curPlayers = plist.getPlayerCount();
			List<ServerPlayer> players = plist.getPlayers();
			sendDiscordMessage(String.format("%d/%d players online", curPlayers, maxPlayers));
			for(ServerPlayer player : players) {
				GameProfile prof = player.getGameProfile();
				String preferredName = BetterForgeChatUtilities.getRawPreferredPlayerName(prof);
				sendDiscordMessage(String.format("Player \"%s\" (aka. \"%s\")", TextFormatter.removeTextFormatting(preferredName), prof.getName()));
			}
		} else sendDiscordMessage("Unknown command, please see !help");
	}
	private void onMessageCreateEvent(MessageCreateEvent e) {
		if(e == null) return;
		Message msg = e.getMessage();
		if(msg == null) return;
		String cStr = msg.getContent();
		if(cStr.startsWith("!") && cStr.length() > 2) {
			runDiscordCommand(cStr.substring(1, cStr.length()));
			return;
		}
		User author = msg.getAuthor().orElse(null);
		if(author.getId().asLong() != client.getSelf().block().getId().asLong()
			&& msg.getChannelId().asLong() == msgChannel.getId().asLong()) {
			for(IDiscordListener list : lists)
				if(list != null)
					list.onDiscordMessageReceived(cStr, author == null ? "Unknown user" : author.getUsername());
		}
	}
	
	@Override public boolean connectAPI() {
		if(client != null) {
			BetterForgeChat.LOGGER.info("Discord API Client already connected?!");
			return true;
		}
		BetterForgeChat.LOGGER.info("Discord API Client connecting!");
		if(token == null) {
			BetterForgeChat.LOGGER.error("Discord API Client failed to connect, null token!");
			return false;
		}
		if(token.length() < 16) {
			BetterForgeChat.LOGGER.error("Discord API Client failed to connect, token must be at least 16 characters!");
			return false;
		}
		DiscordClient cli = DiscordClient.create(token);
		if(cli == null) {
			BetterForgeChat.LOGGER.error("Discord API Client failed to connect, DiscordClient.create() returned null!");
			return false;
		}
		try {
			client = cli.login().block(Duration.ofSeconds(10));
		} catch(IllegalStateException ise) {
			if(ise.getMessage().toLowerCase().contains("timeout"))
				BetterForgeChat.LOGGER.error("Discord API Client failed to connect, DiscordClient.login().block() timed out after 10 seconds!");
			else
				BetterForgeChat.LOGGER.error("Discord API Client failed to connect due to an IllegalStateException: " + ise.getMessage());
			return false;
		}
		if(client == null) {
			BetterForgeChat.LOGGER.error("Discord API Client failed to connect, DiscordClient.login().block() returned null!");
			return false;
		}
		EventDispatcher edsp = client.getEventDispatcher();
		edsp.on(ReadyEvent.class).subscribe((e) -> {
			onReadyEvent(e);
		});
		edsp.on(MessageCreateEvent.class).subscribe((e) -> {
			onMessageCreateEvent(e);
		});
		BetterForgeChat.LOGGER.info("Discord API Client connected!");
		return true;
	}
	@Override public void disconnectAPI() {
		if(client != null) {
			BetterForgeChat.LOGGER.info("Discord API Client disconnecting!");
			if(serverStopMsg != null && serverStopMsg.length() > 0)
				sendDiscordMessage(serverStopMsg);
			client.logout().block(Duration.ofSeconds(10));
			BetterForgeChat.LOGGER.info("Discord API Client disconnected!");
		} else BetterForgeChat.LOGGER.info("Discord API Client already disconnected!");
		client = null;
	}

	public static DiscordHandler bfcFactory() {
		DiscordHandler handler = new DiscordHandler();
		BetterForgeChat.LOGGER.info("Loading Discord API Client configuration!");
		handler.loadConfigOptions(false);
		if(!handler.connectAPI()) return null;
		BetterForgeChat.instance.registerReloadable(handler);
		return handler;
	}

	@Override public void registerDiscordListener(IDiscordListener list) {
		lists.add(list);
	}
}
