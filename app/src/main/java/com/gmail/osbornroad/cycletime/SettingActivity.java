package com.gmail.osbornroad.cycletime;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class SettingActivity extends AppCompatActivity {

    public static final String SETTING_EMAIL_PREFERENCE = "settingEmailPreference";
    private EditText eMailSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        eMailSetting = (EditText) findViewById(R.id.e_mail_setting);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SETTING_EMAIL_PREFERENCE, 0);
        eMailSetting.setText(sharedPreferences.getString("settingEmailPreference", ""));
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SETTING_EMAIL_PREFERENCE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("settingEmailPreference", eMailSetting.getText().toString());
        editor.commit();
    }
}
