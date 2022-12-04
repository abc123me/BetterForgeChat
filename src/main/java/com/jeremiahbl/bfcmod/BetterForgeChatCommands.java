package com.jeremiahbl.bfcmod;

import java.util.Arrays;

import com.jeremiahbl.bfcmod.config.ConfigHandler;
import com.jeremiahbl.bfcmod.config.PermissionsHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraftforge.server.permission.nodes.PermissionNode;

public class BetterForgeChatCommands {
	private static final Iterable<String> bfcModSubCommands = Arrays.asList(new String[] { "info", "colors" });
	
	private static boolean checkPermission(CommandSourceStack c, int lvl, PermissionNode<Boolean> node) {
		if(c.hasPermission(lvl)) {
			try {
				return PermissionsHandler.playerHasPermission(c.getPlayerOrException(), node);
			} catch(CommandSyntaxException e) {
				// Not a player (console or rcon)
				return true;
			}
		} else return false;
	}
	private static boolean checkContextPermission(CommandContext<CommandSourceStack> c, PermissionNode<Boolean> node) {
		try {
			return PermissionsHandler.playerHasPermission(c.getSource().getPlayerOrException(), node);
		} catch (CommandSyntaxException e) {
			// Not a player (console or rcon)
			return true;
		}
	}
	private static int failNoPermission(CommandContext<CommandSourceStack> ctx) {
		ctx.getSource().sendFailure(TextFormatter.stringToFormattedText(TextFormatter.COLOR_RED + "You don't have permission to run this command" + TextFormatter.RESET_ALL_FORMAT));
		return 0;
	}
	
	public static void register(CommandDispatcher<CommandSourceStack> disp) {
		if(ConfigHandler.config.enableColorsCommand.get()) {
			disp.register(Commands.literal("colors").requires((c) -> {
				return checkPermission(c, 1, PermissionsHandler.coloredChatNode);
				}).executes(ctx -> colorCommand(ctx)));
		}
		disp.register(Commands.literal("bfcmod").requires((c) -> {
			return checkPermission(c, 1, PermissionsHandler.bfcModCommand);
				}).then(Commands.argument("mode", StringArgumentType.greedyString())
				.suggests((context, builder) -> SharedSuggestionProvider.suggest(bfcModSubCommands, builder))
				.executes(ctx -> modCommand(ctx))));
	}
	
	public static int modCommand(CommandContext<CommandSourceStack> ctx) {
		String arg = StringArgumentType.getString(ctx, "mode");
		if(arg.contentEquals("colors")) {
			if(checkContextPermission(ctx, PermissionsHandler.bfcModCommandColorsSubCommand))
				return colorCommand(ctx);
			else return failNoPermission(ctx);
		} else if(arg.contentEquals("info")) {
			if(checkContextPermission(ctx, PermissionsHandler.bfcModCommandInfoSubCommand)) {
				ctx.getSource().sendSuccess(TextFormatter.stringToFormattedText(
						"Forge+LitePerms Chat Mod (c) Jeremiah Lowe 2022-2023\n"
						+ BetterForgeChat.MODID + " " + BetterForgeChat.VERSION), false);
				return 1;
			} else return failNoPermission(ctx);
		} else return 0;
	}
	public static int colorCommand(CommandContext<CommandSourceStack> ctx) {
		ctx.getSource().sendSuccess(TextFormatter.stringToFormattedText(
				"Forge+LitePerms Chat Mod (c) Jeremiah Lowe 2022-2023\n"
						+ TextFormatter.colorString()), false);
		return 1;
	}
}
