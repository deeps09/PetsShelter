package com.example.myapps.petsshelter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.myapps.petsshelter.data.PetsContract;
import com.example.myapps.petsshelter.data.PetsDBHelper;

public class MainActivity extends AppCompatActivity {

    ListView petsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        petsListView = (ListView) findViewById(R.id.pets_list);

        showDatabaseInfo();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Intent intent = new Intent(getApplicationContext(), RegisterPet.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
            /*switch (id){
                case R.id.action_insert: insertPet(mName, mBreed, mGender, Integer.getInteger(mWeight));
                    break;
                case R.id.action_delete: deletePet();
                    break;
                case R.id.action_submit: insertPet(mName, mBreed, mGender, Integer.getInteger(mWeight));
                    break;
            }*/

            return false;


        //return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showDatabaseInfo();
    }

    public void showDatabaseInfo(){

        PetsDBHelper dbHelper = new PetsDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(PetsContract.PetsEntry.TABLE_NAME, null, null, null, null, null, null);

        PetsCursorAdapter cursorAdapter = new PetsCursorAdapter(this, cursor);
        petsListView.setAdapter(cursorAdapter);
    }
}
