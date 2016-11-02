package aima.test.unit.environment.map2d;

import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.util.datastructure.Point2D;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author wormi
 */
public class PrintRomaniaMap {

  final int width = 950;
  final int height = 700;
  final double zoom = 1.7;
  final double offsetX = zoom * 350.0;
  final double offsetY = zoom * 300.0;


  @Test
  @Ignore("for manual usage, overwrites /tmp/romania.png with a current map")
  public void writeImage() throws URISyntaxException, IOException {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    final Graphics graphics = image.createGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, width, height);
    graphics.setColor(Color.BLACK);
    draw(graphics);
    ImageIO.write(image, "png", new File("/tmp/romania.png"));
  }

  private void draw(Graphics graphics) {
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
