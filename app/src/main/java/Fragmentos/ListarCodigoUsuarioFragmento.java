package Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iracema.ffik.IOnBackPressed;
import com.iracema.ffik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adaptador.CodigosAdaptador;
import Adaptador.EventosAdaptador;
import Adaptador.SpinnerAdaptador;
import Utils.Codigo;
import Utils.ConexaoBD;
import Utils.Evento;

public class ListarCodigoUsuarioFragmento extends Fragment implements IOnBackPressed, View.OnClickListener{
    Button btn_salvar;
    EditText ed_codigo;
    TextView txt_not_avail;
    Spinner sp_event;
    JSONObject json;
    ArrayList<Evento> eventosLista = new ArrayList<>();
    ArrayList<Codigo> codigosLista  = new ArrayList<>();


    RecyclerView recyclerView;
    EventosAdaptador adapter;
    CodigosAdaptador adapterCodigos;
    RecyclerView.LayoutManager layoutManager;

    private String jstr, codigo, evento_id, idUtilizador, tipoU;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_cod, container, false);

        getIds(view);
        Eventos();




        return view;
    }

    private void getIds(View view) {
        Intent dados = getActivity().getIntent();
        idUtilizador = dados.getStringExtra("id");
        tipoU = dados.getStringExtra("tipo_usuario");

        sp_event = (Spinner)view.findViewById(R.id.sp_eventos);
       // setValues();

        recyclerView = (RecyclerView) view.findViewById(R.id.favourites_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        txt_not_avail = (TextView)view.findViewById(R.id.txt_not_avail);

    }

    private void getValues(){
        codigo = ed_codigo.getText().toString();

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }


    private void Eventos(){
        eventosLista.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.ListarEventoResClienteURL, new Response.Listener<String>() {
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
                                CodigosLista();
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

    private void CodigosLista()
    {
        codigosLista.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.CodigoEventoClienteURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso,msg;
                    sucesso = json.getString("sucesso");
                    msg = json.getString("msg");
                    JSONArray jsonArray = json.getJSONArray("codigos");

                    if (sucesso.equals("1")){
                        for (int i = 0;  i < jsonArray.length(); i++){
                            JSONObject resultado = jsonArray.getJSONObject(i);

                            Codigo codigo = new Codigo();

                            codigo.id = resultado.getString("id");
                            codigo.codigo = resultado.getString("codigo");
                            codigo.status = ""; //resultado.getString("status");
                            codigo.cliente_id = ""; //resultado.getString("cliente_id");

                            codigosLista.add(codigo);

                        }
                        recyclerView.setVisibility(View.VISIBLE);
                        txt_not_avail.setVisibility(View.GONE);


                        adapterCodigos = new CodigosAdaptador(getActivity(), codigosLista);
                        recyclerView.setAdapter(adapterCodigos);

                        adapterCodigos.setClickListner(new CodigosAdaptador.ClickListnerForRecyclerView() {
                            @Override
                            public Void layout_clicked(View view, int position) {
                                switch (view.getId()){
                                    case R.id.home_btn_banir:
                                        //user_id = promotoresLista.get(position).id;
                                        break;
                                }
                                return null;
                            }
                        });

                    } else {
                        txt_not_avail.setText(getResources().getString(R.string.sem_codigos));
                        recyclerView.setVisibility(View.GONE);
                        txt_not_avail.setVisibility(View.VISIBLE);
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
                params.put("evento_id", evento_id);
                params.put("idUtilizador", idUtilizador);

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
