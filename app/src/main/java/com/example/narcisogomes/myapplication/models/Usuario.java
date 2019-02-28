package com.example.narcisogomes.myapplication.models;

public class Usuario {
    private int id_usuario;
    private String nome_usuario;
    private String login;
    private String email;
    private int tipoUsuario_id;
    private int id_ususario_t;

    public Usuario(){

    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNome_usuario() {
        return nome_usuario;
    }

    public void setNome_usuario(String nome_usuario) {
        this.nome_usuario = nome_usuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTipoUsuario_id() {
        return tipoUsuario_id;
    }

    public void setTipoUsuario_id(int tipoUsuario_id) {
        this.tipoUsuario_id = tipoUsuario_id;
    }

    public int getId_ususario_t() {
        return id_ususario_t;
    }

    public void setId_ususario_t(int id_ususario_t) {
        this.id_ususario_t = id_ususario_t;
    }
}
