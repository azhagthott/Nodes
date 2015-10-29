package cl.zecovery.android.administradordenodos.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cl.zecovery.android.administradordenodos.fragment.SettingsActivityFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        super.onPostCreate(savedInstanceState);
        getFragmentManager().
                beginTransaction().
                replace(android.R.id.content, new SettingsActivityFragment()).
                        commit();
    }
}
