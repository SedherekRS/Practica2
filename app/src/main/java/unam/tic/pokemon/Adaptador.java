package unam.tic.pokemon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import unam.tic.pokemon.Modelo.Pokemon;

public class Adaptador extends BaseAdapter {
    private Context contexto;
    private ArrayList<Pokemon> datos;
    private static LayoutInflater inflater = null;

    public Adaptador(Context contexto, ArrayList<Pokemon> datos) {
        this.contexto = contexto;
        this.datos = datos;
        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        return datos.get(position).getId();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View vista = inflater.inflate(R.layout.elemento_lista,null);
        TextView tvNombre = vista.findViewById(R.id.tvNombre);
        TextView tvTipo = vista.findViewById(R.id.tvTipo);
        TextView tvTipo2 = vista.findViewById(R.id.tvTipo2);
        ImageView ivPokemon =   vista.findViewById(R.id.ivPokemon);
        tvNombre.setText(datos.get(position).getName());
        tvTipo.setText(datos.get(position).getType1());
        tvTipo2.setText(datos.get(position).getType2());
        Picasso.get().load(datos.get(position).getUrlPokemon()).into(ivPokemon);
        return vista;
    }
}
