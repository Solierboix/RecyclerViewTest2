package com.example.solierboix.recyclerviewtest;

import java.util.ArrayList;

/**
 * Created by Solierboix on 30.04.2017.
 */

public class Contact {
    private String mName;
    private boolean mOnline;
    private String mCityThumb;

    public Contact(String name, boolean online, String cityImage) {
        mName = name;
        mOnline = online;
        mCityThumb = cityImage;
    }

    public String getName() {
        return mName;
    }

    public boolean isOnline() {
        return mOnline;
    }

    public String getmCityThumb(){
        return mCityThumb;
    }

    private static int lastContactId = 0;

    public static ArrayList<Contact> createContactsList(int numContacts) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Contact("Person " + ++lastContactId, i <= numContacts /2, "ImageUrl"));
        }

        return contacts;
    }

}