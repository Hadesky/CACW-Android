package com.hadesky.cacw.ui.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.hadesky.cacw.R;

/**
 *
 * Created by dzysg on 2016/3/23 0023.
 */
public class SettingFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName("setting");
        addPreferencesFromResource(R.xml.setting);

    }


    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)
    {

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
