package vadymshevchenko.com.doorcameraandroidspecialproject;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class TopMainFragment extends Fragment implements View.OnClickListener {

    public static final String DISABLE_ENABLE_SERVICE = "DISABLE_ENABLE_SERVICE";
    private static final String TEXT_START_BUTTON = "Start service!";
    private SharedPreferences mSettings;
    private Button startService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            startService = (Button) getActivity().findViewById(R.id.start_service);
            Button stopService = (Button) getActivity().findViewById(R.id.stop_service);
            mSettings = this.getActivity().getSharedPreferences(SettingsFragment.APP_PREFERENCES, Context.MODE_PRIVATE);
            Button settingsButton = (Button) getActivity().findViewById(R.id.settings);
            Button historyButton = (Button) getActivity().findViewById(R.id.history);

            startService.setOnClickListener(this);
            stopService.setOnClickListener(this);

            settingsButton.setOnClickListener(this);
            historyButton.setOnClickListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSettings.contains(TEXT_START_BUTTON)) {
            startService.setText(mSettings.getString(TEXT_START_BUTTON, ""));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(TEXT_START_BUTTON, startService.getText().toString());
        editor.apply();
    }

    public void clickOnStartService(View view) {
        PackageManager pm = getActivity().getPackageManager();
        ComponentName componentName = new ComponentName(getActivity(), PowerReceiver.class);
        if(SettingsFragment.isRunBackground) {
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
        Toast.makeText(view.getContext(), "Application is working!", Toast.LENGTH_SHORT).show();
    }

    public void clickOnStopService(final View view) {
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
                        Toast.makeText(view.getContext(), "Application has stopped working!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.start_service:
                clickOnStartService(view);
                break;
            case R.id.stop_service:
                clickOnStopService(view);
                break;
            case R.id.settings:
                fragment = new SettingsFragment();
                replaceFragment(fragment);
                break;
            case R.id.history:
                fragment = new HistoryFragment();
                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, someFragment, "visible_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
