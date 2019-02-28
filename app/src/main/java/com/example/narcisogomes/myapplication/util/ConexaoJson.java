package com.example.narcisogomes.myapplication.util;
import org.json.JSONException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexaoJson {
    private URL url;

    private static String readStream(InputStream in){

        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        try{
            while ((line = r.readLine())!=null){
                total.append(line).append("\n");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return total.toString();
    }

    private static String request(String stringUrl){
        URL url = null;
        HttpURLConnection urlConnection = null;

        try{
            url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return readStream(in);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }

        return "";
    }

    public static String retornaDados(String url) throws JSONException {

        String dados;
        String responseBody = request(url);
        dados = responseBody;
        /*
        JSONObject objeto = new JSONObject(responseBody);
        String[] dados = new String[2];
        dados[0] = objeto.getJSONObject("dados").getString("login");
        dados[1] = objeto.getJSONObject("dados").getString("senha");
        */
        return dados;
    }

}
