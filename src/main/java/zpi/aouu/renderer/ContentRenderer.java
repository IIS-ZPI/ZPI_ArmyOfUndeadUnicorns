package zpi.aouu.renderer;

import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;

public abstract class ContentRenderer {

    public static String renderContent(String... files) {
        StringBuilder html = new StringBuilder();
        try {
            for(String file : files) {
                html.append(IOUtils.toString(Spark.class.getResourceAsStream(file)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html.toString();
    }

}
