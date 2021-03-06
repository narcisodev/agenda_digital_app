package com.example.narcisogomes.myapplication.responsavel;

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
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Ocorrencia;
import com.example.narcisogomes.myapplication.responsavel.ListAdapters.ListViewAdapterOcorrencias;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragListaOcorrencia extends Fragment {
    private ListView lista_ocorrencias;
    private ArrayList<Ocorrencia> al_ocorrencias = new ArrayList();
    ListViewAdapterOcorrencias lvao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.resp_frag_lista_ocorrencias, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lista_ocorrencias = view.findViewById(R.id.lista_ocorrencias_resp);
        new BuscaOcorrencias().execute();

    }

    private class BuscaOcorrencias extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getContext());
            dialog.setTitle(R.string.carregando);
            dialog.setMessage("Estamos carregando a sua requisição...");
            dialog.setCancelable(false);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=8.1&id_resp=" + Values_resp.resp_logado.getId_reponsavel());
            } catch (Exception e) {
                Toast.makeText(getContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            al_ocorrencias.clear();
            Ocorrencia ocorrencia = new Ocorrencia();
            Aluno aluno = new Aluno();
            String responsebody = strings;
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(responsebody);
                boolean is = objeto.getBoolean("success");
                if (is) {
                    JSONArray ocorrencias = objeto.getJSONArray("dados");
                    Log.e(Values.TAG, "PEGOU DADOS");

                    for (int i = 0; i < ocorrencias.length(); i++) {
                        JSONObject oc_json = ocorrencias.getJSONObject(i);

                        ocorrencia.setId(oc_json.getInt("id_ocorrencia"));
                        Log.e(Values.TAG, "ID OCORRENCIA");

                        ocorrencia.setDescricao(oc_json.getString("descricao"));
                        Log.e(Values.TAG, "PEGOU DESCRICAO");

                        ocorrencia.setData(oc_json.getString("data"));
                        //ocorrencia.setPedagogico_nome(oc_json.getString("nome"));
                        Log.e(Values.TAG, "PEGOU DATA");

                        JSONArray alunos = oc_json.getJSONArray("alunos");
                        Log.e(Values.TAG, "PEGOU ALUNO");

                        String nome_alunos = "";
                        int tamanho_array = alunos.length() - 1;
                        for (int ia = 0; ia < alunos.length(); ia++) {
                            JSONObject aluno_json = alunos.getJSONObject(ia);
                            if (ia == tamanho_array) {
                                nome_alunos += aluno_json.getString("nome") + ".";
                            } else {
                                nome_alunos += aluno_json.getString("nome") + ", ";
                            }
                            aluno.setNome_usuario(aluno_json.getString("nome"));
                            aluno.setId_aluno(aluno_json.getInt("id_aluno"));
                            ocorrencia.getArray_alunos().add(aluno);
                            aluno = new Aluno();
                        }

                        ocorrencia.setNome_al(nome_alunos);

                        al_ocorrencias.add(ocorrencia);
                        ocorrencia = new Ocorrencia();
                    }

                    lvao = new ListViewAdapterOcorrencias(getContext(), al_ocorrencias);
                    lista_ocorrencias.setAdapter(lvao);
                    Values_resp.telaResp_context.verificaMenu(2, lista_ocorrencias, lvao);

                } else {
                    Toast.makeText(getContext(), "Não existem tarefas cadastradas para esta matéria", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(getContext(), "ERRO: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }

            dialog.hide();
        }
    }


}
