package com.blacksythe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.EnumSet;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.WorldType;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.ui.overlay.components.LineComponent;

class CustomOverlay extends Overlay
{
    private final Client client;
    private final VorkathRunWarningConfig config;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    private CustomOverlay(Client client, VorkathRunWarningConfig config)
    {
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        setMovable(true);
        this.client = client;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        panelComponent.getChildren().clear();
        String overlayTitle = "Current World:";

        panelComponent.setBackgroundColor(Color.black);
        // Build overlay title
        panelComponent.getChildren().add(TitleComponent.builder()
                .text("RUN IS ENABLED")
                .color(Color.RED)
                .build());

        // Set the size of the overlay (width)
        panelComponent.setPreferredSize(new Dimension(
                graphics.getFontMetrics().stringWidth(overlayTitle) + 30,
                0));

        return panelComponent.render(graphics);
    }
}