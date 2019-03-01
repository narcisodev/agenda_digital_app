package com.example.narcisogomes.myapplication.aluno;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TelaTodasAtividades extends AppCompatActivity {
    Atividade a = new Atividade();
    ArrayList<Atividade> lista_atividade = new ArrayList<>();
    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aluno_activity_tela_todas_atividades);
        fragment = new FragListaTodasAtv();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments, fragment).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Listagem de tarefas");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_listar_atuais) {
            fragment = new FragListaTodasAtv();
            Values_aluno.valor_menu_lista_materias = "after";
            Values_aluno.menu_clicked = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments, fragment).commit();
        } else if (id ==R.id.action_listar_anteriores) {
            fragment = new FragListaTodasAtv();
            Values_aluno.valor_menu_lista_materias = "before";
            Values_aluno.menu_clicked = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments, fragment).commit();
        } else if (id == R.id.action_listar_todas) {
            fragment = new FragListaTodasAtv();
            Values_aluno.valor_menu_lista_materias = "all";
            Values_aluno.menu_clicked = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments, fragment).commit();
        }else{
            super.onBackPressed();
        }



        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listagem_tarefas, menu);
        return true;
    }


}
