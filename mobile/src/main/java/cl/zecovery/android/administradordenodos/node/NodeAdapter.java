package cl.zecovery.android.administradordenodos.node;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cl.zecovery.android.administradordenodos.R;

/**
 * Created by fran on 13-09-15.
 */
public class NodeAdapter extends ArrayAdapter<Node> {

    public NodeAdapter(Context context, List<Node> nodes) {
        super(context, 0, nodes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Node node = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_node, parent, false);
        }

        TextView textViewPointName = (TextView) convertView.findViewById(R.id.textViewPointName);
        TextView textViewPointLatitude = (TextView) convertView.findViewById(R.id.textViewPointLatitude);
        TextView textViewPointLongitude = (TextView) convertView.findViewById(R.id.textViewPointLongitude);
        TextView textViewPointTemperature = (TextView) convertView.findViewById(R.id.textViewPointTemperature);

        textViewPointName.setText(node.name);
        textViewPointLatitude.setText("Latitude: " + String.valueOf(node.lat));
        textViewPointLongitude.setText("Longitude: " + String.valueOf(node.lng));
        textViewPointTemperature.setText(String.valueOf(node.temperature + "\u00b0"));

        return convertView;
    }
}
