package classes;

public class ShippingItem {
    private String Name;
    private float weight;
    ShippingItem(Product product){
        this.Name = product.getName();
        this.weight = product.getWeight();
    }

}
