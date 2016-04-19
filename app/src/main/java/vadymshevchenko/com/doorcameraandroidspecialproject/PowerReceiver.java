package vadymshevchenko.com.doorcameraandroidspecialproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

public class PowerReceiver extends BroadcastReceiver {

    private SharedPreferences mSettings;

    public PowerReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Если не будет работать, значит метод отыгрывает больше чем 5 сек, и я должен
        // запускать сервис, который будет выполянуть эту работу

        boolean isServiceWorking = false;
        if (mSettings.contains(MainActivity.DISABLE_ENABLE_SERVICE)) {
            isServiceWorking = mSettings.getBoolean(MainActivity.DISABLE_ENABLE_SERVICE, true);
        }
        if (isServiceWorking && intent.getAction().equalsIgnoreCase("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            String message = "Power is disconnected, someone is coming - run the browser";

            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            String linkForOpen = "http://www.google.com";
            if (mSettings.contains(SettingsActivity.APP_PREFERENCES_LINKTOWEB)) {
                linkForOpen = mSettings.getString(SettingsActivity.APP_PREFERENCES_LINKTOWEB, "");
            }
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkForOpen));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        }
    }
}
