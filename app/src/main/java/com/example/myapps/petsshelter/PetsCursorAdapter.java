package com.example.myapps.petsshelter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Deepesh_Gupta1 on 09/02/2016.
 */
public class PetsCursorAdapter extends CursorAdapter {

    public PetsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int genderid = cursor.getInt(RegisterPet.COLUMN_INDEX_GENDER);
        String gender = null;

        switch (genderid){
            case 0: gender = "Unknown";
                break;
            case 1: gender = "Male";
                break;
            case 2: gender = "Female";
                break;
        }

        viewHolder.name.setText(cursor.getString(RegisterPet.COLUMN_INDEX_NAME));
        viewHolder.breed.setText(cursor.getString(RegisterPet.COLUMN_INDEX_BREED));
        viewHolder.gender.setText(gender);
        viewHolder.weight.setText(cursor.getString(RegisterPet.COLUMN_INDEX_WEIGHT));
    }

    class ViewHolder {
        TextView name;
        TextView breed;
        TextView gender;
        TextView weight;

        ViewHolder(View view){

            name = (TextView) view.findViewById(R.id.name);
            breed = (   TextView) view.findViewById(R.id.breed);
            gender = (TextView) view.findViewById(R.id.gender);
            weight = (TextView) view.findViewById(R.id.weight);
        }


    }
}
