package com.example.narcisogomes.myapplication.aluno;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.narcisogomes.myapplication.R;

public class FragConsultarNotas extends Fragment {

    ProgressBar superProgressBar;
    ImageView superImageView;
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
        superImageView = view.findViewById(R.id.myImageView);

        superProgressBar.setMax(100);

        superWebView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=https://americalatina.dint.fgv.br/sites/americalatina.dint.fgv.br/files/teste33.pdf");
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

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                superImageView.setImageBitmap(icon);
            }
        });
    }
}
