package com.example.narcisogomes.myapplication.aluno;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.util.RequisicaoPost;
import com.example.narcisogomes.myapplication.util.Values;

import org.json.JSONException;
import org.json.JSONObject;

public class FragConsultarNotas extends Fragment {

    ProgressBar superProgressBar;
    ImageView superImageView;
    WebView superWebView;
    String caminho_doc = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.aluno_frag_relatorio_academico, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        superProgressBar = view.findViewById(R.id.myProgressBar);
        superWebView = view.findViewById(R.id.myWebView);
        superImageView = view.findViewById(R.id.myImageView);

        superProgressBar.setMax(100);

        //superWebView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=https://americalatina.dint.fgv.br/sites/americalatina.dint.fgv.br/files/teste33.pdf");
        superWebView.getSettings().setJavaScriptEnabled(true);
        superWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                superProgressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            /*
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                superImageView.setImageBitmap(icon);
            }*/

        });

        new BuscaDocumento().execute();
    }

    private void loadURL() {
        superWebView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url="+caminho_doc);
    }

    private class BuscaDocumento extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getContext());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setTitle(R.string.carregando);
            dialog.setMessage(getResources().getString(R.string.carregando_requisicao));
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
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=3.2" +
                        "&id_aluno=" + Values_aluno.aluno.getId_aluno()+
                        "&tipo_doc=3");
            } catch (Exception e) {
                dialog.hide();
                Toast.makeText(getContext(), "Mensagem: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return ab;
        }

        @Override
        protected void onPostExecute(String strings) {
            JSONObject objeto = null;
            Log.e(Values.TAG, strings);
            try {
                objeto = new JSONObject(strings);
                String mensagem = objeto.getString("message");
                boolean is = objeto.getBoolean("success");
                if (is) {
                    JSONObject documento = objeto.getJSONObject("dados");
                    caminho_doc = documento.getString("caminho_documento");
                    dialog.hide();
                    loadURL();
                    Toast.makeText(getContext(), mensagem, Toast.LENGTH_LONG).show();

                } else {
                    dialog.hide();
                    Toast.makeText(getContext(), mensagem, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                dialog.hide();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
