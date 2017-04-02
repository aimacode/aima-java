package aima.extra.environment.model.map;

/**
 * Created by @AdrianBZG on 2/04/17.
 */

import aima.core.util.datastructure.Tuple;
import aima.extra.environment.model.map.generator.IGenerator;
import aima.extra.environment.model.map.object.Block;
import aima.extra.environment.model.map.object.MapObject;

import java.util.HashMap;
import java.util.Optional;

/**
 * Represents a full map extension
 *
 * TODO: Map size limitation is Int size (Use BigInteger?)
 * TODO: Test which should be the chunk size to be more efficient in memory
 * TODO: Map IO
 * TODO: Unload unused MapChunk from memory to disk
 *
 */
public class EnvironmentMap implements Cloneable {

    private static int CHUNK_SIZE = 32;

    /**
     * Partial Map with zones explored
     */
    HashMap<MapSector, MapChunk> map = new HashMap<>();

    Optional<Tuple<Integer, Integer>> dimensions = Optional.empty();

    Optional<IGenerator> generator = Optional.empty();


    /**
     * Build a empty map
     */
    public EnvironmentMap() {
        getMap().put(MapSector.pos(0,0), new MapChunk(CHUNK_SIZE));
    }

    /**
     * Map copy constructor
     */
    public EnvironmentMap(HashMap<MapSector, MapChunk> map, Optional<Tuple<Integer, Integer>> dimensions, Optional<IGenerator> generator) {
        this.map = map;
        this.dimensions = dimensions;
        this.generator = generator;
    }

    /**
     * Generate a procedural map
     * @param generator
     */
    public EnvironmentMap(IGenerator generator) {
        this.generator = Optional.of(generator);
        getMap().put(MapSector.pos(0,0), new MapChunk(CHUNK_SIZE));
        generateChunkNoise(MapSector.pos(0,0));
    }


    /**
     *
     * @param pos
     */
    private void generateChunkNoise(MapSector pos) {
        if (generator.isPresent()) {
            for (int i = 0; i < CHUNK_SIZE; i++) {
                for (int j = 0; j < CHUNK_SIZE; j++) {
                    double gen = generator.get().generateAtPoint((pos.getX() * CHUNK_SIZE + i) / 10.0, (pos.getY() * CHUNK_SIZE + j) / 10.0);
                    if (gen > 0.5) {
                        set(pos.getX() * CHUNK_SIZE + i, pos.getY() * CHUNK_SIZE + j, new Block());
                    }
                    else if (gen > 0) {
                        // Another type of object
                    }
                }
            }
        }
    }

    public Optional<MapObject> get(Tuple <Integer, Integer> pos) {
        return get(pos.getX(), pos.getY());
    }

    /**
     * Retrieve object from map position
     * @param x coordinate
     * @param y coordinate
     * @return Object from map
     */
    public Optional<MapObject> get(int x, int y) {
        if (getDimensions().isPresent()) {
            if (getDimensions().get().getWidth() < x || getDimensions().get().getHeight() < y
                    || x < 0 || y < 0) {
                return Optional.empty();
            }
            if (getDimensions().get().getWidth() <= x || getDimensions().get().getHeight() <= y
                    || x <= 0 || y <= 0) {
                return Optional.of(new Block());
            }
        }

        MapSector mapSector = makeSector(x, y);
        MapChunk mapChunk = getMap().get(mapSector);

        if (mapChunk == null) {
            MapChunk mapChunkAux = new MapChunk(CHUNK_SIZE);
            getMap().put(mapSector, mapChunkAux);
            if (getGenerator().isPresent()) {
                generateChunkNoise(mapSector);
                return get(x, y);
            }
            else {
                return Optional.empty();
            }
        }
        else {
            return mapChunk.get(Math.abs(x % CHUNK_SIZE), Math.abs(y % CHUNK_SIZE));
        }
    }

    /**
     * Set a object into map position, it ensure that not overlap other objects, *the chunk should already create*
     */
    public void set(int x, int y, MapObject object) {
        if (getDimensions().isPresent()) {
            if (getDimensions().get().getWidth() < x || getDimensions().get().getHeight() < y
                    || x < 0 || y < 0) {
                return;
            }
        }
        MapChunk mapChunk = getMap().get(makeSector(x, y));
        if (mapChunk != null) {
            removeAt(x,y);
            object.setPosition((new Tuple<>(x, y)));
            mapChunk.set(Math.abs(x % CHUNK_SIZE), Math.abs(y % CHUNK_SIZE), object);
        }
    }

    private MapSector makeSector(int x, int y) {
        return MapSector.pos((int)Math.floor((float)x / CHUNK_SIZE), (int)Math.floor((float)y / CHUNK_SIZE));
    }


    /**
     * Remove a map object in a determined position
     * @param x
     * @param y
     */
    public void removeAt (int x, int y) {
        get(x, y).ifPresent(mapObject -> {
            getMap().get(makeSector(x, y)).removeAt(Math.abs(x % CHUNK_SIZE), Math.abs(y % CHUNK_SIZE));
        });
    }

    /**
     * Remove objects with a position passed as tuple
     * @param position
     */
    public void removeAt(Tuple<Integer,Integer> position) {
        removeAt(position.getX(), position.getY());
    }

    /**
     *
     */
    public HashMap<MapSector, MapChunk> getMap() {
        return map;
    }

    public Optional<IGenerator> getGenerator() {
        return generator;
    }

    public Optional<Tuple<Integer, Integer>> getDimensions() {
        return dimensions;
    }

    public void setDimensions (Tuple<Integer, Integer> dim) {
        this.dimensions = Optional.of(dim);
    }

    public void setDimensions(int x, int y) {
        this.dimensions = Optional.of(new Tuple<>(x, y));
    }

    public void setGenerator(Optional<IGenerator> generator) {
        this.generator = generator;
    }

    @Override
    public EnvironmentMap clone() {
        return new EnvironmentMap(this.map, this.dimensions, this.generator);
    }
}
