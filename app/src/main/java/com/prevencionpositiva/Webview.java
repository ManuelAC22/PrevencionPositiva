package com.prevencionpositiva;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

public class Webview extends AppCompatActivity {
    ImageButton lista,riesgo,clima;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView myWebView = findViewById(R.id.webview);
        myWebView.loadUrl("https://www.arcgis.com/apps/PublicInformation/index.html?appid=021e76e31e35471a821ce69241994115");

        lista = findViewById(R.id.lista);
        lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Webview.this,IncidenciaActivity.class);
                startActivity(intent);
            }
        });

        riesgo = findViewById(R.id.riesgo);
        riesgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Webview.this,Webview.class);
                startActivity(intent);
            }
        });

        clima = findViewById(R.id.clima);
        clima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Webview.this,AgricolaActivity.class);
                startActivity(intent);
            }
        });
    }
}
