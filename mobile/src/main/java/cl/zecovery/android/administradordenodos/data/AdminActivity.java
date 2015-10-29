package cl.zecovery.android.administradordenodos.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import cl.zecovery.android.administradordenodos.R;
import cl.zecovery.android.administradordenodos.activity.MainActivity;

public class AdminActivity extends AppCompatActivity {

    private static final String LOG_TAG = AdminActivity.class.getName();
    private Button buttonDeleteAllData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.red_800));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonDeleteAllData = (Button) findViewById(R.id.buttonDeleteAllData);
        buttonDeleteAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.deleteAllNode();
            }
        });
    }
}
