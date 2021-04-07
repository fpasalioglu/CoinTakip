package com.example.coinyeni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.coinyeni.interfaces.CustomItemClickListener;
import com.example.coinyeni.models.Coin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private RecyclerView lv;
    private TextView anlikbtc, portfoliobtc, portfolioTl, zarardurumtext;
    private Database db;
    private ArrayList<HashMap<String, String>> coin_liste = new ArrayList<>();
    private final ArrayList<Coin> coinler = new ArrayList<>();
    private CoinListAdapter adapter;
    public static SharedPreferences sharedPref;
    private String anlikBTC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new Database(this);

        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);

        anlikbtc = (TextView)findViewById(R.id.anlikbtc);
        portfoliobtc = (TextView)findViewById(R.id.portBtc);
        portfolioTl = (TextView)findViewById(R.id.portTl);
        zarardurumtext = (TextView)findViewById(R.id.zarardurumtextview);

        lv = (RecyclerView)findViewById(R.id.list_view);

        Thread background = new Thread(new Runnable() {
            public void run() {
                veriCek();
            }
        });
        background.start();
    }

    private void veriCek(){
        coin_liste.clear();
        coinler.clear();

        try {
            JSONObject a = Jsonparse.readJsonFromUrl("https://api.binance.com/api/v1/ticker/24hr?symbol=BTCTRY");
            String[] separated = a.getString("lastPrice").split("\\.");
            anlikBTC = separated[0];
            anlikbtc.setText(anlikBTC + " TL");
        }catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        coin_liste = db.coinler();
        if (!coin_liste.isEmpty()){
            for (int i = 0; i<coin_liste.size(); i++){
                try {
                    JSONObject a = Jsonparse.readJsonFromUrl("https://api.binance.com/api/v1/ticker/24hr?symbol=" + coin_liste.get(i).get("coin_adi") + coin_liste.get(i).get("tip"));
                    String price = a.getString("lastPrice");
                    coinler.add(new Coin(coin_liste.get(i).get("coin_adi"), coin_liste.get(i).get("miktar"), coin_liste.get(i).get("fiyat"), coin_liste.get(i).get("tip"), price));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            adapter = new CoinListAdapter(coinler, new CustomItemClickListener(){
                @Override
                public void onItemClick(View v, int position) {
                    Intent intent = new Intent(getApplicationContext(), CoinDetay.class);
                    intent.putExtra("id", Integer.valueOf(coin_liste.get(position).get("id")));
                    startActivity(intent);
                }
            });

            float savedBTC = sharedPref.getFloat("miktar",0);
            float savedTL = sharedPref.getFloat("TLmiktar",0);
            float zarardurum = 0;
            float portBtc = 0;
            for (int i = 0; i<coinler.size();i++){
                if (coinler.get(i).getTip().equals("BTC")) {
                    portBtc += coinler.get(i).getDeger();
                }else {
                    portBtc += (coinler.get(i).getDeger()/Float.parseFloat(anlikBTC));
                }

                if (coinler.get(i).getTip().equals("BTC")) {
                    zarardurum += coinler.get(i).getKar();
                }else {
                    zarardurum += (coinler.get(i).getKar()/Float.parseFloat(anlikBTC));
                }
            }

            float zarartl = zarardurum*Float.parseFloat(anlikBTC);
            zarardurumtext.setText(zarartl+" TL");
            portBtc+=savedBTC;
            portfoliobtc.setText(portBtc+" BTC");
            float portTl = portBtc*Float.parseFloat(anlikBTC);
            portTl+=savedTL;
            portfolioTl.setText(portTl+" TL");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lv.setHasFixedSize(true);
                    lv.setAdapter(adapter);
                    lv.setItemAnimator(new DefaultItemAnimator());
                    lv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ekle:
                startActivity(new Intent(MainActivity.this, CoinEkle.class));
                return true;
            case R.id.yenile:
                veriCek();
                return true;
            case R.id.ayarlar:
                startActivity(new Intent(MainActivity.this, Ayarlar.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}