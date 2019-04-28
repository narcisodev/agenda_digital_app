package com.example.narcisogomes.myapplication.professor.ListAdapters;

import android.content.Context;
import android.content.Intent;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.aluno.TelaDescAtividade;
import com.example.narcisogomes.myapplication.aluno.Values_aluno;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.professor.TelaDescAtv;
import com.example.narcisogomes.myapplication.professor.Values_professor;
import com.example.narcisogomes.myapplication.util.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapterAtividades extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<Atividade> modellist;
    ArrayList<Atividade> arrayList;

    public ListViewAdapterAtividades(Context mContext, List<Atividade> modellist) {
        this.mContext = mContext;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Atividade>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder{
        TextView data_entrega, titulo_atividade, disciplina_atividade, id_atividade;
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
            convertView = inflater.inflate(R.layout.professor_modelo_lista_atvs, null);
            holder.data_entrega = convertView.findViewById(R.id.data_entrega);
            holder.titulo_atividade = convertView.findViewById(R.id.titulo_atividade);
            holder.disciplina_atividade= convertView.findViewById(R.id.disciplina_atividade);
            holder.id_atividade = convertView.findViewById(R.id.id_atividade);
            convertView.setTag(holder);



        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.data_entrega.setText(modellist.get(position).data);
        holder.titulo_atividade.setText(modellist.get(position).titulo);
        holder.disciplina_atividade.setText(modellist.get(position).disiciplina);
        holder.id_atividade.setText(modellist.get(position).id_atividade+"");



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t = v.findViewById(R.id.id_atividade);
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
