import zpi.aouu.sales.ProductSaleData;
import zpi.aouu.sales.Sales;

public class TestProductSaleData {
    public static void main(String[] args) {
        ProductSaleData productSaleData = Sales.getProductSaleData("Pieczarki", "100g, W sam raz na pitce", "Alabama", "Jedzenie", 0.04, 0.25, 1, 0);
        System.out.println(productSaleData.toString());
    }

}
