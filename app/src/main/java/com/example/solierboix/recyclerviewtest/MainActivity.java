package com.example.solierboix.recyclerviewtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    ArrayList<Contact> contacts;
    Button addNewContact;
    EditText enterNameForNewContact;
    public static final String SORT_ORDER = "&sort=interestingness-desc";
    public String cityImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        // Initialize contacts
        contacts = Contact.createContactsList(0);
        // Create adapter passing in the sample user data
        final ContactsAdapter adapter = new ContactsAdapter(this, contacts);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvContacts.addItemDecoration(itemDecoration);
        adapter.setOnItemClickListener(new ContactsAdapter.ClickListener(){

            @Override
            public void onItemClick(int position, View v) {


                Toast.makeText(MainActivity.this, "onItemClick position: " + position, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);

                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                Toast.makeText(MainActivity.this, "onLongItemClick position: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                contacts.remove(position);
                adapter.notifyDataSetChanged();
            }
        };



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvContacts);



        // That's all

        enterNameForNewContact = (EditText) findViewById(R.id.EditTextName);
        addNewContact = (Button) findViewById(R.id.addOneContact);

        addNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=c9e0a18ce5541d03557ac34eb12c22b3&format=json&nojsoncallback=1" + SORT_ORDER + "&text=";
                String finalSearchCity = enterNameForNewContact.getText().toString() + " City";
                url += finalSearchCity;
                FlickrManager flcManager = new FlickrManager(MainActivity.this);
                flcManager.execute(url);
                flcManager.delegete = MainActivity.this;
                final String editTextName = enterNameForNewContact.getText().toString();
                if (editTextName.matches("")){
                    Toast.makeText(MainActivity.this, "Enter name first", Toast.LENGTH_SHORT).show();
                }else {
                    contacts.add(0, new Contact(editTextName, true, cityImageUrl));
                    adapter.notifyItemInserted(0);
                    enterNameForNewContact.getText().clear();
                }

            }

//            private void setImageForThumb() throws ExecutionException, InterruptedException {
//                String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=c9e0a18ce5541d03557ac34eb12c22b3&format=json&nojsoncallback=1" + SORT_ORDER + "&text=";
//                String finalSearchCity = enterNameForNewContact.getText().toString() + " City";
//                url += finalSearchCity;
//                FlickrManager flcManager = new FlickrManager(MainActivity.this);
//                flcManager.execute(url);
//                flcManager.delegete = MainActivity.this;
//            }
    });
}

    @Override
    public void processFinish(String output) {
        cityImageUrl = output;

    }


}
