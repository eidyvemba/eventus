package Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adaptador.EventosAdaptador;
import Adaptador.SpinnerAdaptador;
import Utils.ConexaoBD;
import Utils.Evento;

public class CadastrarCodigoPromotorFragmento extends Fragment implements IOnBackPressed, View.OnClickListener{
    Button btn_salvar;
    EditText ed_codigo;
    Spinner sp_event;
    JSONObject json;
    ArrayList<Evento> eventosLista = new ArrayList<>();
    private String jstr, codigo, evento_id, idUtilizador, tipoU;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_cod, container, false);

        getIds(view);
        Eventos();

        btn_salvar.setOnClickListener(this);

        return view;
    }

    private void getIds(View view) {
        Intent dados = getActivity().getIntent();
        idUtilizador = dados.getStringExtra("id");
        tipoU = dados.getStringExtra("tipo_usuario");

        sp_event = (Spinner)view.findViewById(R.id.sp_eventos);
        btn_salvar = (Button)view.findViewById(R.id.btn_salvar);

        ed_codigo= (EditText)view.findViewById(R.id.edtxt_codigo);
       // setValues();

    }

    private void getValues(){
        codigo = ed_codigo.getText().toString();

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_salvar :
                getValues();

                if (TextUtils.isEmpty(codigo))
                    Toast.makeText(getActivity(), getResources().getString(R.string.preencher), Toast.LENGTH_SHORT).show();
                else
                    AddCodigo();

                break;

        }
    }

    private void AddCodigo()
    {
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.AddCadURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso,msg;
                    sucesso = json.getString("sucesso");
                    msg = json.getString("msg");

                    if (sucesso.equals("1")){
                        /*Intent dados;
                        dados = new Intent(getActivity(), PromotorDashboard.class);
                        dados.putExtra("tipo_usuario","promotor" );
                        dados.putExtra("id",idUtilizador );
                        startActivity(dados);*/
                        Toast.makeText(getActivity(), "Codigo Adicionado sucesso!  "+msg, Toast.LENGTH_SHORT).show();
                        ed_codigo.setText("");

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
                params.put("evento_id",evento_id);
                params.put("codigo",codigo);
                params.put("idUtilizador",idUtilizador);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }


    private void Eventos(){
        eventosLista.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.ListarEventoPromotorURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso,msg;
                    sucesso = json.getString("sucesso");
                    msg = json.getString("msg");
                    JSONArray jsonArray = json.getJSONArray("eventos");

                    if (sucesso.equals("1")){
                        for (int i = 0;  i < jsonArray.length(); i++){
                            JSONObject resultado = jsonArray.getJSONObject(i);

                            Evento evento = new Evento();

                            evento.id = resultado.getString("evento_id");
                            evento.titulo = resultado.getString("evento_titulo");
                            //'evento_status'
                            //evento.status = "promotor";
                            eventosLista.add(evento);

                        }

                        SpinnerAdaptador adapter = new SpinnerAdaptador(getActivity(), R.layout.inflator_for_spinner, R.id.title, eventosLista);
                        sp_event.setAdapter(adapter);

                        sp_event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                evento_id = eventosLista.get(position).id;


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


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
    public boolean onBackPressed() {
        getFragmentManager().popBackStack();
        return true;
    }

}
