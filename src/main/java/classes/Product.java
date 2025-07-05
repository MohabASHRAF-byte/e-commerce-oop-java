package classes;

import Utilities.Validations;
import errors.InvalidDataException;
import interfaces.ShippableProduct;

import java.util.Date;

public class Product implements ShippableProduct {

    public static int IDs = 0;
    public int productID = 0;
    private String name;
    private float price;
    private int quantity;
    private Boolean isExpirable = false;
    private Date expiryDate;
    private Boolean isShippable = false;
    private float weight;

    public Product(
            String name,
            int price,
            int quantity
    ) throws InvalidDataException {
        this.name = Validations.assignNonEmptyString(name);
        this.price = Validations.assignNonNegative((float) price, "Price");
        this.quantity = Validations.assignNonNegative(quantity, "Quantity");
        this.weight = 0;
        Product.IDs++;
        this.productID = Product.IDs;
    }

    public int getProductID() {
        return productID;
    }

    @Override
    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Boolean getExpirable() {
        return isExpirable;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date ExpiryDate) throws InvalidDataException {
        this.expiryDate = Validations.validateNotPastDate(ExpiryDate, "Expiry Date");
        this.isExpirable = true;
        this.expiryDate = ExpiryDate;
    }

    public Boolean getShippable() {
        return isShippable;
    }

    @Override
    public float getWeight() {
        return weight;
    }

    public void setWeight(int weight) throws InvalidDataException {
        this.weight = Validations.assignNonNegative(weight, "Weight");
        this.isShippable = true;
    }
}
