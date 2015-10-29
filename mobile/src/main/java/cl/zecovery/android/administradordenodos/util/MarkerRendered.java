package cl.zecovery.android.administradordenodos.util;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import cl.zecovery.android.administradordenodos.R;
import cl.zecovery.android.administradordenodos.node.NodeCluster;

/**
 * Created by fran on 27-10-15.
 */
public class MarkerRendered extends DefaultClusterRenderer<NodeCluster> {

    public MarkerRendered(Context context, GoogleMap map, ClusterManager<NodeCluster> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(NodeCluster node, MarkerOptions markerOptions) {
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_09_72px));
        markerOptions.title(node.getName(node));
        super.onBeforeClusterItemRendered(node, markerOptions);
    }
}
