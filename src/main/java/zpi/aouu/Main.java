package zpi.aouu;

import zpi.aouu.client.*;
import zpi.aouu.util.Paths;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFiles.location("/static");

        get(Paths.PRODUCTS.path, (req, res) -> {
            res.type("application/json");
            return Product.getProduct();
        });

        get(Paths.STATES.path, State::getStates);

        get("/countries", Country::getCountries);

        post("/priceabroad/:productName/:finalPrice", "application/json", PriceAbroad::calculate);

        post(Paths.CALCULATE_PRICE.path, "application/json", Price::calculate);


        put("/product/:productId", "application/json", Product::updateProduct);

    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
