package com.gabrielgeorge.olxapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.gabrielgeorge.olxapp.R;
import com.gabrielgeorge.olxapp.adapter.AdapterAnuncios;
import com.gabrielgeorge.olxapp.helper.ConfiguracaoFirebase;
import com.gabrielgeorge.olxapp.model.Anuncio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class AnunciosActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private RecyclerView recyclerAnunciosPublicos;
    private Button buttonRegiao, buttonCategoria;
    private AdapterAnuncios adapterAnuncios;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private DatabaseReference anunciosPublicosRef;
    private AlertDialog dialog;
    private String filtroEstado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

        inicializarComponentes();

        //configurações iniciais
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        anunciosPublicosRef = ConfiguracaoFirebase.getFirebase().child("anuncios");

        //Configura RecyclerView
        recyclerAnunciosPublicos.setLayoutManager(new LinearLayoutManager(this));
        recyclerAnunciosPublicos.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(listaAnuncios, this);
        recyclerAnunciosPublicos.setAdapter(adapterAnuncios);

        recuperarAnunciosPublicos();
    }

    public void filtrarPorEstado(View view){

        AlertDialog.Builder dialogEstado = new AlertDialog.Builder(this);
        dialogEstado.setTitle("Selecione o estado desejado");

        //confgurar spinner
        View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);

        //Configura Spinner de estados
        Spinner spinnerEstado = viewSpinner.findViewById(R.id.spinnerFiltro);
        String[] estados = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, estados
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        dialogEstado.setView(viewSpinner);

        dialogEstado.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                filtroEstado = spinnerEstado.getSelectedItem().toString();
                recuperarAnunciosPorEstado();
            }
        });
        dialogEstado.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = dialogEstado.create();
        dialog.show();
    }

    public void recuperarAnunciosPorEstado(){
        //Configura no por estado
        anunciosPublicosRef = ConfiguracaoFirebase.getFirebase()
                .child("anuncios")
                .child(filtroEstado);
        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaAnuncios.clear();
                for (DataSnapshot categorias : dataSnapshot.getChildren()){
                    for (DataSnapshot anuncios : categorias.getChildren()){
                        Anuncio anuncio = anuncios.getValue(Anuncio.class);
                        listaAnuncios.add(anuncio);
                    }
                }
                Collections.reverse(listaAnuncios);
                adapterAnuncios.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void recuperarAnunciosPublicos(){

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Recuperando Anuncios")
                .setCancelable(false)
                .build();
        dialog.show();

        listaAnuncios.clear();
        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot estados : dataSnapshot.getChildren()){
                    for (DataSnapshot categorias : estados.getChildren()){
                        for (DataSnapshot anuncios : categorias.getChildren()){
                            Anuncio anuncio = anuncios.getValue(Anuncio.class);
                            listaAnuncios.add(anuncio);
                        }
                    }
                }
                Collections.reverse(listaAnuncios);
                adapterAnuncios.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //verificações na interface antes de carregar os menus
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(autenticacao.getCurrentUser() == null){ //usuario deslogado
            menu.setGroupVisible(R.id.group_deslogado, true);
        } else { //usuario logado
            menu.setGroupVisible(R.id.group_logado, true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_cadastrar:
                startActivity( new Intent(getApplicationContext(), CadastroActivity.class));
                break;
            case R.id.menu_sair:
                autenticacao.signOut();
                invalidateOptionsMenu(); //invalida os menus, obrigadando a preparar de novo
                break;
            case R.id.menu_anuncios:
                startActivity(new Intent(getApplicationContext(), MeusAnunciosActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void inicializarComponentes(){
        recyclerAnunciosPublicos = findViewById(R.id.recyclerAnunciosPublicos);
    }
}