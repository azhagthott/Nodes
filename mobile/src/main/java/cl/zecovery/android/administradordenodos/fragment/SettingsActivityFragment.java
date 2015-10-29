package cl.zecovery.android.administradordenodos.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import cl.zecovery.android.administradordenodos.R;

/**
 * Created by fran on 07-10-15.
 */
public class SettingsActivityFragment extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
