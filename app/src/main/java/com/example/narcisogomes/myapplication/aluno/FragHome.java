package com.example.narcisogomes.myapplication.aluno;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.models.Materia;
import com.example.narcisogomes.myapplication.professor.TelaDescAtv;
import com.example.narcisogomes.myapplication.professor.Values_professor;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragHome extends Fragment {

    ArrayList<Atividade> lista_atv_urgentes = new ArrayList<>();

    TextView txt_nome, txt_email, txt_login, txt_responsavel, txt_matricula,txt_curso,txt_turma;
    TextView dt1, ta1, d1,dt2, ta2, d2,dt3, ta3, d3,id_atv_1,id_atv_2,id_atv_3;

    LinearLayout atividade_1, atividade_2, atividade_3, sem_atividade, ver_mais;
    Atividade a = new Atividade();//obj usado para a passagem de parametros no Ask Task buscarTarefas

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.aluno_frag_tela_home, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_nome = view.findViewById(R.id.nome);
        txt_email = view.findViewById(R.id.email);
        txt_login = view.findViewById(R.id.login);
        txt_responsavel = view.findViewById(R.id.responsavel);
        txt_matricula = view.findViewById(R.id.matricula);
        txt_curso = view.findViewById(R.id.curso);
        txt_turma = view.findViewById(R.id.turma);
        atividade_1 = view.findViewById(R.id.atividade_1);
        atividade_2 = view.findViewById(R.id.atividade_2);
        atividade_3 = view.findViewById(R.id.atividade_3);
        dt1 = view.findViewById(R.id.data_entrega_1);
        dt2 = view.findViewById(R.id.data_entrega_2);
        dt3 = view.findViewById(R.id.data_entrega_3);
        ta1 = view.findViewById(R.id.titulo_atividade_1);
        ta2 = view.findViewById(R.id.titulo_atividade_2);
        ta3 = view.findViewById(R.id.titulo_atividade_3);
        d1 = view.findViewById(R.id.nome_disciplina_1);
        d2 = view.findViewById(R.id.nome_disciplina_2);
        d3 = view.findViewById(R.id.nome_disciplina_3);
        id_atv_1 = view.findViewById(R.id.id_atividade_1);
        id_atv_2 = view.findViewById(R.id.id_atividade_2);
        id_atv_3 = view.findViewById(R.id.id_atividade_3);
        ver_mais = view.findViewById(R.id.ver_mais);


        sem_atividade = view.findViewById(R.id.sem_atividade);

        txt_nome.setText(Values_aluno.aluno.getNome_usuario());
        txt_email.setText(Values_aluno.aluno.getEmail());
        txt_login.setText(Values_aluno.aluno.getLogin());
        txt_responsavel.setText(Values_aluno.aluno.getResponsavel());
        txt_matricula.setText(Values_aluno.aluno.getMatricula());
        txt_curso.setText(Values_aluno.aluno.getCurso());
        txt_turma.setText(Values_aluno.aluno.getTurma());

        ver_mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Values_aluno.is_search_disc = false;
                startActivity(new Intent(getContext(), TelaTodasAtividades.class));
            }
        });

        atividade_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = Integer.parseInt((String) id_atv_1.getText());
                Atividade a = buscarAtividadePeloID(id);
                if(a != null){
                    Values_aluno.is_obj = true;
                    Values_aluno.atividade_desc = a;
                    startActivity(new Intent(getContext(), TelaDescAtividade.class));
                    Toast.makeText(getContext(), "ID ATIVIDADE: "+ a.id_atividade+"\n TÍTULO: "+ a.titulo, Toast.LENGTH_LONG).show();
                }

            }
        });

        atividade_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt((String) id_atv_2.getText());
                Atividade a = buscarAtividadePeloID(id);
                if(a != null){
                    Values_aluno.is_obj = true;
                    Values_aluno.atividade_desc = a;
                    startActivity(new Intent(getContext(), TelaDescAtividade.class));
                    Toast.makeText(getContext(), "ID ATIVIDADE: "+ a.id_atividade+"\n TÍTULO: "+ a.titulo, Toast.LENGTH_LONG).show();
                }
            }
        });

        atividade_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt((String) id_atv_3.getText());
                Atividade a = buscarAtividadePeloID(id);
                if(a != null){
                    Values_aluno.is_obj = true;
                    Values_aluno.atividade_desc = a;
                    startActivity(new Intent(getContext(), TelaDescAtividade.class));
                    Toast.makeText(getContext(), "ID ATIVIDADE: "+ a.id_atividade+"\n TÍTULO: "+ a.titulo, Toast.LENGTH_LONG).show();
                }
            }
        });

        new CarregaAtividades().execute();
    }


    /**
    * MÉTODO QUE GERENCIA AS TAREFAS URGENTES
    * */
    private void tratarExibicaoTarefas() {

        sem_atividade.setVisibility(View.GONE);
        ver_mais.setVisibility(View.GONE);
        if (lista_atv_urgentes.size() >= 3) {
            a = lista_atv_urgentes.get(0);
            dt1.setText(a.data_entrega);
            ta1.setText(a.titulo);
            d1.setText(a.disiciplina);
            id_atv_1.setVisibility(View.GONE);
            id_atv_1.setText(a.id_atividade+"");

            a = lista_atv_urgentes.get(1);
            dt2.setText(a.data_entrega);
            ta2.setText(a.titulo);
            d2.setText(a.disiciplina);
            id_atv_2.setVisibility(View.GONE);
            id_atv_2.setText(a.id_atividade+"");

            a = lista_atv_urgentes.get(2);
            dt3.setText(a.data_entrega);
            ta3.setText(a.titulo);
            d3.setText(a.disiciplina);
            id_atv_3.setVisibility(View.GONE);
            id_atv_3.setText(a.id_atividade+"");
            if(lista_atv_urgentes.size() > 3){
                ver_mais.setVisibility(View.VISIBLE);
            }

        }else

        if (lista_atv_urgentes.size() == 2) {
            a = lista_atv_urgentes.get(0);
            dt1.setText(a.data_entrega);
            ta1.setText(a.titulo);
            d1.setText(a.disiciplina);
            id_atv_1.setText(a.id_atividade+"");

            a = lista_atv_urgentes.get(1);
            dt2.setText(a.data_entrega);
            ta2.setText(a.titulo);
            d2.setText(a.disiciplina);
            id_atv_2.setText(a.id_atividade+"");
            atividade_3.setVisibility(View.GONE);
        }else

        if (lista_atv_urgentes.size() == 1) {
            a = lista_atv_urgentes.get(0);
            dt1.setText(a.data_entrega);
            ta1.setText(a.titulo);
            d1.setText(a.disiciplina);
            id_atv_1.setText(a.id_atividade+"");
            atividade_2.setVisibility(View.GONE);
            atividade_3.setVisibility(View.GONE);
        }else if(lista_atv_urgentes.size() == 0){
            atividade_1.setVisibility(View.GONE);
            atividade_2.setVisibility(View.GONE);
            atividade_3.setVisibility(View.GONE);
            sem_atividade.setVisibility(View.VISIBLE);

        }
    }


    private Atividade buscarAtividadePeloID(int id){
        Atividade a = new Atividade();
        for(int i = 0; i < lista_atv_urgentes.size(); i++){
            a = lista_atv_urgentes.get(i);
            if(a.id_atividade == id){
                return a;
            }
        }

        return null;
    }




    /**
    * A TASK PARA CARREGAR AS TAREFAS URGENTES
    * */

    private class CarregaAtividades extends AsyncTask<Void, Void, String> {
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=3.1" +
                        "&limit=true&id_turma=" + Values_aluno.id_turma);
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
                        a.id_atividade = disc_json.getInt("id_atividade");
                        a.titulo = disc_json.getString("titulo");
                        a.descricao = disc_json.getString("descricao");
                        a.pontos = disc_json.getString("pontos");
                        a.data = disc_json.getString("data");
                        a.data_entrega = disc_json.getString("dataentrega");
                        a.disiciplina = disc_json.getString("disciplina");
                        a.datacriacao = disc_json.getString("datacriacao");
                        lista_atv_urgentes.add(a);
                        a = new Atividade();
                    }

                    tratarExibicaoTarefas();

                }
            } catch (JSONException e) {
                dialog.hide();
                Toast.makeText(getContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            dialog.hide();
        }
    }




}

