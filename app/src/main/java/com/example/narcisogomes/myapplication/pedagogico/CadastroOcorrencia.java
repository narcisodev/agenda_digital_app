package com.example.narcisogomes.myapplication.pedagogico;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.aluno.TelaAluno;
import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Ocorrencia;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterAlunosOcorrencia;
import com.example.narcisogomes.myapplication.util.Util;
import com.example.narcisogomes.myapplication.util.Values;

import java.util.Calendar;

public class CadastroOcorrencia extends AppCompatActivity {
    AlertDialog alerta;
    ListViewAdapterAlunosOcorrencia lvaa;
    EditText txt_descrica;

    ListView lv_alunos_oc;
    int mes, ano, dia, hora, minuto, segundo, horat, minutot;
    Button data_oc, cad_oc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ped_activity_cadastro_ocorrencia);
        lv_alunos_oc = findViewById(R.id.lista_alunos_oc);
        data_oc = findViewById(R.id.data_oc);
        cad_oc = findViewById(R.id.btn_cad_oc);

        data_oc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarData(v);
            }
        });
        cad_oc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarOcorrencia();
            }
        });

        txt_descrica = findViewById(R.id.descricao_oc);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Cadastro de Ocorrência");

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

        data_oc.setText(Util.formataHora(dia) + "/" + Util.transformaMes(mes) + "/" + ano + "-" + Util.formataHora(hora) + ":" + Util.formataHora(minuto));

        /*
        String[] city_names = {"Óbidos", "Santarém", "Manaus", "Mondongo", "Santos", "Rio de Janeiro", "Fumaça", "Loucuara", "Belterra", "Peixe Boi", "Santa Helema", "Mario Santos", "Junior Gustavo"};
        ArrayAdapter a = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, city_names);
        lv_alunos_oc.setAdapter(a);
        */
    }


    private void cadastrarOcorrencia(){
        String descricao = txt_descrica.getText().toString();
        descricao = descricao.replaceAll(" ", "" );

        if(descricao.length() == 0){
            Toast.makeText(CadastroOcorrencia.this, "Por favor informe a descrição da ocorrencia",Toast.LENGTH_SHORT).show();
        }else{
            Ocorrencia ocorrencia = new Ocorrencia();
            ocorrencia.setDescricao(txt_descrica.getText().toString());
            ocorrencia.setData(ano+"-"+Util.transformaMes(mes+1)+"-"+Util.formataHora(dia)+" "+Util.formataHora(horat)+":"+Util.formataHora(minutot)+":00");
            Toast.makeText(CadastroOcorrencia.this, "Cad sucess: "+ txt_descrica.getText().toString(),Toast.LENGTH_SHORT).show();
        }


    }


    private void cadastrarAlunosOcorrencia(){

    }


    @Override
    protected void onResume() {
        lvaa = new ListViewAdapterAlunosOcorrencia(CadastroOcorrencia.this, Values_pedagogico.lista_alunos);
        lv_alunos_oc.setAdapter(lvaa);
        Values_pedagogico.is_tela_oc = false;
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cad_ocorrencia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.add_alunos){
            Values_pedagogico.is_tela_oc = true;
            startActivity(new Intent(CadastroOcorrencia.this, ListaAlunos.class));
        }else{
            onBackPressed();
        }
        return true;
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
        if (R.id.data_oc == id) {
            return new DatePickerDialog(this,
                    listener, ano, mes, dia);
        }


        return null;
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
                        data_oc.setText(Util.formataHora(dia) + "/" + Util.transformaMes(mes+1) + "/" + ano + " - " + Util.formataHora(horat) + ":" + Util.formataHora(minutot));
                    }
                }, hora, minuto, true);
        timePickerDialog.show();
    }

    public void alertConfirmaRemoveAluno(final int id) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Atenção!");
        //define a mensagem
        builder.setMessage("Gostaria de remover este aluno da lista?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                for(Aluno a: Values_pedagogico.lista_alunos){
                    if(a.getId_aluno() == id){
                        Values_pedagogico.lista_alunos.remove(a);
                        break;
                    }
                }

                lvaa = new ListViewAdapterAlunosOcorrencia(CadastroOcorrencia.this, Values_pedagogico.lista_alunos);
                lv_alunos_oc.setAdapter(lvaa);

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

}