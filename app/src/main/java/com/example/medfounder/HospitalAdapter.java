package com.example.medfounder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medfounder.objetos.Hospital;

import java.util.ArrayList;

public class HospitalAdapter extends ArrayAdapter<Hospital> {

    private final Context context;
    private final ArrayList<Hospital> elementos;

    public HospitalAdapter(Context context, int simple_list_item_1, ArrayList<Hospital> elementos) {
        super(context, R.layout.linha , elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.linha, parent, false);

        TextView nome = (TextView) rowView.findViewById(R.id.nomeHosp);
        TextView endereco = (TextView) rowView.findViewById(R.id.endHosp);

        nome.setText(elementos.get(position).getNome());
        endereco.setText(elementos.get(position).getEndereco());

        return rowView;
    }
}
