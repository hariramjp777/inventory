package com.inventory.model.objects.order.sales;

import com.inventory.model.objects.order.Order;

public class SalesOrder {
    private int organizationID;
    private int customerID;
    private String salesOrderRefNumber;
    private Order[] orders;

    public SalesOrder(int organizationID, int customerID, Order[] orders, String salesOrderRefNumber) {
        this.organizationID = organizationID;
        this.customerID = customerID;
        this.orders = orders;
        this.salesOrderRefNumber = salesOrderRefNumber;
    }
}
