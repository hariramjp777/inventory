package com.model.objects.organization;

import com.model.objects.user.User;

public class Organization {
    private int organizationID;
    private String organizationName;
    private String industryType;
    private String country;

    public Organization(String organizationName, String industryType, String country) {
        this.organizationName = organizationName;
        this.industryType = industryType;
        this.country = country;
    }

    public Organization(int organizationID, String organizationName, String industryType, String country) {
        this.organizationID = organizationID;
        this.organizationName = organizationName;
        this.industryType = industryType;
        this.country = country;
    }
}
