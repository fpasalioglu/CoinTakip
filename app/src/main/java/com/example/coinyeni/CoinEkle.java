package com.example.coinyeni;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class CoinEkle extends AppCompatActivity {
    Button ekle;
    EditText coinadiEdit, coinmiktariEdit, alisfiyatiEdit;
    SwitchMaterial type;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coinekle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Coin Ekle");

        ekle = (Button)findViewById(R.id.button1);
        coinadiEdit = (EditText)findViewById(R.id.editText1);
        coinmiktariEdit = (EditText)findViewById(R.id.editText3);
        alisfiyatiEdit = (EditText)findViewById(R.id.editText4);
        type = (SwitchMaterial)findViewById(R.id.switch1);

        ekle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String coinadi,alisfiyati,miktar;
                boolean tip;
                coinadi = coinadiEdit.getText().toString();
                miktar = coinmiktariEdit.getText().toString();
                alisfiyati = alisfiyatiEdit.getText().toString();
                tip = type.isChecked();

                if(coinadi.matches("") || alisfiyati.matches("") || miktar.matches("")  ){
                    Toast.makeText(getApplicationContext(), "Tüm Bilgileri Eksiksiz Doldurunuz", Toast.LENGTH_LONG).show();
                }else{
                    Database db = new Database(getApplicationContext());
                    db.coinEkle(coinadi, miktar, alisfiyati, tip);
                    db.close();
                    Toast.makeText(getApplicationContext(), "Coin Başarıyla Eklendi.", Toast.LENGTH_LONG).show();
                    coinadiEdit.setText("");
                    coinmiktariEdit.setText("");
                    alisfiyatiEdit.setText("");
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
