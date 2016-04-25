package vadymshevchenko.com.doorcameraandroidspecialproject;

import android.app.Application;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

public class BackgroundJobService extends IntentService {

    public static final String TIME = "time";
    private SharedPreferences mSettings;
    private Handler handler;

    public BackgroundJobService() {
        super("BackgroundJobService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long dataTime = intent.getLongExtra(TIME, 0);
        SQLiteOpenHelper sqLiteOpenHelper = new DoorCameraDatabaseHelper(BackgroundJobService.this);
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
            insertData(db, dataTime);
            db.close();
        }  catch (SQLiteException e) {
            showErrorMessage();
        }
        openBrowserLinkCamera();
    }

    private void openBrowserLinkCamera(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                String message = "Power is disconnected, someone is coming - run the browser";
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                mSettings = getApplicationContext().getSharedPreferences(SettingsActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
                String linkForOpen = "http://www.google.com";
                if (mSettings.contains(SettingsActivity.APP_PREFERENCES_LINKTOWEB)) {
                    linkForOpen = mSettings.getString(SettingsActivity.APP_PREFERENCES_LINKTOWEB, "");
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkForOpen));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(browserIntent);
            }
        });
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
