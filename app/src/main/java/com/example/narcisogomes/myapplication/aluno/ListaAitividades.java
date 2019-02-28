package com.example.narcisogomes.myapplication.aluno;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaAitividades extends AppCompatActivity {
    SimpleAdapter adapter;
    private ListView listaAtividades;
    private List<Map<String, Object>> atividades;
    private ArrayList<Atividade> atividades_array = new ArrayList<>();
    String pesquisa = "acao=3"+"&id_disciplina="+Values.id_disciplina_lista_materias;
    SearchView searchView  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aluno_activity_lista_aitividades);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Listagem de tarefas");
        listaAtividades = findViewById(R.id.lista_atividades);
        listaAtividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> atividade = atividades.get(position);
                Values.atividade_map = atividade;
                //Toast.makeText(getApplicationContext(), "Atividade: "+ "Nome:" + atividade.get("titulo")+ " ID: "+ atividade.get("id"), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ListaAitividades.this, TelaDescAtividade.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //startActivity(new Intent(ListaAitividades.this, TelaDescAtividade.class));
            }
        });

        searchView = findViewById(R.id.app_bar_search);



        new BuscaAtividades().execute();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private List<Map<String,Object>> listarAtividades(){

        atividades = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        Atividade a = new Atividade();

        for(int i = 0; i< atividades_array.size(); i++){
            a = atividades_array.get(i);
            item.put("id", a.id_atividade);
            item.put("titulo", a.titulo);
            item.put("descricao", a.descricao);
            item.put("pontos", a.pontos);
            item.put("data_entrega", a.data_entrega);
            item.put("disciplina", a.disiciplina);
            item.put("data", a.data);
            item.put("datacriacao", a.datacriacao);
            atividades.add(item);
            item = new HashMap<String, Object>();
            a = new Atividade();
        }

        return atividades;
    }

    private class BuscaAtividades extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ListaAitividades.this);
            dialog.setTitle(R.string.carregando);
            dialog.setMessage("Estamos carregando a sua requisição...");
            dialog.show();
        }

        /*
         * onde a operação deve ser implementada, pois este método é executado em outra thread
         * este é o único método obrigatório da classe AsyncTask
         * */
        @Override
        protected String doInBackground(Void... voids) {
            String ab="";
            try {
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, pesquisa);
                Log.e(Values.TAG, ab);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            Atividade atividade = new Atividade();
            String responsebody = strings;
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(responsebody);
                boolean is = objeto.getBoolean("success");
                if(is){
                    JSONArray tarefas = objeto.getJSONArray("dados");
                    for(int i =  0; i< tarefas.length(); i++){
                        JSONObject tarefa_json = tarefas.getJSONObject(i);
                        atividade.id_atividade = tarefa_json.getInt("id_atividade");
                        atividade.titulo = tarefa_json.getString("titulo");
                        atividade.descricao = tarefa_json.getString("descricao");
                        atividade.pontos = tarefa_json.getString("pontos");
                        atividade.data = tarefa_json.getString("data")+ " ";
                        atividade.data_entrega = tarefa_json.getString("dataentrega");
                        atividade.disiciplina = tarefa_json.getString("disciplina");
                        atividade.datacriacao = tarefa_json.getString("datacriacao");
                        atividades_array.add(atividade);
                        atividade = new Atividade();
                    }
                    String[] de ={"disciplina","data","titulo", "descricao"};
                    int[] para = {R.id.materia,R.id.data_atividade, R.id.titulo_atividade,R.id.descricao_atividade};
                    adapter= new SimpleAdapter(getApplicationContext(), listarAtividades(), R.layout.aluno_frag_list_atividades_modelo,de,para);
                    //adapter.setViewBinder(new ListaAtividadesDeAula.AtividadesViewBinder());
                    listaAtividades.setAdapter(adapter);

                }else{
                    Toast.makeText(getApplicationContext(), "Não existem tarefas cadastradas para esta matéria", Toast.LENGTH_LONG);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.hide();
        }
    }



}
