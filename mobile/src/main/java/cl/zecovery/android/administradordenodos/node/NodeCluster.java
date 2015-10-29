package cl.zecovery.android.administradordenodos.node;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;


/**
 * Created by fran on 14-09-15.
 */

public class NodeCluster extends Node implements ClusterItem {
    private final LatLng mPosition;

    public NodeCluster(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getName(Node node){
        return node.getName();
    }
}
