package com.jeremiahbl.bfcmod.commands;


import com.jeremiahbl.bfcmod.BetterForgeChat;
import com.jeremiahbl.bfcmod.TextFormatter;
import com.jeremiahbl.bfcmod.config.ConfigHandler;
import com.jeremiahbl.bfcmod.config.PermissionsHandler;
import com.jeremiahbl.bfcmod.config.PlayerData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

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
			BetterForgeChat.LOGGER.warn("Minimum nickname lenght was greater then maximum, swapped vales");
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
		}).then(Commands.argument("nick", StringArgumentType.string())
			.executes((ctx) -> nickCommand(ctx, false, false))));
		/* /nick <username> <nickname> */
		disp.register(Commands.literal("nick").requires((c) -> {
				return (nicknameIntegrationEnabled || cfgNickEnabled) &&
					(BfcCommands.checkPermission(c, PermissionsHandler.nickCommand)); })
				.then(Commands.argument("user", StringArgumentType.string())
				//.suggests((ctx, builder) -> SharedSuggestionProvider.sugg)
				.then(Commands.argument("nick", StringArgumentType.string())
				.executes((ctx) -> nickCommand(ctx, false, true)))));
		/* /whois <nickname> */
		disp.register(Commands.literal("whois").requires((c) -> {
				return (nicknameIntegrationEnabled || cfgWhoIsEnabled) && 
					(BfcCommands.checkPermission(c, PermissionsHandler.whoisCommand));
			}).then(Commands.argument("name1", StringArgumentType.string())
				.executes((ctx) -> whoisCommand(ctx))));
	}

	private static int whoisCommand(CommandContext<CommandSourceStack> ctx) {
		String name1 = StringArgumentType.getString(ctx, "name1");
		BetterForgeChat.LOGGER.info(String.format("/whois <%s>", name1));
		ctx.getSource().sendSuccess(TextFormatter.stringToFormattedText("ok!"), false);
		return 1;
	}
	private static int nickCommand(CommandContext<CommandSourceStack> ctx, boolean reset, boolean other) {
		ServerPlayer player = null;
		try {
			player = ctx.getSource().getPlayerOrException();
		} catch (CommandSyntaxException e) { }
		String nick = reset ? null : StringArgumentType.getString(ctx, "nick");
		String user = other ? StringArgumentType.getString(ctx, "user") : null;
		if(player != null && user == null) {
			if(nick == null) {
				ctx.getSource().sendSuccess(TextFormatter.stringToFormattedText("&eNickname reset!&r"), false);
				PlayerData.setNickname(player.getUUID(), null);
				return 1;
			} else {
				if(nick.length() >= minNicknameLength && nick.length() <= maxNicknameLength) {
					ctx.getSource().sendSuccess(TextFormatter.stringToFormattedText("&eNickname set to \"" + nick + "&r&e\"!&r"), false);
					PlayerData.setNickname(player.getUUID(), nick);
					return 1;
				} else {
					ctx.getSource().sendFailure(TextFormatter.stringToFormattedText("&cNickname must be between 1 and 50 characters!&r"));
					return 0;
				}
			}
		} return 1;
	}
}
