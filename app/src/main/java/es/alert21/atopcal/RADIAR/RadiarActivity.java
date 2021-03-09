package es.alert21.atopcal.RADIAR;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import es.alert21.atopcal.R;

public class RadiarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radiar);
        getSupportActionBar().setTitle("RADIACIÓN");
    }
}