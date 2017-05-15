package com.example.exjobb.exjobb;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LongList extends AppCompatActivity {

    private ListView longList;
    private LongListAdapter adapter;
    private Button editBtn, deleteBtn;
    private FloatingActionButton addNewFab;
    private ArrayList<Pair<String,String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_list);

        longList = (ListView) findViewById(R.id.longListList);
        addNewFab = (FloatingActionButton) findViewById(R.id.longListFab);
        editBtn = (Button) findViewById(R.id.longListListItemEdit);
        deleteBtn = (Button) findViewById(R.id.longListListItemDelete);


        setUpListeners();
        fillList();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /**
     * Add new "dummy entry" and scroll to bottom
     */
    private void setUpListeners() {
        addNewFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  add new item to the json array
                list.add(new Pair<String,String>("asd", "asd"));
                adapter.notifyDataSetChanged();
                scrollToBottom();
            }
        });
    }

    /*
     *  Scrolls the list to the bottom by "selecting the last element"
     */
    private void scrollToBottom() {
        longList.post(new Runnable() {
            @Override
            public void run() {
                longList.setSelection(adapter.getCount() - 1);
            }
        });
    }

    /*
     *  Scrolls the list to the 0'th element, should be the first.
     */
    private void scrollToTop(){
        longList.post(new Runnable() {
            @Override
            public void run() {
                longList.setSelection(0);
            }
        });
    }

    private void fillList() {
        try {
            String json = loadJson();
            if(json != null) {
                JSONArray data = new JSONArray(loadJson());
                list = new ArrayList<>();
                if (data != null) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = data.getJSONObject(i);
                        list.add(new Pair<String,String>(item.getString("firstName")
                                , item.getString("lastName")));
                    }
                    adapter = new LongListAdapter(getApplicationContext(), list);
                    longList.setAdapter(adapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJson(){
        String json = null;
        try {
            InputStream inStream = getApplicationContext().getAssets().open("longList.json");
            int size = inStream.available();
            byte[] buffer = new byte[size];
            inStream.read(buffer);
            inStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("long List", "error getting data");
            return null;
        }
        return json;
    }


}
