package com.inventory.model.objects.vendor;

public class Vendor {
    private int id;
    private String contactName;
    private String contactEmail;
    private String companyName;
    private String country;
    private int organizationID;
    private float payables;
    private float unusedCredits;

    public Vendor(String contactName, String contactEmail, String companyName, String country, int organizationID) {
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.companyName = companyName;
        this.country = country;
        this.organizationID = organizationID;
        this.payables = 0.0f;
        this.unusedCredits = 0.0f;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(int organizationID) {
        this.organizationID = organizationID;
    }

    public float getPayables() {
        return payables;
    }

    public void setPayables(float payables) {
        this.payables = payables;
    }

    public float getUnusedCredits() {
        return unusedCredits;
    }

    public void setUnusedCredits(float unusedCredits) {
        this.unusedCredits = unusedCredits;
    }
}
