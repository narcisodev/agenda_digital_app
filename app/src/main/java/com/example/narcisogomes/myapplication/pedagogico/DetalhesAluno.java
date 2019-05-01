package com.example.narcisogomes.myapplication.pedagogico;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.models.DetalhesAlunos;
import com.example.narcisogomes.myapplication.models.Ocorrencia;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterAtividadesAlunos;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterOcorrenciasAlunos;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetalhesAluno extends AppCompatActivity {
    boolean refresh = false;
    ProgressDialog dialog;
    DetalhesAlunos das = new DetalhesAlunos();
    TextView n_a, email, n_r, matricula, curso, turma, t_occ, t_t;
    ListView lista_occ, lista_tarefas;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ped_activity_detalhes_aluno);

        //componentes
        n_a = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        n_r = findViewById(R.id.responsavel);
        matricula = findViewById(R.id.matricula);
        curso = findViewById(R.id.curso);
        turma = findViewById(R.id.turma);
        t_occ = findViewById(R.id.ocorrencias);
        t_t = findViewById(R.id.tarefas_qtd);
        lista_occ = findViewById(R.id.lista_occ);
        lista_tarefas = findViewById(R.id.lista_tarefas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detalhes do aluno");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh = true;
                new BuscarDetalhesAluno().execute();
            }
        });



        new BuscarDetalhesAluno().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private class BuscarDetalhesAluno extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DetalhesAluno.this);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=20" +
                        "&id_al=" + Values_pedagogico.id_aluno);
            } catch (Exception e) {
                Toast.makeText(DetalhesAluno.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                    JSONObject d_a = objeto.getJSONObject("dados");
                    das.setN_a(d_a.getString("n_a"));
                    das.setEmail(d_a.getString("email"));
                    das.setN_p(d_a.getString("n_p"));
                    das.setCurso(d_a.getString("nome_curso"));
                    das.setMatricula(d_a.getString("matricula"));
                    das.setTurma(d_a.getString("turma"));
                    das.setT_occ(d_a.getInt("qtd_occ"));
                    das.setT_tar(d_a.getInt("qtd_tarefas"));

                    JSONArray ocorrencias = d_a.getJSONArray("ocorrencias");
                    das.getLista_occ().clear();
                    for (int i = 0; i < ocorrencias.length(); i++) {
                        Ocorrencia oc = new Ocorrencia();
                        JSONObject oc_j = ocorrencias.getJSONObject(i);
                        oc.setDescricao(oc_j.getString("descricao"));
                        oc.setId(oc_j.getInt("id_ocorrencia"));
                        oc.setData(oc_j.getString("data"));
                        das.getLista_occ().add(oc);
                    }

                    JSONArray tarefas = d_a.getJSONArray("tarefas");
                    das.getLista_atv().clear();
                    for (int i = 0; i < tarefas.length(); i++) {
                        Atividade atv = new Atividade();
                        JSONObject jat = tarefas.getJSONObject(i);
                        atv.id_atividade = jat.getInt("id_atv");
                        atv.descricao = jat.getString("descricao");
                        atv.data_entrega = jat.getString("dataentrega");
                        atv.disiciplina = jat.getString("disciplina");
                        das.getLista_atv().add(atv);
                    }
                    dialog.hide();
                    trataExibicao();
                }


            } catch (JSONException e) {
                dialog.hide();
                Toast.makeText(DetalhesAluno.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void trataExibicao() {
        n_a.setText(das.getN_a());
        n_r.setText(das.getN_p());
        email.setText(das.getEmail());
        matricula.setText(das.getMatricula());
        curso.setText(das.getCurso());
        turma.setText(das.getTurma());
        if (das.getT_occ() == 0) {
            t_occ.setText(getResources().getString(R.string.sem_occ_reg));
        } else if (das.getT_occ() == 1) {
            t_occ.setText(getResources().getString(R.string.um_occ_reg));
        } else if (das.getT_occ() > 1) {
            t_occ.setText(das.getT_occ() + " " + getResources().getString(R.string.occs_cad));
        }
        ListViewAdapterOcorrenciasAlunos adapter = new ListViewAdapterOcorrenciasAlunos(DetalhesAluno.this, das.getLista_occ());
        lista_occ.setAdapter(adapter);

        ListViewAdapterAtividadesAlunos lvaa = new ListViewAdapterAtividadesAlunos(DetalhesAluno.this, das.getLista_atv());
        lista_tarefas.setAdapter(lvaa);

        if (das.getT_tar() == 0) {
            t_t.setText(getResources().getString(R.string.sem_tar_reg));
        } else if (das.getT_tar() == 1) {
            t_t.setText(getResources().getString(R.string.um_tar_reg));
        } else if (das.getT_occ() > 1) {
            t_t.setText(das.getT_tar() + " " + getResources().getString(R.string.tars_cad));
        }


        if (refresh) {
            Snackbar.make(fab, "Atualizado com sucesso", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }
}
