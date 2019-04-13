package com.example.narcisogomes.myapplication.pedagogico;

import android.widget.ListView;

import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Ocorrencia;
import com.example.narcisogomes.myapplication.models.Pedagogico;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterOcorrencias;

import java.util.ArrayList;

public class Values_pedagogico {
    public static ListViewAdapterOcorrencias listViewAdapterOcorrencias;
    public static ListView lv_ocorrencias = null;
    public static Pedagogico ped_logado = new Pedagogico();
    //VARIAVEL PARA COMUNICAR A TROCA DE DADOS ENTRE O CADASTRO DE OCORRENCIAS E A LISTAGEM DE ALUNOS
    public static ArrayList<Aluno> lista_alunos = new ArrayList<>();

    public static boolean is_tela_oc = false;

    public static boolean is_tela_l_o = false;

    public static String frag_a = "DASH";

    public static boolean is_editar = false; //false - cadastra - true - edita
    public static Ocorrencia ocorrencia_editar;

    public static boolean pesquisa_ocorrencia_novamente = false; //pesquisar ocorrencia novamente no descocorrencia caso ela tenha sido editada



}

