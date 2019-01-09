package nl.carlodvm.androidapp.Core;

public class Destination extends Grid {
    private String name;
    private int imageIndex;

    public Destination(int x, int y, String name, int imageIndex) {
        super(x, y, true);
        this.name = name;
        this.imageIndex = imageIndex;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    @Override
    public String toString() {
        return name + " (x:" + (getX() + 1)  + ", y:" + (getY() + 1) + ")";
    }
}
