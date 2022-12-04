package com.jeremiahbl.bfcmod;

import java.util.Arrays;

import com.jeremiahbl.bfcmod.config.ConfigHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;

public class BetterForgeChatCommands {
	private static final Iterable<String> bfcModSubCommands = Arrays.asList(new String[] { "info", "colors" });
	
	public static void register(CommandDispatcher<CommandSourceStack> disp) {
		if(ConfigHandler.config.enableColorsCommand.get()) {
			disp.register(Commands.literal("colors").requires((c) -> {
				return c.hasPermission(1); }).executes(ctx -> colorCommand(ctx)));
		}
		disp.register(Commands.literal("bfcmod").requires((c) -> {
			return c.hasPermission(2); }).then(Commands.argument("mode", StringArgumentType.greedyString())
					.suggests((context, builder) -> SharedSuggestionProvider.suggest(bfcModSubCommands, builder))
					.executes(ctx -> modCommand(ctx))));
	}
	
	public static int modCommand(CommandContext<CommandSourceStack> ctx) {
		String arg = StringArgumentType.getString(ctx, "mode");
		if(arg.contentEquals("colors")) return colorCommand(ctx);
		else if(arg.contentEquals("info")) {
			ctx.getSource().sendSuccess(TextFormatter.stringToFormattedText(
					"Forge+LitePerms Chat Mod (c) Jeremiah Lowe 2022-2023\n"
					+ BetterForgeChat.MODID + " " + BetterForgeChat.VERSION), false);
			return 1;
		} else return 0;
	}
	public static int colorCommand(CommandContext<CommandSourceStack> ctx) {
		ctx.getSource().sendSuccess(TextFormatter.stringToFormattedText(
				"Forge+LitePerms Chat Mod (c) Jeremiah Lowe 2022-2023\n"
						+ TextFormatter.colorString()), false);
		return 1;
	}
}
