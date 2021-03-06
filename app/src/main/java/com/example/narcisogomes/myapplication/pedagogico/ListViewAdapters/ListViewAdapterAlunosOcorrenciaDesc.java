package com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Aluno;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapterAlunosOcorrenciaDesc extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<Aluno> modellist;
    ArrayList<Aluno> arrayList;

    public ListViewAdapterAlunosOcorrenciaDesc(Context mContext, List<Aluno> modellist) {
        this.mContext = mContext;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Aluno>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder{
        TextView nome, curso, turma, id_aluno, letra_inicial;
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
            holder = new ListViewAdapterAlunosOcorrenciaDesc.ViewHolder();
            convertView = inflater.inflate(R.layout.ped_lista_alunos_model, null);
            holder.nome = convertView.findViewById(R.id.nome);
            holder.turma = convertView.findViewById(R.id.turma);
            holder.curso = convertView.findViewById(R.id.curso);
            holder.id_aluno = convertView.findViewById(R.id.id_aluno);
            holder.letra_inicial = convertView.findViewById(R.id.letra_inicial);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nome.setText(modellist.get(position).getNome_usuario());
        holder.turma.setText(modellist.get(position).getTurma());
        holder.curso.setText(modellist.get(position).getCurso());
        holder.id_aluno.setText(modellist.get(position).getId_aluno()+"");
        String nome_l = modellist.get(position).getNome_usuario();
        char letra_i = nome_l.charAt(0);
        holder.letra_inicial.setText(letra_i+"");





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
            for(Aluno model:arrayList){
                if(model.getNome_usuario().toLowerCase(Locale.getDefault()).contains(charText)){
                    modellist.add(model);
                }

                if(model.getTurma().toLowerCase(Locale.getDefault()).contains(charText)){
                    modellist.add(model);
                }
                if(model.getCurso().toLowerCase(Locale.getDefault()).contains(charText)){
                    modellist.add(model);
                }



            }
        }

        notifyDataSetChanged();
    }
}
