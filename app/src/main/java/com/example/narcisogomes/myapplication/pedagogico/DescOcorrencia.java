package com.example.narcisogomes.myapplication.pedagogico;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Ocorrencia;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterAlunos;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterAlunosOcorrencia;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterAlunosOcorrenciaDesc;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterOcorrencias;
import com.example.narcisogomes.myapplication.professor.TelaEditAtividade;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DescOcorrencia extends AppCompatActivity {
    boolean ver_menu = false; //valida o menu de arquivar ou editar a ocorrencia
    boolean ver_menu_reabrir = false; //valida para apresentar o menuitem reabrir caso a ocorrencia esteja
    ListViewAdapterAlunosOcorrenciaDesc lvao;
    TextView desc_oc, data_oc, ped_oc, desc_arquivada;
    ListView lista_alunos;
    int ocorrencia_id;
    Ocorrencia ocorrencia = new Ocorrencia();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ped_activity_desc_ocorrencia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle(R.string.desc_occ);
        desc_oc = findViewById(R.id.descricao_oc);
        data_oc = findViewById(R.id.data_oc);
        ped_oc = findViewById(R.id.ped_reg);
        desc_arquivada = findViewById(R.id.desc_arquivada);
        lista_alunos = findViewById(R.id.lista_alunos_oc);
        Intent i = getIntent();
        ocorrencia_id = Integer.parseInt(i.getStringExtra("id_oc"));
        new BuscaOcorrencia().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (ver_menu) {
            getMenuInflater().inflate(R.menu.pedagogico_menu_descricao_ocorrencia, menu);
            if(ver_menu_reabrir){
                MenuItem menuItem = menu.findItem(R.id.ped_menu_action_edit_occ);
                menuItem.setVisible(false);
                menuItem = menu.findItem(R.id.ped_menu_action_arquivar_occ);
                menuItem.setVisible(false);
                menuItem = menu.findItem(R.id.ped_menu_action_reabrir_occ);
                menuItem.setVisible(true);
            }
        }
        return true;
    }

    //MÉTODO USADO QUANDO UM COMPONENTE DO MENU É CLICADO
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.ped_menu_action_edit_occ) {
            Values_pedagogico.ocorrencia_editar = ocorrencia;
            Values_pedagogico.is_editar = true;
            startActivity(new Intent(getApplicationContext(), CadastroOcorrencia.class));
            return true;
        }

        if (id == R.id.ped_menu_action_arquivar_occ) {
            alertConfirmaArquivar();
            return true;
        }
        
        if(id == R.id.ped_menu_action_reabrir_occ){
            alertConfirmaReabrir();
            return true;
        }



        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        if (Values_pedagogico.pesquisa_ocorrencia_novamente) {
            Values_pedagogico.pesquisa_ocorrencia_novamente = false;
            onBackPressed();
        }
        super.onResume();
    }

    private class BuscaOcorrencia extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DescOcorrencia.this);
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
            Log.e("TESTANDO", "ID OCORRENCIA: " + ocorrencia_id);
            String ab = "";
            try {
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=14" +
                        "&um=true" +
                        "&id_oc=" + ocorrencia_id);
            } catch (Exception e) {
                Toast.makeText(DescOcorrencia.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(String strings) {

            Aluno aluno = new Aluno();
            String responsebody = strings;
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(responsebody);
                boolean is = objeto.getBoolean("success");
                if (is) {

                    JSONArray ocorrencias = objeto.getJSONArray("dados");

                    for (int i = 0; i < ocorrencias.length(); i++) {
                        //Log.e("NARCISO02", responsebody);
                        JSONObject oc_json = ocorrencias.getJSONObject(i);
                        ocorrencia.setStatus(oc_json.getInt("id_status"));
                        ocorrencia.setId_pedagogo(oc_json.getInt("id_pedagogo"));
                        ocorrencia.setId(oc_json.getInt("id_ocorrencia"));
                        ocorrencia.setDescricao(oc_json.getString("descricao"));
                        ocorrencia.setData(oc_json.getString("data"));
                        ocorrencia.setData_bd(oc_json.getString("data_bd"));
                        ocorrencia.setPedagogico_nome(oc_json.getString("nome"));
                        JSONArray alunos = oc_json.getJSONArray("alunos");

                        for (int ia = 0; ia < alunos.length(); ia++) {
                            JSONObject aluno_json = alunos.getJSONObject(ia);
                            aluno.setNome_usuario(aluno_json.getString("nome"));
                            aluno.setId_aluno(aluno_json.getInt("id_aluno"));
                            aluno.setTurma(aluno_json.getString("turma"));
                            aluno.setCurso(aluno_json.getString("curso"));
                            ocorrencia.getArray_alunos().add(aluno);
                            aluno = new Aluno();
                        }

                        lvao = new ListViewAdapterAlunosOcorrenciaDesc(DescOcorrencia.this, ocorrencia.getArray_alunos());
                        lista_alunos.setAdapter(lvao);
                        data_oc.setText(ocorrencia.getData());
                        desc_oc.setText(ocorrencia.getDescricao());
                        ped_oc.setText(ocorrencia.getPedagogico_nome());

                        if(ocorrencia.getStatus() == 2){
                            desc_arquivada.setVisibility(1);
                            ver_menu_reabrir = true;
                            invalidateOptionsMenu();
                        }

                        verificaMenu(ocorrencia.getId_pedagogo());
                    }


                } else {
                    Toast.makeText(DescOcorrencia.this, "Não existem tarefas cadastradas para esta matéria", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(DescOcorrencia.this, "erro" + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }

            dialog.hide();
        }
    }

    private void verificaMenu(int id_pedagogico) {
        if (Values_pedagogico.ped_logado.getId_pedagogico() == id_pedagogico) {
            this.ver_menu = true;
            invalidateOptionsMenu();
        }
    }

    private void alertConfirmaArquivar() {
        AlertDialog alerta;
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle(R.string.atencao);
        //define a mensagem
        builder.setMessage(R.string.confirma_arquivar);
        //define um botão como positivo
        builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                new ArquivaOcorrencia().execute();

            }
        });

        //define um botão como negativo.
        builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    private void alertConfirmaReabrir() {
        AlertDialog alerta;
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle(R.string.atencao);
        //define a mensagem
        builder.setMessage(R.string.confirma_reabrir);
        //define um botão como positivo
        builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                new ReabrirOcorrencia().execute();

            }
        });

        //define um botão como negativo.
        builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }


    private class ArquivaOcorrencia extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DescOcorrencia.this);
            dialog.setTitle(R.string.carregando);
            dialog.setMessage("Arquivando Ocorrência...");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String ab = "";
            try {
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=17&reabrir=false&id_occ="+ocorrencia_id);
            } catch (Exception e) {
                Toast.makeText(DescOcorrencia.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return ab;
        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(String strings) {
            String responsebody = strings;
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(responsebody);
                boolean is = objeto.getBoolean("success");
                String mensagem = objeto.getString("message");
                if(is){
                    desc_arquivada.setVisibility(1);
                    Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                Toast.makeText(DescOcorrencia.this, "erro" + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }

            dialog.hide();
        }
    }

    private class ReabrirOcorrencia extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DescOcorrencia.this);
            dialog.setTitle(R.string.carregando);
            dialog.setMessage("Reabrindo Ocorrência...");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String ab = "";
            try {
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=17&reabrir=true&id_occ="+ocorrencia_id);
            } catch (Exception e) {
                Toast.makeText(DescOcorrencia.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return ab;
        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(String strings) {
            String responsebody = strings;
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(responsebody);
                boolean is = objeto.getBoolean("success");
                String mensagem = objeto.getString("message");
                if(is){
                    desc_arquivada.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                Toast.makeText(DescOcorrencia.this, "erro" + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }

            dialog.hide();
        }
    }
}
