package com.example.narcisogomes.myapplication.util;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.models.Responsavel;
import com.example.narcisogomes.myapplication.models.Usuario;
import java.util.Map;

public class Values {
    //DADOS GERAIS USADOS PARA AS CONFIGURAÇÕES DO APKp00ly
    public static final String URL_SERVICE = "http://192.168.43.220:8080/proj/adservice/solicita.php";
    public static Usuario usuario = new Usuario();//guarda os dados do usuário logado para todos as activity de acesso
    public static String TAG = "N22";
    public static Responsavel responsavel = new Responsavel();
    //USADA NO CONTROLE DE DISCIPLINAS NO APK DO ALUNO
    public static int id_disciplina_lista_materias; //id da disciplina na lista_materias
    public static Map<String, Object> atividade_map;
    //USADAS NO CADASTRO DE TAREFAS NO APK DO PROFESSOR
    public static Atividade atividade_obj;//usada quando o professor clica para cadastrar tarefa
    public static int id_disciplina_prof;//usado para passar os dados da disciplina na listagem de disciplinas do professor
    public static String nome_disciplina_prof;//idem de cima
    public static int id_atividade_lista_atv_prof;
    public static boolean is_menu_desc_atv = true;
}
