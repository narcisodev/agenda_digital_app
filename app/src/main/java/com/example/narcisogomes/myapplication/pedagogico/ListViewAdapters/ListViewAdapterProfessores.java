package com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Professor;
import com.example.narcisogomes.myapplication.pedagogico.DetalhesProfessor;
import com.example.narcisogomes.myapplication.pedagogico.Values_pedagogico;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapterProfessores extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<Professor> modellist;
    ArrayList<Professor> arrayList;

    public ListViewAdapterProfessores(Context mContext, List<Professor> modellist) {
        this.mContext = mContext;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Professor>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder{
        TextView letra_inical, nome_prof, formacao_prof, id_professor;
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
            holder = new ListViewAdapterProfessores.ViewHolder();
            convertView = inflater.inflate(R.layout.ped_lista_professores_model, null);
            holder.nome_prof = convertView.findViewById(R.id.nome_prof);
            holder.formacao_prof = convertView.findViewById(R.id.formacao_prof);
            holder.letra_inical = convertView.findViewById(R.id.letra_inicial);
            holder.id_professor = convertView.findViewById(R.id.id_professor);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nome_prof.setText(modellist.get(position).getNome_usuario());
        holder.formacao_prof.setText(modellist.get(position).getFormacao());
        holder.id_professor.setText(modellist.get(position).getId_professor()+"");

        String nome_i= modellist.get(position).getNome_usuario().toUpperCase();
        char letra_i = nome_i.charAt(0);
        holder.letra_inical.setText(letra_i+"");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t_id = v.findViewById(R.id.id_professor);
                int id = Integer.parseInt(t_id.getText().toString());
                Intent intent = new Intent(mContext, DetalhesProfessor.class);
                Values_pedagogico.id_professor = id;
                mContext.startActivity(intent);
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
            for(Professor model:arrayList){
                if(model.getNome_usuario().toLowerCase(Locale.getDefault()).contains(charText)){
                    modellist.add(model);
                }
            }

            for(Professor model:arrayList){
                if(model.getFormacao().toLowerCase(Locale.getDefault()).contains(charText)){
                    modellist.add(model);
                }
            }
        }

        notifyDataSetChanged();
    }
}
