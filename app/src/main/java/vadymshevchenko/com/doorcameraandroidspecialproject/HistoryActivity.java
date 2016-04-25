package vadymshevchenko.com.doorcameraandroidspecialproject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class HistoryActivity extends ListActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ListView listView = getListView();

        try {
            SQLiteOpenHelper starbazzDatabaseHelper = new DoorCameraDatabaseHelper(this);
            db = starbazzDatabaseHelper.getReadableDatabase();
            cursor = db.query(DoorCameraDatabaseHelper.TABLE_NAME,
                    new String[]{"_id", "DATE"},
                    null, null, null, null, "_id" + " DESC");
            if (cursor.getCount() == 0) {
                showDialog();
            }
            CursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"DATE"},
                    new int[]{android.R.id.text1},
                    0) {
                @Override
                public void setViewText(TextView v, String text) {
                    super.setViewText(v, convIntegerToStringDate(v, text));
                }
            };
            listView.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No history data! Empty list!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private String convIntegerToStringDate(TextView v, String text) {
        switch (v.getId()) {
            case android.R.id.text1:
                java.sql.Date dt = new java.sql.Date(Long.parseLong(text));
                DateFormat df = new SimpleDateFormat("dd MMMM yyyy HH:mm");
                return df.format(dt);
        }
        return text;
    }

}
