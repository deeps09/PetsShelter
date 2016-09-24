package com.example.myapps.petsshelter.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Deepesh_Gupta1 on 08/31/2016.
 */
public class PetsContract implements BaseColumns {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private PetsContract() {
    };

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.myapps.petsshelter";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_PETS = "pets";

    public static class PetEntry {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;


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
