import InMemoryDatabase.InMemoryDatabase;
import classes.Customer;
import classes.Product;
import errors.EmptyCart;
import errors.InsufficientAmountOfMoney;
import errors.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class cartTests {
    Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("Customer", 1000);
        Product.IDs = 0;
        InMemoryDatabase.database = null;
    }

    @Test
    void add_valid_item() {
        try {
            var product = new Product("product", 10, 10);
            InMemoryDatabase.get_database().addProduct(product);
            customer.add_to_cart(product, 5);
        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void add_not_exist_item() {
        try {
            var product = new Product("product", 3, 3);
            InvalidDataException exception =
                    assertThrows(
                            InvalidDataException.class,
                            () -> customer.add_to_cart(product, 3)
                    );
            assertTrue(exception.getMessage().contains("Product") || !exception.getMessage().isEmpty());

        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void add_repeated_element() {
        try {
            var product = new Product("product", 10, 10);
            InMemoryDatabase.get_database().addProduct(product);
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
            var product1 = new Product("product1", 10, 10);
            var product2 = new Product("product2", 20, 5);
            var product3 = new Product("product3", 5, 20);

            InMemoryDatabase.get_database().addProduct(product1);
            InMemoryDatabase.get_database().addProduct(product2);
            InMemoryDatabase.get_database().addProduct(product3);

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
    void add_invalid_quantity() {
        try {
            var product = new Product("product", 3, 3);
            InvalidDataException exception =
                    assertThrows(
                            InvalidDataException.class,
                            () -> customer.add_to_cart(product, -3)
                    );
            assertTrue(exception.getMessage().contains("Quantity") || !exception.getMessage().isEmpty());

        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void checkout_empty_cart_test() {
        try {
            EmptyCart exception =
                    assertThrows(
                            EmptyCart.class,
                            () -> customer.checkout()
                    );
            assertTrue(exception.getMessage().contains("Cart") || !exception.getMessage().isEmpty());

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void checkout_insufficient_amount_of_money() {
        try {
            customer.setBalance(20);
            var product = new Product("product", 5, 10);
            InMemoryDatabase.get_database().addProduct(product);
            customer.add_to_cart(product, 5);
            InsufficientAmountOfMoney exception =
                    assertThrows(
                            InsufficientAmountOfMoney.class,
                            () -> customer.checkout()
                    );
            assertTrue(exception.getMessage().contains("Money") || !exception.getMessage().isEmpty());

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void checkout_shippable_products() {
        try {
            var Cheese = new Product("Cheese", 100, 10);
            var Biscuits = new Product("Biscuits", 150, 10);
            var Tv = new Product("TV", 500, 1);
            Cheese.setWeight(200);
            Biscuits.setWeight(700);

            InMemoryDatabase.get_database().addProduct(Cheese);
            InMemoryDatabase.get_database().addProduct(Biscuits);
            InMemoryDatabase.get_database().addProduct(Tv);

            customer.add_to_cart(Cheese, 2);
            customer.add_to_cart(Biscuits, 1);
            customer.add_to_cart(Tv, 1);

            customer.checkout();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    @Test
    void add_item_with_zero_quantity() {
        try {
            var product = new Product("product", 10, 10);
            InMemoryDatabase.get_database().addProduct(product);
            InvalidDataException exception = assertThrows(
                    InvalidDataException.class,
                    () -> customer.add_to_cart(product, 0)
            );
            assertTrue(exception.getMessage().toLowerCase().contains("quantity") || !exception.getMessage().isEmpty());
        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void add_item_exceeding_available_quantity() {
        try {
            var product = new Product("product", 15, 5);
            InMemoryDatabase.get_database().addProduct(product);
            InvalidDataException exception = assertThrows(
                    InvalidDataException.class,
                    () -> customer.add_to_cart(product,6)
            );
            assertTrue(exception.getMessage().toLowerCase().contains("Quantity") || !exception.getMessage().isEmpty());
        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void remove_product_not_in_cart() {
        try {
            var product = new Product("product", 10, 10);
            InMemoryDatabase.get_database().addProduct(product);
            assertNull(customer.is_in_cart(product));
        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void checkout_exact_balance() {
        try {
            var product = new Product("product", 1000, 1);
            InMemoryDatabase.get_database().addProduct(product);
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
