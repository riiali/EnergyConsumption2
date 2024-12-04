package org.EnergyConsArticle.Unpatterned;

import java.util.ArrayList;
import java.util.List;

public class UnpatternedCompoundShape implements UnpatternedShape {
    private int id;
    private List<UnpatternedShape> children = new ArrayList<>();

    public UnpatternedCompoundShape(int id) {
        this.id = id;
    }

    @Override
    public void move(int x, int y) {
        // move shape
    }

    @Override
    public void draw() {
        // draw shape
    }

    public void add(UnpatternedShape shape) {
        children.add(shape);
    }

    @Override
    public String exportXML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<compound_graphic>" + "\n")
                .append("    <id>" + id + "</id>" + "\n");

        for (UnpatternedShape shape : children) {
            String obj = shape.exportXML();
            obj = "    " + obj.replace("\n", "\n    ") + "\n"; // Proper indentation for sub-objects
            sb.append(obj);
        }

        sb.append("</compound_graphic>");
        return sb.toString();
    }

    public int getId() {
        return id;
    }
}
