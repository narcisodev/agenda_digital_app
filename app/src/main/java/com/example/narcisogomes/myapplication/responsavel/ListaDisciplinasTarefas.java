package com.example.narcisogomes.myapplication.responsavel;

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
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.aluno.FragListaMaterias;
import com.example.narcisogomes.myapplication.aluno.ListaTarefasIdDisc;
import com.example.narcisogomes.myapplication.aluno.Values_aluno;
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

public class ListaDisciplinasTarefas extends AppCompatActivity {
    private ListView listaMaterias;
    private List<Map<String, Object>> materias = new ArrayList<>();
    ArrayList<Materia> materiasw = new ArrayList<>();
    private int cor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ped_activity_lista_disciplinas_tarefas);
        listaMaterias = findViewById(R.id.lista_disci_mat_resp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle(R.string.consulta_atividades);
        new BuscaMaterias().execute();

        listaMaterias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> materia = materias.get(position);
                int qtd_tarefa = (int) materia.get("qtd_tarefas");
                if (qtd_tarefa == 0) {
                    Toast.makeText(ListaDisciplinasTarefas.this, "Não há tarefas cadastradas para esta disciplina", Toast.LENGTH_LONG).show();
                } else {
                    Values.id_disciplina_lista_materias = (Integer) materia.get("id");
                    //Toast.makeText(getContext(), "Materia: "+ "Nome:" + materia.get("materia")+ " ID: "+ materia.get("id"), Toast.LENGTH_LONG).show();
                    Values_aluno.is_search_disc = true;
                    startActivity(new Intent(ListaDisciplinasTarefas.this, ListaTarefasIdDisc.class));
                }
            }
        });
    }


    private List<Map<String, Object>> listarMaterias() {

        materias = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        Materia m = new Materia();
        Log.e(Values.TAG, "LISTAR MATERIAS: " + materiasw.size());

        for (int i = 0; i < materiasw.size(); i++) {
            m = materiasw.get(i);
            item.put("id", m.id);
            item.put("materia", m.nome);
            item.put("professor", m.professor);
            item.put("qtd_tarefas", m.qtd_tarefas);
            String nome_l = m.nome;
            char letra_i = nome_l.charAt(0);
            item.put("letra_inicial", letra_i+"");
            materias.add(item);
            item = new HashMap<String, Object>();
            m = new Materia();
        }
        return materias;
    }

    private class BuscaMaterias extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ListaDisciplinasTarefas.this);
            dialog.setCanceledOnTouchOutside(false);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "cont_m=1&acao=2&idaluno=" + Values_resp.id_aluno_selecionado);
            } catch (Exception e) {
                Toast.makeText(ListaDisciplinasTarefas.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            Materia m = new Materia();
            String responsebody = strings;
            JSONObject objeto = null;

            try {
                objeto = new JSONObject(responsebody);
                JSONArray a = objeto.getJSONArray("dados");
                for (int i = 0; i < a.length(); i++) {

                    JSONObject materia = a.getJSONObject(i);
                    m.id = materia.getInt("id");
                    m.nome = materia.getString("nome");
                    m.professor = materia.getString("professor");
                    m.qtd_tarefas = materia.getInt("qtd_tarefas");
                    materiasw.add(m);
                    m = new Materia();
                }

                String[] de = {"materia", "professor", "letra_inicial", "qtd_tarefas"};
                int[] para = {R.id.materia, R.id.professor, R.id.letra_inicial, R.id.qtd_tarefas};
                SimpleAdapter adapter = new SimpleAdapter(ListaDisciplinasTarefas.this, listarMaterias(), R.layout.aluno_frag_lista_materias_modelo, de, para);
                //adapter.setViewBinder(new FragListaMaterias.MateriasViewBinder());
                listaMaterias.setAdapter(adapter);
                dialog.hide();
            } catch (JSONException e) {
                Toast.makeText(ListaDisciplinasTarefas.this, "Erro na leitura de dados: " + e.getMessage(), Toast.LENGTH_SHORT);
                dialog.hide();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
