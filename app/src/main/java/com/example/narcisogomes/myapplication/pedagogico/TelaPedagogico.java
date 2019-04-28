package com.example.narcisogomes.myapplication.pedagogico;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.aluno.TelaAluno;
import com.example.narcisogomes.myapplication.pedagogico.ListViewAdapters.ListViewAdapterOcorrencias;
import com.example.narcisogomes.myapplication.professor.FragTarefas;
import com.example.narcisogomes.myapplication.professor.Values_professor;
import com.example.narcisogomes.myapplication.util.Values;

public class TelaPedagogico extends AppCompatActivity {
    boolean ver_menu_minhas_occ = false;
    AlertDialog alerta;
    FragListaOcorrencias fragListaOcorrencias = null;
    int count_menu = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_inicio:
                    Values_pedagogico.is_tela_l_o = false;
                    Values_pedagogico.frag_a = "DASH";
                    fragment = new FragTelaInicial();
                    invalidateOptionsMenu();
                    break;
                case R.id.navigation_ocorrencias:
                    Values_pedagogico.is_tela_l_o = true;
                    Values_pedagogico.frag_a = "OCR";
                    fragListaOcorrencias = new FragListaOcorrencias();
                    fragment = fragListaOcorrencias;
                    invalidateOptionsMenu();
                    break;
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
        setContentView(R.layout.pedagogico_act_tela_princ);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportActionBar().setTitle("Área do Pedagogo");
        //iniciar com o fragment FragTarefas aberto
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments, new FragTelaInicial()).commit();

    }

    @Override
    public void onBackPressed() {
        alertConfirmaSair();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        count_menu++;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pedagogico, menu);
        if (Values_pedagogico.frag_a == "DASH") {
            MenuItem menu_searItem = menu.findItem(R.id.action_search_ped);
            menu_searItem.setVisible(false);
            MenuItem myActionMenuItem = menu.findItem(R.id.action_my_occ);
            myActionMenuItem.setVisible(false);
            MenuItem myActionMenuItem2 = menu.findItem(R.id.action_occ_arq);
            myActionMenuItem2.setVisible(false);
            MenuItem myActionMenuItem3 = menu.findItem(R.id.action_occ_abertas);
            myActionMenuItem3.setVisible(false);
        }

        if (Values_pedagogico.frag_a == "OCR") {
            MenuItem myActionMenuItem = menu.findItem(R.id.action_search_ped);
            myActionMenuItem.setVisible(true);
            MenuItem myActionMenuItem1 = menu.findItem(R.id.action_my_occ);
            myActionMenuItem1.setVisible(true);
            MenuItem myActionMenuItem2 = menu.findItem(R.id.action_occ_arq);
            myActionMenuItem2.setVisible(true);
            SearchView searchView = (SearchView) myActionMenuItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(Values_pedagogico.is_tela_l_o){
                        if (TextUtils.isEmpty(newText)) {
                            Values_pedagogico.listViewAdapterOcorrencias = new ListViewAdapterOcorrencias();
                            Values_pedagogico.listViewAdapterOcorrencias.filter("");
                            Values_pedagogico.lv_ocorrencias.clearTextFilter();
                        } else {
                            Values_pedagogico.listViewAdapterOcorrencias.filter(newText);
                        }
                    }
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_my_occ){
            fragListaOcorrencias.buscarMinhasOcorrencias();
        }

        if(id == R.id.action_occ_arq){
            fragListaOcorrencias.buscarOcorrenciasArquivadas();
        }

        if(id == R.id.action_occ_abertas){
            fragListaOcorrencias.buscarOcorrenciasAbertas();
        }

        if (id == R.id.action_search_ped) {

        }

        if (id == R.id.action_refresh_ped) {

        }

        if (id == R.id.action_logoff_ped) {
            alertConfirmaSair();
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
                TelaPedagogico.super.onBackPressed();

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
