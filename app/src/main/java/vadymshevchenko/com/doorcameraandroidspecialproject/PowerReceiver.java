package vadymshevchenko.com.doorcameraandroidspecialproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;

public class PowerReceiver extends BroadcastReceiver {

    private SharedPreferences mSettings;

    public PowerReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("PowerReceiver", "Get the receiver message!");
        mSettings = context.getSharedPreferences(SettingsFragment.APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean isServiceWorking = false;
        if (mSettings.contains(TopMainFragment.DISABLE_ENABLE_SERVICE)) {
            isServiceWorking = mSettings.getBoolean(TopMainFragment.DISABLE_ENABLE_SERVICE, true);
        }
        if (isServiceWorking && intent.getAction().equalsIgnoreCase("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            Log.v("PowerReceiver", "Start service with different jobs!");
            Intent service = new Intent(context, BackgroundJobService.class);
            service.putExtra(BackgroundJobService.TIME, new Date().getTime());
            context.startService(service);
        }
    }
}
