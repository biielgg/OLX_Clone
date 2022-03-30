package com.gabrielgeorge.olxapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.gabrielgeorge.olxapp.R;

import java.util.Locale;

public class CadastrarAnuncioActivity extends AppCompatActivity {

    private EditText campoTitulo, campoDescricao;
    private CurrencyEditText campoValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncio);

        inicializarComponentes();
    }

    public void salvarAnuncio(View view){
        String valor = campoValor.getText().toString();
    }

    private void inicializarComponentes(){
        campoTitulo     = findViewById(R.id.editTitulo);
        campoDescricao  = findViewById(R.id.editDescricao);
        campoValor      = findViewById(R.id.editValor);

        //Configura a localidade para Brasil
        Locale locale = new Locale("pt", "BR");
        campoValor.setLocale(locale);
    }
}