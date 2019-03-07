package com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Ocorrencia;
import com.example.narcisogomes.myapplication.models.Professor;
import com.example.narcisogomes.myapplication.pedagogico.CadastroOcorrencia;
import com.example.narcisogomes.myapplication.pedagogico.DescOcorrencia;
import com.example.narcisogomes.myapplication.pedagogico.ListaAlunos;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapterOcorrencias extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<Ocorrencia> modellist = new ArrayList<>();
    ArrayList<Ocorrencia> arrayList = new ArrayList<>();

    public ListViewAdapterOcorrencias(){

    }

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
        holder.alunos_ocorrencia.setText(modellist.get(position).getNome_al());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id_ocorrencia;
                TextView t = v.findViewById(R.id.id_ocorrencia);
                String id_s = t.getText().toString();
                id_ocorrencia = Integer.parseInt(id_s);
                Intent i = new Intent(mContext, DescOcorrencia.class);
                i.putExtra("id_oc", id_ocorrencia+"");
                mContext.startActivity(i);
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
