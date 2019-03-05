package com.example.narcisogomes.myapplication.professor;

import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.models.Materia;
import com.example.narcisogomes.myapplication.models.Professor;
import com.example.narcisogomes.myapplication.professor.ListAdapters.ListViewAdapterAtividades;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class Values_professor {
    public static Professor professor  = new Professor();
    public static int total_tarefas;
    public static Atividade atividade_obj= new Atividade();
    public static List<Materia> list_materias = new ArrayList<>();
    public static boolean alterou_tarefa = false;
    public static ListView lista_atv;
    public static ListViewAdapterAtividades listViewAdapterAtividades;


}
