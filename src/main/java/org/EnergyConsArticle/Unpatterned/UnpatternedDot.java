package org.EnergyConsArticle.Unpatterned;

public class UnpatternedDot implements UnpatternedShape {
    private int id;
    private int x;
    private int y;

    public UnpatternedDot() {
    }

    public UnpatternedDot(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(int x, int y) {
        // move shape
    }

    @Override
    public void draw() {
        // draw shape
    }

    @Override
    public String exportXML() {
        return "<dot>" + "\n" +
                "    <id>" + id + "</id>" + "\n" +
                "    <x>" + x + "</x>" + "\n" +
                "    <y>" + y + "</y>" + "\n" +
                "</dot>";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }
}
