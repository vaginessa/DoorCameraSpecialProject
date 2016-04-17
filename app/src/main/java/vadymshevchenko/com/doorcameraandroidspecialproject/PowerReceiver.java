package vadymshevchenko.com.doorcameraandroidspecialproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class PowerReceiver extends BroadcastReceiver {
    public PowerReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            String message = "Питание отсоединено, кто-то пришел, запускаем камеру";

            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        }
    }
}
