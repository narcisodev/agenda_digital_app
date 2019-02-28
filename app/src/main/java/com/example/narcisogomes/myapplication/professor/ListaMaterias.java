package com.example.narcisogomes.myapplication.professor;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Materia;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaMaterias extends AppCompatActivity {
    Materia m_geral = new Materia();
    ArrayList<Materia> materias = new ArrayList<>();
    ListView lista;
    private List<Map<String, Object>> materias_m = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professor_lista_materias_);
        lista = findViewById(R.id.lista_materias_prof);
        new CarregaDisciplinas().execute();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Suas Disciplinas");

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> materia = materias_m.get(position);
                //Values.id_disciplina_lista_materias = (Integer) materia.get("id");
                Values.id_disciplina_prof = (int) materia.get("id");
                Values.nome_disciplina_prof = (String) materia.get("nome");
                ListaMaterias.super.onBackPressed();
            }
        });
    }

    private List<Map<String,Object>> listarMaterias(){
        materias_m = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        Materia m = new Materia();
        for(int i  = 0; i < materias.size(); i++ ){
            m = materias.get(i);
            item.put("id", m.id);
            item.put("nome", m.nome);
            item.put("nome_curso", m.nome_curso);
            item.put("numero_turma", m.numero_turma);
            materias_m.add(item);
            item = new HashMap<String, Object>();
            m = new Materia();
        }
        return materias_m;
    }


    //MÉTODO USADO QUANDO UM COMPONENTE DO MENU É CLICADO
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }


    private class CarregaDisciplinas extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ListaMaterias.this);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=5" +
                        "&id_professor=" + Values_professor.professor.getId_professor());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(strings);
                boolean is = objeto.getBoolean("success");
                if (is) {
                    JSONArray disciplinas = objeto.getJSONArray("dados");
                    for (int i = 0; i < disciplinas.length(); i++) {
                        JSONObject disc_json = disciplinas.getJSONObject(i);
                        m_geral.id = disc_json.getInt("id");
                        m_geral.nome = disc_json.getString("nome");
                        m_geral.nome_curso = disc_json.getString("nome_curso");
                        m_geral.numero_turma = disc_json.getString("numero_turma");
                        materias.add(m_geral);
                        m_geral = new Materia();
                    }

                    String[] de ={"nome","nome_curso", "numero_turma"};
                    int[] para = {R.id.disciplina, R.id.curso,R.id.turma};
                    SimpleAdapter adapter= new SimpleAdapter(getApplicationContext(), listarMaterias(), R.layout.professor_lista_materias_modelo,de,para);
                    //adapter.setViewBinder(new FragListaMaterias.MateriasViewBinder());
                    lista.setAdapter(adapter);
                }
            } catch (JSONException e) {
                dialog.hide();
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            dialog.hide();
        }
    }
}
