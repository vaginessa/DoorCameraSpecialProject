package vadymshevchenko.com.doorcameraandroidspecialproject;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class HistoryFragment extends ListFragment {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            SQLiteOpenHelper starbazzDatabaseHelper = new DoorCameraDatabaseHelper(getActivity());
            db = starbazzDatabaseHelper.getReadableDatabase();
            cursor = db.query(DoorCameraDatabaseHelper.TABLE_NAME,
                    new String[]{"_id", "DATE"},
                    null, null, null, null, "_id");
            if (cursor.getCount() == 0) {
                showDialog(inflater.getContext());
            }
            CursorAdapter listAdapter = new SimpleCursorAdapter(inflater.getContext(),
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
            setListAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(inflater.getContext(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }
    }

    private void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("No history data! Empty list!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getFragmentManager().popBackStack();
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
