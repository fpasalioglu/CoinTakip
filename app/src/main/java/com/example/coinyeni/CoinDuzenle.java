package com.example.coinyeni;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class CoinDuzenle extends AppCompatActivity {
    Button b1;
    EditText e1,e2,e3;
    int id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coinduzenle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Coin Düzenle");

        b1 = (Button)findViewById(R.id.button1);
        e1 = (EditText)findViewById(R.id.coinname);
        e2 = (EditText)findViewById(R.id.coinmiktar);
        e3 = (EditText)findViewById(R.id.coinfiyat);

        Intent intent=getIntent();
        id = intent.getIntExtra("id", 0);

        Database db = new Database(getApplicationContext());
        HashMap<String, String> map = db.coinDetay(id);

        e1.setText(map.get("coin_adi"));
        e2.setText(map.get("miktar").toString());
        e3.setText(map.get("fiyat").toString());

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String adi,miktar,fiyati;
                adi = e1.getText().toString();
                miktar = e2.getText().toString();
                fiyati = e3.getText().toString();
                if(adi.matches("") || miktar.matches("") || fiyati.matches("")  ){
                    Toast.makeText(getApplicationContext(), "Tüm bilgileri eksiksiz doldurun", Toast.LENGTH_LONG).show();
                }else{
                    Database db = new Database(getApplicationContext());
                    db.coinDuzenle(adi, miktar, fiyati,id);
                    db.close();
                    Toast.makeText(getApplicationContext(), "Coin Başarıyla Düzenlendi.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
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
