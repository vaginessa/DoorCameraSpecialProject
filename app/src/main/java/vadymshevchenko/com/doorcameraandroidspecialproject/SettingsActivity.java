package vadymshevchenko.com.doorcameraandroidspecialproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
        editor.putBoolean(APP_PREFERENCES_RUNBACKGROUND, isRunBackground);
        linkToWeb = editTextLinkToWeb.getText().toString();
        editor.putString(APP_PREFERENCES_LINKTOWEB, linkToWeb);
        editor.apply();
    }

    public void onSwitchClickedStartBackground(View view) {
        isRunBackground = ((Switch) view).isChecked();
    }

    public void clickOnSave(View view) {
        finish();
        Toast.makeText(SettingsActivity.this, "Settings save! You should stop/start application for changes take effect", Toast.LENGTH_LONG).show();
    }
}
