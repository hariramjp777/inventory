package com.inventory.model.objects.customer;

public class Customer {
    private String contactName;
    private String companyName;
    private String customerType;
    private String country;
    private int organizationID;

    public Customer(String contactName, String companyName, String customerType, String country, int organizationID) {
        this.contactName = contactName;
        this.companyName = companyName;
        this.country = country;
        this.customerType = customerType;
        this.organizationID = organizationID;
    }

}
