import com.google.gson.Gson;
import zpi.aouu.sales.ProductSaleData;
import zpi.aouu.sales.Sales;

public class TestProductSaleData {
    public static void main(String[] args) {
        ProductSaleData productSaleData = Sales.getProductSaleData("Pieczarki", "100g, W sam raz na pitce", "Alabama", "Jedzenie", 0.23, 100, 1.23, 2, 0);
        System.out.println(productSaleData.toString());

        System.out.println(new Gson().toJson(productSaleData));

        System.out.println(Sales.quantityPriceModifier(2, 10, 10, 99, 0.1, 0.95));
    }

}
