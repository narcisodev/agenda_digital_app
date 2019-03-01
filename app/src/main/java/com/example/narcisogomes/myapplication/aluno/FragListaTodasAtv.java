package com.example.narcisogomes.myapplication.aluno;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.aluno.ListAdapters.ListViewAdapterAtividades;
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

public class FragListaTodasAtv extends Fragment{

    ListViewAdapterAtividades listViewAdapterAtividades;
    private ListView listaAtividades;
    private List<Map<String, Object>> atividades;
    private ArrayList<Atividade> atividades_array = new ArrayList<>();
    String search_url_service = "acao=3.1&cause=after" + "&id_turma=" + Values_aluno.id_turma;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.aluno_frag_lista_todas_atv, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listaAtividades = view.findViewById(R.id.lista_atividades);

        listaAtividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> atividades_c = atividades.get(position);
                Values.atividade_map = atividades_c;
                startActivity(new Intent(getContext(), TelaDescAtividade.class));
            }
        });


        /*
        * Verificação para saber se as tarefas serão pesquisadas pelo id_turma ou pelo id_disciplina
        * Também verifica se o usuário selecionou o menu de opções da listagem
        * */
        if(Values_aluno.menu_clicked){
            Values_aluno.menu_clicked = false;
            if(Values_aluno.is_search_disc){
                search_url_service = "acao=3&cause="+Values_aluno.valor_menu_lista_materias+"&id_disciplina="+Values.id_disciplina_lista_materias;
            }else {
                search_url_service = "acao=3.1&cause="+Values_aluno.valor_menu_lista_materias + "&id_turma=" + Values_aluno.id_turma;
            }
        }else{
            if(Values_aluno.is_search_disc){
                search_url_service = "acao=3&cause=after"+"&id_disciplina="+Values.id_disciplina_lista_materias;
            }else {
                search_url_service = "acao=3.1&cause=after" + "&id_turma=" + Values_aluno.id_turma;
            }
        }
        new BuscaAtividades().execute();
    }


    private List<Map<String, Object>> listarAtividades() {
       atividades = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        Atividade a = new Atividade();
        for (int i = 0; i < atividades_array.size(); i++) {
            a = atividades_array.get(i);
            item.put("id", a.id_atividade);
            item.put("titulo", a.titulo);
            item.put("descricao", a.descricao);
            item.put("pontos", a.pontos);
            item.put("data_entrega", a.data_entrega);
            item.put("disciplina", a.disiciplina);
            item.put("data", a.data);
            item.put("datacriacao", a.datacriacao);

            atividades.add(item);
            item = new HashMap<String, Object>();
            a = new Atividade();
        }
        return atividades;
    }


    private class BuscaAtividades extends AsyncTask<Void, Void, String> {
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
            String ab="";
            try {
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE,search_url_service);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            Atividade atividade = new Atividade();
            String responsebody = strings;
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(responsebody);
                boolean is = objeto.getBoolean("success");
                if(is){
                    JSONArray tarefas = objeto.getJSONArray("dados");
                    for(int i =  0; i< tarefas.length(); i++){
                        JSONObject tarefa_json = tarefas.getJSONObject(i);
                        atividade.id_atividade = tarefa_json.getInt("id_atividade");
                        atividade.titulo = tarefa_json.getString("titulo");
                        atividade.descricao = tarefa_json.getString("descricao");
                        atividade.pontos = tarefa_json.getString("pontos");
                        atividade.data = tarefa_json.getString("data")+ " ";
                        atividade.data_entrega = tarefa_json.getString("dataentrega");
                        atividade.disiciplina = tarefa_json.getString("disciplina");
                        atividade.data = tarefa_json.getString("data");
                        atividade.datacriacao = tarefa_json.getString("datacriacao");
                        atividades_array.add(atividade);
                        atividade = new Atividade();
                    }

                    listViewAdapterAtividades = new ListViewAdapterAtividades(getContext(), atividades_array);
                    /*
                    String[] de ={"disciplina","data","titulo", "descricao"};
                    int[] para = {R.id.materia, R.id.data_atividade, R.id.titulo_atividade,R.id.descricao_atividade};
                    SimpleAdapter adapter= new SimpleAdapter(getContext(), listarAtividades(), R.layout.aluno_frag_list_atividades_modelo,de,para);
                    //adapter.setViewBinder(new ListaAtividadesDeAula.AtividadesViewBinder());*/
                    listaAtividades.setAdapter(listViewAdapterAtividades);

                }else{
                    Toast.makeText(getContext(), "Não existem tarefas cadastradas para esta matéria", Toast.LENGTH_LONG);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.hide();
        }
    }

}
