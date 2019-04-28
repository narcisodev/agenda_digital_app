package com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Ocorrencia;
import com.example.narcisogomes.myapplication.pedagogico.DescOcorrencia;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapterOcorrenciasAlunos extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<Ocorrencia> modellist = new ArrayList<>();
    ArrayList<Ocorrencia> arrayList = new ArrayList<>();

    public ListViewAdapterOcorrenciasAlunos(){

    }

    public ListViewAdapterOcorrenciasAlunos(Context mContext, List<Ocorrencia> modellist) {
        this.mContext = mContext;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Ocorrencia>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder{
        TextView id_occ, ocorrencia, data, letra_inicial;
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
            holder = new ListViewAdapterOcorrenciasAlunos.ViewHolder();
            convertView = inflater.inflate(R.layout.ped_modelo_lista_occ_aluno, null);
            holder.id_occ = convertView.findViewById(R.id.id_occ);
            holder.ocorrencia = convertView.findViewById(R.id.ocorrencia);
            holder.data = convertView.findViewById(R.id.data);
            holder.letra_inicial = convertView.findViewById(R.id.letra_inicial);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.id_occ.setText(modellist.get(position).getId()+"");
        holder.ocorrencia.setText(modellist.get(position).getDescricao());
        holder.data.setText(modellist.get(position).getData());


        char nome_i= modellist.get(position).getDescricao().charAt(0);
        holder.letra_inicial.setText(nome_i+"");


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* int id_ocorrencia;
                TextView t = v.findViewById(R.id.id_occ);
                String id_s = t.getText().toString();
                id_ocorrencia = Integer.parseInt(id_s);
                Intent i = new Intent(mContext, DescOcorrencia.class);
                i.putExtra("id_oc", id_ocorrencia+"");
                mContext.startActivity(i);\*/
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

                if(ocorrencia.getData().toLowerCase(Locale.getDefault()).contains(charText)){
                    modellist.add(ocorrencia);
                }

                if(ocorrencia.getNome_al().toLowerCase(Locale.getDefault()).contains(charText)){
                    modellist.add(ocorrencia);
                }

                if((ocorrencia.getId()+"").toLowerCase(Locale.getDefault()).contains(charText)){
                    modellist.add(ocorrencia);
                }
            }
        }

        notifyDataSetChanged();
    }
}
