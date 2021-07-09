package com.atmaluhur.law.latihan8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Makanan extends AppCompatActivity {
private  String TAG = MainActivity.class.getSimpleName();
private ListView lv;
ArrayList<HashMap<String, String>> makananlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan);

        makananlist = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listviewmakanan);
        new AmbilDataMakanan().execute();
    }

private class AmbilDataMakanan extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Makanan.this,"Loading Data... ",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... argo) {

            HttpHandler sh = new HttpHandler();
            //Membuat Koneksi ke PHP
            String url = "http://10.0.2.2:8070/cafe/data_makanan.php";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Data Dari PHP : " + jsonStr);
            if (jsonStr !=null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray data_makanan = jsonObj.getJSONArray("makanan");

                    // looping through All Data Makanan
                    for (int i = 0; i < data_makanan.length(); i++) {
                        JSONObject c = data_makanan.getJSONObject(i);
                        //memasukan string ke HashMap
                        HashMap<String, String> detail_makanan = new HashMap<>();
                        detail_makanan.put("id", c.getString("id"));
                        detail_makanan.put("nama_menu", c.getString("nama_menu"));
                        detail_makanan.put("harga", c.getString("harga"));
                        detail_makanan.put("keterangan", c.getString("keterangan"));

                        // adding data_makanan to detail_makanan list
                        makananlist.add(detail_makanan);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Gagal Kirim Data: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Gagal Kirim Data: " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }else{
                Toast.makeText(getApplicationContext(),"Data Kosong", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(Makanan.this, makananlist,
                    R.layout.list_item_makanan, new  String[]{ "nama_menu","harga","keterangan"},
                    new int[]{R.id.txtnamamakanan, R.id.txtharga, R.id.txtketerangan});
            lv.setAdapter(adapter);
        }
    }
}