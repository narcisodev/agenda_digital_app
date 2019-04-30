package com.example.narcisogomes.myapplication.pedagogico;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.narcisogomes.myapplication.*;
import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Ocorrencia;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterOcorrencias2;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ListaOcorrencias extends AppCompatActivity {
    GroupAdapter adapter = new GroupAdapter();
    String parametros_ocorrencia = "";
    ListView lista;
    ListViewAdapterOcorrencias2 listViewAdapterOcorrencias2;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ped_activity_lista_ocorrencias);
        lista = findViewById(R.layout.activity_list_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Listagem de ocorrências");

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListaOcorrencias.this, CadastroOcorrencia.class));
            }
        });
        new BuscaOcorrencias().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pedagogico_lista_alunos, menu);
        MenuItem myActionMenuItem= menu.findItem(R.id.action_search_ped);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    listViewAdapterOcorrencias2.filter("");
                    lista.clearTextFilter();
                }else{
                    listViewAdapterOcorrencias2.filter(newText);
                }
                return true;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void buscarMinhasOcorrencias(){
        parametros_ocorrencia = "&my_occ=true&id_ped="+Values_pedagogico.ped_logado.getId_pedagogico();
        new BuscaOcorrencias().execute();
    }

    public void buscarOcorrenciasArquivadas(){
        parametros_ocorrencia = "&occ_a=true";
        new BuscaOcorrencias().execute();
    }

    public void buscarOcorrenciasAbertas() {
        parametros_ocorrencia = "";
        new BuscaOcorrencias().execute();
    }

    private class OcorrenciaItem extends Item<ViewHolder> {
        private final Ocorrencia oc;

        private OcorrenciaItem(Ocorrencia oc) {
            this.oc = oc;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView descricao = viewHolder.itemView.findViewById(R.id.descricao);
            TextView alunos = viewHolder.itemView.findViewById(R.id.alunos);
            TextView data = viewHolder.itemView.findViewById(R.id.data);
            TextView registradopor = viewHolder.itemView.findViewById(R.id.registradopor);

            descricao.setText(oc.getDescricao());
            alunos.setText(oc.getNome_al());
            data.setText(oc.getData());
            registradopor.setText(oc.getPedagogico_nome());

        }

        @Override
        public int getLayout() {
            return R.layout.ped_lista_ocorrencia_model_2;
        }
    }



    private class BuscaOcorrencias extends AsyncTask<Void, Void, String> {
        ArrayList<Ocorrencia> list = new ArrayList<>();
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ListaOcorrencias.this);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=14" + parametros_ocorrencia);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            Ocorrencia ocorrencia = new Ocorrencia();
            Aluno aluno = new Aluno();
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

                        JSONArray alunos = oc_json.getJSONArray("alunos");

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
                        list.add(ocorrencia);
                        ocorrencia = new Ocorrencia();
                    }


                     listViewAdapterOcorrencias2 = new ListViewAdapterOcorrencias2(ListaOcorrencias.this, list);
                    lista.setAdapter(listViewAdapterOcorrencias2);
                    //Values_pedagogico.listViewAdapterOcorrencias = new ListViewAdapterOcorrencias(getApplicationContext(), al_ocorrencias);
                    //Values_pedagogico.lv_ocorrencias.setAdapter(Values_pedagogico.listViewAdapterOcorrencias);

                } else {
                    Toast.makeText(getApplicationContext(), "Não existem tarefas cadastradas para esta matéria", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "erro" + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }

            dialog.hide();
        }
    }
}
