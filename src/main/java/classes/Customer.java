package classes;

import errors.EmptyCart;
import errors.InsufficientAmountOfMoney;
import errors.InvalidDataException;

public class Customer {

    private String name;

    public void setBalance(float balance) {
        this.balance = balance;
    }

    private float balance;
    private Cart cart;

    public Customer(String name, float balance) {
        this.name = name;
        this.balance = balance;
        cart = new Cart();
    }

    public void add_to_cart(Product product, int quantity) throws InvalidDataException {
        this.cart.add_item(product.productID, quantity);
    }

    public Integer is_in_cart(Product product) {
        return this.cart.is_in_cart(product.productID);
    }

    public void checkout() throws EmptyCart, InsufficientAmountOfMoney {
        var total_price = this.cart.checkout(balance);
        this.balance -= total_price;
        System.out.println("Current Balance : " + balance + "\n");
    }
}
