package cl.zecovery.android.administradordenodos.com;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cl.zecovery.android.administradordenodos.data.DatabaseHandler;
import cl.zecovery.android.administradordenodos.node.Node;
import cl.zecovery.android.administradordenodos.node.NodeAdapter;
import cl.zecovery.android.administradordenodos.util.Constants;

/**
 * Created by fran on 16-09-15.
 */
public class NodeDataRequest {

    private final String LOG_TAG = NodeDataRequest.class.getSimpleName();

    private JSONObject jsonObject;
    private Context mContext;

    private JSONObject contextElement;
    private JSONObject element;
    private JSONArray attributes;
    private JSONArray contextResponses;

    public NodeDataRequest() {
    }

    public NodeDataRequest(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void getLocalDataForList(NodeAdapter adapter, Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        adapter.addAll(db.getAllNodes());
        db.close();
    }

    public void getLocalDataForMap(Context context, GoogleMap mMap){
        DatabaseHandler db = new DatabaseHandler(context);
        db.getAllNodes();

        try {

            for(int i = 0; i < db.getAllNodes().size(); i++){
                Node node = db.getAllNodes().get(i);

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(node.getLat(), node.getLng()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .title(node.getName())
                );
            }
        db.close();

        }catch (Exception e){
            Log.d(LOG_TAG, "Exception :  " + e);
        }
    }

    public void getDataForListView(NodeAdapter adapter, Context context) {

        adapter.clear();

        DatabaseHandler db = new DatabaseHandler(context);

        try {
            contextResponses = jsonObject.getJSONArray("contextResponses");

            for (int i = 0; i < contextResponses.length(); i++){

                Node node = new Node();

                contextElement = contextResponses.getJSONObject(i);
                element = contextElement.getJSONObject("contextElement");
                attributes = element.getJSONArray("attributes");

                if (attributes.length() == Constants.ATTRIBUTE_LENGHT) {

                    JSONObject pointId = attributes.getJSONObject(0);
                    JSONObject pointName = attributes.getJSONObject(1);
                    JSONObject pointLatitude = attributes.getJSONObject(2);
                    JSONObject pointLongitude = attributes.getJSONObject(3);
                    JSONObject pointTemperature = attributes.getJSONObject(4);

                    int id = pointId.getInt("value");
                    String name = pointName.getString("value");
                    double lat = pointLatitude.getDouble("value");
                    double lng = pointLongitude.getDouble("value");
                    double temperature = pointTemperature.getDouble("value");

                    node.setId(id);
                    node.setName(name);
                    node.setLat(lat);
                    node.setLng(lng);
                    node.setTemperature(temperature);

                    if(!db.findNode(id)){
                        db.addNode(new Node(id, name, lat, lng, temperature));
                    }
                }else {
                    continue;
                }
                adapter.addAll(node);
            }
            db.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDataForMap(GoogleMap mMap) {

        try {
            contextResponses = jsonObject.getJSONArray("contextResponses");

            for (int i = 0; i < contextResponses.length(); i++){

                Node node = new Node();

                contextElement = contextResponses.getJSONObject(i);
                element = contextElement.getJSONObject("contextElement");
                attributes = element.getJSONArray("attributes");

                if (attributes.length() == Constants.ATTRIBUTE_LENGHT) {

                    JSONObject pointId = attributes.getJSONObject(0);
                    JSONObject pointName = attributes.getJSONObject(1);
                    JSONObject pointLatitude = attributes.getJSONObject(2);
                    JSONObject pointLongitude = attributes.getJSONObject(3);
                    JSONObject pointTemperature = attributes.getJSONObject(4);

                    int id = pointId.getInt("value");
                    String name = pointName.getString("value");
                    double lat = pointLatitude.getDouble("value");
                    double lng = pointLongitude.getDouble("value");
                    double temperature = pointTemperature.getDouble("value");

                    // Crea el Nodo
                    node.setId(id);
                    node.setName(name);
                    node.setLat(lat);
                    node.setLng(lng);
                    node.setTemperature(temperature);

                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(node.getLat(), node.getLng()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .title(node.getName())
                            .snippet(String.valueOf(node.getTemperature()))
                    );

                }else {
                    continue;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject makePostRequest(Node node){

        // Attributes array
        JSONArray jsonArrayAttributes = new JSONArray();
        // All body object
        JSONObject jsonObjectBody = new JSONObject();
        // Id Object
        JSONObject jsonObjectPointId = new JSONObject();
        // Name Object
        JSONObject jsonObjectPointName = new JSONObject();
        // Latitude Object
        JSONObject jsonObjectPointLatitude = new JSONObject();
        // Longitude Object
        JSONObject jsonObjectPointLongitude = new JSONObject();
        // Temperature Object
        JSONObject jsonObjectPointTemperature = new JSONObject();

        try{

            /*{
                "name":"id",
                "type":"string",
                "value":"429431529"
            },*/
            jsonObjectPointId.put("name", "id");
            jsonObjectPointId.put("type", "string");
            jsonObjectPointId.put("value", String.valueOf(node.getId()));

            /*{
                "name":"name",
                "type":"string",
                "value":"429431529"
            },*/
            jsonObjectPointName.put("name", "name");
            jsonObjectPointName.put("type", "string");
            jsonObjectPointName.put("value", "Point: "+String.valueOf(node.getId()));

            /*{
                "name":"lat",
                "type":"float",
                "value":"-33.505165558161806"
            },*/
            jsonObjectPointLatitude.put("name", "lat");
            jsonObjectPointLatitude.put("type", "float");
            jsonObjectPointLatitude.put("value", String.valueOf(node.getLat()));

            /*{
                "name":"lng",
                "type":"float",
                "value":"-70.64302694052458"
            },*/
            jsonObjectPointLongitude.put("name", "lng");
            jsonObjectPointLongitude.put("type", "float");
            jsonObjectPointLongitude.put("value", String.valueOf(node.getLng()));

            /*{
                "name":"temperature",
                "type":"float",
                "value":"0.0"
            }*/
            jsonObjectPointTemperature.put("name", "temperature");
            jsonObjectPointTemperature.put("type", "float");
            jsonObjectPointTemperature.put("value", String.valueOf(node.getTemperature()));


            /* "attributes":[
                {
                    "name":"id",
                    "type":"string",
                    "value":"429431529"
                },
                {
                    "name":"name",
                    "type":"string",
                    "value":"429431529"
                },
                {
                    "name":"lat",
                    "type":"float",
                    "value":"-33.505165558161806"
                },
                {
                    "name":"lng",
                    "type":"float",
                    "value":"-70.64302694052458"
                },
                {
                    "name":"temperature",
                    "type":"float",
                    "value":"0.0"
                }
            ]*/
            jsonArrayAttributes.put(0, jsonObjectPointId);
            jsonArrayAttributes.put(1, jsonObjectPointName);
            jsonArrayAttributes.put(2, jsonObjectPointLatitude);
            jsonArrayAttributes.put(3, jsonObjectPointLongitude);
            jsonArrayAttributes.put(4, jsonObjectPointTemperature);

            jsonObjectBody.put("id", String.valueOf(node.getId()));
            jsonObjectBody.put("type", "Point");
            jsonObjectBody.put("attributes", jsonArrayAttributes);

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObjectBody;
    }

    public String getResponseStatusCode(){

        String response="";
        try{
            contextResponses = jsonObject.getJSONArray("contextResponses");
            JSONObject statusCode = contextResponses.getJSONObject(1);
            JSONObject code = statusCode.getJSONObject("code");
            JSONObject reasonPhrase = statusCode.getJSONObject("reasonPhrase");
            response = code.toString()+ " : " + reasonPhrase.toString();

        }catch (Exception e){
            Log.d(LOG_TAG, "Exception" + e);
        }
        return response;
    }

    public boolean isNetworkAvailable (Context context){
        mContext = context;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
