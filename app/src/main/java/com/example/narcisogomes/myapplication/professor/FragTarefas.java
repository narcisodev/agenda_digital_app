package com.example.narcisogomes.myapplication.professor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragTarefas extends Fragment {
    private FloatingActionButton fab;

    private ArrayList<Atividade> lista_atividades = new ArrayList<>();
    private List<Map<String, Object>> atividades = new ArrayList<>();
    private ListView lv_tarefas;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.professor_frag_tarefas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = view.findViewById(R.id.fab);
        lv_tarefas = view.findViewById(R.id.lista_tarefas);

        //ACIONA AÇÃO DO CLICK NO BOTÃO FLUTUANTE
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TelaCadTarefas.class));
            }
        });

        //CLIQUES NA LISTVIEW - LISTA_MATERIAS
        lv_tarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Map<String, Object> atv = atividades.get(position);
                Values.atividade_obj = lista_atividades.get(position);
                //Values.id_atividade_lista_atv_prof = (Integer) atv.get("id_atividade");
                //Toast.makeText(getContext(), "Atividade: "+atv.get("titulo"), Toast.LENGTH_LONG).show();
                //iniciarNovoFragment();
                startActivity(new Intent(getContext(), TelaDescAtv.class));

            }
        });

        new BuscaTarefas().execute();
    }


    private List<Map<String, Object>> listarTarefas() {

        atividades = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        Atividade m = new Atividade();

        for (int i = 0; i < lista_atividades.size(); i++) {
            m = lista_atividades.get(i);
            item.put("id_atividade", m.id_atividade);
            item.put("titulo", m.titulo);
            item.put("descricao", m.descricao);
            item.put("pontos", m.pontos);
            item.put("data", m.data);
            item.put("dataentrega", m.data_entrega);
            item.put("disciplina", m.disiciplina);
            atividades.add(item);
            item = new HashMap<String, Object>();
            m = new Atividade();
        }
        return atividades;
    }


    //Adicionar preenchimento de tarefas cadastradas pelo prof


    private class BuscaTarefas extends AsyncTask<Void, Void, String> {
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=6" + "&id_professor=" + Values_professor.professor.getId_professor());
            } catch (Exception e) {
                Toast.makeText(getContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            Atividade atv = new Atividade();
            String responsebody = strings;
            JSONObject objeto = null;

            try {
                objeto = new JSONObject(responsebody);
                JSONArray a = objeto.getJSONArray("dados");
                boolean isTarefa = objeto.getBoolean("success");
                if (isTarefa) {
                    for (int i = 0; i < a.length(); i++) {
                        JSONObject atividade = a.getJSONObject(i);
                        atv.id_atividade = atividade.getInt("id_atividade");
                        atv.titulo = atividade.getString("titulo");
                        atv.descricao = atividade.getString("descricao");
                        atv.pontos = atividade.getString("pontos");
                        atv.data = atividade.getString("data");
                        atv.hora = atividade.getString("hora");
                        atv.data_entrega = atividade.getString("dataentrega");
                        atv.disiciplina = atividade.getString("disciplina");
                        lista_atividades.add(atv);
                        atv = new Atividade();
                    }

                    String[] de = {"dataentrega", "titulo", "disciplina"};
                    int[] para = {R.id.data_entrega, R.id.titulo_atividade, R.id.disciplina_atividade};
                    SimpleAdapter adapter = new SimpleAdapter(getContext(), listarTarefas(), R.layout.professor_modelo_lista_atvs, de, para);
                    lv_tarefas.setAdapter(adapter);
                }
                dialog.hide();
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Erro na leitura de dados: " + e.getMessage(), Toast.LENGTH_SHORT);
                dialog.hide();
            }
        }
    }
}
