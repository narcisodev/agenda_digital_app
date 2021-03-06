package com.example.narcisogomes.myapplication.aluno;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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

public class ListaTarefas extends AppCompatActivity {
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
        search_url_service = "acao=3.1&cause=all&id_turma=" + Values_aluno.id_turma;
        new BuscaAtividades().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listagem_tarefas, menu);
        //ocultar os menus Atuais, Anteriores e Todas porque ele está sendo usado em ListaTarefasIdDisc
        MenuItem m = menu.findItem(R.id.action_listar_atuais);
        m.setVisible(false);
        m = menu.findItem(R.id.action_listar_anteriores);
        m.setVisible(false);
         m = menu.findItem(R.id.action_listar_todas);
        m.setVisible(false);
        MenuItem myActionMenuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    listViewAdapterAtividades.filter("");
                    listaAtividades.clearTextFilter();
                } else {
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
            search_url_service = "acao=3.1&cause=after&id_turma=" + Values_aluno.id_turma;
            new BuscaAtividades().execute();
        } else if (id == R.id.action_listar_anteriores) {
            search_url_service = "acao=3.1&cause=before&id_turma=" + Values_aluno.id_turma;
            new BuscaAtividades().execute();
        } else if (id == R.id.action_listar_todas) {
            search_url_service = "acao=3.1&cause=all&id_turma=" + Values_aluno.id_turma;
            new BuscaAtividades().execute();
        } else {
            super.onBackPressed();
        }


        return true;
    }


    private class BuscaAtividades extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ListaTarefas.this);
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
            String ab = "";
            try {
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, search_url_service);
            } catch (Exception e) {
                Toast.makeText(ListaTarefas.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                if (is) {
                    JSONArray tarefas = objeto.getJSONArray("dados");

                    for (int i = 0; i < tarefas.length(); i++) {
                        JSONObject tarefa_json = tarefas.getJSONObject(i);
                        atividade.id_atividade = tarefa_json.getInt("id_atividade");
                        atividade.titulo = tarefa_json.getString("titulo");
                        atividade.descricao = tarefa_json.getString("descricao");
                        atividade.pontos = tarefa_json.getString("pontos");
                        atividade.data = tarefa_json.getString("data") + " ";
                        atividade.data_entrega = tarefa_json.getString("dataentrega");
                        atividade.disiciplina = tarefa_json.getString("disciplina");
                        atividade.data = tarefa_json.getString("data");
                        atividade.datacriacao = tarefa_json.getString("datacriacao");
                        atividade.nome_professor = tarefa_json.getString("professor");
                        atividades_array.add(atividade);
                        atividade = new Atividade();
                    }

                    listViewAdapterAtividades = new ListViewAdapterAtividades(ListaTarefas.this, atividades_array);


                    listaAtividades.setAdapter(listViewAdapterAtividades);

                } else {
                    Toast.makeText(ListaTarefas.this, "Não existem tarefas cadastradas para esta matéria", Toast.LENGTH_LONG);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.hide();
        }
    }
}
