package com.inventory.model.objects.order;

public class Order {
    public int itemID;
    public int quantity;

    public Order(int itemID, int quantity) {
        this.itemID = itemID;
        this.quantity = quantity;
    }
}
