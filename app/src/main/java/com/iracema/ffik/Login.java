package com.iracema.ffik;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import Utils.ConexaoBD;

public class Login extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
    Button btn_login,  btn_visitante;
    CheckBox chk_lembrar;
    EditText ed_email, ed_password;
    TextView txt_cadastrar, txt_esqueceu_password;
    Boolean clickMe = false;
    private String jstr, email, password, URL;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getIds();

        btn_login.setOnClickListener(this);
        btn_visitante.setOnClickListener(this);
        txt_esqueceu_password.setOnClickListener(this);
        txt_cadastrar.setOnClickListener(this);
        chk_lembrar.setOnCheckedChangeListener(this);
    }

    private void getIds() {
        chk_lembrar = (CheckBox) findViewById(R.id.login_chk_lembrar);
        btn_login = (Button) findViewById(R.id.login_btn_login);
        btn_visitante = (Button) findViewById(R.id.login_btn_pular);
        ed_email = (EditText) findViewById(R.id.login_edtxt_email);
        ed_password = (EditText) findViewById(R.id.login_edtxt_password);

        txt_cadastrar = (TextView) findViewById(R.id.login_txt_cadastrar);
        txt_esqueceu_password = (TextView) findViewById(R.id.login_txt_esqueceu_password);
    }

    private void getValues() {
        email = ed_email.getText().toString();
        password = ed_password.getText().toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.login_txt_cadastrar:
                startActivity(new Intent(Login.this, Cadastro.class));
                break;

            /*
            case R.id.login_txt_esqueceu_password:
                startActivity(new Intent(Login.this, ForgotPassword.class));
                break;*/


            case R.id.login_btn_login:
                getValues();
                /*if (email.isEmpty() || password.isEmpty())
                    Toast.makeText(Login.this, "Preencher todos os campos", Toast.LENGTH_SHORT).show();
                else
                    Login();
                break;*/
                Login();
                break;

            case R.id.login_btn_pular:
                startActivity(new Intent(Login.this, VisitanteDashboard.class));
                finish();
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { clickMe = isChecked; }

    private void Login() {
        /*startActivity(new Intent(Login.this, PromotorDashboard.class));
        finish();*/
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.LoginURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso,msg;
                    sucesso = json.getString("sucesso");
                    msg = json.getString("msg");
                    JSONArray jsonArray = json.getJSONArray("login");

                    if (sucesso.equals("1")){
                        for (int i = 0;  i < jsonArray.length(); i++){
                            JSONObject resultado = jsonArray.getJSONObject(i);

                            String idUsuario =  resultado.getString("id");
                            String nomeUsuario =  resultado.getString("nome");
                            String emailUsuario =  resultado.getString("email");
                            String acessoUsuario =  resultado.getString("acesso");


                            if (acessoUsuario.equals("usuario")) {
                                Toast.makeText(Login.this, "Bem vindo "+nomeUsuario, Toast.LENGTH_SHORT).show();
                                Intent dados = new Intent(Login.this, ClienteDashboard.class);
                                dados.putExtra("id",idUsuario );
                                dados.putExtra("tipo_usuario","usuario" );
                                startActivity(dados);
                                finish();
                            }
                            else if (acessoUsuario.equals("promotor")) {
                                Toast.makeText(Login.this, "Bem vindo "+nomeUsuario, Toast.LENGTH_SHORT).show();
                                Intent dados = new Intent(Login.this, PromotorDashboard.class);
                                dados.putExtra("id",idUsuario );
                                dados.putExtra("tipo_usuario","promotor" );
                                startActivity(dados);
                                finish();
                            }
                            else if (acessoUsuario.equals("gestor")) {
                                Toast.makeText(Login.this, "Bem vindo "+nomeUsuario, Toast.LENGTH_SHORT).show();
                                Intent dados = new Intent(Login.this, GestorDashboard.class);
                                dados.putExtra("id",idUsuario );
                                dados.putExtra("tipo_usuario","gestor" );
                                startActivity(dados);
                                finish();
                            }
                            else {
                                Toast.makeText(Login.this, "Utilizador "+nomeUsuario+" banido da aplicação! ", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        Toast.makeText(Login.this, "Erro:  "+msg, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(Login.this, "Login sem sucesso! " +e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },  new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(Login.this, "Conexão erro! " +error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("pass",password);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);

    }
}
