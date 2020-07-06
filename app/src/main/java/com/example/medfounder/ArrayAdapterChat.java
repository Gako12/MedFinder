package com.example.medfounder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medfounder.objetos.Hospital;
import com.example.medfounder.objetos.Mensagens;

import java.util.ArrayList;
import java.util.List;

public class ArrayAdapterChat extends ArrayAdapter<Mensagens> {

    private final Context context;
    private final ArrayList<Mensagens> mensagens;

    public ArrayAdapterChat(Context context, int simple_list_item_1, ArrayList<Mensagens> mensagens) {
        super(context, R.layout.left , mensagens);
        this.context = context;
        this.mensagens = mensagens;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = rowView = inflater.inflate(R.layout.left, parent, false);

        if(!mensagens.get(position).getLado()) {
            rowView = inflater.inflate(R.layout.right, parent, false);
        }

        TextView mensagem = (TextView) rowView.findViewById(R.id.msgr);
        mensagem.setText(mensagens.get(position).getMensagem());

        return rowView;
    }

}
