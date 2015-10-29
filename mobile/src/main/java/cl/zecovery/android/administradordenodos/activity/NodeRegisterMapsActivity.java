package cl.zecovery.android.administradordenodos.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONObject;

import java.util.Date;

import cl.zecovery.android.administradordenodos.R;
import cl.zecovery.android.administradordenodos.com.CustomJsonRequest;
import cl.zecovery.android.administradordenodos.com.NodeDataRequest;
import cl.zecovery.android.administradordenodos.data.DatabaseHandler;
import cl.zecovery.android.administradordenodos.node.Node;
import cl.zecovery.android.administradordenodos.node.NodeCluster;
import cl.zecovery.android.administradordenodos.util.Constants;

public class NodeRegisterMapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private static final String LOG_TAG = NodeRegisterMapsActivity.class.getName();

    private GoogleMap mMap;
    private ClusterManager<NodeCluster> mClusterManager;

    private LocationManager locationManager;
    private String provider;

    private LatLng mLocation;

    private CustomJsonRequest request;
    private NodeDataRequest nodeDataRequest;
    private Node node;

    private TextView textViewLat;
    private TextView textViewLng;

    private int nodeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_register_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        node = new Node();
        nodeDataRequest = new NodeDataRequest();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            Log.d(LOG_TAG, "Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            Log.d(LOG_TAG, "No provider has been selected.");
        }

        textViewLat = (TextView) findViewById(R.id.textViewLat);
        textViewLng = (TextView) findViewById(R.id.textViewLng);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final ClusterManager<NodeCluster> mClusterManager;
        mClusterManager = new ClusterManager<>(getApplicationContext(), mMap);
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        mMap.setMyLocationEnabled(true);
        mLocation = new LatLng(node.getLat(), node.getLng());

        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        nodeCount = db.getNodeCount();

        // UI Config
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        // Camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(node.getLat(), node.getLng()), 14.0f));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                textViewLat.setText("LAT: " + String.valueOf(latLng.latitude));
                textViewLng.setText("LNG: " + String.valueOf(latLng.longitude));
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {

                Date date = new Date();
                int unique = date.hashCode();

                node = new Node(unique, "Point: " + unique, latLng.latitude, latLng.longitude);

                final NodeDataRequest nodeDataRequest = new NodeDataRequest();
                final JSONObject jsonObject = nodeDataRequest.makePostRequest(node);

                nodeDataRequest.getResponseStatusCode();

                if (nodeDataRequest.isNetworkAvailable(getApplicationContext())) {

                    request = new CustomJsonRequest(
                            Request.Method.POST,
                            Constants.URL_POST_ONE_POINT + node.getId(),
                            jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObjectResponse) {

                                    String response = nodeDataRequest.getResponseStatusCode();
                                    Log.d(LOG_TAG, "response ::: " + response);

                                    Toast.makeText(getApplicationContext(), "Punto agregado", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(LOG_TAG, "VolleyError: " + error);
                        }
                    });

                    request.setPriority(Request.Priority.LOW);
                    Volley.newRequestQueue(getApplicationContext()).add(request);
                }

                db.addNode(node);
                db.close();

                textViewLat.setText("LAT: " + String.valueOf(latLng.latitude));
                textViewLng.setText("LNG: " + String.valueOf(latLng.longitude));

                mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                .title(Constants.NO_SAVED_NODE)
                );
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                //FIXME: WTF???
                return true;
            }
        });

        //TODO: add support fot clustering marker
        if (nodeDataRequest.isNetworkAvailable(getApplicationContext())){

            request = new CustomJsonRequest(
                    Request.Method.GET,
                    Constants.URL_GET_POINTS,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            nodeDataRequest = new NodeDataRequest(jsonObject);
                            nodeDataRequest.getDataForMap(mMap);
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(LOG_TAG, "ERROR:::: " + error);
                            error.printStackTrace();
                        }
                    });

            request.setPriority(Request.Priority.HIGH);
            Volley.newRequestQueue(getApplicationContext()).add(request);
        }else{
            nodeDataRequest.getLocalDataForMap(getApplicationContext(),mMap);
        }
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
