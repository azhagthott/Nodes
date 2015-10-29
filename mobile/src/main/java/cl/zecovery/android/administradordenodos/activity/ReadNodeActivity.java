package cl.zecovery.android.administradordenodos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.Result;

import cl.zecovery.android.administradordenodos.R;
import cl.zecovery.android.administradordenodos.node.Node;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ReadNodeActivity extends AppCompatActivity implements  ZXingScannerView.ResultHandler{

    private static final String LOG_TAG = ReadNodeActivity.class.getName();
    private ZXingScannerView qrScanner;
    private TextView textViewQrResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_node);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewQrResult = (TextView) findViewById(R.id.textViewQrResult);


        qrScanner = (ZXingScannerView) findViewById(R.id.qrScanner);

    }
    @Override
    public void onResume() {
        super.onResume();
        qrScanner.setResultHandler(this);
        qrScanner.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        qrScanner.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        try {

            if(result.getText()!= null){
                Node node = new Node();
                node.setName(result.getText());
                textViewQrResult.setText(result.getText());
            }

        }catch (Exception e){
            Log.v(LOG_TAG, e.toString());
        }
    }
}
