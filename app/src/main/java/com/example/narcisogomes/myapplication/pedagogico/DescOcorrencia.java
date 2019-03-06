package com.example.narcisogomes.myapplication.pedagogico;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Ocorrencia;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterAlunos;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterAlunosOcorrencia;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterAlunosOcorrenciaDesc;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterOcorrencias;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DescOcorrencia extends AppCompatActivity {
    ListViewAdapterAlunosOcorrenciaDesc lvao;
    TextView desc_oc, data_oc;
    ListView lista_alunos;
    int ocorrencia_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ped_activity_desc_ocorrencia);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Descrição da Ocorrência");

        desc_oc = findViewById(R.id.descricao_oc);
        data_oc = findViewById(R.id.data_oc);
        lista_alunos = findViewById(R.id.lista_alunos_oc);

        Intent i = getIntent();
        ocorrencia_id = Integer.parseInt(i.getStringExtra("id_oc"));

        new BuscaOcorrencia().execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private class BuscaOcorrencia extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DescOcorrencia.this);
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
            Log.e("TESTANDO", "ID OCORRENCIA: "+ ocorrencia_id);
            String ab = "";
            try {
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=14"+
                "&um=true"+
                "&id_oc="+ ocorrencia_id);
            } catch (Exception e) {
                Toast.makeText(DescOcorrencia.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            Ocorrencia ocorrencia = new Ocorrencia();
            Aluno aluno = new Aluno();
            String responsebody = strings;
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(responsebody);
                boolean is = objeto.getBoolean("success");
                if (is) {

                    JSONArray ocorrencias = objeto.getJSONArray("dados");

                    for (int i = 0; i < ocorrencias.length(); i++) {
                        JSONObject oc_json = ocorrencias.getJSONObject(i);
                        ocorrencia.setId(oc_json.getInt("id_ocorrencia"));
                        ocorrencia.setDescricao(oc_json.getString("descricao"));
                        ocorrencia.setData(oc_json.getString("data"));
                        ocorrencia.setPedagogico_nome(oc_json.getString("nome"));

                        JSONArray  alunos   = oc_json.getJSONArray("alunos");

                        for(int ia = 0; ia <alunos.length(); ia++){
                            JSONObject aluno_json = alunos.getJSONObject(ia);
                            aluno.setNome_usuario(aluno_json.getString("nome"));
                            aluno.setId_aluno(aluno_json.getInt("id_aluno"));
                            aluno.setTurma(aluno_json.getString("turma"));
                            aluno.setCurso(aluno_json.getString("curso"));
                            ocorrencia.getArray_alunos().add(aluno);
                            aluno = new Aluno();
                        }

                        lvao = new ListViewAdapterAlunosOcorrenciaDesc(DescOcorrencia.this, ocorrencia.getArray_alunos());
                        lista_alunos.setAdapter(lvao);
                        data_oc.setText(ocorrencia.getData());
                        desc_oc.setText(ocorrencia.getDescricao());
                    }


                } else {
                    Toast.makeText(DescOcorrencia.this, "Não existem tarefas cadastradas para esta matéria", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(DescOcorrencia.this, "erro"+e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }

            dialog.hide();
        }
    }
}
