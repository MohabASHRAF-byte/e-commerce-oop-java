package classes;

import InMemoryDatabase.InMemoryDatabase;
import errors.EmptyCart;
import errors.InsufficientAmountOfMoney;
import errors.InvalidDataException;

import java.util.ArrayList;

public class Cart {
    private ArrayList<CartItem> items;
    private float subtotalPrice, shippingFees, totalPrice, totalWeightInGrams;

    public Cart() {
        items = new ArrayList<>();
    }

    public void add_item(int productId, int quantity) throws InvalidDataException {
        boolean found = false;
        for (var cartItem : items) {
            if (cartItem.productId == productId) {
                found = true;
                cartItem.quantity += quantity;
            }
        }
        if (!found) {
            items.add(new CartItem(productId, quantity));
        }
    }

    public Integer is_in_cart(int productID) {
        for (var cartItem : items) {
            if (cartItem.productId == productID) {
                return cartItem.quantity;
            }
        }
        return null;
    }

    public float checkout(float balance) throws EmptyCart, InsufficientAmountOfMoney {
        if (items.isEmpty()) {
            throw new EmptyCart();
        }
        subtotalPrice = CalculatePriceWithoutShipping();
        shippingFees = CalculateShippingPrice();
        totalPrice = subtotalPrice + shippingFees;
        if (totalPrice > balance) {
            throw new InsufficientAmountOfMoney();
        }
        if (ExecuteTransaction()) {
            print_checkout();
            items.clear();
        } else {
            totalPrice = 0;
        }
        return totalPrice;
    }

    private void print_checkout() {
        StringBuilder shippingPrint = new StringBuilder();
        StringBuilder checkOutPrint = new StringBuilder();
        shippingPrint.append("** Shipment notice **\n");
        checkOutPrint.append("** Checkout receipt **\n");
        for (var item : items) {
            var product = InMemoryDatabase.get_database().findProductById(item.productId);
            if (product != null && product.getShippable()) {
                var total_product_weight = item.quantity * product.getWeight();
                shippingPrint.append(item.quantity)
                        .append("x ")
                        .append(product.getName())
                        .append("\t")
                        .append(String.format("%.2f", total_product_weight))
                        .append(" g\n");
            }
            if (product != null) {
                var productPrice = product.getPrice() * item.quantity;
                checkOutPrint.append(item.quantity)
                        .append("x ")
                        .append(product.getName())
                        .append("\t")
                        .append(productPrice)
                        .append("\n");
            }
        }
        shippingPrint.append("Total package weight : ").append(totalWeightInGrams).append("g\n");
        checkOutPrint.append(".......................\n")
                .append("Subtotal :").append("\t").append(subtotalPrice).append("\n")
                .append("Shipping :").append("\t").append(shippingFees).append("\n")
                .append("Amount :").append("\t").append(totalPrice).append("\n");
        System.out.println(shippingPrint);
        System.out.println(checkOutPrint);
    }

    private boolean ExecuteTransaction() {
        ArrayList<CartItem> Executed = new ArrayList<>();
        try {
            for (var item : items) {
                var product = InMemoryDatabase.get_database().findProductById(item.productId);
                if (product != null && product.getQuantity() >= item.quantity) {
                    product.setQuantity(product.getQuantity() - item.quantity);
                    Executed.add(item);
                }
            }
        } catch (Exception ex) {
            rollbackExecutedItems(Executed);
            return false;
        }
        if (Executed.size() != items.size()) {
            rollbackExecutedItems(Executed);
            return false;
        }
        return true;
    }

    private void rollbackExecutedItems(ArrayList<CartItem> executed) {
        for (var item : executed) {
            var product = InMemoryDatabase.get_database().findProductById(item.productId);
            if (product != null) {
                product.setQuantity(product.getQuantity() + item.quantity);
            }
        }
    }

    private float CalculateShippingPrice() {
        var db = InMemoryDatabase.get_database();
        totalWeightInGrams = 0;
        for (var item : items) {
            var product = db.findProductById(item.productId);
            if (product.getShippable()) {
                totalWeightInGrams += product.getWeight() * item.quantity;
            }
        }
        return totalWeightInGrams * InMemoryDatabase.ShippingPricePerGram;
    }

    private float CalculatePriceWithoutShipping() {
        var db = InMemoryDatabase.get_database();
        float totalPrice = 0;
        for (var item : items) {
            var product = db.findProductById(item.productId);
            totalPrice += product.getPrice() * item.quantity;
        }
        return totalPrice;
    }
}
