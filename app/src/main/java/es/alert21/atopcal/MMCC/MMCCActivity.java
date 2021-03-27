package es.alert21.atopcal.MMCC;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import es.alert21.atopcal.R;

public class MMCCActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_m_c_c);
        getSupportActionBar().setTitle("MMCC");
    }
}