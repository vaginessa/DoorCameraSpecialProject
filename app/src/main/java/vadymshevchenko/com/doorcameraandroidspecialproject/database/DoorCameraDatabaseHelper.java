package vadymshevchenko.com.doorcameraandroidspecialproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DoorCameraDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "doorcamera";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "CAMERA";

    public DoorCameraDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "DATE INTEGER);");
            insertData(db, new java.util.Date().getTime());
        }
    }

    private static void insertData(SQLiteDatabase db, Long date) {
        ContentValues cv = new ContentValues();
        cv.put("DATE", date);
        db.insert(DoorCameraDatabaseHelper.TABLE_NAME, null, cv);
    }

}
