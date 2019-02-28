package com.example.narcisogomes.myapplication.professor;

import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.models.Materia;
import com.example.narcisogomes.myapplication.models.Professor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class Values_professor {
    public static Professor professor  = new Professor();
    public static int total_tarefas;
    public static Atividade atividade_obj= new Atividade();
    public static List<Materia> list_materias = new ArrayList<>();
    public static boolean alterou_tarefa = false;
}
