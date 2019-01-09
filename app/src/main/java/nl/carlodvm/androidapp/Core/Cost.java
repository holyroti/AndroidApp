package nl.carlodvm.androidapp.Core;

public class Cost {
    private double gCost;
    private double hCost;
    private Grid parent;

    public Cost() {
        gCost = 0;
        hCost = 0;
    }

    public double getFCost() {
        return getgCost() + gethCost();
    }

    public double getgCost() {
        return gCost;
    }

    public void setgCost(double gCost) {
        this.gCost = gCost;
    }

    public double gethCost() {
        return hCost;
    }

    public void sethCost(double hCost) {
        this.hCost = hCost;
    }

    public Grid getParent() {
        return parent;
    }

    public void setParent(Grid parent) {
        this.parent = parent;
    }
}
