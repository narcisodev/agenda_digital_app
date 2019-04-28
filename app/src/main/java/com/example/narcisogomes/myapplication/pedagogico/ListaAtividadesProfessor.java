package com.example.narcisogomes.myapplication.pedagogico;

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
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.professor.ListAdapters.ListViewAdapterAtividades;
import com.example.narcisogomes.myapplication.professor.Values_professor;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaAtividadesProfessor extends AppCompatActivity {
    ArrayList<Atividade> lista_atividades = new ArrayList<>();
    ListViewAdapterAtividades listViewAdapterAtividades;
    ListView listaAtividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ped_activity_lista_atividades_professor);
        listaAtividade = findViewById(R.id.lista_atividades);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Lista Atividades");
        new BuscaTarefas().execute();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pedagogico_lista_alunos, menu);
        MenuItem myActionMenuItem= menu.findItem(R.id.action_search_ped);
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
                    listaAtividade.clearTextFilter();
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
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private class BuscaTarefas extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ListaAtividadesProfessor.this);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=6" + "&id_professor=" + Values_pedagogico.id_professor);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            Atividade atv = new Atividade();
            String responsebody = strings;
            JSONObject objeto = null;

            try {
                objeto = new JSONObject(responsebody);
                JSONArray a = objeto.getJSONArray("dados");
                boolean isTarefa = objeto.getBoolean("success");
                if (isTarefa) {
                    for (int i = 0; i < a.length(); i++) {
                        JSONObject atividade = a.getJSONObject(i);
                        atv.id_atividade = atividade.getInt("id_atividade");
                        atv.titulo = atividade.getString("titulo");
                        atv.descricao = atividade.getString("descricao");
                        atv.pontos = atividade.getString("pontos");
                        atv.data = atividade.getString("data");
                        atv.hora = atividade.getString("hora");
                        atv.data_entrega = atividade.getString("dataentrega");
                        atv.disiciplina = atividade.getString("disciplina");
                        atv.curso = atividade.getString("curso");
                        atv.turma = atividade.getString("turma");
                        lista_atividades.add(atv);
                        atv = new Atividade();
                    }

                    listViewAdapterAtividades = new ListViewAdapterAtividades(getApplicationContext(), lista_atividades);
                    listaAtividade.setAdapter(listViewAdapterAtividades);
                }
                dialog.hide();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Erro na leitura de dados: " + e.getMessage(), Toast.LENGTH_SHORT);
                dialog.hide();
            }
        }
    }
}
