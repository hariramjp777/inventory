package com.inventory.model.objects.order.sales;

import com.inventory.model.objects.order.Order;

import java.util.ArrayList;

public class SalesOrder {
    private int id;
    private int customerID;
    private int organizationID;
    private String status;
    private boolean isInvoiced;
    private float totalPrice;
    private ArrayList<Order> orders;
    private String salesOrderRefNumber;

    public SalesOrder(int customerID, int organizationID, String status, ArrayList<Order> orders, String salesOrderRefNumber) {
        this.customerID = customerID;
        this.organizationID = organizationID;
        this.status = status;
        this.orders = orders;
        this.salesOrderRefNumber = salesOrderRefNumber;
        this.isInvoiced = false;
        this.totalPrice = 0.0f;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
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

    public boolean isInvoiced() {
        return isInvoiced;
    }

    public void setInvoiced(boolean invoiced) {
        isInvoiced = invoiced;
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

    public String getSalesOrderRefNumber() {
        return salesOrderRefNumber;
    }

    public void setSalesOrderRefNumber(String salesOrderRefNumber) {
        this.salesOrderRefNumber = salesOrderRefNumber;
    }
}
