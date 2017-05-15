package com.tutorial;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by exjobb on 2017-04-11.
 */

public class ContactsProvider {

    private Uri QUERY_URI = ContactsContract.Contacts.CONTENT_URI;
    private String CONTACT_ID = ContactsContract.Contacts._ID;
    private String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;

    private List<Contact> contactList = new ArrayList<Contact>();

    String[] projection = new String[]{CONTACT_ID, DISPLAY_NAME};

    private ContentResolver contentResolver;

    public ContactsProvider(Context context){
        this.contentResolver = context.getContentResolver();
    }

    /**
     * Reads the contact id and name and append them as a Contact to contactList
     * @return  ContactList
     */
    public List<Contact> getContacts(){
        Cursor cursor = contentResolver.query(QUERY_URI, projection, null, null, null);

        while(cursor != null && cursor.moveToNext()){
            Contact contact = new Contact();
            contact.id = cursor.getInt(cursor.getColumnIndex(CONTACT_ID));
            contact.name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

            contactList.add(contact);
        }

        return contactList;
    }

    public class Contact{
        int id;
        String name;
    }
}
