package com.inventory.model.objects.customer;

import com.inventory.model.objects.organization.Organization;

public class Customer {
    private int id;
    private String contactName;
    private String contactEmail;
    private String type;
    private String country;
    private int organizationID;
    private float receivables;
    private float unusedCredits;

    public Customer(String contactName, String contactEmail, String country, int organizationID) {
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.country = country;
        this.organizationID = organizationID;
        this.type = "";
        this.receivables = 0.0f;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getOrganizationID() {
        return this.organizationID;
    }

    public void setOrganizationID(int organizationID) {
        this.organizationID = organizationID;
    }

    public float getReceivables() {
        return receivables;
    }

    public void setReceivables(float receivables) {
        this.receivables = receivables;
    }

    public float getUnusedCredits() {
        return unusedCredits;
    }

    public void setUnusedCredits(float unusedCredits) {
        this.unusedCredits = unusedCredits;
    }
}
