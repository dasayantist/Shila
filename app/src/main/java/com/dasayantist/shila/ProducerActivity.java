package com.dasayantist.shila;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.ListView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ProducerActivity extends AppCompatActivity {

    ListView list;
    ProducerListAdapter adapter;
    ArrayList<Producer> data;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_producer);
        list= (ListView) findViewById(R.id.producersList);
        data=new ArrayList<>();
        adapter=new ProducerListAdapter(this,data);
        list.setAdapter(adapter);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading producers list....");
        progressDialog.show();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("producers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item:dataSnapshot.getChildren()){
                    Map<String, Object> map= (Map<String, Object>) item.getValue();
                    Producer producer=new Producer(map.get("name").toString(),map.get("phone").toString(),map.get("location").toString(),map.get("area").toString());
                    data.add(producer);
                }
                adapter.notifyDataSetChanged();
                adapter.refresh(data);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    public void addContact(View view){
        Intent main = new Intent(ProducerActivity.this, AddProducerActivity.class);
        startActivity(main);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_search) {
            Intent main = new Intent(ProducerActivity.this, AddProducerActivity.class);
            startActivity(main);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_producers, menu);
        SearchView searchView =(SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("SEARCH", newText);
                adapter.filter(newText);
                return false;
            }
        });
        return  true;

    }
}