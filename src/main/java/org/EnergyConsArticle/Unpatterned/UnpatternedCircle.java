package org.EnergyConsArticle.Unpatterned;

public class UnpatternedCircle extends UnpatternedDot {
    private int radius;

    public UnpatternedCircle(int id, int x, int y, int radius) {
        super(id, x, y);
        this.radius = radius;
    }

    @Override
    public String exportXML() {
        return "<circle>" + "\n" +
                "    <id>" + getId() + "</id>" + "\n" +
                "    <x>" + getX() + "</x>" + "\n" +
                "    <y>" + getY() + "</y>" + "\n" +
                "    <radius>" + radius + "</radius>" + "\n" +
                "</circle>";
    }

    public int getRadius() {
        return radius;
    }
}
