package com.gabrielgeorge.olxapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gabrielgeorge.olxapp.R;
import com.gabrielgeorge.olxapp.model.Anuncio;

import java.util.List;

public class AdapterAnuncios extends RecyclerView.Adapter<AdapterAnuncios.MyViewHolder> {

    private List<Anuncio> anuncios;
    private Context context;

    public AdapterAnuncios(List<Anuncio> anuncios, Context context) {
        this.anuncios = anuncios;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_anuncio, viewGroup, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(AdapterAnuncios.MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView titulo;
        TextView valor;
        ImageView foto;

        public MyViewHolder(View itemView){
            super (itemView);

            titulo  = itemView.findViewById(R.id.textTitulo);
            valor   = itemView.findViewById(R.id.textPreco);
            foto    = itemView.findViewById(R.id.imageAnuncio);
        }
    }
}
