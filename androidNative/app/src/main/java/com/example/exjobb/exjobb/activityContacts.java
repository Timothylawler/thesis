package com.example.exjobb.exjobb;

import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class activityContacts extends AppCompatActivity implements AdapterView.OnItemClickListener, android.app.LoaderManager.LoaderCallbacks<Cursor> {


    private ListView contactList;
    private SimpleCursorAdapter cursorAdapter;

    private final String[] FROM = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
    private final int[] TO = {R.id.contactListItemName};

    //  Used for when selecting contacts
    private long contactId;
    private String contactKey;
    private Uri contactUri;

    private final String SELECTION = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;
    //  Could use this if we want to search for a specific contact
    //private String searchString = "J";
    //private String[] selectionArgs = {searchString};

    //  Returned objects from contacts query
    private final String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        //  Get views
        contactList = (ListView)findViewById(R.id.contactListView);

        //  Create cursor for the list view
        cursorAdapter = new SimpleCursorAdapter(
                getApplicationContext(),
                R.layout.contact_list_item,
                null,
                FROM,
                TO,
                0
        );
        contactList.setOnItemClickListener(this);
        contactList.setAdapter(cursorAdapter);


        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Clicked: " + id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //  Make selection args into a pattern to search for
        //  If search is enabled then change first two null below to SELECTION and selectionArgs
        //selectionArgs[0] = "%" + searchString + "%";

        return new CursorLoader(
                getApplicationContext(),
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
        System.out.println("Data.");
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        //  clear cursor to free memory
        cursorAdapter.swapCursor(null);
    }
}
