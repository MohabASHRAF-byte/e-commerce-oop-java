import InMemoryDatabase.InMemoryDatabase;
import classes.Product;
import errors.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
    void add_product_test() {
        for (Product p : products) {
            InMemoryDatabase.get_database().addProduct(p);
            var foundProduct = InMemoryDatabase.get_database().findProductById(p.getProductID());
            assertNotNull(foundProduct);
            assertEquals(p.getProductID(), foundProduct.getProductID());
        }
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

    @ParameterizedTest(name = "Add and remove {0} products")
    @ValueSource(ints = {10, 100, 1000, 10000, 100000, 1000000})
    void addAndRemoveProductsPerformance(int count) {
        Product.resetIDsCounter();
        InMemoryDatabase.reset_database();

        ArrayList<Product> products = new ArrayList<>();

        long startAdd = System.currentTimeMillis();
        try {
            for (int i = 1; i <= count; i++) {
                Product p = new Product("product" + i, i, i);
                InMemoryDatabase.get_database().addProduct(p);
                products.add(p);
            }
        } catch (InvalidDataException e) {
            fail("Failed to create/add product: " + e.getMessage());
        }
        long endAdd = System.currentTimeMillis();

        System.out.println("Adding " + count + " products took " + (endAdd - startAdd) + " ms");

        assertEquals(count, InMemoryDatabase.get_database().getNumberOfProducts());

        long startRemove = System.currentTimeMillis();
        for (Product p : products) {
            boolean removed = InMemoryDatabase.get_database().removeProduct(p.getProductID());
            assertTrue(removed);
        }
        long endRemove = System.currentTimeMillis();

        System.out.println("Removing " + count + " products took " + (endRemove - startRemove) + " ms");

        assertEquals(0, InMemoryDatabase.get_database().getNumberOfProducts());
    }

}
