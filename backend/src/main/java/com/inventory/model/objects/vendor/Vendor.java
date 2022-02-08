package com.inventory.model.objects.vendor;

public class Vendor {
    private String contactName;
    private String companyName;
    private String country;
    private int organizationID;

    public Vendor(String contactName, String companyName, String country, int organizationID) {
        this.contactName = contactName;
        this.companyName = companyName;
        this.country = country;
        this.organizationID = organizationID;
    }
}
