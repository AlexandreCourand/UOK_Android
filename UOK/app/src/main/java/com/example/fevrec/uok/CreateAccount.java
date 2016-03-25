package com.example.fevrec.uok;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class CreateAccount extends Activity {

    EditText pseudo, mdp, mail, nom;
    String pseud, pw, email, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /** if (id == R.id.action_settings) {
         return true;
         }*/

        return super.onOptionsItemSelected(item);
    }

    public void saveData(View view){
        pseudo = (EditText) findViewById(R.id.pseudo);
        mdp = (EditText) findViewById(R.id.mdp);
        mail = (EditText) findViewById(R.id.mail);
        nom = (EditText) findViewById(R.id.nom);

        pseud = String.valueOf(pseudo.getText());
        pw = String.valueOf(mdp.getText());
        email = String.valueOf(mail.getText());
        name = String.valueOf(nom.getText());
        Log.d(pseud,pw);
    }


}
