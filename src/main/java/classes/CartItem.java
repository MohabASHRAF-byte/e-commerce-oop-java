package classes;

import InMemoryDatabase.InMemoryDatabase;
import errors.InvalidDataException;

public class CartItem {
    public int productId;
    public int quantity;

    public CartItem(int productId, int quantity)throws InvalidDataException {
        if (quantity<0) {
            throw new InvalidDataException("Quantity can't be negative");
        }
        if(!InMemoryDatabase.get_database().isProductExist(productId)){
            throw new InvalidDataException("This Product is Not Exist");
        }
        this.productId =productId;
        this.quantity =quantity;
    }
}