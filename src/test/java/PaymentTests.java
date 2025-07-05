import InMemoryDatabase.InMemoryDatabase;
import classes.Customer;
import classes.Product;
import errors.EmptyCart;
import errors.InsufficientAmountOfMoney;
import errors.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTests {

    Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("Test Customer", 1000);
        Product.IDs = 0;
        InMemoryDatabase.database = null;
    }

    @Test
    void checkout_with_sufficient_balance() {
        try {
            var product = new Product("Item A", 100, 10);
            InMemoryDatabase.get_database().addProduct(product);
            customer.add_to_cart(product, 5); // Total product cost: 500
            float total = customer.checkout();
            assertEquals(500 + (product.getWeight() * 5 * InMemoryDatabase.ShippingPricePerGram), total, 0.01);
            assertEquals(1000 - total, customer.getBalance(), 0.01);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void checkout_with_exact_balance() {
        try {
            var product = new Product("Item B", 1000, 1);
            InMemoryDatabase.get_database().addProduct(product);
            customer.add_to_cart(product, 1);
            customer.setBalance(1000 + (product.getWeight() * InMemoryDatabase.ShippingPricePerGram));
            float total = customer.checkout();
            assertEquals(0, customer.getBalance(), 0.01);
            assertEquals(1000 + (product.getWeight() * InMemoryDatabase.ShippingPricePerGram), total, 0.01);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void checkout_with_insufficient_balance_throws() {
        try {
            var product = new Product("Item C", 500, 5);
            InMemoryDatabase.get_database().addProduct(product);
            customer.add_to_cart(product, 5); // Total product cost: 2500
            customer.setBalance(100); // Insufficient balance
            assertThrows(InsufficientAmountOfMoney.class, () -> {
                customer.checkout();
            });
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void checkout_empty_cart_throws() {
        assertThrows(EmptyCart.class, () -> {
            customer.checkout();
        });
    }

    @Test
    void multiple_items_checkout_calculation() {
        try {
            var product1 = new Product("Item D", 50, 10);
            var product2 = new Product("Item E", 200, 5);
            InMemoryDatabase.get_database().addProduct(product1);
            InMemoryDatabase.get_database().addProduct(product2);
            customer.add_to_cart(product1, 2); // Cost: 100
            customer.add_to_cart(product2, 1); // Cost: 200
            float expectedSubtotal = 100 + 200;
            float expectedShipping = (product1.getWeight() * 2 + product2.getWeight()) * InMemoryDatabase.ShippingPricePerGram;
            customer.setBalance(expectedSubtotal + expectedShipping + 1000);
            float total = customer.checkout();
            assertEquals(expectedSubtotal + expectedShipping, total, 0.01);
            assertEquals(customer.getBalance(), 1000, 0.01);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void checkout_with_zero_weight_product() {
        try {
            var product = new Product("Light Item", 100, 10);
            // weight defaults to 0, so no shipping
            InMemoryDatabase.get_database().addProduct(product);
            customer.add_to_cart(product, 3); // 300 total
            float total = customer.checkout();
            assertEquals(300, total, 0.01);
            assertEquals(700, customer.getBalance(), 0.01);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void checkout_with_multiple_quantities_of_same_product() {
        try {
            var product = new Product("Bulk Item", 20, 100);
            product.setWeight(50);
            InMemoryDatabase.get_database().addProduct(product);
            customer.add_to_cart(product, 2);
            customer.add_to_cart(product, 3);
            float expectedSubtotal = 20 * 5;
            float expectedShipping = 50 * 5 * InMemoryDatabase.ShippingPricePerGram;
            customer.setBalance(expectedSubtotal + expectedShipping + 100);
            float total = customer.checkout();
            assertEquals(expectedSubtotal + expectedShipping, total, 0.01);
            assertEquals(100, customer.getBalance(), 0.01);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void add_item_with_zero_quantity_throws() {
        try {
            var product = new Product("Invalid Qty Item", 10, 10);
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
    void add_item_exceeding_available_quantity_throws() {
        try {
            var product = new Product("Exceed Qty Item", 15, 5);
            InMemoryDatabase.get_database().addProduct(product);
            InvalidDataException exception = assertThrows(
                    InvalidDataException.class,
                    () -> customer.add_to_cart(product, 6)
            );
            assertTrue(exception.getMessage().toLowerCase().contains("quantity") || !exception.getMessage().isEmpty());
        } catch (InvalidDataException ex) {
            fail(ex.getMessage());
        }
    }

    // Test checkout calculations with decimal prices and weights ensuring precision to 2 decimals
    @Test
    void checkout_with_decimal_prices_and_weights_precision() {
        try {
            var product = new Product("Decimal Item", 99, 10);
            product.setPrice(99.99f);
            product.setWeight(123);

            InMemoryDatabase.get_database().addProduct(product);
            customer.add_to_cart(product, 3);
            float expectedShipping = 123 * 3 * InMemoryDatabase.ShippingPricePerGram;

            float expectedTotal = 299.97f + expectedShipping;

            customer.setBalance(1000);
            float total = customer.checkout();
            assertEquals(expectedTotal, total, 0.01);
            assertEquals(1000 - total, customer.getBalance(), 0.01);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
