package com.example.narcisogomes.myapplication.pedagogico;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.models.Curso;
import com.example.narcisogomes.myapplication.models.Ocorrencia;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterCursos;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterOcorrencias;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragListaOcorrencias extends Fragment {

    private ListView lv_ocorrencias;
    private ArrayList<Ocorrencia> al_ocorrencias = new ArrayList<>();
    private ListViewAdapterOcorrencias lavo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pedagogico_frag_lista_ocorrencias, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_ocorrencias = view.findViewById(R.id.lista_ocorrencias);
        new BuscaOcorrencias().execute();
    }


    private class BuscaOcorrencias extends AsyncTask<Void, Void, String> {
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=14");
            } catch (Exception e) {
                Toast.makeText(getContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            al_ocorrencias.clear();
            Ocorrencia ocorrencia = new Ocorrencia();
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



                        al_ocorrencias.add(ocorrencia);
                        ocorrencia= new Ocorrencia();
                    }

                    lavo = new ListViewAdapterOcorrencias(getContext(), al_ocorrencias);


                    lv_ocorrencias.setAdapter(lavo);

                } else {
                    Toast.makeText(getContext(), "Não existem tarefas cadastradas para esta matéria", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.hide();
        }
    }
}
