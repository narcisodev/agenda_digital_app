package com.example.narcisogomes.myapplication.professor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONException;
import org.json.JSONObject;

public class TelaDescAtv extends AppCompatActivity {
    TextView txt_titulo, txt_descricao, txt_pontos, txt_data_criacao, txt_data_entrega,curso, disciplina,turma;
    Atividade a = new Atividade();
    private AlertDialog alerta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professor_act_atv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Descrição da Atividade");
        txt_titulo = findViewById(R.id.titulo_atividade);
        txt_descricao = findViewById(R.id.descricao_atividade);
        txt_pontos = findViewById(R.id.pontos);
        txt_data_criacao = findViewById(R.id.data_criacao);
        txt_data_entrega = findViewById(R.id.data_entrega);
        curso = findViewById(R.id.curso);
        disciplina = findViewById(R.id.descricao);
        turma = findViewById(R.id.turma);
        a = Values_professor.atividade_obj;
        turma.setText(a.turma);
        disciplina.setText(a.disiciplina);
        curso.setText(a.curso);
        txt_titulo.setText(a.titulo);
        txt_descricao.setText(a.descricao);
        txt_pontos.setText(a.pontos);
        txt_data_criacao.setText(a.datacriacao);
        txt_data_entrega.setText(a.data_entrega);
    }

    @Override
    protected void onResume() {
        if (Values_professor.alterou_tarefa) {
            Atividade a = Values_professor.atividade_obj;
            txt_titulo.setText(a.titulo);
            txt_descricao.setText(a.descricao);
            txt_pontos.setText(a.pontos);
            txt_data_criacao.setText(a.data + " às " + a.hora);
            txt_data_entrega.setText(a.data_entrega);
            Values_professor.alterou_tarefa = false;
        }

        super.onResume();
    }

    //MÉTODO USADO QUANDO UM COMPONENTE DO MENU É CLICADO
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.prof_menu_action_edit_tarefa) {
            startActivity(new Intent(getApplicationContext(), TelaEditAtividade.class));
        }

        if (id == R.id.prof_menu_action_delete_tarefa) {
            alertConfirmDelete();
        }

        if (id == 16908332) {
            onBackPressed();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(Values.is_menu_desc_atv){
            getMenuInflater().inflate(R.menu.prof_menu_desctarefa, menu);
        }

        return true;
    }


    /*
     *
     * CÓDIGOS PARA APAGAR A TAREFA
     *
     * */

    private void alertConfirmDelete() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Atenção!");
        //define a mensagem
        builder.setMessage("Tem serteza que deseja apagar esta atividade?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                new deleteTask().execute();
            }
        });

        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }


    private class deleteTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(TelaDescAtv.this);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=4.2" +
                        "&id_tarefa="+ Values_professor.atividade_obj.id_atividade);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            dialog.hide();
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(strings);
                boolean is = objeto.getBoolean("success");
                String mensagem = objeto.getString("message");
                if (is) {
                    Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
                    onBackPressed();
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao deletar a tarefa: " + mensagem, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Erro 003: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }


    }
}
