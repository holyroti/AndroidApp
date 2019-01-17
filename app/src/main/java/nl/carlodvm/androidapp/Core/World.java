package nl.carlodvm.androidapp.Core;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class World {
    private List<Grid> etage;
    private List<Destination> destinations;
    private int width, height;

    public World(List<Grid> grid, int width, int height) {
        this.etage = grid;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public List<Grid> getEtage() {
        return etage;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public Grid getGrid(int x, int y) {
        List<Grid> grids = etage.stream().filter((grid) -> grid.getX() == x && grid.getY() == y).collect(Collectors.toList());
        
        return grids.get(0);
    }

    public Grid getDestination(int imageId) {
        List<Grid> grids = destinations.stream().filter((grid) -> grid.getImageIndex() == imageId).collect(Collectors.toList());
        if (grids.size() > 1)
            throw new IllegalStateException("There should not be multiple grids with the same imageID.");
        if (grids.size() == 0)
            return null;
        return grids.get(0);
    }

    public Destination getDestination(Grid dest) {
        List<Destination> grids = destinations.stream().filter((grid) -> grid.getX() == dest.getX() && grid.getY() == dest.getY()).collect(Collectors.toList());
        if (grids.size() > 1)
            throw new IllegalStateException("There should not be multiple grids on the same coordinate.");
        if (grids.size() == 0)
            return null;
        return grids.get(0);
    }

    public Grid getGrid(Pair<Integer, Integer> coord) {
        return getGrid(coord.first, coord.second);
    }

    public List<Grid> getValidNeighbours(Grid current) {
        List<Grid> result = new ArrayList<>();
        for (Pair<Integer, Integer> neighbourCoord : current.getDirections()) {
            if (isOnWorld(neighbourCoord)) {
                Grid t = getGrid(neighbourCoord);
                if (t.isPassable()) {
                    result.add(t);
                }
            }
        }
        return result;
    }

    private boolean isOnWorld(Grid grid) {
        int x = grid.getX();
        int y = grid.getY();
        return ((x >= 0) && (y >= 0) && (x < getWidth()) && (y < getHeight()));
    }

    private boolean isOnWorld(Pair<Integer, Integer> coord) {
        int x = coord.first;
        int y = coord.second;
        return ((x >= 0) && (y >= 0) && (x < getWidth()) && (y < getHeight()));
    }
}
