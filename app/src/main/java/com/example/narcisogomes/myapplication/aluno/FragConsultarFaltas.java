package com.example.narcisogomes.myapplication.aluno;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.util.Values;

public class FragConsultarFaltas extends Fragment {

    ProgressBar superProgressBar;

    WebView superWebView;

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

        superProgressBar.setMax(100);

        superWebView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=http://portalfns.saude.gov.br/images/banners/Sigem/Portaria_448_de_13_de_Setembro_de_2002.pdf");
        //superWebView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=http://192.168.43.220:8080/proj/files/teste.pdf");
        superWebView.getSettings().setJavaScriptEnabled(true);
        superWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                superProgressBar.setProgress(newProgress);
            }
        });
    }

}
