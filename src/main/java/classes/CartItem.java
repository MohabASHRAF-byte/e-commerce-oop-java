package classes;

import InMemoryDatabase.InMemoryDatabase;
import errors.InvalidDataException;

public class CartItem {
    public int productId;
    public int quantity;

    public CartItem(int productId, int quantity)throws InvalidDataException {
        if (quantity<=0) {
            throw new InvalidDataException("Quantity can't be negative");
        }
        if(!InMemoryDatabase.get_database().canBeAddedToCart(productId,quantity)){
            throw new InvalidDataException("This Product is Not Exist or NO Enough Quantity");
        }
        this.productId =productId;
        this.quantity =quantity;
    }
}