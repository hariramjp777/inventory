package com.inventory.model.objects.order.purchase;

import com.inventory.model.objects.order.Order;

import java.util.ArrayList;

public class PurchaseOrder {
    private int id;
    private int vendorID;
    private int organizationID;
    private String status;
    private boolean isBilled;
    private boolean isReceived;
    private float totalPrice;
    private ArrayList<Order> orders;
    private String purchaseOrderRefNumber;

    public PurchaseOrder(int vendorID, int organizationID, String status, ArrayList<Order> orders, String purchaseOrderRefNumber) {
        this.vendorID = vendorID;
        this.organizationID = organizationID;
        this.status = status;
        this.orders = orders;
        this.purchaseOrderRefNumber = purchaseOrderRefNumber;
        this.isBilled = false;
        this.totalPrice = 0.0f;
        this.isReceived = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVendorID() {
        return vendorID;
    }

    public void setVendorID(int vendorID) {
        this.vendorID = vendorID;
    }

    public int getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(int organizationID) {
        this.organizationID = organizationID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isBilled() {
        return isBilled;
    }

    public void setBilled(boolean billed) {
        isBilled = billed;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setReceived(boolean received) {
        isReceived = received;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public String getPurchaseOrderRefNumber() {
        return purchaseOrderRefNumber;
    }

    public void setPurchaseOrderRefNumber(String purchaseOrderRefNumber) {
        this.purchaseOrderRefNumber = purchaseOrderRefNumber;
    }
}
