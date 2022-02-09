package com.inventory.model.objects.organization;

import com.inventory.model.objects.user.User;

public class Organization {
    private int id;
    private User user;
    private String name;
    private String industry = "";
    private String businessLocation;
    private String organizationCreatedDate;
    private String logoURL = "";
    private boolean isOrganizationActive = true;
    private int fiscalYearStartMonth;

    public Organization(User user, String name, String businessLocation, int fiscalYearStartMonth) {
        this.user = user;
        this.name = name;
        this.businessLocation = businessLocation;
        this.fiscalYearStartMonth = fiscalYearStartMonth;
    }

    public Organization(int id, String name, String industry, String businessLocation, String organizationCreatedDate, boolean isOrganizationActive, String logoURL, int fiscalYearStartMonth) {
        this.id = id;
        this.name = name;
        this.industry = industry;
        this.businessLocation = businessLocation;
        this.organizationCreatedDate = organizationCreatedDate;
        this.isOrganizationActive = isOrganizationActive;
        this.logoURL = logoURL;
        this.fiscalYearStartMonth = fiscalYearStartMonth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(String businessLocation) {
        this.businessLocation = businessLocation;
    }

    public String getOrganizationCreatedDate() {
        return organizationCreatedDate;
    }

    public void setOrganizationCreatedDate(String organizationCreatedDate) {
        this.organizationCreatedDate = organizationCreatedDate;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public boolean isOrganizationActive() {
        return isOrganizationActive;
    }

    public void setOrganizationActive(boolean organizationActive) {
        isOrganizationActive = organizationActive;
    }

    public int getFiscalYearStartMonth() {
        return fiscalYearStartMonth;
    }

    public void setFiscalYearStartMonth(int fiscalYearStartMonth) {
        this.fiscalYearStartMonth = fiscalYearStartMonth;
    }
}
