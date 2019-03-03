package com.example.narcisogomes.myapplication.professor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Materia;
import com.example.narcisogomes.myapplication.professor.ListAdapters.ListViewAdapterAtividades;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class FragHome extends Fragment {
    TextView nome, email, login, matricula, formacao;
    TextView nome_disciplina_1, turma_disciplina_1, nome_disciplina_2, turma_disciplina_2, nome_disciplina_3, turma_disciplina_3;
    Materia materia = new Materia();
    List<Materia> list_materias = new ArrayList<>();
    LinearLayout ll_sem_disciplina, ll_ver_mais, ll_d1, ll_d2, ll_d3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.professor_frag_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nome_disciplina_1 = view.findViewById(R.id.nome_disciplina_1);
        turma_disciplina_1 = view.findViewById(R.id.turma_disciplina_1);
        nome_disciplina_2 = view.findViewById(R.id.nome_disciplina_2);
        turma_disciplina_2 = view.findViewById(R.id.turma_disciplina_2);
        nome_disciplina_3 = view.findViewById(R.id.nome_disciplina_3);
        turma_disciplina_3 = view.findViewById(R.id.turma_disciplina_3);
        ll_sem_disciplina = view.findViewById(R.id.sem_disciplina);
        ll_ver_mais = view.findViewById(R.id.ver_mais);
        ll_d1 = view.findViewById(R.id.disciplina_1);
        ll_d2 = view.findViewById(R.id.disciplina_2);
        ll_d3 = view.findViewById(R.id.disciplina_3);

        nome = view.findViewById(R.id.nome);
        email = view.findViewById(R.id.email);
        login = view.findViewById(R.id.login);
        matricula = view.findViewById(R.id.matricula);
        formacao = view.findViewById(R.id.formacao);
        nome.setText(Values_professor.professor.getNome_usuario());
        email.setText(Values_professor.professor.getEmail());
        login.setText(Values_professor.professor.getLogin());
        matricula.setText(Values_professor.professor.getMatricula());
        formacao.setText(Values_professor.professor.getFormacao());

        ll_ver_mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ListaMaterias.class));
            }
        });



        new CarregaDisciplinas().execute();
    }

    private void tratarExibicaoDisciplinas() {

        ll_sem_disciplina.setVisibility(View.GONE);
        ll_ver_mais.setVisibility(View.GONE);
        if (list_materias.size() >= 3) {
            materia = list_materias.get(0);
            nome_disciplina_1.setText(materia.nome);
            turma_disciplina_1.setText(materia.sigla_curso+ " " +materia.numero_turma);
            materia = list_materias.get(1);
            nome_disciplina_2.setText(materia.nome);
            turma_disciplina_2.setText(materia.sigla_curso+ " " +materia.numero_turma);
            materia = list_materias.get(2);
            nome_disciplina_3.setText(materia.nome);
            turma_disciplina_3.setText(materia.sigla_curso+ " " +materia.numero_turma);
            if(list_materias.size()>3){
                ll_ver_mais.setVisibility(View.VISIBLE);
            }

        }else

        if (list_materias.size() == 2) {
            materia = list_materias.get(0);
            nome_disciplina_1.setText(materia.nome);
            turma_disciplina_1.setText(materia.sigla_curso+ " " +materia.numero_turma);
            materia = list_materias.get(1);
            nome_disciplina_2.setText(materia.nome);
            turma_disciplina_2.setText(materia.sigla_curso+ " " +materia.numero_turma);
            ll_d3.setVisibility(View.GONE);
        }else

        if (list_materias.size() == 1) {
            materia = list_materias.get(0);
            nome_disciplina_1.setText(materia.nome);
            turma_disciplina_1.setText(materia.sigla_curso+ " " +materia.numero_turma);
            ll_d2.setVisibility(View.GONE);
            ll_d3.setVisibility(View.GONE);
        }else if(list_materias.size() == 0){
            ll_d1.setVisibility(View.GONE);
            ll_d2.setVisibility(View.GONE);
            ll_d3.setVisibility(View.GONE);
            ll_sem_disciplina.setVisibility(View.VISIBLE);

        }
    }


    private class CarregaDisciplinas extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getContext());
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
                Toast.makeText(getContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                        materia.id = disc_json.getInt("id");
                        materia.nome = disc_json.getString("nome");
                        materia.nome_curso = disc_json.getString("nome_curso");
                        materia.numero_turma = disc_json.getString("numero_turma");
                        materia.sigla_curso = disc_json.getString("sigla_curso");
                        list_materias.add(materia);
                        materia = new Materia();
                    }
                    Values_professor.list_materias = list_materias;
                }
                tratarExibicaoDisciplinas();
            } catch (JSONException e) {
                dialog.hide();
                Toast.makeText(getContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            dialog.hide();
        }
    }


}
