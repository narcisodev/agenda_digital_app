package com.example.narcisogomes.myapplication.professor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.pedagogico.TelaPedagogico;
import com.example.narcisogomes.myapplication.professor.ListAdapters.ListViewAdapterAtividades;
import com.example.narcisogomes.myapplication.util.Mensagens;

public class TelaProfessor extends AppCompatActivity {
    AlertDialog alerta;
    private TextView mTextMessage;
    private String frag_ativo = "frag_home";
    ListViewAdapterAtividades listViewAdapterAtividades;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_tarefas:
                    frag_ativo = "frag_tarefas";
                    if (Values_professor.total_tarefas == 0) {
                        fragment = new Mensagens();
                    } else {

                        fragment = new FragTarefas();

                    }

                    break;
                case R.id.navigation_home:
                    frag_ativo = "frag_home";
                    fragment = new FragHome();
                    break;
                    /*
                case R.id.navigation_notifications:
                    return true;
                    //mTextMessage.setText(R.string.title_notifications);
                    */
            }
            if (fragment != null) {


                getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments, fragment).commit();
            }
            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professor_activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportActionBar().setTitle("Área do Professor");
        //iniciar com o fragment FragTarefas aberto
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments, new FragHome()).commit();

    }



    /*
     * CRIAÇÃO E CONTROLE DO MENU
     *
     *
     * */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_professor, menu);

        MenuItem myActionMenuItem= menu.findItem(R.id.app_bar_search_prof);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    Values_professor.listViewAdapterAtividades.filter("");
                    Values_professor.lista_atv.clearTextFilter();
                }else{
                    Values_professor.listViewAdapterAtividades.filter(newText);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.navigation_logoff_prof) {
            alertConfirmaSair();
        }

        if (id == R.id.navigation_refresh_prof) {
            if(frag_ativo == "frag_tarefas"){
                if (Values_professor.total_tarefas == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments, new Mensagens()).commit();
                } else {

                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments, new FragTarefas()).commit();

                }

            }else
            if(frag_ativo == "frag_home"){
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments, new FragHome()).commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void alertConfirmaSair() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Atenção!");
        //define a mensagem
        builder.setMessage("Tem serteza que deseja sair?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                onBackPressed();

            }
        });

        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
}
