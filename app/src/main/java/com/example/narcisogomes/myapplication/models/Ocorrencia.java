package com.example.narcisogomes.myapplication.models;

import android.app.AliasActivity;

import java.util.ArrayList;

public class Ocorrencia {
    private int id;
    private int id_pedagogo;
    private String data;
    private String data_bd;
    private String descricao;
    private String pedagogico_nome;
    private String nome_al;
    private ArrayList<Aluno> array_alunos = new ArrayList<>();


    public String getData_bd() {
        return data_bd;
    }

    public void setData_bd(String data_bd) {
        this.data_bd = data_bd;
    }

    public String getPedagogico_nome() {
        return pedagogico_nome;
    }

    public void setPedagogico_nome(String pedagogico_nome) {
        this.pedagogico_nome = pedagogico_nome;
    }

    public int getId_pedagogo() {
        return id_pedagogo;
    }

    public void setId_pedagogo(int id_pedagogo) {
        this.id_pedagogo = id_pedagogo;
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

    public String getNome_al() {
        return nome_al;
    }

    public void setNome_al(String nome_al) {
        this.nome_al = nome_al;
    }
}
