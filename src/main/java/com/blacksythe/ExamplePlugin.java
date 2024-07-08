package com.blacksythe;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import net.runelite.api.WorldView;
import net.runelite.api.PlayerComposition;
import net.runelite.api.GraphicsObject;
import net.runelite.api.Model;
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
	private ExampleConfig config;

	public WorldView worldview;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "hi", null);
		}
	}
	@Subscribe
	public void onClientTick(ClientTick clientTick)
	{
		for (GraphicsObject graphicsObject : worldview.getGraphicsObjects())
		{
			if (
					scytheTrailIds.contains(graphicsObject.getId()) ||
							(graphicsObject.getId() >= 1231 && graphicsObject.getId() <= 1235) || // chally trails + 1 red trail.
							(graphicsObject.getId() >= 1891 && graphicsObject.getId() <= 1898) // sara and sang scythe swing trails
			)
			{
				recolorAllFaces(graphicsObject.getModel(), getColor(graphicsObject.getLocation().hashCode()));
			}

			if(graphicsObject.getId() == 164 || graphicsObject.getId() == 2605 ) {
				recolorAllFaces(graphicsObject.getModel(), getColor(graphicsObject.getLocation().hashCode()));
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "hi", null);
			}

		}

	}




	public Color getColor(int hashCode)
	{
		return new Color(0,0,0);
	}


	private static final List<Integer> scytheTrailIds = Arrays.asList(478, 506, 1172);
	private static final List<Integer> bowfaList = Arrays.asList(25865);
	private int colorToRs2hsb(Color color)
	{
		float[] hsbVals = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

		// "Correct" the brightness level to avoid going to white at full saturation, or having a low brightness at
		// low saturation
		hsbVals[2] -= Math.min(hsbVals[1], hsbVals[2] / 2);

		int encode_hue = (int)(hsbVals[0] * 63);
		int encode_saturation = (int)(hsbVals[1] * 7);
		int encode_brightness = (int)(hsbVals[2] * 127);
		return (encode_hue << 10) + (encode_saturation << 7) + (encode_brightness);
	}

	private void recolorAllFaces(Model model, Color color)
	{
		if (model == null || color == null)
		{
			return;
		}

		int rs2hsb = colorToRs2hsb(color);
		int[] faceColors1 = model.getFaceColors1();
		int[] faceColors2 = model.getFaceColors2();
		int[] faceColors3 = model.getFaceColors3();

		for (int i = 0; i < faceColors1.length; i++)
		{
			faceColors1[i] = rs2hsb;
		}
		for (int i = 0; i < faceColors2.length; i++)
		{
			faceColors2[i] = rs2hsb;
		}
		for (int i = 0; i < faceColors3.length; i++)
		{
			faceColors3[i] = rs2hsb;
		}
	}
	@Provides
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}
}
