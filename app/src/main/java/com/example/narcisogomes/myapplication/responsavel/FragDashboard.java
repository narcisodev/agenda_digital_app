package com.example.narcisogomes.myapplication.responsavel;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Responsavel;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragDashboard extends Fragment {

    ListView lista_alunos;
    TextView txt_nome, txt_email, txt_rua, txt_bairro, txt_numero;
    Aluno aluno_obj = new Aluno();
    ArrayList<Aluno> al_alunos = new ArrayList<>();
    List<Map<String, Object>> lis_alunos_map = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.resp_dashboard,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lista_alunos = view.findViewById(R.id.lista_alunos);
        txt_nome = view.findViewById(R.id.nome_resp);
        txt_email = view.findViewById(R.id.email_resp);
        txt_rua = view.findViewById(R.id.rua_resp);
        txt_bairro = view.findViewById(R.id.bairro_resp);
        txt_numero = view.findViewById(R.id.numero_resp);
        Responsavel r = Values_resp.resp_logado;
        txt_nome.setText("Nome: "+r.getNome_usuario());
        txt_email.setText("Email: "+ r.getEmail());
        txt_rua.setText("Rua: "+ r.getRua());
        txt_bairro.setText("Bairro: "+r.getBairro());
        txt_numero.setText("Número: "+r.getNumero());

        lista_alunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> aluno= lis_alunos_map.get(position);
                Values_resp.id_aluno_selecionado = (Integer) aluno.get("id");
                Values_resp.turma_aluno_selecionado = Integer.parseInt((String) aluno.get("turma"));
                startActivity(new Intent(getActivity(), DescAluno.class));
            }
        });
        new CarregaAlunos().execute();
    }

    private List<Map<String,Object>> listarAlunos(){
        lis_alunos_map = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        Aluno a = new Aluno();
        for(int i  = 0; i < al_alunos.size(); i++ ){
            a = al_alunos.get(i);
            item.put("id", a.getId_aluno());
            item.put("nome", a.getNome_usuario());
            item.put("curso", a.getCurso());
            item.put("turma", a.getTurma());
            lis_alunos_map.add(item);
            item = new HashMap<String, Object>();
            a = new Aluno();
        }
        return lis_alunos_map;
    }


    private class CarregaAlunos extends AsyncTask<Void, Void, String> {
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=7" +
                        "&id_resp=" + Values_resp.resp_logado.getId_reponsavel());
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
                    JSONArray alunos = objeto.getJSONArray("dados");
                    for (int i = 0; i < alunos.length(); i++) {
                        JSONObject aln_json = alunos.getJSONObject(i);
                        aluno_obj.setId_aluno(aln_json.getInt("id_aluno"));
                        aluno_obj.setNome_usuario(aln_json.getString("nome"));
                        aluno_obj.setCurso(aln_json.getString("curso"));
                        aluno_obj.setTurma(aln_json.getString("turma"));
                        al_alunos.add(aluno_obj);
                        aluno_obj = new Aluno();
                    }

                    String[] de ={"nome","curso"};
                    int[] para = {R.id.nome_aluno, R.id.curso_aluno};
                    SimpleAdapter adapter= new SimpleAdapter(getContext(), listarAlunos(), R.layout.resp_lista_aluno_model,de,para);
                    //adapter.setViewBinder(new FragListaMaterias.MateriasViewBinder());
                    lista_alunos.setAdapter(adapter);
                }
            } catch (JSONException e) {
                dialog.hide();
                Toast.makeText(getContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            dialog.hide();
        }
    }
}

