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
public class Print2DMap {

  private static double width;
  private static double height;
  private static double zoom = 1;
  private static double offsetX;
  private static double offsetY;
  // some border is needed because text does not stay within width
  private static int border = 300;

  public static void main (String[] args) throws URISyntaxException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
    if (args.length < 2) {
      System.err.println("run Print2DMap through your IDE," +
          " `Map2DClassSimpleName yourOutPath [zoom] [border]` as arguments\n" +
          " e.g `SimplifiedRoadMapOfPartOfRomania /tmp 1.4 100`\n" +
          "The map to print must implement the `Map2D` interface\n" +
          "and must reside in the same package");
      System.exit(1);
    }
    if (args.length >= 3) {
      zoom = Double.parseDouble(args[2]);
    }
    if (args.length >= 4) {
      border = Integer.parseInt(args[3]);
    }

    final String fullQualifiedClassName = Print2DMap.class.getPackage().getName() + "." + args[0];
    Class<?> c = Class.forName(fullQualifiedClassName);
    Map2D mapFromClassPath = (Map2D) c.newInstance();

    calculateDimensions(mapFromClassPath);

    int _width = ((Double) width).intValue();
    int _height = ((Double) height).intValue();
    BufferedImage image = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
    final Graphics graphics = image.createGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, _width, _height);
    draw(graphics, mapFromClassPath);

    String path = args[1];
    if (path.endsWith("/")) {
      checkIfDirectoryIsReadable(path);
      path += "map";
    }
    if (!path.endsWith(".png")) {
      path += ".png";
    }
    ImageIO.write(image, "png", new File(path));
  }

  private static void checkIfDirectoryIsReadable(String path) {
    if (!new File(path).canRead()) throw new RuntimeException("can't read from " + path);
  }

  private static void draw(Graphics graphics, Map2D map) {
    for (String location : map.getLocations()) {
      final Point2D position = map.getPosition(location);
      Double x = position.getX() + offsetX;
      Double y = position.getY() + offsetY;
      x *= zoom;
      x += border / 2 * zoom;
      y *= zoom;
      y += border / 2 * zoom;
      graphics.setColor(new Color(88, 118, 174));
      graphics.fillOval(x.intValue() - 5, y.intValue() - 5, 10, 10);
      final List<String> linkedLocations = map.getLocationsLinkedTo(location);
      for (String otherLocation : linkedLocations) {
        graphics.setColor(new Color(88, 118, 174));
        final Point2D linkedPostion = map.getPosition(otherLocation);
        Double otherX = linkedPostion.getX() + offsetX;
        Double otherY = linkedPostion.getY() + offsetY;
        otherX *= zoom;
        otherX += border / 2 * zoom;
        otherY *= zoom;
        otherY += border / 2 * zoom;
        graphics.drawLine(x.intValue(), y.intValue(), otherX.intValue(), otherY.intValue());
        graphics.setColor(new Color(0,0,0));
        final Double distance = map.getDistance(location, otherLocation);
        graphics.drawString(distance + "", new Double((x + otherX) / 2).intValue(), new Double((y +
            otherY) / 2).intValue() - 5);
      }
      graphics.setColor(new Color(0,0,0));
      graphics.drawString(location, x.intValue(), y.intValue() - 10);
    }
  }

  private static List<String> calculateDimensions(Map2D map) {
    final List<String> locations = map.getLocations();
    final double minX = locations.stream().mapToDouble(l ->
        map.getPosition(l).getX()).min().orElse(0);
    final double maxX = locations.stream().mapToDouble(l ->
        map.getPosition(l).getX()).max().orElse(0);
    final double minY = locations.stream().mapToDouble(l ->
        map.getPosition(l).getY()).min().orElse(0);
    final double maxY = locations.stream().mapToDouble(l ->
        map.getPosition(l).getY()).max().orElse(0);
    final double dx = maxX - minX;
    width = dx * zoom;
    width += border * zoom;
    final double dy = maxY - minY;
    height = dy * zoom;
    height += border * zoom;
    offsetX = -minX;
    offsetY = -minY;
    return locations;
  }
}
