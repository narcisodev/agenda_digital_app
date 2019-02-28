package com.example.narcisogomes.myapplication.models;

public class Responsavel extends Usuario{
    private int id_reponsavel;
    private String rua;
    private String bairro;
    private String numero;
    private String cidade;

    public void setUsuario(Usuario usuario) {
        setId_usuario(usuario.getId_usuario());
        setNome_usuario(usuario.getNome_usuario());
        setLogin(usuario.getLogin());
        setTipoUsuario_id(usuario.getTipoUsuario_id());
        setId_ususario_t(usuario.getId_ususario_t());
        setEmail(usuario.getEmail());
        setLogin(usuario.getLogin());
    }

    public void setId_reponsavel(int id_reponsavel) {
        this.id_reponsavel = id_reponsavel;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getId_reponsavel() {

        return id_reponsavel;
    }

    public String getRua() {
        return rua;
    }

    public String getBairro() {
        return bairro;
    }

    public String getNumero() {
        return numero;
    }

    public String getCidade() {
        return cidade;
    }
}
