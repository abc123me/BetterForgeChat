package com.jeremiahbl.bfcmod.commands;

import java.util.List;
import java.util.UUID;

import com.jeremiahbl.bfcmod.BetterForgeChat;
import com.jeremiahbl.bfcmod.TextFormatter;
import com.jeremiahbl.bfcmod.config.ConfigHandler;
import com.jeremiahbl.bfcmod.config.PermissionsHandler;
import com.jeremiahbl.bfcmod.config.PlayerData;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

public class NickCommands {
	private static boolean cfgWhoIsEnabled = false;
	private static boolean cfgNickEnabled = false;
	private static int minNicknameLength = -1;
	private static int maxNicknameLength = -1;
	
	public static boolean nicknameIntegrationEnabled = false;
	
	public static void reloadConfig() {
		minNicknameLength = ConfigHandler.config.minimumNicknameLength.get();
		maxNicknameLength = ConfigHandler.config.maximumNicknameLength.get();
		if(minNicknameLength > maxNicknameLength) {
			int oldMin = minNicknameLength;
			minNicknameLength = maxNicknameLength;
			maxNicknameLength = oldMin;
			BetterForgeChat.LOGGER.warn("Minimum nickname length was greater then maximum, swapped vales");
			BetterForgeChat.LOGGER.warn(minNicknameLength + " < nickname.length() < " + maxNicknameLength);
		}
		cfgWhoIsEnabled = ConfigHandler.config.enableWhoisCommand.get();
		cfgNickEnabled = ConfigHandler.config.enableChatNicknameCommand.get();
	}
	public static void register(CommandDispatcher<CommandSourceStack> disp) {
		/* /nick */
		disp.register(Commands.literal("nick").requires((c) -> {
			return (nicknameIntegrationEnabled || cfgNickEnabled) &&
				(BfcCommands.checkPermission(c, PermissionsHandler.nickCommand));
		}).executes((ctx) -> nickCommand(ctx, true, false)));
		/* /nick <nickname> */
		disp.register(Commands.literal("nick").requires((c) -> {
			return (nicknameIntegrationEnabled || cfgNickEnabled) &&
				(BfcCommands.checkPermission(c, PermissionsHandler.nickCommand));
		}).then(Commands.argument("nickname", StringArgumentType.greedyString())
			.executes((ctx) -> nickCommand(ctx, false, false))));
		/* /nickfor <username> <nickname> */
		disp.register(Commands.literal("nickfor").requires((c) -> {
				return (nicknameIntegrationEnabled || cfgNickEnabled) &&
					(BfcCommands.checkPermission(c, PermissionsHandler.nickOthersCommand)); })
				.then(Commands.argument("username", StringArgumentType.string())
				//.suggests((ctx, builder) -> SharedSuggestionProvider.sugg)
				.then(Commands.argument("nickname", StringArgumentType.greedyString())
				.executes((ctx) -> nickCommand(ctx, false, true)))));
		/* /nickfor <username> */
		disp.register(Commands.literal("nickfor").requires((c) -> {
				return (nicknameIntegrationEnabled || cfgNickEnabled) &&
					(BfcCommands.checkPermission(c, PermissionsHandler.nickOthersCommand)); })
				.then(Commands.argument("username", StringArgumentType.string())
				//.suggests((ctx, builder) -> SharedSuggestionProvider.sugg)
				.executes((ctx) -> nickCommand(ctx, true, true))));
		/* /whois <nickname> */
		disp.register(Commands.literal("whois").requires((c) -> {
				return (nicknameIntegrationEnabled || cfgWhoIsEnabled) && 
					(BfcCommands.checkPermission(c, PermissionsHandler.whoisCommand));
			}).then(Commands.argument("displayname", StringArgumentType.string())
				.executes((ctx) -> whoisCommand(ctx))));
	}

	@SuppressWarnings("resource")
	private static GameProfile lookupGameProfile(String user) {
		MinecraftServer serv = ServerLifecycleHooks.getCurrentServer();
		if(serv != null) {
			user = user.trim().toLowerCase();
			List<ServerPlayer> players = serv.getPlayerList().getPlayers();
			for(ServerPlayer player : players) {
				GameProfile prof = player.getGameProfile();
				if(prof != null) {
					String uname = prof.getName().trim().toLowerCase();
					if(user.equals(uname)) return prof;
					String nname = BetterForgeChat.instance.nicknameProvider.getPlayerNickname(prof).trim().toLowerCase();
					if(user.equals(TextFormatter.removeTextFormatting(nname))) return prof; 
				}
			}
		}
		return null;
	}
	private static int whoisCommand(CommandContext<CommandSourceStack> ctx) {
		String user = StringArgumentType.getString(ctx, "displayname");
		GameProfile prof = lookupGameProfile(user);
		if(prof != null) {
			ctx.getSource().sendSuccess(TextFormatter.stringToFormattedText("&eFound a name matching " + user + ": \"" + prof.getName() + "\"\n&eUUID: " + prof.getId() + "&r"), false);
			return 1;
		} else {
			ctx.getSource().sendFailure(TextFormatter.stringToFormattedText("&cUnknown username/nickname!&r"));
			return 0;
		}
	}
	private static int assignNickname(CommandContext<CommandSourceStack> ctx, UUID uuid, String nick) {
		if(nick == null) {
			ctx.getSource().sendSuccess(TextFormatter.stringToFormattedText("&eNickname reset!&r"), false);
			PlayerData.setNickname(uuid, null);
			return 1;
		} else {
			if(nick.length() >= minNicknameLength && nick.length() <= maxNicknameLength) {
				ctx.getSource().sendSuccess(TextFormatter.stringToFormattedText("&eNickname set to \"" + nick + "&r&e\"!&r"), false);
				PlayerData.setNickname(uuid, nick);
				return 1;
			} else {
				ctx.getSource().sendFailure(TextFormatter.stringToFormattedText("&cNickname must be between 1 and 50 characters!&r"));
				return 0;
			}
		}
	}
	private static int nickCommand(CommandContext<CommandSourceStack> ctx, boolean reset, boolean other) {
		ServerPlayer player = null;
		try {
			player = ctx.getSource().getPlayerOrException();
		} catch (CommandSyntaxException e) { }
		String nick = reset ? null : StringArgumentType.getString(ctx, "nickname");
		String user = other ? StringArgumentType.getString(ctx, "username") : null;
		/* /nick OR /nick <nickname> */
		if(player != null && user == null) 
			return assignNickname(ctx, player.getUUID(), nick);
		/* /nickfor <user> OR /nickfor <user> <nickname> */
		if(user != null) {
			GameProfile prof = lookupGameProfile(user);
			if(prof != null) {
				return assignNickname(ctx, prof.getId(), nick);
			} else {
				ctx.getSource().sendFailure(TextFormatter.stringToFormattedText("&cUnknown player: \"" + user + "\"!&r"));
				return 0;
			}
		}
		ctx.getSource().sendFailure(TextFormatter.stringToFormattedText("&cUnknown error!&r"));
		return 0;
	}
}
