package vadymshevchenko.com.doorcameraandroidspecialproject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends Activity {

    public static boolean isRunBackground = true;
    public static String linkToWeb = "https://google.com.ua";

    // имя файла настройки
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_RUNBACKGROUND = "RUNBACKGROUND";
    public static final String APP_PREFERENCES_LINKTOWEB = "LINKTOWEB";
    private SharedPreferences mSettings;

    private Switch switchBackground;
    private EditText editTextLinkToWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_settings);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        switchBackground = (Switch) findViewById(R.id.switchBackground);
        editTextLinkToWeb = (EditText) findViewById(R.id.editText);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // load data from SharedPreferences
        if (mSettings.contains(APP_PREFERENCES_RUNBACKGROUND)) {
            isRunBackground = mSettings.getBoolean(APP_PREFERENCES_RUNBACKGROUND, true);
        }
        if (mSettings.contains(APP_PREFERENCES_LINKTOWEB)) {
            linkToWeb = mSettings.getString(APP_PREFERENCES_LINKTOWEB, "");
        }
        switchBackground.setChecked(isRunBackground);
        editTextLinkToWeb.setText(linkToWeb);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        isRunBackground = switchBackground.isChecked();
        editor.putBoolean(APP_PREFERENCES_RUNBACKGROUND, isRunBackground);
        linkToWeb = editTextLinkToWeb.getText().toString();
        editor.putString(APP_PREFERENCES_LINKTOWEB, linkToWeb);
        editor.apply();
    }

    public void clickOnRemoveDB(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        clearDataBase();
                        Toast.makeText(SettingsActivity.this, "DataBase successfully cleared!", Toast.LENGTH_SHORT).show();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Are you sure you want to clear history from database?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void clickOnSave(View view) {
        finish();
        Toast.makeText(SettingsActivity.this, "Settings save! You should stop/start application for changes take effect", Toast.LENGTH_LONG).show();
    }

    private void clearDataBase() {
        try {
            DoorCameraDatabaseHelper doorCameraDatabaseHelper = new DoorCameraDatabaseHelper(this);
            SQLiteDatabase db = doorCameraDatabaseHelper.getWritableDatabase();
            db.delete(DoorCameraDatabaseHelper.TABLE_NAME, null, null);
            db.close();
        } catch (SQLiteException e) {
            Toast.makeText(SettingsActivity.this, "Something went wrong with DataBase", Toast.LENGTH_SHORT).show();
        }
    }

}
