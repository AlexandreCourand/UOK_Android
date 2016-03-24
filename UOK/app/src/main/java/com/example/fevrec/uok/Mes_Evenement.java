package com.example.fevrec.uok;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Mes_Evenement extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes__evenement);
        Spinner sp = (Spinner) findViewById(R.id.spinnerEvent);
        ArrayAdapter arrayAdapter= ArrayAdapter.createFromResource(this,0,android.R.layout.simple_spinner_item); // changer le 0 par les événements contenu dans la base

        sp.setAdapter(arrayAdapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                afficherEvenement();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }

    }

    public void afficherEvenement(int position){

    }



}
