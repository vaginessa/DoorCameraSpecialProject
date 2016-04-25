package vadymshevchenko.com.doorcameraandroidspecialproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String DISABLE_ENABLE_SERVICE = "DISABLE_ENABLE_SERVICE";
    private static final String TEXT_START_BUTTON = "Start service!";
    private SharedPreferences mSettings;
    private Button startService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService = (Button) findViewById(R.id.start_service);
        mSettings = getSharedPreferences(SettingsActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSettings.contains(TEXT_START_BUTTON)) {
            startService.setText(mSettings.getString(TEXT_START_BUTTON, ""));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(TEXT_START_BUTTON, startService.getText().toString());
        editor.apply();
    }

    public void clickOnStartService(View view) {
        PackageManager pm = MainActivity.this.getPackageManager();
        ComponentName componentName = new ComponentName(MainActivity.this, PowerReceiver.class);
        if(SettingsActivity.isRunBackground) {
            Log.v("MainActivity", "Run background application");
            pm.setComponentEnabledSetting(componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        } else {
            Log.v("MainActivity", "Don't run background application");
            pm.setComponentEnabledSetting(componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(DISABLE_ENABLE_SERVICE, true);
        editor.putString(TEXT_START_BUTTON, getString(R.string.service_working));
        editor.apply();
        startService.setText(getString(R.string.service_working));
        Toast.makeText(getApplicationContext(), "Application is working!", Toast.LENGTH_SHORT).show();
    }

    public void clickOnStopService(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putBoolean(DISABLE_ENABLE_SERVICE, false);
                        editor.putString(TEXT_START_BUTTON, getString(R.string.start_service));
                        editor.apply();
                        startService.setText(getString(R.string.start_service));
                        Toast.makeText(getApplicationContext(), "Application has stopped working!", Toast.LENGTH_SHORT).show();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void clickOnSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void clickOnHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
}
