package com.example.narcisogomes.myapplication.pedagogico;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.professor.Values_professor;


public class DescricaoAtividade extends AppCompatActivity {

    TextView txt_titulo, txt_descricao, txt_pontos, txt_data_criacao, txt_data_entrega;
    Atividade a = new Atividade();
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ped_activity_descricao_atividade);
        txt_titulo = findViewById(R.id.titulo_atividade);
        txt_descricao = findViewById(R.id.descricao_atividade);
        txt_pontos = findViewById(R.id.pontos);
        txt_data_criacao = findViewById(R.id.data_criacao);
        txt_data_entrega = findViewById(R.id.data_entrega);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Descrição da Atividade");

        a = Values_pedagogico.atividade;
        txt_titulo.setText(a.titulo);
        txt_descricao.setText(a.descricao);
        txt_pontos.setText(a.pontos);
        txt_data_criacao.setText(a.data + " às " + a.hora);
        txt_data_entrega.setText(a.data_entrega);
    }
}
