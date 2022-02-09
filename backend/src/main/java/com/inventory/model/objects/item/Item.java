package com.inventory.model.objects.item;

public class Item {
    private int id;
    private String name;
    private String unit;
    private String type;
    private float salesRate;
    private float purchaseRate;
    private int physicalStockOnHand;
    private int physicalCommittedStock;
    private int physicalAvailableForSale;
    private int accountingStockOnHand;
    private int accountingCommittedStock;
    private int accountingAvailableForSale;
    private int organizationID;

    public Item(String name, float salesRate, float purchaseRate, int organizationID) {
        this.name = name;
        this.salesRate = salesRate;
        this.purchaseRate = purchaseRate;
        this.organizationID = organizationID;
        this.type = "Goods";
        this.unit = "pcs";
        this.physicalStockOnHand = 0;
        this.physicalCommittedStock = 0;
        this.physicalAvailableForSale = 0;
        this.accountingStockOnHand = 0;
        this.accountingCommittedStock = 0;
        this.accountingAvailableForSale = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getSalesRate() {
        return salesRate;
    }

    public void setSalesRate(float salesRate) {
        this.salesRate = salesRate;
    }

    public float getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(float purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public int getPhysicalStockOnHand() {
        return physicalStockOnHand;
    }

    public void setPhysicalStockOnHand(int physicalStockOnHand) {
        this.physicalStockOnHand = physicalStockOnHand;
    }

    public int getPhysicalCommittedStock() {
        return physicalCommittedStock;
    }

    public void setPhysicalCommittedStock(int physicalCommittedStock) {
        this.physicalCommittedStock = physicalCommittedStock;
    }

    public int getPhysicalAvailableForSale() {
        return physicalAvailableForSale;
    }

    public void setPhysicalAvailableForSale(int physicalAvailableForSale) {
        this.physicalAvailableForSale = physicalAvailableForSale;
    }

    public int getAccountingStockOnHand() {
        return accountingStockOnHand;
    }

    public void setAccountingStockOnHand(int accountingStockOnHand) {
        this.accountingStockOnHand = accountingStockOnHand;
    }

    public int getAccountingCommittedStock() {
        return accountingCommittedStock;
    }

    public void setAccountingCommittedStock(int accountingCommittedStock) {
        this.accountingCommittedStock = accountingCommittedStock;
    }

    public int getAccountingAvailableForSale() {
        return accountingAvailableForSale;
    }

    public void setAccountingAvailableForSale(int accountingAvailableForSale) {
        this.accountingAvailableForSale = accountingAvailableForSale;
    }

    public int getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(int organizationID) {
        this.organizationID = organizationID;
    }
}
