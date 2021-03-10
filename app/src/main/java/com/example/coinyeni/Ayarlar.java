package com.example.coinyeni;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.coinyeni.MainActivity.sharedPref;

public class Ayarlar extends AppCompatActivity {
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayarlar);

        final EditText miktarText = (EditText) findViewById(R.id.editText);
        final EditText TLmiktarText = (EditText) findViewById(R.id.editText2);
        Button kaydet = (Button) findViewById(R.id.button);
        EditText exportText = (EditText) findViewById(R.id.export);

        editor = sharedPref.edit();
        Database db = new Database(this);

        float savedBTC = sharedPref.getFloat("miktar",0);
        miktarText.setText(String.valueOf(savedBTC));
        float savedTL = sharedPref.getFloat("TLmiktar",0);
        TLmiktarText.setText(String.valueOf(savedTL));

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putFloat("miktar", Float.parseFloat(miktarText.getText().toString()));
                editor.putFloat("TLmiktar", Float.parseFloat(TLmiktarText.getText().toString()));
                editor.apply();
            }
        });

        ArrayList<HashMap<String, String>> coinler = db.coinler();
        exportText.setText(coinler.toString());
    }
}