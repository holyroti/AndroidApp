package nl.carlodvm.androidapp.Core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PathFinder {
    private final Map<Grid, Cost> map;

    public PathFinder() {
        this.map = new HashMap<>();
    }

    public List<Grid> calculateShortestPath(World kaart, Grid start, Grid end) {
        Grid startNode = start;
        Grid endNode = end;
        startNode.setPassable(true);
        end.setPassable(true);

        ArrayList<Grid> open = new ArrayList<>();
        HashSet<Grid> closed = new HashSet<>();

        open.add(startNode);
        for (int i = 0; i < kaart.getWidth(); i++) {
            for (int j = 0; j < kaart.getHeight(); j++) {
                map.put(kaart.getGrid(i, j), new Cost());
            }
        }

        while (open.size() > 0) {
            Grid current = open.get(0);
            for (int i = 1; i < open.size(); i++) {
                if (map.get(open.get(i)).getFCost() < map.get(current).getFCost() || map.get(open.get(i)).getFCost() == map.get(current).getFCost() && map.get(open.get(i)).gethCost() < map.get(current).gethCost()) {
                    current = open.get(i);
                }
            }

            open.remove(current);
            closed.add(current);

            if (current.equals(endNode)) {
                return track(startNode, endNode);
            }

            for (Grid neighbour : kaart.getValidNeighbours(current)) {
                if (!neighbour.isPassable() || closed.contains(neighbour))
                    continue;

                Cost currentCost = map.get(current);

                double newMovementCostToNeighbour = currentCost.getgCost() + manhattanDistance(current, neighbour);

                if (newMovementCostToNeighbour < map.get(neighbour).getgCost() || !open.contains(neighbour)) {
                    Cost neighbourCost = map.get(neighbour);
                    neighbourCost.setgCost(newMovementCostToNeighbour);
                    neighbourCost.sethCost(manhattanDistance(neighbour, end));
                    neighbourCost.setParent(current);
                    if (!open.contains(neighbour)) {
                        open.add(neighbour);
                    }
                }
            }

        }
        return null;
    }

    public Destination getClosestDestination(World world, List<Grid> worldGridPath) {
        return world.getDestinations().stream().filter(x -> worldGridPath.stream().skip(1).anyMatch(y -> y.getX() == x.getX() && y.getY() == x.getY()))
                .findFirst().get();
    }

    public List<Destination> getDestinationsFromPath(World world, List<Grid> worldGridPath) {
        return worldGridPath.stream().map(world::getDestination).filter(x -> x != null).collect(Collectors.toList());

    }

    private List<Grid> track(Grid start, Grid end) {
        List<Grid> pad = new ArrayList<>();
        Grid current = end;
        while (!current.equals(start)) {
            pad.add(current);
            current = map.get(current).getParent();
        }
        pad.add(current);
        Collections.reverse(pad);
        return pad;
    }

    private double manhattanDistance(Grid van, Grid naar) {
        return Math.abs(van.getX() - naar.getX()) + Math.abs(van.getY() - naar.getY());
    }
}
