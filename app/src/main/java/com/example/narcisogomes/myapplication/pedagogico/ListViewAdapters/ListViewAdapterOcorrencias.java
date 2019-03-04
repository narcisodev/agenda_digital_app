package com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Ocorrencia;
import com.example.narcisogomes.myapplication.models.Professor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapterOcorrencias extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<Ocorrencia> modellist;
    ArrayList<Ocorrencia> arrayList;

    public ListViewAdapterOcorrencias(Context mContext, List<Ocorrencia> modellist) {
        this.mContext = mContext;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Ocorrencia>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder{
        TextView id_ocorrencia, data_ocorrencia, descrica_ocorrencia, alunos_ocorrencia;
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
            holder = new ListViewAdapterOcorrencias.ViewHolder();
            convertView = inflater.inflate(R.layout.pedagogico_modelo_lista_ocorrencias, null);
            holder.id_ocorrencia = convertView.findViewById(R.id.id_ocorrencia);
            holder.data_ocorrencia = convertView.findViewById(R.id.data_ocorrencia);
            holder.descrica_ocorrencia = convertView.findViewById(R.id.descricao_ocorrencia);
            holder.alunos_ocorrencia = convertView.findViewById(R.id.alunos_ocorrencia);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.id_ocorrencia.setText(modellist.get(position).getId()+"");
        holder.data_ocorrencia.setText(modellist.get(position).getData());
        holder.descrica_ocorrencia.setText(modellist.get(position).getDescricao());
        String nomes_alunos= "";
        int tamanho_array = modellist.get(position).getArray_alunos().size();
        ArrayList<Aluno> al_a = modellist.get(position).getArray_alunos();
        for(int i = 0; i< al_a.size(); i++){
            Aluno a  = al_a.get(i);
            if(i == tamanho_array-1){
                nomes_alunos += a.getNome_usuario()+".";
            }else{
                nomes_alunos += a.getNome_usuario()+", ";
            }
        }
        holder.alunos_ocorrencia.setText(nomes_alunos);

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
            for(Ocorrencia ocorrencia:arrayList){
                if(ocorrencia.getDescricao().toLowerCase(Locale.getDefault()).contains(charText)){
                    modellist.add(ocorrencia);
                }
            }
        }

        notifyDataSetChanged();
    }
}
