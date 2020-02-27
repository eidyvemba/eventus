package Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iracema.ffik.ClienteDashboard;
import com.iracema.ffik.GestorDashboard;
import com.iracema.ffik.IOnBackPressed;
import com.iracema.ffik.PromotorDashboard;
import com.iracema.ffik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import Utils.ConexaoBD;

public class MudarPasswordFragmento  extends Fragment implements IOnBackPressed, View.OnClickListener{
    Button btn_change_password;
    EditText ed_current_pass, ed_new_pass, ed_cnf_pass;
    JSONObject json;
    private String jstr, user_id, password, atual_password, nova_password, confirma_password, idUtilizador, tipoU;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mudar_password, container, false);

        getIds(view);


        btn_change_password.setOnClickListener(this);

        return view;
    }

    private void getIds(View view) {
        Intent dados = getActivity().getIntent();
        idUtilizador = dados.getStringExtra("id");
        tipoU = dados.getStringExtra("tipo_usuario");

        btn_change_password = (Button)view.findViewById(R.id.change_pass_btn_change);

        ed_cnf_pass = (EditText)view.findViewById(R.id.change_pass_edtxt_cnf_password);
        ed_new_pass = (EditText)view.findViewById(R.id.change_pass_edtxt_new_password);
        ed_current_pass = (EditText)view.findViewById(R.id.change_pass_edtxt_current_password);
        setValues();

    }

    private void getValues(){
        atual_password = ed_current_pass.getText().toString();
        nova_password = ed_new_pass.getText().toString();
        confirma_password = ed_cnf_pass.getText().toString();

    }

    private void setValues()
    {
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.BuscaUtilizadorURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso,msg;
                    sucesso = json.getString("sucesso");
                    msg = json.getString("msg");
                    JSONArray jsonArray = json.getJSONArray("dados");

                    if (sucesso.equals("1")){
                        for (int i = 0;  i < jsonArray.length(); i++){
                            JSONObject resultado = jsonArray.getJSONObject(i);

                            password =  resultado.getString("password");

                        }
                    } else {
                        Toast.makeText(getActivity(), "Erro:  "+msg, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Busca sem sucesso! " +e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },  new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getActivity(), "Conexão erro! " +error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", idUtilizador);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_pass_btn_change :
                getValues();

                if (TextUtils.isEmpty(atual_password) || TextUtils.isEmpty(nova_password) || TextUtils.isEmpty(confirma_password))
                    Toast.makeText(getActivity(), getResources().getString(R.string.preencher), Toast.LENGTH_SHORT).show();
                else if (!nova_password.equals(confirma_password))
                    Toast.makeText(getActivity(), getResources().getString(R.string.password_nao_confere), Toast.LENGTH_SHORT).show();
                else
                    Mudar_Password();

                break;

        }
    }

    private void Mudar_Password()
    {
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.MudarPasswordURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso,msg;
                    sucesso = json.getString("sucesso");
                    msg = json.getString("msg");

                    if (sucesso.equals("1")){
                        Intent dados;


                        if (tipoU.equals("usuario"))
                        {
                            dados = new Intent(getActivity(), ClienteDashboard.class);
                            dados.putExtra("tipo_usuario","usuario" );
                        }
                        else if (tipoU.equals("promotor"))
                        {
                            dados = new Intent(getActivity(), PromotorDashboard.class);
                            dados.putExtra("tipo_usuario","promotor" );
                        }
                        else
                        {
                            dados = new Intent(getActivity(), GestorDashboard.class);
                            dados.putExtra("tipo_usuario","gestor" );
                        }

                        dados.putExtra("id",idUtilizador );
                        startActivity(dados);
                        Toast.makeText(getActivity(), "Password alterado com sucesso!  "+msg, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), "Erro:  "+msg, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "sem sucesso! " +e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },  new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getActivity(), "Conexão erro! " +error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password",password);
                params.put("atual_password",atual_password);
                params.put("nova_password",nova_password);
                params.put("id", idUtilizador);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }

    @Override
    public boolean onBackPressed() {
        getFragmentManager().popBackStack();
        return true;
    }


}
