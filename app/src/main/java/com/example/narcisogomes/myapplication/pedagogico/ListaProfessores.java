package com.example.narcisogomes.myapplication.pedagogico;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.narcisogomes.myapplication.models.Curso;
import com.example.narcisogomes.myapplication.models.Professor;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterCursos;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterProfessores;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaProfessores extends AppCompatActivity {
    private ListView lv_professores;
    private ListViewAdapterProfessores lvap;
    private ArrayList<Professor> al_professores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ped_activity_lista_professores);
        lv_professores = findViewById(R.id.lista_professores);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Listagem de Professores");

        new BuscaProfessores().execute();

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
                    lvap.filter("");
                    lv_professores.clearTextFilter();
                }else{
                    lvap.filter(newText);
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

    private class BuscaProfessores extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ListaProfessores.this);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE,"acao=13");
            } catch (Exception e) {
                Toast.makeText(ListaProfessores.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            al_professores.clear();
            Professor professor  = new Professor();
            String responsebody = strings;
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(responsebody);
                boolean is = objeto.getBoolean("success");
                if(is){
                    JSONArray professores = objeto.getJSONArray("dados");

                    for(int i = 0; i< professores.length(); i++){
                        JSONObject professor_json = professores.getJSONObject(i);
                        professor.setId_professor(professor_json.getInt("id_professor"));
                        professor.setNome_usuario(professor_json.getString("nome_usuario"));
                        professor.setFormacao(professor_json.getString("formacao"));



                        al_professores.add(professor);
                        professor = new Professor();
                    }

                    lvap = new ListViewAdapterProfessores(ListaProfessores.this, al_professores);


                    lv_professores.setAdapter(lvap);

                }else{
                    Toast.makeText(ListaProfessores.this, "Não professores cadastrados", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.hide();
        }
    }
}

