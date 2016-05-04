package vadymshevchenko.com.doorcameraandroidspecialproject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsFragment extends Fragment implements View.OnClickListener {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            mSettings = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            switchBackground = (Switch) getActivity().findViewById(R.id.switchBackground);
            editTextLinkToWeb = (EditText) getActivity().findViewById(R.id.editText);
            editTextLinkToWeb.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    if (!isValidUrl(s.toString())) {
                        String invalid = "Invalid Url";
                        editTextLinkToWeb.setError(invalid);
                    }
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
            Button save = (Button) getActivity().findViewById(R.id.save);
            Button deleteFromDB = (Button) getActivity().findViewById(R.id.delete);

            save.setOnClickListener(this);
            deleteFromDB.setOnClickListener(this);
        }
    }

    @Override
    public void onResume() {
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
    public void onPause() {
        super.onPause();
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        isRunBackground = switchBackground.isChecked();
        editor.putBoolean(APP_PREFERENCES_RUNBACKGROUND, isRunBackground);
        linkToWeb = editTextLinkToWeb.getText().toString();
        editor.putString(APP_PREFERENCES_LINKTOWEB, linkToWeb);
        editor.apply();
    }

    public void clickOnRemoveDB(final View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        clearDataBase(view);
                        Toast.makeText(view.getContext(), "DataBase successfully cleared!", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(view.getContext(), "Settings save! You should stop/start application for changes take effect", Toast.LENGTH_LONG).show();
    }

    private void clearDataBase(View view) {
        try {
            DoorCameraDatabaseHelper doorCameraDatabaseHelper = new DoorCameraDatabaseHelper(getActivity());
            SQLiteDatabase db = doorCameraDatabaseHelper.getWritableDatabase();
            db.delete(DoorCameraDatabaseHelper.TABLE_NAME, null, null);
            db.close();
        } catch (SQLiteException e) {
            Toast.makeText(view.getContext(), "Something went wrong with DataBase", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                clickOnSave(view);
                break;
            case R.id.delete:
                clickOnRemoveDB(view);
                break;
        }
    }

    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url);
        if(m.matches())
            return true;
        else
            return false;
    }
}
