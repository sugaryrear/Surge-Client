package net.runelite.client.plugins.npctile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

//class NpcRespawnOverlay extends Overlay
//{
//    // Anything but white text is quite hard to see since it is drawn on
//    // a dark background
//    private static final Color TEXT_COLOR = Color.WHITE;
//
//    private static final NumberFormat TIME_LEFT_FORMATTER = DecimalFormat.getInstance(Locale.US);
//
//    static
//    {
//        ((DecimalFormat)TIME_LEFT_FORMATTER).applyPattern("#0.0");
//    }
//
//    private final Client client;
//    private final NpcIndicatorsConfig config;
//    private final NpcIndicatorsPlugin plugin;
//
//    @Inject
//    NpcRespawnOverlay(Client client, NpcIndicatorsConfig config, NpcIndicatorsPlugin plugin)
//    {
//        this.client = client;
//        this.config = config;
//        this.plugin = plugin;
//        setPosition(OverlayPosition.DYNAMIC);
//        setLayer(OverlayLayer.ABOVE_SCENE);
//    }
//
//    @Override
//    public Dimension render(Graphics2D graphics)
//    {
//        Map<Integer, MemorizedNpc> deadNpcsToDisplay = plugin.getDeadNpcsToDisplay();
//        if (deadNpcsToDisplay.isEmpty() || !config.showRespawnTimer())
//        {
//            return null;
//        }
//
//        deadNpcsToDisplay.forEach((id, npc) -> renderNpcRespawn(npc, graphics));
//        return null;
//    }
//
//    private void renderNpcRespawn(final MemorizedNpc npc, final Graphics2D graphics)
//    {
//        if (npc.getPossibleRespawnLocations().isEmpty())
//        {
//            return;
//        }
//
//        final WorldPoint respawnLocation = npc.getPossibleRespawnLocations().get(0);
//        final LocalPoint lp = LocalPoint.fromWorld(client, respawnLocation.getX(), respawnLocation.getY());
//
//        if (lp == null)
//        {
//            return;
//        }
//
//        final LocalPoint centerLp = new LocalPoint(
//                lp.getX() + Perspective.LOCAL_TILE_SIZE * (npc.getNpcSize() - 1) / 2,
//                lp.getY() + Perspective.LOCAL_TILE_SIZE * (npc.getNpcSize() - 1) / 2);
//
//        final Polygon poly = Perspective.getCanvasTileAreaPoly(client, centerLp, npc.getNpcSize());
//        renderPoly(graphics, config.highlightColor(), config.fillColor(), poly);
//
//        final Instant now = Instant.now();
//        final double baseTick = ((npc.getDiedOnTick() + npc.getRespawnTime()) - client.getTickCount()) * (Constants.GAME_TICK_LENGTH / 1000.0);
//        final double sinceLast = (now.toEpochMilli() - plugin.getLastTickUpdate().toEpochMilli()) / 1000.0;
//        final double timeLeft = Math.max(0.0, baseTick - sinceLast);
//        final String timeLeftStr = TIME_LEFT_FORMATTER.format(timeLeft);
//
//        final int textWidth = graphics.getFontMetrics().stringWidth(timeLeftStr);
//        final int textHeight = graphics.getFontMetrics().getAscent();
//
//        final Point canvasPoint = Perspective
//                .localToCanvas(client, centerLp, respawnLocation.getPlane());
//
//        if (canvasPoint != null)
//        {
//            final Point canvasCenterPoint = new Point(
//                    canvasPoint.getX() - textWidth / 2,
//                    canvasPoint.getY() + textHeight / 2);
//
//            OverlayUtil.renderTextLocation(graphics, canvasCenterPoint, timeLeftStr, TEXT_COLOR);
//        }
//    }
//
//    private void renderPoly(Graphics2D graphics, Color borderColor, Color fillColor, Shape polygon)
//    {
//        if (polygon != null)
//        {
//            graphics.setColor(borderColor);
//            graphics.setStroke(new BasicStroke((float) config.borderWidth()));
//            graphics.draw(polygon);
//            graphics.setColor(fillColor);
//            graphics.fill(polygon);
//        }
//    }
//}