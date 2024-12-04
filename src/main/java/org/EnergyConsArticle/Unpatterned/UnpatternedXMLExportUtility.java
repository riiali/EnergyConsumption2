package org.EnergyConsArticle.Unpatterned;

public class UnpatternedXMLExportUtility {

    public static String export(UnpatternedShape... shapes) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n");
        for (UnpatternedShape shape : shapes) {
            sb.append(shape.exportXML()).append("\n");
        }
        return sb.toString();
    }
}
