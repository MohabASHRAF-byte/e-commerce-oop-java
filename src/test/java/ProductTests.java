import classes.Product;
import errors.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTests {
    String productName = "Product1";
    int productPrice = 10;
    int productQuantity = 4;
    int weight = 10;
    Date expiryDate;

    @BeforeEach
    void setUp() {
        productName = "Product1";
        productPrice = 10;
        productQuantity = 4;
        weight = 10;
        LocalDate localDate = LocalDate.of(2025, 7, 10);
        expiryDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Product.resetIDsCounter();
    }

    private Product createValidProduct() {
        try {
            return new Product(productName, productPrice, productQuantity);
        } catch (InvalidDataException e) {
            fail("Unexpected exception: " + e.getMessage());
            return null;
        }
    }

    private Product createValidProduct(String name, float price, int quantity) {
        try {
            return new Product(name, price, quantity);
        } catch (InvalidDataException e) {
            fail("Unexpected exception: " + e.getMessage());
            return null;
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @CsvSource({
            "'Regular product', 'ProductA', 10, 5",
            "'Zero price', 'ProductB', 0, 10",
            "'Zero quantity', 'ProductC', 100, 0",
            "'Zero price and quantity', 'ProductD', 0, 0",
            "'Large price', 'ProductE', 1000000, 1",
            "'Large quantity', 'ProductF', 1, 1000000"
    })
    void add_valid_product_test(String testName, String productName, int price, int quantity) {
        var product = createValidProduct(productName, price, quantity);

        assertNotNull(product, "Product should be created");
        assertEquals(productName, product.getName(), "Name mismatch");
        assertEquals(price, product.getPrice(), "Price mismatch");
        assertEquals(quantity, product.getQuantity(), "Quantity mismatch");
        assertFalse(product.getExpirable(), "Product should not be expirable initially");
        assertFalse(product.getShippable(), "Product should not be shippable initially");
        assertEquals(1, product.getProductID(), "Product ID should be 1 because IDs reset before each test");
    }

    @ParameterizedTest(name = "[{index}] {4}")
    @CsvSource({
            "'', 10, 4, Name, invalid name",
            "Product1, -10, 4, Price, invalid price",
            "Product1, 10, -4, Quantity, invalid quantity",
            "'   ', 10, 4, Name, invalid name"

    })
    void add_product_invalid_params_throws(
            String name,
            int price,
            int quantity,
            String expectedMessagePart,
            String displayName) {

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            new Product(name, price, quantity);
        });
        assertTrue(exception.getMessage().contains(expectedMessagePart));
    }


    @Test
    void add_product_with_expiry_date_test() {
        try {
            var product = createValidProduct();
            assertNotNull(product);
            product.setExpiryDate(expiryDate);
            assertEquals(expiryDate, product.getExpiryDate());
            assertTrue(product.getExpirable());
        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void add_product_with_invalid_expiry_date_test() {
        LocalDate localDate = LocalDate.of(2024, 7, 1);
        expiryDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        InvalidDataException exception =
                assertThrows(
                        InvalidDataException.class,
                        () -> {
                            var product = createValidProduct();
                            assertNotNull(product);
                            product.setExpiryDate(expiryDate);
                        }
                );

        assertTrue(exception.getMessage().contains("Expiry"));
    }

    @Test
    void add_product_with_valid_weight() {
        try {
            var product = createValidProduct();
            assertNotNull(product);
            product.setWeight(weight);
            assertEquals(weight, product.getWeight());
            assertTrue(product.getShippable());
        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void add_product_with_invalid_weight() {
        InvalidDataException exception =
                assertThrows(
                        InvalidDataException.class,
                        () -> {
                            var product = createValidProduct();
                            assertNotNull(product);
                            product.setWeight(-1);
                        }
                );

        assertTrue(exception.getMessage().contains("Weight") || !exception.getMessage().isEmpty());

    }
}
