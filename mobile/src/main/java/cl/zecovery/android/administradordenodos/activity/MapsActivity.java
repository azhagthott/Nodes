package cl.zecovery.android.administradordenodos.activity;

import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.HashMap;

import cl.zecovery.android.administradordenodos.R;
import cl.zecovery.android.administradordenodos.data.DatabaseHandler;
import cl.zecovery.android.administradordenodos.node.Node;
import cl.zecovery.android.administradordenodos.node.NodeCluster;
import cl.zecovery.android.administradordenodos.util.MarkerRendered;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private static final String LOG_TAG = MapsActivity.class.getName();

    private GoogleMap mMap;

    private Node node;
    private SparseArray<Node> markers;

    private HashMap<Integer, Marker> visibleMarkers = new HashMap<Integer, Marker>();


    private String provider;
    private LatLng mLocation;
    private LocationManager locationManager;
    private ClusterManager<NodeCluster> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        node = new Node();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        Log.d(LOG_TAG, "location: " + location);

        if (location != null) {
            Log.d(LOG_TAG, "Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            Log.d(LOG_TAG, "No provider has been selected.");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mLocation = new LatLng(node.getLat(), node.getLng());

        mClusterManager = new ClusterManager<>(this, mMap);

        // UI Config
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        // Camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(node.getLat(), node.getLng()), 15.0f));

        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        addNodes();
    }

    private void addNodes() {

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        db.getAllNodes();


        if(this.mMap!=null){
            LatLngBounds bounds = this.mMap.getProjection().getVisibleRegion().latLngBounds;

            try {
                for (int i = 0; i < db.getAllNodes().size(); i++) {
                    Node node = db.getAllNodes().get(i);
                    NodeCluster nodeCluster = new NodeCluster(
                            node.getLat(),
                            node.getLng()
                    );

                    mClusterManager.setRenderer(new MarkerRendered(getApplicationContext(), mMap, mClusterManager));
                    mClusterManager.addItem(nodeCluster);
                    drawCircle(node);
                    //drawLine(node);
                }
                db.close();


            } catch (Exception e) {
                Log.d(LOG_TAG, "Exception :  " + e);
            }

        }


    }

    private void drawLine(Node node) {

        mMap.addPolyline(
                new PolylineOptions()
                        .add(new LatLng(node.getLat(), node.getLng()))
                        .add(new LatLng(mLocation.latitude, mLocation.longitude))
                        .geodesic(true)
                        .color(Color.argb(100, 255, 87, 34))
                        .width(3f)
        );
    }

    private void drawCircle(Node node){

        mMap.addCircle(
                new CircleOptions()
                        .center(new LatLng(node.getLat(), node.getLng()))
                        .radius(100)
                                //green
                                //.fillColor(Color.argb(50, 76, 175, 80))
                                //.strokeColor(Color.argb(100, 56, 142, 60))
                                //orange
                        .fillColor(Color.argb(50, 255, 87, 34))
                        .strokeColor(Color.argb(100, 255, 87, 34))
                        .strokeWidth(3.2f)
        );
    }

    @Override
    public void onLocationChanged(Location location) {
        node.setLat(location.getLatitude());
        node.setLng(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}