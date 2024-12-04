package org.EnergyConsArticle.Unpatterned;

public class UnpatternedRectangle implements UnpatternedShape {
    private int id;
    private int x;
    private int y;
    private int width;
    private int height;

    public UnpatternedRectangle(int id, int x, int y, int width, int height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
        return "<rectangle>" + "\n" +
                "    <id>" + id + "</id>" + "\n" +
                "    <x>" + x + "</x>" + "\n" +
                "    <y>" + y + "</y>" + "\n" +
                "    <width>" + width + "</width>" + "\n" +
                "    <height>" + height + "</height>" + "\n" +
                "</rectangle>";
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
