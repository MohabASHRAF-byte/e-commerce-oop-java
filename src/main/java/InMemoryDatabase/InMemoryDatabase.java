package InMemoryDatabase;

import classes.Product;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDatabase {
    public static float ShippingPricePerGram = 0.01f;
    public static InMemoryDatabase database;

    public static InMemoryDatabase get_database() {
        if (database == null) {
            database = new InMemoryDatabase();
        }
        return database;
    }

    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public Product findProductById(int productId) {
        for (Product p : products) {
            if (p.getProductID() == productId) {
                return p;
            }
        }
        return null;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public boolean removeProduct(int productId) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductID() == productId) {
                products.remove(i);
                return true;
            }
        }
        return false;
    }

    public int getNumberOfProducts() {
        return products.size();
    }

    public boolean isProductExist(int productId) {
        for (Product p : products) {
            if (p.getProductID() == productId) {
                return true;
            }
        }
        return false;
    }
}
