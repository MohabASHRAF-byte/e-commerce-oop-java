package InMemoryDatabase;

import classes.Product;

import java.util.HashMap;
import java.util.Map;

public class InMemoryDatabase {
    public static float ShippingPricePerGram = 0.01f;
    public static InMemoryDatabase database;
    private final Map<Integer, Product> products = new HashMap<>();

    public static InMemoryDatabase get_database() {
        if (database == null) {
            database = new InMemoryDatabase();
        }
        return database;
    }

    public static void reset_database() {
        database = new InMemoryDatabase();
    }

    public void addProduct(Product product) {
        products.put(product.getProductID(), product);
    }

    public Product findProductById(int productId) {
        return products.get(productId);
    }

    public boolean removeProduct(int productId) {
        return products.remove(productId) != null;
    }

    public int getNumberOfProducts() {
        return products.size();
    }

    public boolean isProductExist(int productId) {
        return products.containsKey(productId);
    }

    public boolean canBeAddedToCart(int productId, int quantity) {
        Product p = products.get(productId);
        return p != null && p.getQuantity() >= quantity;
    }
}
