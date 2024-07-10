package com.blacksythe;

import com.google.inject.Provides;
import javax.inject.Inject;

import com.sun.tools.jconsole.JConsoleContext;
import lombok.extern.slf4j.Slf4j;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import net.runelite.api.WorldView;
import net.runelite.api.PlayerComposition;
import net.runelite.api.GraphicsObject;
import net.runelite.api.Model;
import net.runelite.client.ui.overlay.OverlayManager;

import net.runelite.api.events.ClientTick;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Example"
)
public class ExamplePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private CustomOverlay overlay;

	@Inject
	private ExampleConfig config;

	public WorldView worldview;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "hi", null);
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", Arrays.toString(client.getMapRegions()), null);
			System.out.println(Arrays.toString(client.getMapRegions()));

		}
	}

	private boolean checkInMainRegion(){
		int[] currentMapRegions = client.getMapRegions();
		return Arrays.stream(currentMapRegions).anyMatch(x -> x == 9280);
	}

	@Subscribe
	public void onClientTick(ClientTick clientTick)
	{
		if (checkInMainRegion()) {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", Arrays.toString(client.getMapRegions()), null);

		}
	}

	@Provides
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}
}
