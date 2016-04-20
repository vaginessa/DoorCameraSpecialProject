package vadymshevchenko.com.doorcameraandroidspecialproject;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.widget.Toast;

public class UpdateDataBaseService extends IntentService {

    public static final String TIME = "time";
    private Handler handler;

    public UpdateDataBaseService() {
        super("UpdateDataBaseService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long dataTime = intent.getLongExtra(TIME, 0);
        SQLiteOpenHelper sqLiteOpenHelper = new DoorCameraDatabaseHelper(UpdateDataBaseService.this);
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
            insertData(db, dataTime);
            db.close();
        }  catch (SQLiteException e) {
            showErrorMessage();
        }
    }

    private static void insertData(SQLiteDatabase db, Long date) {
        ContentValues cv = new ContentValues();
        cv.put("DATE", date);
        db.insert(DoorCameraDatabaseHelper.TABLE_NAME, null, cv);
    }

    private void showErrorMessage() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Database unavailable", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
