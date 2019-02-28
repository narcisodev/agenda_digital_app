package com.example.narcisogomes.myapplication.models;

public class Professor extends Usuario {
    private int id_professor;
    private String matricula;
    private String formacao;

    public void setUsuario(Usuario usuario) {
        setId_usuario(usuario.getId_usuario());
        setNome_usuario(usuario.getNome_usuario());
        setLogin(usuario.getLogin());
        setTipoUsuario_id(usuario.getTipoUsuario_id());
        setId_ususario_t(usuario.getId_ususario_t());
        setEmail(usuario.getEmail());
        setLogin(usuario.getLogin());
    }

    public int getId_professor() {
        return id_professor;
    }

    public void setId_professor(int id_professor) {
        this.id_professor = id_professor;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getFormacao() {
        return formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }
}
