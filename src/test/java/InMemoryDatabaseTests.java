import InMemoryDatabase.InMemoryDatabase;
import classes.Product;
import errors.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryDatabaseTests {
    int numberOfProducts = 10;
    ArrayList<Product> products;

    @BeforeEach
    void setUp() {
        Product.resetIDsCounter();
        InMemoryDatabase.reset_database();
        this.products = new ArrayList<>();
        try {
            for (int i = 0; i < numberOfProducts; i++) {
                products.add(new Product("product " + i, i * 2, i));
            }
        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void add_single_product() {
        InMemoryDatabase.get_database().addProduct(products.getFirst());
        var product = InMemoryDatabase.get_database().findProductById(1);
        assertEquals(product.getProductID(), products.getFirst().getProductID());
    }

    @Test
    void test_add_products() {
        for (var product : products) {
            InMemoryDatabase.get_database().addProduct(product);
        }
        assertEquals(numberOfProducts, InMemoryDatabase.get_database().getNumberOfProducts());
    }

    @Test
    void test_remove_products() {
        for (var product : products)
            InMemoryDatabase.get_database().addProduct(product);
        assertTrue(InMemoryDatabase.get_database().removeProduct(1));
        var product = InMemoryDatabase.get_database().findProductById(1);
        assertNull(product);
    }

    @Test
    void test_is_exist() {
        for (var product : products)
            InMemoryDatabase.get_database().addProduct(product);
        assertTrue(InMemoryDatabase.get_database().isProductExist(1));
    }
}
