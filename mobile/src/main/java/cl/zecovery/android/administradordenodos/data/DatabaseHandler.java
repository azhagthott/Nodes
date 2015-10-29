package cl.zecovery.android.administradordenodos.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cl.zecovery.android.administradordenodos.node.Node;
import cl.zecovery.android.administradordenodos.node.NodeCrud;
import cl.zecovery.android.administradordenodos.util.Constants;

/**
 * Created by fran on 15-09-15.
 */
public class DatabaseHandler extends SQLiteOpenHelper implements NodeCrud{

    private final String LOG_TAG = DatabaseHandler.class.getSimpleName();

    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_NODES =
                "CREATE TABLE " + Constants.TABLE_NODES
                        + "("
                        + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                        + Constants.KEY_NAME + " TEXT,"
                        + Constants.KEY_LAT + " TEXT,"
                        + Constants.KEY_LNG + " TEXT,"
                        + Constants.KEY_TEMPERATURE + " TEXT"
                        + ");";

        Log.d(LOG_TAG, "data base name: " + CREATE_TABLE_NODES);

        db.execSQL(CREATE_TABLE_NODES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(Constants.DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void addNode(Node node) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();

            values.put(Constants.KEY_ID, String.valueOf(node.getId()));
            values.put(Constants.KEY_NAME, node.getName());
            values.put(Constants.KEY_LAT, node.getLat());
            values.put(Constants.KEY_LNG, node.getLng());
            values.put(Constants.KEY_TEMPERATURE, node.getTemperature());

            db.insert(Constants.TABLE_NODES, null, values);
            db.close();

        }catch (Exception e){
            Log.d(LOG_TAG, "Exception: " + e);
        }
    }

    @Override
    public Node getNode(int nodeId) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                Constants.TABLE_NODES,

                new String[] {
                        Constants.KEY_NAME,
                        Constants.KEY_LAT,
                        Constants.KEY_LNG,
                        Constants.KEY_TEMPERATURE,
                    },
                Constants.KEY_ID + "=?",

                new String[] {String.valueOf(nodeId)},
                null,
                null,
                null
        );

        if(cursor!=null)
            cursor.moveToFirst();

        return new Node(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getDouble(2),
                cursor.getDouble(3),
                cursor.getDouble(4)
        );
    }

    @Override
    public List<Node> getAllNodes() {

        List<Node> nodeList = new ArrayList<>();

        String selectAllQuery = Constants.SELECT_ALL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectAllQuery, null);

        if(cursor.moveToFirst()){
            do {
                Node node = new Node();
                node.setId(Integer.parseInt(cursor.getString(0)));
                node.setName(cursor.getString(1));
                node.setLat(Double.parseDouble(cursor.getString(2)));
                node.setLng(Double.parseDouble(cursor.getString(3)));
                node.setTemperature(Double.parseDouble(cursor.getString(4)));
                nodeList.add(node);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return nodeList;
    }


    @Override
    public int getNodeCount() {

        String countQuery = Constants.SELECT_ALL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int i = cursor.getCount();
        cursor.close();

        return i;
    }


    @Override
    public int updateNode(Node node) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.KEY_NAME, node.getName());
        values.put(Constants.KEY_LAT, node.getLat());
        values.put(Constants.KEY_LNG, node.getLng());
        values.put(Constants.KEY_TEMPERATURE, node.getTemperature());

        int i = db.update(Constants.TABLE_NODES, values, Constants.KEY_ID + "=?", new String[]{
                String.valueOf(node.getId())
        });

        return i;
    }

    @Override
    public void deleteNode(Node node) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NODES, Constants.KEY_ID + "=?", new String[]{
           String.valueOf(node.getId())
        });
        db.close();
    }

    @Override
    public void deleteAllNode() {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteAll = Constants.DELETE_ALL;
        db.execSQL(deleteAll);
        db.close();
    }

    @Override
    public boolean findNode(int nodeId) {

        String whereQuery = Constants.SELECT_ALL_WHERE_NODEID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(whereQuery, new String[]{String.valueOf(nodeId)});
        if(cursor.getCount()==1){
            return true;
        }else{
            return false;
        }
    }
}
