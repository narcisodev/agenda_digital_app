package com.example.narcisogomes.myapplication.professor;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.admin.SystemUpdatePolicy;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.util.MaskEditUtil;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Util;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class TelaEditAtividade extends AppCompatActivity {
    int mes, ano, dia, hora, minuto, horat, minutot;
    Button materia;
    Button cad_tarefas, dataentrega;
    TextView titulo, descricao;
    TextView pontos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professor_act_edit_atividade);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Edição de Tarefas");

        materia = findViewById(R.id.materia);
        cad_tarefas = findViewById(R.id.btn_cadastrar);
        titulo = findViewById(R.id.titulo);
        descricao = findViewById(R.id.descricao);
        pontos = findViewById(R.id.pontos);
        pontos.addTextChangedListener(MaskEditUtil.mask((EditText) pontos, MaskEditUtil.FORMAT_NOTA));
        dataentrega = findViewById(R.id.data_entrega);


        materia.setText(Values_professor.atividade_obj.disiciplina);
        titulo.setText(Values_professor.atividade_obj.titulo);
        descricao.setText(Values_professor.atividade_obj.descricao);
        pontos.setText(Values_professor.atividade_obj.pontos);
        dataentrega.setText(Values_professor.atividade_obj.data_entrega);

        //CONFIG DATA ENTREGA
        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        hora = calendar.get(Calendar.HOUR_OF_DAY);
        minuto = calendar.get(Calendar.MINUTE);

        dataentrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarData(v);
            }
        });

        cad_tarefas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Values_professor.atividade_obj.titulo = titulo.getText().toString();
                Values_professor.atividade_obj.descricao = descricao.getText().toString();
                Values_professor.atividade_obj.pontos = pontos.getText().toString();
                Values_professor.atividade_obj.data_entrega = ano+"-"+Util.transformaMes(mes+1)+"-"+Util.formataHora(dia)+" "+Util.formataHora(horat)+":"+Util.formataHora(minutot)+":00";
                new updateTask().execute();
            }
        });

    }


    //MÉTODO USADO QUANDO UM COMPONENTE DO MENU É CLICADO
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }


    public void selecionarData(View view) {
        showDialog(view.getId());
    }

    private void timePicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minuto = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        horat = hourOfDay;
                        minutot = minute;
                        dataentrega.setText(Util.formataHora(dia) + "/" + Util.transformaMes(mes+1) + "/" + ano + " - " + Util.formataHora(horat) + ":" + Util.formataHora(minutot));
                    }
                }, hora, minuto, true);
        timePickerDialog.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (R.id.data_entrega == id) {
            return new DatePickerDialog(this,
                    listener, ano, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,
                              int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;
            timePicker();

        }
    };


    private class updateTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(TelaEditAtividade.this);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=4.1" +
                        "&id_tarefa="+ Values_professor.atividade_obj.id_atividade+
                        "&titulo=" + Values_professor.atividade_obj.titulo +
                        "&descricao=" + Values_professor.atividade_obj.descricao +
                        "&pontos=" + Values_professor.atividade_obj.pontos +
                        "&data_entrega=" + Values_professor.atividade_obj.data_entrega);
                Log.e("NARCISO", ab);

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
                    if(Values_professor.total_tarefas == 0){
                        Values_professor.total_tarefas = 1;
                    }
                    Values_professor.alterou_tarefa = true;
                    onBackPressed();

                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao salvar a tarefa: " + mensagem, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Erro 003: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }


    }

    private class deleteTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(TelaEditAtividade.this);
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=4.1" +
                        "&id_tarefa="+ Values_professor.atividade_obj.id_atividade+
                        "&titulo=" + Values_professor.atividade_obj.titulo +
                        "&descricao=" + Values_professor.atividade_obj.descricao +
                        "&pontos=" + Values_professor.atividade_obj.pontos +
                        "&data_entrega=" + Values_professor.atividade_obj.data_entrega);
                Log.e("NARCISO", ab);

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
                    if(Values_professor.total_tarefas == 0){
                        Values_professor.total_tarefas = 1;
                    }
                    Values_professor.alterou_tarefa = true;
                    onBackPressed();

                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao salvar a tarefa: " + mensagem, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Erro 003: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }


    }
}
