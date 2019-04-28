package com.example.narcisogomes.myapplication.models;

import java.util.ArrayList;

public class DetalhesAlunos {
    private String n_a;
    private String email;
    private String n_p;
    private String matricula;
    private String curso;
    private String turma;
    private int t_occ;
    private int t_tar;

    private ArrayList<Ocorrencia> lista_occ = new ArrayList<>();
    private ArrayList<Atividade> lista_atv = new ArrayList<>();

    public int getT_tar() {
        return t_tar;
    }

    public void setT_tar(int t_tar) {
        this.t_tar = t_tar;
    }

    public int getT_occ() {
        return t_occ;
    }

    public void setT_occ(int t_occ) {
        this.t_occ = t_occ;
    }

    public ArrayList<Atividade> getLista_atv() {
        return lista_atv;
    }

    public void setLista_atv(ArrayList<Atividade> lista_atv) {
        this.lista_atv = lista_atv;
    }

    public void setLista_occ(ArrayList<Ocorrencia> lista_occ) {
        this.lista_occ = lista_occ;
    }

    public ArrayList<Ocorrencia> getLista_occ() {
        return lista_occ;
    }

    public String getN_a() {
        return n_a;
    }

    public void setN_a(String n_a) {
        this.n_a = n_a;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getN_p() {
        return n_p;
    }

    public void setN_p(String n_p) {
        this.n_p = n_p;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }
}
