package com.example.narcisogomes.myapplication.pedagogico;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.aluno.ListAdapters.ListViewAdapterAtividades;
import com.example.narcisogomes.myapplication.aluno.ListaTarefas;
import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterAlunos;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaAlunos extends AppCompatActivity {
    ArrayList<Aluno> lista_alunos = new ArrayList<>();
    ListViewAdapterAlunos laa;
    ListView lv_alunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ped_activity_lista_alunos);
        lv_alunos = findViewById(R.id.lista_alunos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Listagem de alunos");

        new BuscaAlunos().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pedagogico_lista_alunos, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search_ped);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    laa.filter("");
                    lv_alunos.clearTextFilter();
                } else {
                    laa.filter(newText);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private class BuscaAlunos extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ListaAlunos.this);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=11");
            } catch (Exception e) {
                Toast.makeText(ListaAlunos.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            lista_alunos.clear();
            Aluno aluno = new Aluno();
            String responsebody = strings;
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(responsebody);
                boolean is = objeto.getBoolean("success");
                if (is) {
                    JSONArray alunos = objeto.getJSONArray("dados");

                    for (int i = 0; i < alunos.length(); i++) {
                        JSONObject aluno_json = alunos.getJSONObject(i);
                        aluno.setId_aluno(aluno_json.getInt("id_aluno"));
                        aluno.setCurso(aluno_json.getString("curso"));
                        aluno.setNome_usuario(aluno_json.getString("nome"));
                        aluno.setTurma(aluno_json.getString("turma"));
                        lista_alunos.add(aluno);
                        aluno = new Aluno();
                    }

                    laa = new ListViewAdapterAlunos(ListaAlunos.this, lista_alunos);
                    lv_alunos.setAdapter(laa);

                } else {
                    Toast.makeText(ListaAlunos.this, "Não existem tarefas cadastradas para esta matéria", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.hide();
        }
    }
}
