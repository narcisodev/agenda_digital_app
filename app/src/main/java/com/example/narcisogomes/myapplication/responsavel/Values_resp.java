package com.example.narcisogomes.myapplication.responsavel;

import com.example.narcisogomes.myapplication.models.Responsavel;

public class Values_resp {
    /*
    * Usada para carregar o aluno exato ao seleciona-lo na lista de alunos do resp
    * */
    public static int id_aluno_selecionado;
    public static int turma_aluno_selecionado;

    /*
    * Armazena os dados do resonsavel logado
    * */
    public static Responsavel resp_logado;

    /*
    * Armazena o Contexto da Classe TelaResp para manipulação do menu
    */
    public static TelaResp telaResp_context;


   /*
   * Para Testes
   * */

    public static String[] dados_teste_listview = new String[] { "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread",
            "Honeycomb", "Ice Cream Sandwich", "Jelly Bean",
            "KitKat", "Lollipop", "Marshmallow", "Nougat" };


}
