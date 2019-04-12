package com.example.narcisogomes.myapplication.responsavel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DescAluno extends AppCompatActivity {
    TextView nome, email, matricula, curso, turma;
    CardView atv_aula, rel_academico, cons_notas, cons_faltas;
    Aluno obj_aluno = new Aluno();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resp_act_desc_aluno);
        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        matricula = findViewById(R.id.matricula);
        curso = findViewById(R.id.curso);
        turma = findViewById(R.id.turma);

        atv_aula = findViewById(R.id.atv_de_aula);
        rel_academico = findViewById(R.id.rel_academico);
        cons_notas = findViewById(R.id.cons_notas);
        cons_faltas = findViewById(R.id.cons_faltas);

        atv_aula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DescAluno.this, ListaTarefasFilho.class));
            }
        });

        rel_academico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DescAluno.this, "RELATÓRIO ACADÊMICO", Toast.LENGTH_SHORT).show();
            }
        });

        cons_notas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DescAluno.this, "CONSULTAR NOTAS", Toast.LENGTH_SHORT).show();
            }
        });

        cons_faltas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DescAluno.this, "CONSULTAR FALTAS", Toast.LENGTH_SHORT).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Descrição do Aluno");
        new CarregaDescAlunos().execute();
    }

    /*
     * AÇÃO QUANDO O USUÁRIO APERTAR A SETA DE VOLTAR
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }

    /*
     * BUSCAR DADOS DO ALUNO DE ACORDO COM O ID DA VALUES_RESP
     *
     */
    private class CarregaDescAlunos extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(DescAluno.this);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=8" +
                        "&id_aluno=" + Values_resp.id_aluno_selecionado);
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
                    JSONArray aluno = objeto.getJSONArray("dados");
                    for (int i = 0; i < aluno.length(); i++) {
                        JSONObject aln_json = aluno.getJSONObject(i);
                        obj_aluno.setId_aluno(aln_json.getInt("id_aluno"));
                        obj_aluno.setNome_usuario(aln_json.getString("nome"));
                        obj_aluno.setEmail(aln_json.getString("email"));
                        obj_aluno.setMatricula(aln_json.getString("matricula"));
                        obj_aluno.setCurso(aln_json.getString("curso"));
                        obj_aluno.setTurma(aln_json.getString("turma"));
                    }
                }

                nome.setText("Nome: "+obj_aluno.getNome_usuario());
                email.setText("Email: "+obj_aluno.getEmail());
                matricula.setText("Matrícula: "+obj_aluno.getMatricula());
                curso.setText("Curso: "+obj_aluno.getCurso());
                turma.setText("Turma: "+obj_aluno.getTurma());
            } catch (JSONException e) {
                dialog.hide();
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            dialog.hide();
        }
    }
}
