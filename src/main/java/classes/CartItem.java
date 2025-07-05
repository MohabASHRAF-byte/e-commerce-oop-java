package classes;

import InMemoryDatabase.InMemoryDatabase;
import Utilities.Validations;
import errors.InvalidDataException;

public class CartItem {
    public int productId;
    public int quantity;

    public CartItem(int productId, int quantity) throws InvalidDataException {
        Validations.assignPositive(quantity, "Quantity");
        if (!InMemoryDatabase.get_database().canBeAddedToCart(productId, quantity)) {
            throw new InvalidDataException("This Product is Not Exist or NO Enough Quantity");
        }
        this.productId = productId;
        this.quantity = quantity;
    }
}