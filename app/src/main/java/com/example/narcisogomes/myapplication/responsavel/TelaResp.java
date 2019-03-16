package com.example.narcisogomes.myapplication.responsavel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.narcisogomes.myapplication.R;

import com.example.narcisogomes.myapplication.responsavel.ListAdapters.ListViewAdapterOcorrencias;
import com.example.narcisogomes.myapplication.util.SobreAct;

public class TelaResp extends AppCompatActivity {

    private TextView mTextMessage;
    int id_tela;
    ListViewAdapterOcorrencias lvao;
    ListView lv_oc;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    verificaMenu(1);
                    fragment = new FragDashboard();
                    break;

                case R.id.navigation_oc_resp:
                    fragment = new FragListaOcorrencia();
                    break;

            }
            if (fragment != null) {
                Values_resp.telaResp_context = TelaResp.this;
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments, fragment).commit();
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resp_activity_tela_resp);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //iniciar com o fragment FragTarefas aberto
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragments, new FragDashboard()).commit();

        getSupportActionBar().setTitle("Área do Responsável");
    }


    public void verificaMenu(int id_tela){
        if(id_tela ==1){
            this.id_tela = id_tela;
            invalidateOptionsMenu();
        }
    }

    public void verificaMenu(int id_tela, ListView listaOcorrencias, ListViewAdapterOcorrencias lvao){
        if(id_tela == 2){
            this.id_tela = id_tela;
            this.lv_oc = listaOcorrencias;
            this.lvao = lvao;
            invalidateOptionsMenu();
        }
    }



    /*
     * CRIAÇÃO E CONTROLE DO MENU
     *
     *
     * */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resp, menu);
        if(id_tela == 1){
            MenuItem menu_searItem = menu.findItem(R.id.action_search_resp);
            menu_searItem.setVisible(false);
        }
        if(id_tela == 2){
            MenuItem menu_searItem = menu.findItem(R.id.action_search_resp);
            menu_searItem.setVisible(true);
            SearchView searchView = (SearchView) menu_searItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(TextUtils.isEmpty(newText)){
                        lvao.filter("");
                        lv_oc.clearTextFilter();
                    }else{
                        lvao.filter(newText);
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


        //noinspection SimplifiableIfStatement
        if (id == R.id.sair) {
            onBackPressed();
        }

        if (id == R.id.action_config) {

        }

        if (id == R.id.action_sobre) {
            startActivity(new Intent(getApplicationContext(), SobreAct.class));
        }

        return super.onOptionsItemSelected(item);
    }

}
