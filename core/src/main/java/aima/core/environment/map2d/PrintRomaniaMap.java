package aima.core.environment.map2d;

import aima.core.util.datastructure.Point2D;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * I was sceptic if the java implementation matches the images I found. Now one can compare.
 * @author wormi
 */
public class PrintRomaniaMap {

  private final static int width = 950;
  private final static int height = 700;
  private final static double zoom = 1.7;
  private final static double offsetX = zoom * 350.0;
  private final static double offsetY = zoom * 300.0;


  public static void main (String[] args) throws URISyntaxException, IOException {
    if (args.length != 1) {
      System.err.println("usage: java PrintRomaniaMap outPath");
    }

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    final Graphics graphics = image.createGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, width, height);
    graphics.setColor(Color.BLACK);
    draw(graphics);

    String path = args[0];
    if (path.endsWith("/")) {
      path += "romania";
    }
    if (!path.endsWith(".png")) {
      path += ".png";
    }
    ImageIO.write(image, "png", new File(path));
  }

  private static void draw(Graphics graphics) {
    SimplifiedRoadMapOfPartOfRomania map = new SimplifiedRoadMapOfPartOfRomania();
    for (String location : map.getLocations()) {
      final Point2D position = map.getPosition(location);
      final Double x = position.getX() * zoom + offsetX;
      final Double y = position.getY() * zoom + offsetY;
      graphics.drawOval(x.intValue() - 5, y.intValue() - 5, 10, 10);
      graphics.drawString(location, x.intValue(), y.intValue() - 10);
      final List<String> linkedLocations = map.getLocationsLinkedTo(location);
      for (String otherLocation : linkedLocations) {
        final Point2D linkedPostion = map.getPosition(otherLocation);
        final Double otherX = linkedPostion.getX() * zoom + offsetX;
        final Double otherY = linkedPostion.getY() * zoom + offsetY;
        graphics.drawLine(x.intValue(), y.intValue(), otherX.intValue(), otherY.intValue());
        final Double distance = map.getDistance(location, otherLocation);
        graphics.drawString(distance + "", new Double((x + otherX) / 2).intValue(), new Double((y +
            otherY) / 2).intValue() - 5);
      }
    }
  }
}
