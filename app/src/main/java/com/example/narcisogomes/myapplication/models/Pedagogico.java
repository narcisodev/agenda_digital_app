package com.example.narcisogomes.myapplication.models;
import com.example.narcisogomes.myapplication.models.Usuario;

public class Pedagogico extends Usuario {

    private int id_pedagogico;
    private String matricula;



    public void setUsuario(Usuario usuario) {
        setId_usuario(usuario.getId_usuario());
        setNome_usuario(usuario.getNome_usuario());
        setLogin(usuario.getLogin());
        setTipoUsuario_id(usuario.getTipoUsuario_id());
        setId_ususario_t(usuario.getId_ususario_t());
        setEmail(usuario.getEmail());
        setLogin(usuario.getLogin());
    }

    public int getId_pedagogico() {
        return id_pedagogico;
    }

    public void setId_pedagogico(int id_pedagogico) {
        this.id_pedagogico = id_pedagogico;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}

