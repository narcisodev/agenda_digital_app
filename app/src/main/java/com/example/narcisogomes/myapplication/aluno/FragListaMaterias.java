package com.example.narcisogomes.myapplication.aluno;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FragListaMaterias extends Fragment {
    private ListView listaMaterias;
    private List<Map<String, Object>> materias = new ArrayList<>();
    ArrayList<Materia> materiasw = new ArrayList<>();
    private int cor = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.aluno_frag_list_materias, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //meus controllers
        listaMaterias = view.findViewById(R.id.lista_atividades);
        new BuscaMaterias().execute();
        listaMaterias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Map<String, Object> materia = materias.get(position);
                int qtd_tarefa = (int) materia.get("qtd_tarefas");
                if (qtd_tarefa == 0) {
                    Toast.makeText(getContext(), "Não há tarefas cadastradas para esta disciplina", Toast.LENGTH_LONG).show();
                } else {
                    Values.id_disciplina_lista_materias = (Integer) materia.get("id");
                    //Toast.makeText(getContext(), "Materia: "+ "Nome:" + materia.get("materia")+ " ID: "+ materia.get("id"), Toast.LENGTH_LONG).show();
                    Values_aluno.is_search_disc = true;
                   // startActivity(new Intent(getActivity(), TelaTodasAtividades.class));
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
            item.put("cor", "1");
            materias.add(item);
            item = new HashMap<String, Object>();
            m = new Materia();
        }
        return materias;
    }

    //Binder
    private class MateriasViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {

            if (view.getId() == R.id.cor_materia) {
                cor += 1;
                if ((cor % 2) == 0) {
                    //view.setBackgroundColor(getResources().getColor(R.color.materia1));
                } else {
                    //view.setBackgroundColor(getResources().getColor(R.color.materia2));
                }

                return true;
            }
            return false;
        }
    }


    private class BuscaMaterias extends AsyncTask<Void, Void, String> {
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "cont_m=1&acao=2&idaluno=" + Values_aluno.aluno.getId_aluno());
                Log.e(Values.TAG, "Entrou para ler dados: " + ab);
                Log.e(Values.TAG, "ID ALUNO: " + Values_aluno.aluno.getId_aluno());
            } catch (Exception e) {
                Toast.makeText(getContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

                String[] de = {"materia", "professor", "cor"};
                int[] para = {R.id.materia, R.id.professor, R.id.cor_materia};
                SimpleAdapter adapter = new SimpleAdapter(getContext(), listarMaterias(), R.layout.aluno_frag_lista_materias_modelo, de, para);
                adapter.setViewBinder(new MateriasViewBinder());
                listaMaterias.setAdapter(adapter);

                dialog.hide();
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Erro na leitura de dados: " + e.getMessage(), Toast.LENGTH_SHORT);
                dialog.hide();
            }
        }
    }
}
