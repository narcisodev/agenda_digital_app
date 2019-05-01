package com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.models.Professor;
import com.example.narcisogomes.myapplication.pedagogico.DescricaoAtividade;
import com.example.narcisogomes.myapplication.pedagogico.ListaProfessores;
import com.example.narcisogomes.myapplication.pedagogico.Values_pedagogico;
import com.example.narcisogomes.myapplication.professor.TelaDescAtv;
import com.example.narcisogomes.myapplication.professor.Values_professor;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapterAtividadesAlunos extends BaseAdapter {
    int id_atv;
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
                id_atv = Integer.parseInt(id_s);
                new BuscaAtividade().execute();
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








    /**
     * Buscar a atividade da lista
     * */

    private class BuscaAtividade extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(mContext);
            dialog.setTitle(R.string.carregando);
            dialog.setMessage("Estamos carregando a sua requisição...");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String ab="";
            try {
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE,"acao="+Values.SEARCH_TASK+"&id_atividade="+id_atv);
                Log.e("NARCISO222", ab);
            } catch (Exception e) {
                Toast.makeText(mContext, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {

            String responsebody = strings;
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(responsebody);
                boolean is = objeto.getBoolean("success");
                if(is){
                    JSONObject atividade = objeto.getJSONObject("dados");
                    Atividade a  = new Atividade();
                    a.descricao = atividade.getString("descricao");
                    a.titulo = atividade.getString("titulo");
                    a.pontos = atividade.getString("pontos");
                    a.datacriacao = atividade.getString("datacriacao");
                    a.data = atividade.getString("data");
                    a.data_entrega = atividade.getString("dataentrega");
                    a.disiciplina = atividade.getString("disciplina");
                    a.curso = atividade.getString("curso");
                    a.turma = atividade.getString("turma");
                    Values_professor.atividade_obj = a;
                    mContext.startActivity(new Intent(mContext, TelaDescAtv.class));
                }else{
                    Toast.makeText(mContext, "Não conseguimos buscar as iformações necessárias", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.hide();
        }
    }
}
