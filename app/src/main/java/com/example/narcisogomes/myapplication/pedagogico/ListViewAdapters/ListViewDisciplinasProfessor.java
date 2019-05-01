package com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Materia;

import java.util.ArrayList;
import java.util.List;

public class ListViewDisciplinasProfessor extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<Materia> modellist;
    ArrayList<Materia> arrayList;

    public ListViewDisciplinasProfessor(Context mContext, List<Materia> modellist) {
        this.mContext = mContext;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Materia>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder {
        TextView disciplina, curso, turma, qtdatividades;
    }

    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int position) {
        return modellist.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ListViewDisciplinasProfessor.ViewHolder();
            convertView = inflater.inflate(R.layout.ped_lista_disciplinas_modelo, null);
            holder.disciplina = convertView.findViewById(R.id.disciplina);
            holder.turma = convertView.findViewById(R.id.turma);
            holder.curso = convertView.findViewById(R.id.curso);
            holder.qtdatividades = convertView.findViewById(R.id.qtdatividade);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.curso.setText(modellist.get(position).nome_curso);
        holder.turma.setText(modellist.get(position).numero_turma);
        holder.disciplina.setText(modellist.get(position).nome);
        if(modellist.get(position).qtd_tarefas == 0){
            holder.qtdatividades.setText("Nenhuma atividade cadastrada");
        }

        if(modellist.get(position).qtd_tarefas == 1){
            holder.qtdatividades.setText("1 atividade cadastrada");
        }

        if(modellist.get(position).qtd_tarefas>1){
            holder.qtdatividades.setText(modellist.get(position).qtd_tarefas+" atividades cadastradas");
        }

        return convertView;
    }
}
