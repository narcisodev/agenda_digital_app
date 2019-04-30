package com.example.narcisogomes.myapplication.professor;
import com.example.narcisogomes.myapplication.R;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.narcisogomes.myapplication.models.Materia;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListaMaterias extends AppCompatActivity {
    GroupAdapter adapter = new GroupAdapter();
    Materia m_geral = new Materia();
    RecyclerView lista;
    private List<Map<String, Object>> materias_m = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professor_lista_materias_);
        lista = findViewById(R.id.lista_materias_prof);
        lista.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Mostra o botão
        getSupportActionBar().setHomeButtonEnabled(true);//Ativa o botão
        getSupportActionBar().setTitle("Suas Disciplinas");

        /*
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> materia = materias_m.get(position);
                //Values.id_disciplina_lista_materias = (Integer) materia.get("id");
                Values.id_disciplina_prof = (int) materia.get("id");
                Values.nome_disciplina_prof = (String) materia.get("nome");
                ListaMaterias.super.onBackPressed();
            }
        });
        */
        new CarregaDisciplinas().execute();
    }


    private class DisciplinaItem extends Item<ViewHolder> {
        private final Materia materia;

        private DisciplinaItem(Materia materia) {
            this.materia = materia;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            Log.e("NARCISO222", "entrou no bind");
            TextView disciplina = viewHolder.itemView.findViewById(R.id.descricao);
            TextView curso = viewHolder.itemView.findViewById(R.id.curso);
            TextView turma = viewHolder.itemView.findViewById(R.id.turma);

            disciplina.setText(materia.nome);
            curso.setText(materia.nome_curso);
            turma.setText(materia.numero_turma);

        }

        @Override
        public int getLayout() {
            return R.layout.professor_lista_materias_modelo;
        }
    }


    //MÉTODO USADO QUANDO UM COMPONENTE DO MENU É CLICADO
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }


    private class CarregaDisciplinas extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ListaMaterias.this);
            dialog.setTitle(R.string.carregando);
            dialog.setMessage("Estamos carregando a sua requisição...");
            dialog.show();
        }

        /*
         * onde a operação deve ser implementada, pois este método é executado em outra thread
         * este é o único método obrigatório da classe AsyncTask
         * */
        @Override
        protected String doInBackground(Void... voids) {
            String ab = "";
            try {
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=5" +
                        "&id_professor=" + Values_professor.professor.getId_professor());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return ab;
        }


        @Override
        protected void onPostExecute(String strings) {
            JSONObject objeto = null;
            try {
                objeto = new JSONObject(strings);
                boolean is = objeto.getBoolean("success");
                if (is) {
                    JSONArray disciplinas = objeto.getJSONArray("dados");
                    for (int i = 0; i < disciplinas.length(); i++) {
                        JSONObject disc_json = disciplinas.getJSONObject(i);
                        m_geral.id = disc_json.getInt("id");
                        m_geral.nome = disc_json.getString("nome");
                        m_geral.nome_curso = disc_json.getString("nome_curso");
                        m_geral.numero_turma = disc_json.getString("numero_turma");
                        adapter.add(new DisciplinaItem(m_geral));
                        m_geral = new Materia();
                    }

                    adapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(@NonNull Item item, @NonNull View view) {
                            DisciplinaItem ditem = (DisciplinaItem) item;
                            int id = ditem.materia.id;
                            Toast.makeText(ListaMaterias.this, "MATERIA " + ditem.materia.nome + " ID " + id, Toast.LENGTH_LONG).show();

                        }
                    });
                    lista.setAdapter(adapter);
                    dialog.hide();
                }
            } catch (JSONException e) {
                dialog.hide();
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

    }
}
