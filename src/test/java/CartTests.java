import InMemoryDatabase.InMemoryDatabase;
import classes.Customer;
import classes.Product;
import errors.EmptyCart;
import errors.InsufficientAmountOfMoney;
import errors.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class CartTests {
    Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("Customer", 1000);
        Product.resetIDsCounter();
        InMemoryDatabase.database = null;
    }

    private Product createAndAddProduct(String name, int price, int quantity) {
        try {
            Product product = new Product(name, price, quantity);
            InMemoryDatabase.get_database().addProduct(product);
            return product;
        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
            return null;
        }
    }

    @Test
    void add_valid_item() {
        assertNotNull(createAndAddProduct("product", 10, 10));
    }

    @Test
    void add_not_exist_item() {
        InvalidDataException exception =
                assertThrows(
                        InvalidDataException.class,
                        () -> {
                            var product = new Product("product", 3, 3);
                            customer.add_to_cart(product, 3);
                        }
                );
        assertTrue(exception.getMessage().contains("Product"));
    }

    @Test
    void add_repeated_element() {
        try {
            var product = createAndAddProduct("product", 10, 10);
            assertNotNull(product);
            customer.add_to_cart(product, 2);
            customer.add_to_cart(product, 3);
            assertEquals(5, customer.is_in_cart(product));
        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void add_multiple_products_with_various_quantities() {
        try {
            var product1 = createAndAddProduct("product1", 10, 10);
            var product2 = createAndAddProduct("product2", 20, 5);
            var product3 = createAndAddProduct("product3", 5, 20);

            assertNotNull(product1);
            assertNotNull(product2);
            assertNotNull(product3);

            customer.add_to_cart(product1, 3);
            customer.add_to_cart(product2, 4);
            customer.add_to_cart(product3, 10);

            assertEquals(3, customer.is_in_cart(product1));
            assertEquals(4, customer.is_in_cart(product2));
            assertEquals(10, customer.is_in_cart(product3));

        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void checkout_empty_cart_test() {
        EmptyCart exception =
                assertThrows(
                        EmptyCart.class,
                        () -> customer.checkout()
                );
        assertTrue(exception.getMessage().contains("Cart"));
    }

    @Test
    void checkout_insufficient_amount_of_money() {

        InsufficientAmountOfMoney exception =
                assertThrows(
                        InsufficientAmountOfMoney.class,
                        () -> {
                            customer.setBalance(20);
                            var product = createAndAddProduct("product", 5, 10);
                            assertNotNull(product);
                            customer.add_to_cart(product, 5);
                            customer.checkout();
                        }
                );
        assertTrue(exception.getMessage().contains("Money"));

    }

    @Test
    void checkout_shippable_products() {
        try {
            var Cheese = createAndAddProduct("Cheese", 100, 10);
            var Biscuits = createAndAddProduct("Biscuits", 150, 10);
            var Tv = createAndAddProduct("TV", 500, 1);

            assertNotNull(Cheese);
            assertNotNull(Biscuits);
            assertNotNull(Tv);

            Cheese.setWeight(200);
            Biscuits.setWeight(700);

            customer.add_to_cart(Cheese, 2);
            customer.add_to_cart(Biscuits, 1);
            customer.add_to_cart(Tv, 1);

            customer.checkout();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @ParameterizedTest(name = "[{index}] {1}")
    @CsvSource({
            "-3, 'negative quantity'",
            "0, 'zero quantity'",
            "6, 'exceeding available quantity'"
    })
    void add_invalid_quantity_throws(int invalidQuantity, String testName) {
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> {
                    var product = createAndAddProduct("product", 5, 5); // stock = 5
                    assertNotNull(product);
                    customer.add_to_cart(product, invalidQuantity);
                }
        );
        assertTrue(exception.getMessage().toLowerCase().contains("quantity"));
    }

    @Test
    void remove_product_not_in_cart() {
        var product = createAndAddProduct("product", 10, 10);
        assertNotNull(product);
        assertNull(customer.is_in_cart(product));

    }

    @Test
    void checkout_exact_balance() {
        try {
            var product = createAndAddProduct("product", 1000, 1);
            assertNotNull(product);
            customer.add_to_cart(product, 1);
            customer.setBalance(1000);
            float totalPrice = customer.checkout();
            assertEquals(0, customer.getBalance(), 0.01);
            assertEquals(1000, totalPrice, 0.01);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}
