package com.example.narcisogomes.myapplication.pedagogico;

import android.widget.ListView;

import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Pedagogico;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterOcorrencias;

import java.util.ArrayList;

public class Values_pedagogico {
    public static ListViewAdapterOcorrencias listViewAdapterOcorrencias;
    public static ListView lv_ocorrencias;


    public static Pedagogico ped_logado = new Pedagogico();

    //VARIAVEL PARA COMUNICAR A TROCA DE DADOS ENTRE O CADASTRO DE OCORRENCIAS E A LISTAGEM DE ALUNOS
    public static ArrayList<Aluno> lista_alunos = new ArrayList<>();
    public static boolean is_tela_oc = false;

}
