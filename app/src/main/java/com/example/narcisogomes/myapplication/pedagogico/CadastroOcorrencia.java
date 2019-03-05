package com.example.narcisogomes.myapplication.pedagogico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.narcisogomes.myapplication.R;

public class CadastroOcorrencia extends AppCompatActivity {

    ListView lv_alunos_oc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ped_activity_cadastro_ocorrencia);
        lv_alunos_oc = findViewById(R.id.lista_alunos_oc);

        String[] city_names = {"Óbidos", "Santarém", "Manaus", "Mondongo", "Santos", "Rio de Janeiro", "Fumaça", "Loucuara", "Belterra", "Peixe Boi", "Santa Helema", "Mario Santos", "Junior Gustavo"};
        ArrayAdapter a = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, city_names);
        lv_alunos_oc.setAdapter(a);
    }
}
