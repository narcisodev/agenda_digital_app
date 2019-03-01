package com.example.narcisogomes.myapplication.aluno.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.narcisogomes.myapplication.models.Atividade;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.example.narcisogomes.myapplication.R;
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
        TextView materia, data_atividade, titulo_atividade, descricao_atividade;
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
            convertView = inflater.inflate(R.layout.aluno_frag_list_atividades_modelo, null);
            holder.materia = convertView.findViewById(R.id.materia);
            holder.data_atividade = convertView.findViewById(R.id.data_atividade);
            holder.titulo_atividade= convertView.findViewById(R.id.titulo_atividade);
            holder.descricao_atividade= convertView.findViewById(R.id.descricao_atividade);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.materia.setText(modellist.get(position).disiciplina);
        holder.data_atividade.setText(modellist.get(position).data);
        holder.titulo_atividade.setText(modellist.get(position).titulo);
        holder.descricao_atividade.setText(modellist.get(position).descricao);



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView a = v.findViewById(R.id.titulo_atividade);
                String  as = a.getText().toString();
                Toast.makeText(mContext, "CLICOU: " +as, Toast.LENGTH_SHORT).show();
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

