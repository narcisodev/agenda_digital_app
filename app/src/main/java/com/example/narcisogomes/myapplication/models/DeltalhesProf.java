package com.example.narcisogomes.myapplication.models;

import java.util.ArrayList;

public class DeltalhesProf {
    public int id_prof;
    public String nome;
    public String email;
    public String matricula;
    public String formacao;
    public int qtd_materias;
    public ArrayList<Materia> lista_materias = new ArrayList<>();
    public int qtd_atividades;
    public ArrayList<Atividade> lista_atividades = new ArrayList<>();
}
