package com.tutorial;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by exjobb on 2017-04-11.
 */

//  TODO: I should really create a class to extend for when creating ui components.

public class ContactsView implements AdapterView.OnItemClickListener{

    //  Listview to show the contacts
    private ListView listView;
    private ViewGroup viewGroup;

    //  Contacts provider
    ContactsProvider contactsProvider;

    private LinearLayout mLayout;


    private Context context;

    public ContactsView(Context context) {
        this.context = context;
        this.contactsProvider = new ContactsProvider(context);
        this.viewGroup = createScreen();
    }

    /**
     * Creates the main ViewGroup for the contacts scene
     *  Creates a layout by calling createLayout which is filled with a listview.
     * @return  ViewGroup containing all views for the contacts scene
     */
    private ViewGroup createScreen() {
        ViewGroup layout = createLayout(context);
        listView = setUpListView();
        layout.addView(listView);
        return layout;
    }

    private ViewGroup createLayout(Context context){
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.mLayout = ll;
        return ll;
    }



    /**
     * Create Listview instance to hold the contacts and assign the cursor adapter as the adapter
     * @return ListView with layoutparams set to MATCH_PARENT x 2
     */
    private ListView setUpListView(){
        ListView lv = new ListView(context);
        lv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //  Get nasty and seathe the first name instead of creating a custom adapter.
        List<String> data = new ArrayList<>();
        List<ContactsProvider.Contact> temp = contactsProvider.getContacts();
        for(int i = 0; i< temp.size(); i++){
            data.add(temp.get(i).name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, data);

        lv.setAdapter(adapter);
        return lv;
    }



    public ViewGroup getViewGroup(){
        return viewGroup;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //  Do stuff when pressing a row
    }

}
