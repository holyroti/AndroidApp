package nl.carlodvm.androidapp.Core;

public class Destination extends Grid {
    private String name;

    public Destination(int x, int y, String name) {
        super(x, y, true);
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
