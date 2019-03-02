package com.example.narcisogomes.myapplication.aluno;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.aluno.ListAdapters.ListViewAdapterAtividades;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.util.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListaAtividadesIdTurma extends AppCompatActivity {
    protected String search_url_service;
    private List<Atividade> atividades_array = new ArrayList<>();
    private ListViewAdapterAtividades listViewAdapterAtividades;
    private ListView listaAtividades;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_atividades_id_turma);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Listagem de tarefas");

        listaAtividades = findViewById(R.id.lista_atividades);

        searchView = findViewById(R.id.app_bar_search);
        //new ListaAitividades.BuscaAtividades().execute();


    }
}
