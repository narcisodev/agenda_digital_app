package com.example.narcisogomes.myapplication.pedagogico;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.models.DeltalhesProf;
import com.example.narcisogomes.myapplication.models.Materia;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewDisciplinasProfessor;
import com.example.narcisogomes.myapplication.professor.TelaDescAtv;
import com.example.narcisogomes.myapplication.professor.Values_professor;
import com.example.narcisogomes.myapplication.responsavel.ListaDisciplinasTarefas;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class DetalhesProfessor extends AppCompatActivity {
    private ProgressDialog dialog;

    private TextView nome_prof, email_prof, matricula_professor, formaca_prof, na1, na2, na3, ta1, ta2, ta3, qtd_disciplinas, qtd_atividades, id_atividade1, id_atividade2, id_atividade3;
    private LinearLayout atividade_1, atividade_2, atividade_3, ver_mais_atividade, sem_atividade;
    JSONObject json_detalhes_prof;
    DeltalhesProf dtp = new DeltalhesProf();
    ListView listaDisciplinas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ped_activity_detalhes_professor);
        nome_prof = findViewById(R.id.nome);
        email_prof = findViewById(R.id.email);
        matricula_professor = findViewById(R.id.matricula);
        listaDisciplinas = findViewById(R.id.lista_disciplinas);

        //ID_ATIVIDADE E DISCIPLINAS
        id_atividade1 = findViewById(R.id.id_atividade1);
        id_atividade2 = findViewById(R.id.id_atividade2);
        id_atividade3 = findViewById(R.id.id_atividade3);

        //DICIPLINAS
        formaca_prof = findViewById(R.id.formacao);

        //ATIVIDADES
        atividade_1 = findViewById(R.id.atividade_1);
        atividade_2 = findViewById(R.id.atividade_2);
        atividade_3 = findViewById(R.id.atividade_3);
        na1 = findViewById(R.id.nome_atividade_1);
        ta1 = findViewById(R.id.turma_atividade_1);
        na2 = findViewById(R.id.nome_atividade_2);
        ta2 = findViewById(R.id.turma_atividade_2);
        na3 = findViewById(R.id.nome_atividade_3);
        ta3 = findViewById(R.id.turma_atividade_3);
        ver_mais_atividade = findViewById(R.id.ver_mais_atividade);
        sem_atividade = findViewById(R.id.sem_atividade);

        qtd_disciplinas = findViewById(R.id.disciplinas_lecionadas);
        qtd_atividades = findViewById(R.id.atividades_cadastradas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Detalhes do Professor");
        new BuscarDetalhesProfessor().execute();


        atividade_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nome = v.findViewById(R.id.nome_atividade_1);
                TextView id_t = v.findViewById(R.id.id_atividade1);
                int id = Integer.parseInt(id_t.getText().toString());

                for(Atividade a: dtp.lista_atividades){
                    if(a.id_atividade == id){
                        Values_professor.atividade_obj = a;
                        startActivity(new Intent(DetalhesProfessor.this, TelaDescAtv.class));
                    }
                }


            }
        });

        atividade_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nome = v.findViewById(R.id.nome_atividade_2);
                TextView id_t = v.findViewById(R.id.id_atividade2);
                int id = Integer.parseInt(id_t.getText().toString());
                for(Atividade a: dtp.lista_atividades){
                    if(a.id_atividade == id){
                        Values_professor.atividade_obj = a;
                        startActivity(new Intent(DetalhesProfessor.this, TelaDescAtv.class));
                    }
                }
            }
        });

        atividade_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nome = v.findViewById(R.id.nome_atividade_3);
                TextView id_t = v.findViewById(R.id.id_atividade3);
                int id = Integer.parseInt(id_t.getText().toString());
                for(Atividade a: dtp.lista_atividades){
                    if(a.id_atividade == id){
                        Values_professor.atividade_obj = a;
                        startActivity(new Intent(DetalhesProfessor.this, TelaDescAtv.class));
                    }
                }
            }
        });

        ver_mais_atividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DetalhesProfessor.this, ListaAtividadesProfessor.class));
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);

    }

    private class BuscarDetalhesProfessor extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DetalhesProfessor.this);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=18" +
                        "&id_prof=" + Values_pedagogico.id_professor);
            } catch (Exception e) {
                Toast.makeText(DetalhesProfessor.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                    JSONArray details_teacher = objeto.getJSONArray("dados");
                    for (int i = 0; i < details_teacher.length(); i++) {
                        json_detalhes_prof = details_teacher.getJSONObject(i);
                        dtp.id_prof = json_detalhes_prof.getInt("id_professor");
                        dtp.nome = json_detalhes_prof.getString("nome_usuario");
                        dtp.email = json_detalhes_prof.getString("email");
                        dtp.matricula = json_detalhes_prof.getString("matricula");
                        dtp.formacao = json_detalhes_prof.getString("formacao");
                        dtp.qtd_materias = json_detalhes_prof.getInt("quantidade_materias");
                        dtp.qtd_atividades = json_detalhes_prof.getInt("quantidade_atividades");

                        JSONArray array_mat = json_detalhes_prof.getJSONArray("materias");
                        for (int a = 0; a < array_mat.length(); a++) {
                            JSONObject json_m = array_mat.getJSONObject(a);
                            Materia m = new Materia();
                            m.id = json_m.getInt("id");
                            m.nome = json_m.getString("nome");
                            m.nome_curso = json_m.getString("nome_curso");
                            m.sigla_curso = json_m.getString("sigla_curso");
                            m.numero_turma = json_m.getString("numero_turma");
                            m.qtd_tarefas = json_m.getInt("qtd_tarefas");
                            dtp.lista_materias.add(m);
                        }

                        JSONArray array_tarefas = json_detalhes_prof.getJSONArray("atividades_prof");
                        for (int b = 0; b < array_tarefas.length(); b++) {
                            JSONObject atividade = array_tarefas.getJSONObject(b);
                            Atividade atv = new Atividade();
                            atv.id_atividade = atividade.getInt("id_atividade");
                            atv.titulo = atividade.getString("titulo");
                            atv.descricao = atividade.getString("descricao");
                            atv.pontos = atividade.getString("pontos");
                            atv.data = atividade.getString("data");
                            atv.hora = atividade.getString("hora");
                            atv.data_entrega = atividade.getString("dataentrega");
                            atv.disiciplina = atividade.getString("disciplina");
                            atv.turma = atividade.getString("turma");
                            atv.curso = atividade.getString("curso");
                            dtp.lista_atividades.add(atv);
                        }
                    }

                    tratarExibicao();
                }

            } catch (JSONException e) {
                dialog.hide();
                Toast.makeText(DetalhesProfessor.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void listarDisciplinasProfessor(){
        ListViewDisciplinasProfessor lap = new ListViewDisciplinasProfessor(getApplicationContext(), dtp.lista_materias);
        listaDisciplinas.setAdapter(lap);
    }

    private void tratarExibicao() {
        nome_prof.setText(dtp.nome);
        email_prof.setText(dtp.email);
        matricula_professor.setText(dtp.matricula);
        formaca_prof.setText(dtp.formacao);
        tratarExibicaoAtividades();
        tratarExibicaoQtds();
        listarDisciplinasProfessor();
        dialog.hide();
    }

    private void tratarExibicaoQtds() {
        if(dtp.qtd_materias == 0){
            qtd_disciplinas.setText("Sem disciplinas lecionandas");
        }else if(dtp.qtd_materias == 1){
            qtd_disciplinas.setText(dtp.qtd_materias + " disciplina lecionada");
        }else if(dtp.qtd_materias > 1){
            qtd_disciplinas.setText(dtp.qtd_materias + " disciplinas lecionadas");
        }

        if(dtp.qtd_atividades == 0){
            qtd_atividades.setText("Sem atividades cadastradas");
        }else if(dtp.qtd_atividades == 1){
            qtd_atividades.setText(dtp.qtd_atividades + " atividade cadastrada");
        }else if(dtp.qtd_atividades > 1){
            qtd_atividades.setText(dtp.qtd_atividades + " atividades cadastradas");
        }
    }


    private void tratarExibicaoAtividades() {

        sem_atividade.setVisibility(View.GONE);
        ver_mais_atividade.setVisibility(View.GONE);
        if (dtp.lista_atividades.size() >= 3) {
            Atividade atividade = dtp.lista_atividades.get(0);
            na1.setText(atividade.titulo);
            ta1.setText(atividade.data);
            Log.e("NARCISO222", "ID_ATIVIDADE="+atividade.id_atividade);
            id_atividade1.setText(atividade.id_atividade+"");

            atividade = dtp.lista_atividades.get(1);
            na2.setText(atividade.titulo);
            ta2.setText(atividade.data);
            Log.e("NARCISO222", "ID_ATIVIDADE="+atividade.id_atividade);
            id_atividade2.setText(atividade.id_atividade+"");

            atividade = dtp.lista_atividades.get(2);
            na3.setText(atividade.titulo);
            ta3.setText(atividade.data);
            id_atividade3.setText(atividade.id_atividade+"");

            if(dtp.lista_atividades.size()>3){
                ver_mais_atividade.setVisibility(View.VISIBLE);
            }

        }else

        if (dtp.lista_atividades.size() == 2) {
            Atividade atividade = dtp.lista_atividades.get(0);
            na1.setText(atividade.titulo);
            ta1.setText(atividade.data);
            id_atividade1.setText(atividade.id_atividade+"");

            atividade = dtp.lista_atividades.get(1);
            na2.setText(atividade.titulo);
            ta2.setText(atividade.data);
            id_atividade2.setText(atividade.id_atividade+"");
            atividade_3.setVisibility(View.GONE);
        }else

        if (dtp.lista_atividades.size() == 1) {
            Atividade atividade = dtp.lista_atividades.get(0);
            na1.setText(atividade.titulo);
            ta1.setText(atividade.data);
            id_atividade1.setText(atividade.id_atividade+"");

            atividade_2.setVisibility(View.GONE);
            atividade_3.setVisibility(View.GONE);
        }else if(dtp.lista_atividades.size() == 0){
            atividade_3.setVisibility(View.GONE);
            atividade_2.setVisibility(View.GONE);
            atividade_1.setVisibility(View.GONE);
            sem_atividade.setVisibility(View.VISIBLE);

        }
    }
}
