package com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.professor.TelaDescAtv;
import com.example.narcisogomes.myapplication.professor.Values_professor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapterAtividadesAlunos extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<Atividade> modellist;
    ArrayList<Atividade> arrayList;

    public ListViewAdapterAtividadesAlunos(Context mContext, List<Atividade> modellist) {
        this.mContext = mContext;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Atividade>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder{
        TextView titulo, data, disciplina, letra_inicial, id_atv;
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

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.ped_lista_modelo_tarefas_alunos, null);
            holder.titulo = convertView.findViewById(R.id.titulo);
            holder.data = convertView.findViewById(R.id.data);
            holder.disciplina= convertView.findViewById(R.id.disciplina);
            holder.letra_inicial = convertView.findViewById(R.id.letra_inicial);
            holder.id_atv = convertView.findViewById(R.id.id_atv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titulo.setText(modellist.get(position).descricao);
        holder.data.setText(modellist.get(position).data_entrega);
        holder.disciplina.setText(modellist.get(position).disiciplina);
        holder.id_atv.setText(modellist.get(position).id_atividade+"");

        char li = modellist.get(position).descricao.charAt(0);
        holder.letra_inicial.setText(li+"");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t = v.findViewById(R.id.id_atv);
                String id_s = t.getText().toString();
                int id = Integer.parseInt(id_s);
                for(Atividade a: modellist){
                    if(a.id_atividade == id){
                        Values_professor.atividade_obj = a;
                        mContext.startActivity(new Intent(mContext, TelaDescAtv.class));
                        break;
                    }
                }
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
            for(Atividade model:arrayList){
                if(model.titulo.toLowerCase(Locale.getDefault()).contains(charText)){
                    modellist.add(model);
                }
            }
        }

        notifyDataSetChanged();
    }
}
