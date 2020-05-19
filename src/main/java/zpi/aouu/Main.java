package zpi.aouu;

import spark.Spark;
import spark.utils.IOUtils;
import zpi.aouu.client.Price;
import zpi.aouu.client.State;
import zpi.aouu.client.Product;
import zpi.aouu.util.Paths;

import java.io.IOException;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFiles.location("/static");

        get(Paths.START_PAGE.path, (q, a) -> renderContent("/static/index.html"));

        get(Paths.PRODUCTS.path, (req, res) -> {
            res.type("application/json");
            return Product.getProduct();
        });

        get(Paths.STATES_FOR_PRODUCT.path, "application/json", (req, res) -> {
            String product = req.queryParams("product");
            if (product == null) throw new IllegalArgumentException();
            else return Product.getAvailableStates(product);
        });

        get(Paths.STATES.path, State::getStates);

        post(Paths.CALCULATE_PRICE.path, "application/json", Price::calculate);


    }

    private static String renderContent(String htmlFile) {
        String html = null;
        try {
            html = IOUtils.toString(Spark.class.getResourceAsStream(htmlFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
