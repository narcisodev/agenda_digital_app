package com.example.narcisogomes.myapplication.pedagogico;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

    public class FragListaOcorrencias extends Fragment {

        private ListView listaOcorrencias;
        private List<Map<String, Object>> ocorrencias;
        private ArrayList<Atividade> atividades_array = new ArrayList<>();

        @Nullable
        @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pedagogico_frag_lista_ocorrencias,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private List<Map<String,Object>> listarOcorrencias(){

        ocorrencias = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        Atividade a = new Atividade();

        for(int i = 0; i< atividades_array.size(); i++){
            a = atividades_array.get(i);
            item.put("id", a.id_atividade);
            item.put("data", a.data);
            item.put("titulo", a.titulo);
            item.put("descricao", a.descricao);
            item.put("pontos", a.pontos);
            item.put("data_entrega", a.data_entrega);
            ocorrencias.add(item);
            item = new HashMap<String, Object>();
            a = new Atividade();
        }

        return ocorrencias;
    }
}
