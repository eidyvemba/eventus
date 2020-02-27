package Fragmentos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iracema.ffik.ClienteDashboard;
import com.iracema.ffik.IOnBackPressed;
import com.iracema.ffik.PromotorDashboard;
import com.iracema.ffik.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Utils.ConexaoBD;

public class ComentarioFragmento extends Fragment implements View.OnClickListener, IOnBackPressed{
    Button btn_enviar;
    EditText ed_menssagem;
    LinearLayout lay_voltar;
    JSONObject json;
    HashMap<String, String> user;
    private String jstr, idUtilizador, mensagem, language;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);


        getIds(view);

        lay_voltar.setOnClickListener(this);
        btn_enviar.setOnClickListener(this);

        return view;
    }

    private void getIds(View view) {
        Intent dados = getActivity().getIntent();
        idUtilizador = dados.getStringExtra("id");
        btn_enviar = (Button)view.findViewById(R.id.feedback_btn_submit);

        ed_menssagem =(EditText)view.findViewById(R.id.feedback_edtxt);

        lay_voltar = (LinearLayout)view.findViewById(R.id.feedback_lay_back);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.feedback_btn_submit:
                mensagem = ed_menssagem.getText().toString();
                Comentario();
                break;

            case R.id.feedback_lay_back:
                getFragmentManager().popBackStack();
                break;
        }
    }

    private void Comentario() {
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.AddComentarioURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso = json.getString("sucesso");

                    if (sucesso.equals("1")){
                        //startActivity(new Intent(getActivity(), Promotor_Dashboard.class));
                        Toast.makeText(getActivity(), "Enviado com  sucesso!", Toast.LENGTH_SHORT).show();
                        Intent dados = new Intent(new Intent(getActivity(), ClienteDashboard.class));
                        dados.putExtra("id",idUtilizador );
                        startActivity(dados);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Enviado com  sucesso!", Toast.LENGTH_SHORT).show();
                    Intent dados = new Intent(new Intent(getActivity(), ClienteDashboard.class));
                    dados.putExtra("id",idUtilizador );
                    startActivity(dados);
                    //Toast.makeText(getActivity(), "Envio sem sucesso! " +e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },  new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getActivity(), "Conexão erro! " +error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mensagem",mensagem);
                params.put("idUtilizador", idUtilizador);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);
    }

    @Override
    public boolean onBackPressed() {
        getFragmentManager().popBackStack();
        return false;
    }
}
