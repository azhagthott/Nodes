package cl.zecovery.android.administradordenodos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import cl.zecovery.android.administradordenodos.R;

public class NodeDetailedScrollingActivity
        extends
        AppCompatActivity
        implements
        OnChartGestureListener,
        OnChartValueSelectedListener {

    private static final String LOG_TAG = NodeDetailedScrollingActivity.class.getName();

    private int nodeId;
    private String nodeName;
    private double nodeLatitude;
    private double nodeLongitude;
    private double nodeTemperature;
    private LineChart mChartTemperature;
    private LineChart mChartHumidity;
    private LineChart mChartCO2;

    private RelativeLayout relativeLayoutFragmentNodeDetailed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_detailed_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nodeId = getIntent().getIntExtra("node_id", 0);
        nodeName = getIntent().getStringExtra("node_name");
        nodeLatitude = getIntent().getDoubleExtra("node_latitude", 0);
        nodeLongitude = getIntent().getDoubleExtra("node_longitude", 0);
        nodeTemperature = getIntent().getDoubleExtra("node_temperature", 0);

        relativeLayoutFragmentNodeDetailed = (RelativeLayout) findViewById(R.id.relativeLayoutFragmentNodeDetailed);

        TextView textViewNodeName = (TextView) findViewById(R.id.textViewNodeName);
        TextView textViewNodeLatitude = (TextView) findViewById(R.id.textViewNodeLatitude);
        TextView textViewNodeLongitude = (TextView) findViewById(R.id.textViewNodeLongitude);
        TextView textViewNodeTemperature = (TextView) findViewById(R.id.textViewNodeTemperature);

        textViewNodeName.setText(nodeName);
        textViewNodeLatitude.setText(String.valueOf(nodeLatitude));
        textViewNodeLongitude.setText(String.valueOf(nodeLongitude));
        textViewNodeTemperature.setText(String.valueOf(nodeTemperature));

        /*
        * Chart definition
        *
        * */

        mChartTemperature = (LineChart) findViewById(R.id.mChartTemperature);
        mChartHumidity = (LineChart) findViewById(R.id.mChartHumidity);
        mChartCO2 = (LineChart) findViewById(R.id.mChartCO2);

        mChartTemperature.setOnChartGestureListener(this);
        mChartTemperature.setDrawGridBackground(false);
        mChartTemperature.setHighlightEnabled(true);
        mChartTemperature.setTouchEnabled(true);

        //Colors
        //mChartTemperature.setBackgroundColor(getResources().getColor(R.color.barChartBackgroundColor2));

        mChartTemperature.setBackgroundColor(getResources().getColor(R.color.barChartBackgroundColor2));
        mChartTemperature.setDrawBorders(false);
        mChartTemperature.getAxis(YAxis.AxisDependency.LEFT).setEnabled(false);
        mChartTemperature.getAxis(YAxis.AxisDependency.RIGHT).setEnabled(false);

        mChartTemperature.getXAxis().setEnabled(false);



        mChartTemperature.setDragEnabled(true);
        mChartTemperature.setScaleEnabled(true);
        mChartTemperature.setPinchZoom(true);

        YAxis yAxis = mChartTemperature.getAxisLeft();

        yAxis.setAxisLineColor(getResources().getColor(R.color.default_background));

        LimitLine ll = new LimitLine(21f,"Ideal Temperature");

        // Setting ideal temperature
        ll.setTextColor(getResources().getColor(R.color.barChartBackgroundColor2));
        ll.setLineColor(getResources().getColor(R.color.barChartBarColor));
        yAxis.addLimitLine(ll);








        mChartHumidity.setOnChartGestureListener(this);
        mChartHumidity.setDrawGridBackground(false);
        mChartHumidity.setHighlightEnabled(true);
        mChartHumidity.setTouchEnabled(true);

        //Colors
        mChartHumidity.setBackgroundColor(getResources().getColor(R.color.barChartBackgroundColor2));
        mChartHumidity.setDragEnabled(true);
        mChartHumidity.setScaleEnabled(true);
        mChartHumidity.setPinchZoom(true);
        mChartHumidity.getXAxis().setEnabled(false);


        mChartCO2.setOnChartGestureListener(this);
        mChartCO2.setDrawGridBackground(false);
        mChartCO2.setHighlightEnabled(true);
        mChartCO2.setTouchEnabled(true);

        //Colors
        mChartCO2.setBackgroundColor(getResources().getColor(R.color.barChartBackgroundColor2));
        mChartCO2.setDragEnabled(true);
        mChartCO2.setScaleEnabled(true);
        mChartCO2.setPinchZoom(true);


        setData(31, 80);

        mChartTemperature.animateX(2500, Easing.EasingOption.EaseInOutQuart);
        mChartHumidity.animateX(2500, Easing.EasingOption.EaseInOutQuart);
        mChartCO2.animateX(2500, Easing.EasingOption.EaseInOutQuart);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
    private void setData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {

            float mult = (range + 1);
            float val = (float) (Math.random() * mult) + 3;// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Temperature");

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(getResources().getColor(R.color.barChartBarColor));
        set1.setCircleColor(getResources().getColor(R.color.barChartBarColor));
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setValueTextColor(getResources().getColor(R.color.barChartBarColor));
        set1.setFillAlpha(75);
        set1.setFillColor(getResources().getColor(R.color.barChartBarHighLight));
        set1.setDrawFilled(true);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChartTemperature.setData(data);
        mChartHumidity.setData(data);
        mChartCO2.setData(data);

    }
}
