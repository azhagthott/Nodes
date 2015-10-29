package cl.zecovery.android.administradordenodos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cl.zecovery.android.administradordenodos.R;
import cl.zecovery.android.administradordenodos.com.NodeDataRequest;

public class SplashScreenActivity extends Activity {

    private RelativeLayout relativeLayoutSplashScreen;
    private ProgressBar progressBar;
    private TextView textViewWorkingOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        relativeLayoutSplashScreen = (RelativeLayout) findViewById(R.id.relativeLayoutSplashScreen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textViewWorkingOffline = (TextView) findViewById(R.id.textViewWorkingOffline);

        progressBar.setVisibility(View.INVISIBLE);

        NodeDataRequest nodeDataRequest = new NodeDataRequest();
        if(nodeDataRequest.isNetworkAvailable(getApplicationContext())){
            progressBar.setVisibility(View.VISIBLE);
            textViewWorkingOffline.setText(getResources().getText(R.string.mode_online));
        }else {
            textViewWorkingOffline.setText(getResources().getText(R.string.mode_offline));
        }

        relativeLayoutSplashScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
