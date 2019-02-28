package com.example.narcisogomes.myapplication.professor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.models.Materia;
import com.example.narcisogomes.myapplication.util.MaskEditUtil;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Util;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TelaCadTarefas extends AppCompatActivity implements DialogInterface.OnClickListener {
    Button materia;
    int mes, ano, dia, hora, minuto, segundo, horat, minutot;
    Button cad_tarefas, dataentrega;
    TextView titulo, descricao;
    TextView pontos;
    Atividade atv = new Atividade();
    String mHoraString;

    //Spinner disciplinas;
    //List<String> materiasa = new ArrayList<>(Arrays.asList("Matemática", "Geografia", "Biologia", "Português"));
    List<String> materias_list = new ArrayList<>();
    Materia materia_geral = new Materia();
    private AlertDialog alertDialog;//Informar que a tarefa foi cadastrada com sucesso

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professor_act_cad_tarefas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Cadastro de Tarefas");
        cad_tarefas = findViewById(R.id.btn_cadastrar);
        titulo = findViewById(R.id.titulo);
        descricao = findViewById(R.id.descricao);
        pontos = findViewById(R.id.pontos);
        pontos.addTextChangedListener(MaskEditUtil.mask((EditText) pontos, MaskEditUtil.FORMAT_NOTA));
        dataentrega = findViewById(R.id.data_entrega);
        alertDialog = criaAviso("");

        //disciplinas = findViewById(R.id.materia);
        //new CarregaDisciplinas().execute();
        //CARREGA O SPINNER
        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, materias_list);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //disciplinas.setAdapter(dataAdapter);

        cad_tarefas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarTarefa();
            }
        });

        dataentrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarData(v);
            }
        });

        //CONFIG DATA ENTREGA
        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        hora = calendar.get(Calendar.HOUR_OF_DAY);
        minuto = calendar.get(Calendar.MINUTE);
        segundo = calendar.get(Calendar.SECOND);
        horat = hora;
        minutot = minuto;
        //dataentrega.setText(dia + "/" + Util.transformaMes(mes + 1) + "/" + ano);
        dataentrega.setText(Util.formataHora(dia) + "/" + Util.transformaMes(mes) + "/" + ano + "-" + Util.formataHora(hora) + ":" + Util.formataHora(minuto));

        materia = findViewById(R.id.materia);
        materia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaCadTarefas.this, ListaMaterias.class));
            }
        });

    }

    //cria diálogo de confirmação
    private AlertDialog criaAviso(String aviso) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(aviso);
        builder.setTitle("Atenção");
        builder.setPositiveButton(getString(R.string.ok), (DialogInterface.OnClickListener) TelaCadTarefas.this);
        return builder.create();
    }


    public void selecionarData(View view) {
        showDialog(view.getId());
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

    @Override
    protected Dialog onCreateDialog(int id) {
        if (R.id.data_entrega == id) {
            return new DatePickerDialog(this,
                    listener, ano, mes, dia);
        }


        return null;
    }


    //MÉTODO USADO QUANDO UM COMPONENTE DO MENU É CLICADO
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        //verifica se existe as variaveis de disciplina escolhida
        if (Values.nome_disciplina_prof != null) {
            materia.setText(Values.nome_disciplina_prof);
        } else {
            materia.setText("Escolha a disciplina");
        }
        super.onResume();
    }

    public void limparForm() {
        materia.setText("Escolha a disciplina");
        titulo.setText("");
        descricao.setText("");
        pontos.setText("");
        dataentrega.setText(dia + "/" + Util.transformaMes(mes + 1) + "/" + ano);

    }

    //GERENCIA O CADASTRAMENTO DE TAREFAS, CRIA O OBJETO E REPASSA PARA O WEBSERVICE
    public void cadastrarTarefa() {

        Log.e(Values.TAG, "Entrou cadastrar tarefas");

        atv.id_materia = Values.id_disciplina_prof;
        atv.titulo = titulo.getText().toString();
        atv.descricao = descricao.getText().toString();
        atv.pontos = pontos.getText().toString();
        atv.data_entrega = ano+"-"+Util.transformaMes(mes+1)+"-"+Util.formataHora(dia)+" "+Util.formataHora(horat)+":"+Util.formataHora(minutot)+":00";
        atv.id_professor = Values_professor.professor.getId_professor();
        Log.e(Values.TAG, "ID PROFESSOR = " + atv.id_professor + "");
        /*
        int id_materia = Values.id_disciplina_prof;
        String titulo_l = (String) titulo.getText();
        String descricao_l = (String) descricao.getText();
        CharSequence pontoss = pontos.getText();
        String pontos_l = pontoss.toString();
        String dataentrega_l = (String) dataentrega.getText();
        */

        Values.atividade_obj = atv;


        /*
        new CadastrarAtividade().execute();
        */

        new CadastrarAtividade().execute();

        //String a = (String) disciplinas.getSelectedItem();
        //chamar a classe CadastrarAtividade
    }

    @Override
    public void onClick(DialogInterface dialog, int item) {
        switch (item) {
            case DialogInterface.BUTTON_POSITIVE:
                alertDialog.hide();
                break;
        }
    }


    private class CadastrarAtividade extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            Log.e(Values.TAG, "ENTROU PRE EXECUTE - CADASTRAR ATIVIDADE");

            dialog = new ProgressDialog(TelaCadTarefas.this);
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
            Log.e(Values.TAG, "ENTROU DOINBACKGROUNDI - CADASTRAR ATIVIDADE");
            try {
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=4" +
                        "&titulo=" + Values.atividade_obj.titulo +
                        "&descricao=" + Values.atividade_obj.descricao +
                        "&pontos=" + Values.atividade_obj.pontos +
                        "&id_professor=" + Values.atividade_obj.id_professor +
                        "&id_disciplina=" + Values.atividade_obj.id_materia +
                        "&data_entrega=" + Values.atividade_obj.data_entrega);
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
                    limparForm();
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao salvar a tarefa: " + mensagem, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Erro 003: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }


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
                        /*mHoraString = (hourOfDay + ":" + minute).toString();*/
    /*dataentrega.setText(hourOfDay + ":" + minute);*/
    /*milisegundos*/
    /*long horaMil = TimeUnit.HOURS.toMillis(hourOfDay);*/
    /*long minMil = TimeUnit.MINUTES.toMillis(minute);*/
    /*long soma_mils = horaMil + minMil;*/
    /*String mHoraMilisegundos = Long.toString(soma_mils);*/
                    }
                }, hora, minuto, true);
        timePickerDialog.show();
    }


}