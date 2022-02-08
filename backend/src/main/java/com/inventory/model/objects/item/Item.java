package com.inventory.model.objects.item;

public class Item {
    private String itemName;
    private String itemUnit;
    private String productType;
    private int stockOnHand;
    private int committedStock;
    private int availableForSale;
    private float salesRate;
    private float purchaseRate;
    private int organizationID;

    public Item(String itemName, String itemUnit, String productType, int stockOnHand, float salesRate, float purchaseRate, int organizationID) {
        this.itemName = itemName;
        this.itemUnit = itemUnit;
        this.productType = productType;
        this.stockOnHand = stockOnHand;
        this.salesRate = salesRate;
        this.purchaseRate = purchaseRate;
        this.organizationID = organizationID;
    }

    public Item(String itemName, String itemUnit, String productType, int stockOnHand, int committedStock, int availableForSale, float salesRate, float purchaseRate, int organizationID) {
        this.itemName = itemName;
        this.itemUnit = itemUnit;
        this.productType = productType;
        this.stockOnHand = stockOnHand;
        this.salesRate = salesRate;
        this.purchaseRate = purchaseRate;
        this.organizationID = organizationID;
        this.committedStock = committedStock;
        this.availableForSale = availableForSale;
    }
}
