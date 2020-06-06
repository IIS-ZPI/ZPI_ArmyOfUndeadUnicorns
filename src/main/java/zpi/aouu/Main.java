package zpi.aouu;

import zpi.aouu.client.Price;
import zpi.aouu.client.Product;
import zpi.aouu.client.State;
import zpi.aouu.renderer.ContentRenderer;
import zpi.aouu.util.Paths;

import static spark.Spark.*;

public class Main {
    private static final String contentType = "application/json";
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFiles.location("/static");

        /*get(Paths.START_PAGE.path, (q, a) -> {
            return ContentRenderer.renderContent("/htmls/header.html", "/htmls/warehouse.html");
        });*/

        get(Paths.PRODUCTS.path, (req, res) -> {
            res.type(contentType);
            return Product.getProduct();
        });

        get(Paths.STATES_FOR_PRODUCT.path, contentType, (req, res) -> {
            String product = req.queryParams("product");
            if (product == null) throw new IllegalArgumentException();
            else return Product.getAvailableStates(product);
        });

        get(Paths.STATES.path, State::getStates);

        post(Paths.CALCULATE_PRICE.path, contentType, Price::calculate);


    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
