package com.example.myapps.petsshelter;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapps.petsshelter.data.PetsContract.PetEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    final static String LOG_TAG = MainActivity.class.getSimpleName();
    PetsCursorAdapter cursorAdapter;
    ListView petsListView;
    Menu menu1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cursorAdapter = new PetsCursorAdapter(this, null);
        petsListView = (ListView) findViewById(R.id.pets_list);
        petsListView.setAdapter(cursorAdapter);

        registerForContextMenu(petsListView);

        getLoaderManager().initLoader(1, null, this);

        //showDatabaseInfo();

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

        petsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Uri newUri = Uri.withAppendedPath(PetEntry.CONTENT_URI, "/" + String.valueOf(id));
                Intent intent = new Intent(getApplicationContext(), RegisterPet.class);
                intent.setData(newUri);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED
        };

        return new CursorLoader(this,
                PetEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

   /* @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.pets_list) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item_submit = (MenuItem) menu.findItem(R.id.action_submit);
        item_submit.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
            switch (id){
                case R.id.action_dummy_data:
                    inserDummyData();
                    break;
                case R.id.action_delete:
                    new AlertDialog.Builder(this)
                            .setTitle("Delete All")
                            .setMessage("This action cannot be undone..")
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteAll();
                        }
                    }).create().show();
            }
        return true;


        //return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //showDatabaseInfo();
    }

    private void inserDummyData (){

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Dummy Name");
        values.put(PetEntry.COLUMN_PET_BREED, "Dummy Breed");
        values.put(PetEntry.COLUMN_PET_GENDER, 2);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 5);

        getContentResolver().insert(PetEntry.CONTENT_URI, values);
    }

    private void deleteAll (){
        getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
        Toast.makeText(this, "All entries deleted !!", Toast.LENGTH_SHORT).show();
    }
}
