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
import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Curso;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterAlunos;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterCursos;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaCursos extends AppCompatActivity {
    ListView lv_cursos;
    ArrayList<Curso> lista_cursos = new ArrayList<>();
    ListViewAdapterCursos lvac;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ped_activity_lista_cursos);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Listagem de cursos");

        lv_cursos = findViewById(R.id.lista_cursos);
        new BuscaCursos().execute();

    }

    @Override
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
                    lvac.filter("");
                    lv_cursos.clearTextFilter();
                }else{
                    lvac.filter(newText);
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


    private class BuscaCursos extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ListaCursos.this);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE,"acao=12");
            } catch (Exception e) {
                Toast.makeText(ListaCursos.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            lista_cursos.clear();
            Curso curso = new Curso();
            String responsebody = strings;
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(responsebody);
                boolean is = objeto.getBoolean("success");
                if(is){
                    JSONArray cursos = objeto.getJSONArray("dados");

                    for(int i = 0; i< cursos.length(); i++){
                        JSONObject curso_json = cursos.getJSONObject(i);
                        curso.setId(curso_json.getInt("id_curso"));
                        curso.setNome(curso_json.getString("nome_curso"));
                        curso.setSigla(curso_json.getString("sigla"));

                        lista_cursos.add(curso);
                        curso = new Curso();
                    }

                    lvac = new ListViewAdapterCursos(ListaCursos.this, lista_cursos);


                    lv_cursos.setAdapter(lvac);

                }else{
                    Toast.makeText(ListaCursos.this, "Não existem tarefas cadastradas para esta matéria", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.hide();
        }
    }
}
