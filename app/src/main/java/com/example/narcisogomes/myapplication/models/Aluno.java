package com.example.narcisogomes.myapplication.models;

public class Aluno extends Usuario{

    private int id_aluno;
    private String matricula;
    private String responsavel;
    private String turma;
    private String curso;

    public void setUsuario(Usuario usuario) {
        setId_usuario(usuario.getId_usuario());
        setNome_usuario(usuario.getNome_usuario());
        setLogin(usuario.getLogin());
        setTipoUsuario_id(usuario.getTipoUsuario_id());
        setId_ususario_t(usuario.getId_ususario_t());
        setEmail(usuario.getEmail());
        setLogin(usuario.getLogin());
    }
    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public String getCurso() {
        return curso;
    }

    public int getId_aluno() {
        return id_aluno;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getTurma() {
        return turma;
    }

    public void setId_aluno(int id_aluno) {
        this.id_aluno = id_aluno;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }
}
