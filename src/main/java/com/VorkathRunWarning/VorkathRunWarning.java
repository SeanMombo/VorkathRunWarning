package com.VorkathRunWarning;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import net.runelite.api.WorldView;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.Notifier;
import net.runelite.client.ui.overlay.OverlayManager;

import net.runelite.api.events.ClientTick;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.events.VarbitChanged;
@Slf4j
@PluginDescriptor(
	name = "Vorkath Run Warning"
)
public class VorkathRunWarning extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private CustomOverlay overlay;

	@Inject
	private VorkathRunWarningConfig config;

	@Inject
	private Notifier notifier;

	public WorldView worldview;

	@Override
	protected void startUp() throws Exception
	{
		//
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	private boolean previousOverlayStatus = false;
	private boolean currentOverlayStatus = false;
	private int localX = -1;
	private int localY = -1;
	private int isRunEnabled = -1;

	private void checkInMainRegion(){
		localY = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getY();
		localX = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getX();

		currentOverlayStatus = (localX >= 2261 && localX <= 2283 && localY >= 4054 && localY <= 4076) && isRunEnabled == 1;
	}

	private void updateWarning() {
		checkInMainRegion();

		if (previousOverlayStatus != currentOverlayStatus) {
			if (currentOverlayStatus) {
				overlayManager.add(overlay);
				notifier.notify(
						"WARNING: RUN IS ENABLED"
				);
			} else {
				overlayManager.remove(overlay);
			}
		}

		previousOverlayStatus = currentOverlayStatus;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
//		updateWarning();
		if (client.getGameState() == GameState.LOGGED_IN) {
			isRunEnabled = client.getVarpValue(173);
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarpId() == 173)
		{
			isRunEnabled = client.getVarpValue(173);
		}
	}

	@Subscribe
	public void onClientTick(ClientTick clientTick)
	{
		updateWarning();
	}

	@Provides
	VorkathRunWarningConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(VorkathRunWarningConfig.class);
	}
}
