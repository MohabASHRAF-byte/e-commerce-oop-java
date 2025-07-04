import classes.Product;
import errors.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
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
        Product.IDs=0;
    }

    @Test
    void add_valid_product_test() {

        try {
            var product = new Product(productName, productPrice, productQuantity);

            assertEquals(productName, product.getName());
            assertEquals(productPrice, product.getPrice());
            assertEquals(productQuantity, product.getQuantity());
            assertEquals(false, product.getExpirable());
            assertEquals(false, product.getShippable());
            assertEquals(1,product.productID);
        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void add_invalid_product_name_test() {
        this.productName = "";
        InvalidDataException exception =
                assertThrows(
                        InvalidDataException.class,
                        () -> new Product(productName, productPrice, productQuantity)
                );
        assertTrue(exception.getMessage().contains("Name") || !exception.getMessage().isEmpty());
    }

    @Test
    void add_invalid_product_price_test() {
        this.productPrice = -10;
        InvalidDataException exception =
                assertThrows(
                        InvalidDataException.class,
                        () -> new Product(productName, productPrice, productQuantity)
                );
        assertTrue(exception.getMessage().contains("Price") || !exception.getMessage().isEmpty());
    }

    @Test
    void add_invalid_product_quantity_test() {

        this.productQuantity = -4;

        InvalidDataException exception =
                assertThrows(
                        InvalidDataException.class,
                        () -> new Product(productName, productPrice, productQuantity)
                );
        assertTrue(exception.getMessage().contains("Quantity") || !exception.getMessage().isEmpty());
    }

    @Test
    void add_product_with_expiry_date_test() {
        try {
            var product = new Product(productName, productPrice, productQuantity);
            product.setExpiryDate(expiryDate);
            assertEquals(expiryDate, product.getExpiryDate());
            assertEquals(true, product.getExpirable());
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
                            var product = new Product(productName, productPrice, productQuantity);
                            product.setExpiryDate(expiryDate);
                        }
                );

        assertTrue(exception.getMessage().contains("Expiry") || !exception.getMessage().isEmpty());
    }

    @Test
    void add_product_with_valid_weight(){
        try {
            var product = new Product(productName, productPrice, productQuantity);
            product.setWeight(weight);
            assertEquals(weight, product.getWeight());
            assertEquals(true, product.getShippable());
        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
        }
    }
    @Test
    void add_product_with_invalid_weight(){
        this.weight = -1;
        InvalidDataException exception =
                assertThrows(
                        InvalidDataException.class,
                        () -> {
                            var product = new Product(productName, productPrice, productQuantity);
                            product.setWeight(weight);
                        }
                );

        assertTrue(exception.getMessage().contains("Weight") || !exception.getMessage().isEmpty());

    }
}
