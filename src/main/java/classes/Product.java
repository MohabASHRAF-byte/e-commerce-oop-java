package classes;

import errors.InvalidDataException;
import interfaces.ShippableProduct;

import java.time.LocalDate;
import java.time.ZoneId;
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
        if (name.isEmpty()) {
            throw new InvalidDataException("Name Can't be empty");
        }
        this.name = name;
        if (price < 0) {
            throw new InvalidDataException("Price Can't be negative");
        }
        this.price = price;
        if (quantity < 0) {
            throw new InvalidDataException("Quantity Can't be negative");
        }
        this.quantity = quantity;
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
        LocalDate today = LocalDate.now();
        Date todayDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        if (ExpiryDate.before(todayDate))
            throw new InvalidDataException("Expiry Date should not be past date");
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
        if (weight < 0) {
            throw new InvalidDataException("Weight can't be empty");
        }
        this.weight = weight;
        this.isShippable = true;
    }
}
