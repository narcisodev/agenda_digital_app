package com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Curso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapterCursos extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<Curso> modellist;
    ArrayList<Curso> arrayList;

    public ListViewAdapterCursos(Context mContext, List<Curso> modellist) {
        this.mContext = mContext;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Curso>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder{
        TextView nome_curso, id_curso, sigla_curso, letra_inicial;
    }

    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int position) {
        return modellist.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            holder = new ListViewAdapterCursos.ViewHolder();
            convertView = inflater.inflate(R.layout.ped_lista_cursos_model, null);
            holder.nome_curso = convertView.findViewById(R.id.nome_curso);
            holder.sigla_curso = convertView.findViewById(R.id.sigla_curso);
            holder.id_curso = convertView.findViewById(R.id.id_curso);
            holder.letra_inicial = convertView.findViewById(R.id.letra_inicial);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nome_curso.setText(modellist.get(position).getNome());
        holder.sigla_curso.setText(modellist.get(position).getSigla());
        holder.id_curso.setText(modellist.get(position).getId()+"");

        String nome_i= modellist.get(position).getNome();
        char l_i = nome_i.charAt(0);
        holder.letra_inicial.setText(l_i+"");



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        modellist.clear();
        if(charText.length() == 0){
            modellist.addAll(arrayList);
        }else{
            for(Curso model:arrayList){
                if(model.getNome().toLowerCase(Locale.getDefault()).contains(charText)){
                    modellist.add(model);
                }

                if(model.getSigla().toLowerCase(Locale.getDefault()).contains(charText)){
                    modellist.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }
}
