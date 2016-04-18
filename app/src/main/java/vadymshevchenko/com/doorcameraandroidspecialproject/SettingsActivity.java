package vadymshevchenko.com.doorcameraandroidspecialproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

public class SettingsActivity extends Activity {

    private boolean isStartAtBoot;
    private boolean isRunBackground;
    private String linkToWeb;
    private static final String DEFLINK = "https://google.com.ua";

    private Switch switchStartAtBoot;
    private Switch switchBackground;
    private EditText editTextLinkToWeb;

    // имя файла настройки
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_STARTBOOT = "STARTBOOT";
    public static final String APP_PREFERENCES_RUNBACKGROUND = "RUNBACKGROUND";
    public static final String APP_PREFERENCES_LINKTOWEB = "LINKTOWEB";
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        switchStartAtBoot = (Switch) findViewById(R.id.switchStart);
        switchBackground = (Switch) findViewById(R.id.switchBackground);
        editTextLinkToWeb = (EditText) findViewById(R.id.editText);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mSettings.contains(APP_PREFERENCES_STARTBOOT)) {
            // Получаем число из настроек
            isStartAtBoot = mSettings.getBoolean(APP_PREFERENCES_STARTBOOT, true);
            Log.v("onresume", "load - " + isStartAtBoot);
            // Выводим на экран данные из настроек
            switchStartAtBoot.setChecked(isStartAtBoot);
        }
        if (mSettings.contains(APP_PREFERENCES_RUNBACKGROUND)) {
            isRunBackground = mSettings.getBoolean(APP_PREFERENCES_RUNBACKGROUND, true);
            Log.v("onresume", "load - " + isRunBackground);
            switchBackground.setChecked(isRunBackground);
        }
        if (mSettings.contains(APP_PREFERENCES_LINKTOWEB)) {
            linkToWeb = mSettings.getString(APP_PREFERENCES_LINKTOWEB, DEFLINK);
            editTextLinkToWeb.setText(linkToWeb);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_STARTBOOT, isStartAtBoot);
        editor.putBoolean(APP_PREFERENCES_RUNBACKGROUND, isRunBackground);
        linkToWeb = editTextLinkToWeb.getText().toString();
        editor.putString(APP_PREFERENCES_LINKTOWEB, linkToWeb);
        editor.apply();
    }

    public void clickOnSave(View view) {
        finish();
    }

    public void onSwitchClickedStartBoot(View view) {
        isStartAtBoot = ((Switch) view).isChecked();
    }

    public void onSwitchClickedStartBackground(View view) {
        isRunBackground = ((Switch) view).isChecked();
    }
}
