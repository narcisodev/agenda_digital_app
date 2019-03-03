package com.example.narcisogomes.myapplication.aluno;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.professor.Values_professor;
import com.example.narcisogomes.myapplication.util.Values;

public class TelaDescAtividade extends AppCompatActivity {
    Atividade atv = new Atividade();
    TextView titulo_atividade;
    TextView descricao_atividade;
    TextView data_criacao;
    TextView data_entrega;
    TextView pontos;
    TextView disciplina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aluno_activity_tela_desc_atividade);

        atv = Values_aluno.atividade_desc;

        titulo_atividade = findViewById(R.id.titulo_atividade);
        descricao_atividade = findViewById(R.id.descricao_atividade);
        data_criacao = findViewById(R.id.data_criacao);
        data_entrega = findViewById(R.id.data_entrega);
        disciplina = findViewById(R.id.disciplina);
        pontos = findViewById(R.id.pontos);

        pontos.setText(atv.pontos+"");
        titulo_atividade.setText(atv.titulo);
        descricao_atividade.setText(atv.descricao);
        data_criacao.setText(atv.datacriacao);
        data_entrega.setText(atv.data_entrega+"");
        disciplina.setText(atv.disiciplina);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Descrição da Atividade");



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { super.onBackPressed(); return true; }

}
