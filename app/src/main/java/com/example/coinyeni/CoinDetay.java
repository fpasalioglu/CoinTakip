package com.example.coinyeni;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class CoinDetay extends AppCompatActivity {
    Button b1,b2;
    TextView t1,t2,t3,t4,t5,hacimView, karzararView, yuzdeView,tlView, enyuksek, endusuk;
    int id;

    TextView tip1,tip2,tip3,tip4,tip5,tip6,tip7;
    LinearLayout tllineer;

    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coindetay);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Coin Bilgileri");

        b1 = (Button)findViewById(R.id.button1);
        b2 = (Button)findViewById(R.id.button2);

        t1 = (TextView)findViewById(R.id.adi);
        t2 = (TextView)findViewById(R.id.miktar);
        t3 = (TextView)findViewById(R.id.alisfiyati);
        t4 = (TextView)findViewById(R.id.fiyati);
        t5 = (TextView)findViewById(R.id.degeri);

        endusuk = (TextView)findViewById(R.id.endusuk);
        enyuksek = (TextView)findViewById(R.id.enyuksek);

        tip1 = (TextView)findViewById(R.id.text1);
        tip2 = (TextView)findViewById(R.id.text2);
        tip3 = (TextView)findViewById(R.id.text3);
        tip4 = (TextView)findViewById(R.id.text4);
        tip5 = (TextView)findViewById(R.id.text5);
        tip6 = (TextView)findViewById(R.id.text22);
        tip7 = (TextView)findViewById(R.id.text33);

        hacimView = (TextView)findViewById(R.id.hacim);
        karzararView = (TextView)findViewById(R.id.karzarar);
        yuzdeView = (TextView)findViewById(R.id.yuzde);
        tlView = (TextView)findViewById(R.id.tldeg);

        tllineer = (LinearLayout)findViewById(R.id.tllinear);

        Intent intent=getIntent();
        id = intent.getIntExtra("id", 0);

        Database db = new Database(getApplicationContext());
        HashMap<String, String> coin = db.coinDetay(id);

        t1.setText(coin.get("coin_adi"));
        t2.setText(coin.get("miktar"));
        t3.setText(coin.get("fiyat"));

        String tip = coin.get("tip");
        tip1.setText(tip);
        tip2.setText(tip);
        tip3.setText(tip);
        tip4.setText(tip);
        tip5.setText(tip);
        tip6.setText(tip);
        tip7.setText(tip);

        float maliyet = Float.parseFloat(coin.get("fiyat")) * Float.parseFloat(coin.get("miktar"));

        try {
            JSONObject a = Jsonparse.readJsonFromUrl("https://api.binance.com/api/v1/ticker/24hr?symbol="+coin.get("coin_adi")+tip);
            String price = a.getString("lastPrice");
            String endusukprice = a.getString("lowPrice");
            String enyuksekprice = a.getString("highPrice");
            float deger = Float.parseFloat(coin.get("miktar")) * Float.parseFloat(price);
            float zarar = deger - maliyet;

            if (tip.equals("BTC")) {
                JSONObject b = Jsonparse.readJsonFromUrl("https://api.binance.com/api/v1/ticker/24hr?symbol=BTCTRY");
                String fiyat = b.getString("lastPrice");
                float tldeger = zarar * Float.parseFloat(fiyat);
                tlView.setText(String.format("%.2f", tldeger));
            }else
                tllineer.setVisibility(View.GONE);

            float yuzde = (zarar * 100)/maliyet;

            yuzdeView.setText(String.format("%.10f", yuzde));
            hacimView.setText(a.getString("quoteVolume"));
            t4.setText(price);

            if (tip.equals("TRY")){
                t5.setText(String.format("%.2f", deger));
                karzararView.setText(String.format("%.2f",zarar));
            }else {
                t5.setText(String.format("%.10f", deger));
                karzararView.setText(String.format("%.10f",zarar));
            }
            endusuk.setText(endusukprice);
            enyuksek.setText(enyuksekprice);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CoinDuzenle.class);
                intent.putExtra("id", (int)id);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CoinDetay.this);
                alertDialog.setTitle("Uyarı");
                alertDialog.setMessage("Coin Silinsin mi?");
                alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Database db = new Database(getApplicationContext());
                        db.coinSil(id);
                        Toast.makeText(getApplicationContext(), "Coin Başarıyla Silindi", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                alertDialog.setNegativeButton("Hayır",null);
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
