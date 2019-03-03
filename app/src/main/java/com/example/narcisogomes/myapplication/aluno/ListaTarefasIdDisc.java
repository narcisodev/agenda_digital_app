package com.example.narcisogomes.myapplication.aluno;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.aluno.ListAdapters.ListViewAdapterAtividades;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaTarefasIdDisc extends AppCompatActivity {
    String search_url_service;
    List<Atividade> atividades_array = new ArrayList<>();
    ListViewAdapterAtividades listViewAdapterAtividades;
    ListView listaAtividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.aluno_activity_lista_tarefas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Listagem de tarefas");
        listaAtividades = findViewById(R.id.lista_atividades);
        search_url_service = "acao=3&cause=all&id_disciplina=" + Values.id_disciplina_lista_materias;
        new BuscaAtividades().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listagem_tarefas, menu);
        MenuItem myActionMenuItem= menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    listViewAdapterAtividades.filter("");
                    listaAtividades.clearTextFilter();
                }else{
                    listViewAdapterAtividades.filter(newText);
                }
                return true;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_listar_atuais) {
            search_url_service = "acao=3&cause=after&id_disciplina=" + Values.id_disciplina_lista_materias;
            new BuscaAtividades().execute();
        } else if (id ==R.id.action_listar_anteriores) {
            search_url_service = "acao=3&cause=before&id_disciplina=" + Values.id_disciplina_lista_materias;
            new BuscaAtividades().execute();
        } else if (id == R.id.action_listar_todas) {
            search_url_service = "acao=3&cause=all&id_disciplina=" + Values.id_disciplina_lista_materias;
            new BuscaAtividades().execute();
        }else{
            super.onBackPressed();
        }



        return true;
    }


    private class BuscaAtividades extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ListaTarefasIdDisc.this);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE,search_url_service);
            } catch (Exception e) {
                Toast.makeText(ListaTarefasIdDisc.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            atividades_array.clear();
            Atividade atividade = new Atividade();
            String responsebody = strings;
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(responsebody);
                boolean is = objeto.getBoolean("success");
                if(is){
                    JSONArray tarefas = objeto.getJSONArray("dados");

                    for(int i = 0; i< tarefas.length(); i++){
                        JSONObject tarefa_json = tarefas.getJSONObject(i);
                        atividade.id_atividade = tarefa_json.getInt("id_atividade");
                        atividade.titulo = tarefa_json.getString("titulo");
                        atividade.descricao = tarefa_json.getString("descricao");
                        atividade.pontos = tarefa_json.getString("pontos");
                        atividade.data = tarefa_json.getString("data")+ " ";
                        atividade.data_entrega = tarefa_json.getString("dataentrega");
                        atividade.disiciplina = tarefa_json.getString("disciplina");
                        atividade.data = tarefa_json.getString("data");
                        atividade.datacriacao = tarefa_json.getString("datacriacao");
                        atividades_array.add(atividade);
                        atividade = new Atividade();
                    }

                    listViewAdapterAtividades = new ListViewAdapterAtividades(ListaTarefasIdDisc.this, atividades_array);


                    listaAtividades.setAdapter(listViewAdapterAtividades);

                }else{
                    Toast.makeText(ListaTarefasIdDisc.this, "Não existem tarefas cadastradas para esta matéria", Toast.LENGTH_LONG);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.hide();
        }
    }
}
