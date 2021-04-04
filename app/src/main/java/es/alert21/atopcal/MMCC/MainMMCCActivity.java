package es.alert21.atopcal.MMCC;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import es.alert21.atopcal.R;

public class MainMMCCActivity extends AppCompatActivity {
    Button buttonSiguiente,buttonSalir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_m_m_c_c);
        getSupportActionBar().setTitle("MMCC cfg");
        buttonSiguiente = findViewById(R.id.buttonSiguiente);
        buttonSalir = findViewById(R.id.buttonSalir);
        buttonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMMCCActivity.this, MMCCActivity.class);
                startActivity(intent);
            }
        });
    }
}