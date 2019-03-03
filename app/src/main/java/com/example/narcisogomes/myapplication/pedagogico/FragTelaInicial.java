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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.models.Atividade;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragTelaInicial extends Fragment {
    TextView nome_ped, email_ped, qtd_alunos_ped, qtd_ocorrencias_ped, qtd_curso_ped, qtd_professores_ped;
   int  qtd_alunos, qtd_curso, qtd_professores;
    int qtd_ocorrencias;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pedagogico_frag_inicial,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nome_ped = view.findViewById(R.id.nome_ped);
        email_ped = view.findViewById(R.id.email_ped);
        qtd_alunos_ped = view.findViewById(R.id.qtd_aluno_ped);
        qtd_ocorrencias_ped = view.findViewById(R.id.qtd_ocorrencias_ped);
        qtd_curso_ped = view.findViewById(R.id.qtd_cursos_ped);
        qtd_professores_ped = view.findViewById(R.id.qtd_professores_ped);

        nome_ped.setText(Values_pedagogico.ped_logado.getNome_usuario());
        email_ped.setText(Values_pedagogico.ped_logado.getEmail());

        new BuscaDadosInicial().execute();
    }





    private class BuscaDadosInicial extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(getContext());
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
            String ab="";
            try {
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=10");
            } catch (Exception e) {
                Toast.makeText(getContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            Atividade atv = new Atividade();
            String responsebody = strings;
            JSONObject objeto = null;


            try {
                objeto = new JSONObject(responsebody);
                JSONObject a = objeto.getJSONObject("dados");
                Log.e("NARCISO", "PEGOU DADOS DADOS");
                qtd_alunos = a.getInt("qtd_alunos");
                qtd_curso       = a.getInt("qtd_cursos");
                qtd_professores    = a.getInt("qtd_professores");
                qtd_ocorrencias  = a.getInt("qtd_ocorrencias");

                String desc_qtd;
                if(qtd_alunos <= 1){ desc_qtd = " aluno"; }else{ desc_qtd = " alunos"; }
                qtd_alunos_ped.setText(qtd_alunos + desc_qtd);

                if(qtd_professores <= 1){ desc_qtd = " professor"; }else{ desc_qtd = " professores"; }
                qtd_professores_ped.setText(qtd_professores+ desc_qtd);

                if(qtd_ocorrencias <= 1){ desc_qtd = " ocorrência"; }else{ desc_qtd = " ocorrências"; }
                qtd_ocorrencias_ped.setText(qtd_ocorrencias+ desc_qtd);

                if(qtd_curso <= 1){ desc_qtd = " curso"; }else{ desc_qtd = " cursos"; }
                qtd_curso_ped.setText(qtd_curso+ desc_qtd);

            } catch (JSONException e) {
                Toast.makeText(getContext(), "Erro na leitura de dados: "+ e.getMessage(), Toast.LENGTH_SHORT);
                dialog.hide();
            }

            dialog.hide();
        }
    }
}
