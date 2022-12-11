import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;

public class DiscordBotTest {
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		FileInputStream fis = new FileInputStream("secret/discord.properties");
		props.load(fis); fis.close();
		// Haha person reading this, you can't have my token
		String token = (String) props.get("token");
		
		DiscordClient cli = DiscordClient.create(token);
		System.out.println("Connecting to discord!");
		GatewayDiscordClient client = cli.login().block(Duration.ofSeconds(3));
		System.out.println("Connected!");
		
		client.getEventDispatcher().on(ReadyEvent.class).subscribe(event -> {
			System.out.println("ReadyEvent");
			client.rest().getChannelById(Snowflake.of((String) props.get("channel"))).createMessage("**mwhahaha i've entered the matrix**").subscribe();
		});
		client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(event -> {
			System.out.println("MessageCreateEvent");
			
		});
		
		Thread.sleep(10000);
		System.out.println("Logging out!");
		client.logout().block();
		System.out.println("Bye");
	}
}
