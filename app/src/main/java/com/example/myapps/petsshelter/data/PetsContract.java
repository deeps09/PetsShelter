package com.example.myapps.petsshelter.data;

import android.provider.BaseColumns;

/**
 * Created by Deepesh_Gupta1 on 08/31/2016.
 */
public class PetsContract implements BaseColumns {

    public static class PetsEntry {

        public static String TABLE_NAME = "pets";

        public static String _ID = "_id";

        public static String COLUMN_PET_NAME = "title";

        public static String COLUMN_PET_BREED = "breed";

        public static String COLUMN_PET_GENDER = "gender";

        public static String COLUMN_PET_WEIGHT = "weight";

        public static int GENDER_UNKNOWN = 0;
        public static int GENDER_MALE = 1;
        public static int GENDER_FEMALE = 2;
    }

}
