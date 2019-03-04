package com.example.narcisogomes.myapplication.models;

import android.app.AliasActivity;

import java.util.ArrayList;

public class Ocorrencia {
    private int id;
    private String data;
    private String descricao;
    private String pedagogico_nome;
    private ArrayList<Aluno> array_alunos = new ArrayList<>();

    public String getPedagogico_nome() {
        return pedagogico_nome;
    }

    public void setPedagogico_nome(String pedagogico_nome) {
        this.pedagogico_nome = pedagogico_nome;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ArrayList<Aluno> getArray_alunos() {
        return array_alunos;
    }

    public void setArray_alunos(ArrayList<Aluno> array_alunos) {
        this.array_alunos = array_alunos;
    }
}
