package com.example.narcisogomes.myapplication.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narcisogomes.myapplication.R;
import com.example.narcisogomes.myapplication.aluno.TelaAluno;
import com.example.narcisogomes.myapplication.aluno.Values_aluno;
import com.example.narcisogomes.myapplication.models.Aluno;
import com.example.narcisogomes.myapplication.models.Pedagogico;
import com.example.narcisogomes.myapplication.models.Professor;
import com.example.narcisogomes.myapplication.models.Responsavel;
import com.example.narcisogomes.myapplication.models.Usuario;
import com.example.narcisogomes.myapplication.pedagogico.TelaPedagogico;
import com.example.narcisogomes.myapplication.pedagogico.Values_pedagogico;
import com.example.narcisogomes.myapplication.professor.Values_professor;
import com.example.narcisogomes.myapplication.professor.TelaProfessor;
import com.example.narcisogomes.myapplication.responsavel.TelaResp;
import com.example.narcisogomes.myapplication.responsavel.Values_resp;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends Activity implements DialogInterface.OnClickListener {

    //variaveis usadas para classe registerUser
    String senha_user;
    String cpf_user;

    private AlertDialog alerta;
    private EditText user;//campo de informação do usuario
    private EditText pass;//campo de informação da senha
    private String senha;//recebe a senha quando o usuario clicar em logar
    private String login;//recebe o login quando o usuário clicar em logar
    private AlertDialog alertDialog;//recebe o alert dialog utilizado para informar se o login foi efetuado com sucesso
    private Usuario usuario = new Usuario();//guarda os dados do usuário logado
    private Button p_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util_login_activity);
        user = (EditText) findViewById(R.id.usuario);
        pass = (EditText) findViewById(R.id.senha);
        this.alertDialog = criaAviso("", ""); //cria aviso para uso posterior
        user.setText("alessandra");
        pass.setText("123");

        user.addTextChangedListener(MaskEditUtil.mask((EditText) user, MaskEditUtil.FORMAT_CPF));
        p_a = findViewById(R.id.primeiro_acesso);

        p_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutRegisterUsuario();
            }
        });
    }

    //quando usuario clicar no botão entrar
    public void logar(View v) {
        senha = pass.getText().toString();
        login = user.getText().toString();
        login = login.replace(".","").replace("-","");
        new buscaLogin().execute();
    }

    @Override
    public void onBackPressed() {
        user.setText("");
        pass.setText("");
        super.onBackPressed();
    }

    //cria diálogo de confirmação
    private AlertDialog criaAviso(String aviso, String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(aviso);
        builder.setTitle("Atenção");
        builder.setPositiveButton(getString(R.string.ok), (DialogInterface.OnClickListener) Login.this);
        return builder.create();
    }

    //chamado para cliques de botões do alertDialog
    @Override
    public void onClick(DialogInterface v, int item) {
        switch (item) {
            case DialogInterface.BUTTON_POSITIVE:
                alertDialog.hide();
                break;

        }
    }


    //se o usuario for autentico inicia a Dashboard (Inicial)
    public void isLogado(int tipo_usuario) {
        //startActivity(new Intent(this, DashDiscente.class));
        Values.usuario = usuario;
        Log.e(Values.TAG, "ISLOGADP--VALOR: " + tipo_usuario);
        switch (tipo_usuario) {
            case 1:
                //PEDAGOGICO
                startActivity(new Intent(Login.this, TelaPedagogico.class));
                break;
            case 2:
                //PROFESSOR
                startActivity(new Intent(Login.this, TelaProfessor.class));
                break;
            case 3:
                //RESPONÁVEL
                startActivity(new Intent(Login.this, TelaResp.class));
                break;
            case 4:
                //ALUNO
                startActivity(new Intent(Login.this, TelaAluno.class));
                break;
        }
    }


    private void layoutRegisterUsuario() {
        //LayoutInflater é utilizado para inflar nosso layout em uma view.
        //-pegamos nossa instancia da classe
        LayoutInflater li = getLayoutInflater();

        //inflamos o layout alerta.xml na view
        final View view = li.inflate(R.layout.util_register_user_activity, null);
        //definimos para o botão do layout um clickListener
        TextView login = view.findViewById(R.id.login);
        login.addTextChangedListener(MaskEditUtil.mask((EditText) login, MaskEditUtil.FORMAT_CPF));
        view.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText senha, conf_senha, login;
                login = view.findViewById(R.id.login);
                senha = view.findViewById(R.id.senha);
                conf_senha = view.findViewById(R.id.conf_senha);
                String logins = login.getText().toString();
                String senha1 = senha.getText().toString();
                String senha2 = conf_senha.getText().toString();
                //Log.e("NARCISO222", "SENHA 1 -> "+ senha1 + " SENHA 2 ->"+senha2);

                if (logins == null || logins.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.login_informe, Toast.LENGTH_SHORT).show();
                } else if ((senha1 == null) || senha1.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.senhas_informe, Toast.LENGTH_SHORT).show();
                } else if ((senha2 == null) || senha2.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.senhas_confirme_informe, Toast.LENGTH_SHORT).show();
                } else if (senha1.compareTo(senha2) != 0) {
                    Toast.makeText(getApplicationContext(), R.string.senhas_n_coicidem, Toast.LENGTH_SHORT).show();
                } else {
                    senha_user = senha1;
                    cpf_user = logins.replace(".","").replace("-","");
                    new registerUser().execute();
                }

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registrar Usuário");
        builder.setView(view);
        alerta = builder.create();
        alerta.show();
    }


    /*
     * classe para executar comunicação com API em background
     * Params - é o tipo dos parâmetros que são enviados para a execução.
     * Progress - é o tipo que representa a unidade de progresso da tarefa.
     * Result  - é o tipo de retorno da operação ralizada
     */

    private class buscaLogin extends AsyncTask<String[], Void, String> {
        String login_this = login;
        String senha_this = senha;

        private ProgressDialog dialog;//apresetado quando a operação estiver em execução em outra thread

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Login.this);
            dialog.setTitle(R.string.carregando);
            dialog.setMessage("Estamos carregando a sua requisição...");
            dialog.show();
        }


        /*
         * onde a operação deve ser implementada, pois este método é executado em outra thread
         * este é o único método obrigatório da classe AsyncTask
         * */
        @Override
        protected String doInBackground(String[]... strings) {
            String ab = "";
            try {
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=1&login=" + login + "&senha=" + senha);
            } catch (Exception e) {
                Toast.makeText(Login.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return ab;

        }


        @Override
        protected void onPostExecute(String strings) {
            String responsebody = strings;
            JSONObject objeto = null;


            try {
                objeto = new JSONObject(responsebody);
                boolean isLogado = objeto.getBoolean("success");
                if (isLogado) {
                    JSONObject user = objeto.getJSONObject("dados");
                    int tipo_usuario = user.getInt("tipoUsuario_id");
                    usuario.setId_usuario(user.getInt("id_usuario"));
                    usuario.setNome_usuario(user.getString("nome"));
                    //usuario.setLogin(user.getString("login"));
                    usuario.setEmail(user.getString("email"));
                    usuario.setTipoUsuario_id(user.getInt("tipoUsuario_id"));

                    if (usuario.getTipoUsuario_id() == 1) {
                        Pedagogico p = new Pedagogico();
                        p.setUsuario(usuario);
                        p.setId_pedagogico(user.getInt("id_pedagogico"));
                        p.setMatricula(user.getString("matricula"));
                        Values_pedagogico.ped_logado = p;
                    } else if (usuario.getTipoUsuario_id() == 2) {
                        Professor prof = new Professor();
                        prof.setUsuario(usuario);
                        prof.setId_professor(user.getInt("id_professor"));
                        prof.setMatricula(user.getString("matricula"));
                        prof.setFormacao(user.getString("formacao"));
                        Values_professor.professor = prof;
                        JSONObject dados_a = objeto.getJSONObject("adicionais");
                        Values_professor.total_tarefas = dados_a.getInt("tarefas_cadastradas");

                    } else if (usuario.getTipoUsuario_id() == 3) {
                        Responsavel responsavel = new Responsavel();
                        responsavel.setUsuario(usuario);
                        responsavel.setRua(user.getString("rua"));
                        responsavel.setBairro(user.getString("bairro"));
                        responsavel.setNumero(user.getString("numero"));
                        responsavel.setCidade(user.getString("cidade"));
                        responsavel.setId_reponsavel(user.getInt("id_responsavel"));
                        Values_resp.resp_logado = responsavel;

                    } else if (usuario.getTipoUsuario_id() == 4) {
                        Aluno aluno = new Aluno();
                        aluno.setUsuario(usuario);
                        aluno.setId_aluno(user.getInt("id_aluno"));
                        aluno.setMatricula(user.getString("matricula"));
                        aluno.setResponsavel(user.getString("resp"));
                        aluno.setTurma(user.getString("turma"));
                        aluno.setCurso(user.getString("curso"));
                        Values_aluno.aluno = aluno;
                        JSONObject dados_a = objeto.getJSONObject("adicionais");
                        Values_aluno.id_turma = dados_a.getInt("id_turma");
                    }

                    dialog.hide();
                    isLogado(usuario.getTipoUsuario_id());

                } else {
                    dialog.hide();
                    alertDialog = criaAviso(getResources().getString(R.string.login_errado), getResources().getString(R.string.title_atenction));
                    alertDialog.show();
                }

            } catch (JSONException e) {
                //exibe erro caso ocorra durante a leitura do JSON
                Toast.makeText(getApplicationContext(), "Erro - Login: " + e.getMessage(), Toast.LENGTH_LONG).show();
                alertDialog.hide();
            }
        }
    }


    private class registerUser extends AsyncTask<String[], Void, String> {
        private ProgressDialog dialog;//apresetado quando a operação estiver em execução em outra thread
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Login.this);
            dialog.setTitle(R.string.carregando);
            dialog.setMessage("Estamos carregando a sua requisição...");
            dialog.show();
        }


        /*
         * onde a operação deve ser implementada, pois este método é executado em outra thread
         * este é o único método obrigatório da classe AsyncTask
         * */
        @Override
        protected String doInBackground(String[]... strings) {
            String ab = "";
            try {
                ab = RequisicaoPost.sendPost(Values.URL_SERVICE, "acao=1.1&cpf=" + cpf_user + "&senha=" + senha_user);
            } catch (Exception e) {
                Toast.makeText(Login.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return ab;

        }


        @Override
        protected void onPostExecute(String strings) {
            String responsebody = strings;
            JSONObject objeto = null;


            try {
                objeto = new JSONObject(responsebody);
                boolean isLogado = objeto.getBoolean("success");
                String mensagem = objeto.getString("message");
                if (isLogado) {
                    dialog.hide();
                    alertDialog = criaAviso(mensagem, getResources().getString(R.string.title_sucess));
                    alertDialog.show();
                    alerta.dismiss();
                } else {
                    dialog.hide();
                    alertDialog = criaAviso(mensagem, getResources().getString(R.string.title_atenction));
                    alertDialog.show();
                }

            } catch (JSONException e) {
                //exibe erro caso ocorra durante a leitura do JSON
                Toast.makeText(getApplicationContext(), "Erro - Login: " + e.getMessage(), Toast.LENGTH_LONG).show();
                alertDialog.hide();
            }
        }
    }

}
