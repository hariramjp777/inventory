package com.inventory.model.objects.order.purchase;

import com.inventory.model.objects.order.Order;

public class PurchaseOrder {
    private int organizationID;
    private int vendorID;
    private String purchaseOrderRefNumber;
    private Order[] orders;

    public PurchaseOrder(int organizationID, int vendorID, Order[] orders, String purchaseOrderRefNumber) {
        this.organizationID = organizationID;
        this.vendorID = vendorID;
        this.purchaseOrderRefNumber = purchaseOrderRefNumber;
        this.orders = orders;
    }
}
