package com.example.narcisogomes.myapplication.pedagogico;

import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Pedagogico;

import java.util.ArrayList;

public class Values_pedagogico {
    public static Pedagogico ped_logado = new Pedagogico();

    //VARIAVEL PARA COMUNICAR A TROCA DE DADOS ENTRE O CADASTRO DE OCORRENCIAS E A LISTAGEM DE ALUNOS
    public static ArrayList<Aluno> lista_alunos = new ArrayList<>();
    public static boolean is_tela_oc = false;

}
