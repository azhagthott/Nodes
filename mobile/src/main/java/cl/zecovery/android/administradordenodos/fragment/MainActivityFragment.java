package cl.zecovery.android.administradordenodos.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import cl.zecovery.android.administradordenodos.R;
import cl.zecovery.android.administradordenodos.activity.NodeDetailedScrollingActivity;
import cl.zecovery.android.administradordenodos.com.CustomJsonRequest;
import cl.zecovery.android.administradordenodos.com.NodeDataRequest;
import cl.zecovery.android.administradordenodos.data.DatabaseHandler;
import cl.zecovery.android.administradordenodos.node.Node;
import cl.zecovery.android.administradordenodos.node.NodeAdapter;
import cl.zecovery.android.administradordenodos.util.Constants;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private NodeAdapter adapter;
    private CustomJsonRequest request;
    private NodeDataRequest nodeDataRequest;
    private ListView listViewNode;
    private DatabaseHandler db;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textViewNodeCount;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        textViewNodeCount = (TextView) rootView.findViewById(R.id.textViewNodeCount);

        db = new DatabaseHandler(getActivity().getApplicationContext());
        //textViewNodeCount = (TextView) rootView.findViewById(R.id.textViewNodeCount);
        nodeDataRequest = new NodeDataRequest();

        ArrayList<Node> arrayOfNode = new ArrayList<>();
        adapter = new NodeAdapter(getActivity().getApplicationContext(), arrayOfNode);

        listViewNode =(ListView) rootView.findViewById(R.id.listViewNode);
        listViewNode.setAdapter(adapter);

        listViewNode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Node nodeDetail = adapter.getItem(position);
                int nodeId = nodeDetail.getId();
                String nodeName = nodeDetail.getName();
                double nodeLatitude = nodeDetail.getLat();
                double nodeLongitude = nodeDetail.getLng();
                double nodeTemperature = nodeDetail.getTemperature();

                Intent intent = new Intent(getContext(), NodeDetailedScrollingActivity.class);

                intent.putExtra("node_id", nodeId);
                intent.putExtra("node_name", nodeName);
                intent.putExtra("node_latitude", nodeLatitude);
                intent.putExtra("node_longitude", nodeLongitude);
                intent.putExtra("node_temperature", nodeTemperature);

                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter.clear();
                if (nodeDataRequest.isNetworkAvailable(getActivity().getApplicationContext())) {

                    request = new CustomJsonRequest(
                            Request.Method.GET,
                            Constants.URL_GET_POINTS,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    nodeDataRequest = new NodeDataRequest(jsonObject);
                                    nodeDataRequest.getDataForListView(adapter, getActivity().getApplicationContext());
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(LOG_TAG, "ERROR: " + error);
                                    swipeRefreshLayout.setRefreshing(false);
                                    nodeDataRequest.getLocalDataForList(adapter, getActivity().getApplicationContext());
                                    error.printStackTrace();
                                }
                            });
                    request.setPriority(Request.Priority.LOW);
                    Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    nodeDataRequest.getLocalDataForList(adapter, getActivity().getApplicationContext());
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();

        //TODO: synchronize db with Orion

        textViewNodeCount.setText(String.valueOf(db.getNodeCount()));
        adapter.clear();

        if(nodeDataRequest.isNetworkAvailable(getActivity().getApplicationContext())){

            request = new CustomJsonRequest(
                    Request.Method.GET,
                    Constants.URL_GET_POINTS,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            nodeDataRequest = new NodeDataRequest(jsonObject);
                            nodeDataRequest.getDataForListView(adapter, getActivity().getApplicationContext());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(LOG_TAG, "ERROR: " + error);
                            swipeRefreshLayout.setRefreshing(false);
                            nodeDataRequest.getLocalDataForList(adapter, getActivity().getApplicationContext());
                            error.printStackTrace();
                        }
                    });
            request.setPriority(Request.Priority.LOW);
            Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
        }else{
            nodeDataRequest.getLocalDataForList(adapter, getActivity().getApplicationContext());
        }
    }
}