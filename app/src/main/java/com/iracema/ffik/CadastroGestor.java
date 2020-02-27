package com.iracema.ffik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Utils.ConexaoBD;

public class CadastroGestor extends AppCompatActivity implements View.OnClickListener{
    Button btn_cadastro;
    TextView txt_login;
    RadioButton rd_uuario, rd_promotor;
    EditText ed_username, ed_email, ed_password, ed_confirma_password;
    private String jstr, username, email, password, confirma_password,acesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_gestor);

        getIds();

        txt_login.setOnClickListener(this);
        btn_cadastro.setOnClickListener(this);
    }

    private void getIds(){
        btn_cadastro = (Button)findViewById(R.id.cadastro_btn_cadastro);

        txt_login = (TextView)findViewById(R.id.cadastro_txt_login);

        rd_uuario = (RadioButton)findViewById(R.id.cadastro_radio_usuario);
        rd_promotor = (RadioButton)findViewById(R.id.cadastro_radio_promotor);

        ed_username = (EditText)findViewById(R.id.cadastro_edtxt_username);
        ed_email = (EditText)findViewById(R.id.cadastro_edtxt_email);
        ed_password = (EditText)findViewById(R.id.cadastro_edtxt_password);
        ed_confirma_password = (EditText)findViewById(R.id.cadastro_edtxt_confirma_password);
    }

    private void getValues(){
        username = ed_username.getText().toString();
        email = ed_email.getText().toString();
        password = ed_password.getText().toString();
        confirma_password = ed_confirma_password.getText().toString();

        if (rd_uuario.isChecked())
            acesso = getResources().getString(R.string.usuario);
        else if (rd_promotor.isChecked())
            acesso = getResources().getString(R.string.promotor);

    }

    private boolean isValidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cadastro_txt_login:
                startActivity(new Intent(CadastroGestor.this, Login.class));
                break;

            case R.id.cadastro_btn_cadastro:
                getValues();

                if (TextUtils.isEmpty(username))
                    Toast.makeText(CadastroGestor.this, getResources().getString(R.string.enter_username), Toast.LENGTH_SHORT).show();
                else if (!isValidEmail(email))
                    Toast.makeText(CadastroGestor.this, getResources().getString(R.string.email_nao_valido), Toast.LENGTH_SHORT).show();
                else if (!password.equals(confirma_password))
                    Toast.makeText(CadastroGestor.this, getResources().getString(R.string.password_nao_confere), Toast.LENGTH_SHORT).show();
                else if (acesso==null)
                    Toast.makeText(CadastroGestor.this, getResources().getString(R.string.seleciona_tipo_usuario), Toast.LENGTH_SHORT).show();
                else
                    Cadastrar();

                break;

        }
    }

    private void Cadastrar() {

        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.CadastroURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso = json.getString("sucesso");

                    if (sucesso.equals("1")){
                        startActivity(new Intent(CadastroGestor.this, Login.class));
                        finish();
                        Toast.makeText(CadastroGestor.this, "Cadastro com  sucesso! Fazer  o login", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(CadastroGestor.this, "Cadastro sem sucesso! " +e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },  new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(CadastroGestor.this, "Conexão erro! " +error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nome", username);
                params.put("username",username);
                params.put("email", email);
                params.put("password",password);
                params.put("acesso", acesso);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);

    }


}
