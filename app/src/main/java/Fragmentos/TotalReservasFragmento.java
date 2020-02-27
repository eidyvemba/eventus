package Fragmentos;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iracema.ffik.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import Adaptador.EventosAdaptador;
import Adaptador.TotalReservasAdaptador;
import Utils.ConexaoBD;
import Utils.Evento;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

public class TotalReservasFragmento extends Fragment {
    JSONObject json;
    Spinner sp_event;
    RecyclerView recyclerView;
    HashMap<String, String> user;
    EventosAdaptador adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView txt_total_booking, txt_event_status, txt_not_avail;
    ArrayList<Evento> reservas = new ArrayList<>();
    ArrayList<Evento> eventos = new ArrayList<>();
    ArrayList<Evento> eventosLista = new ArrayList<>();
    private String jstr, idUtilizador, event_id, language;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_booking, container, false);

        getIds(view);
        Eventos();

        return view;
    }

    private void getIds(View view) {
        Intent dados = getActivity().getIntent();
        idUtilizador = dados.getStringExtra("id");
        txt_not_avail = (TextView)view.findViewById(R.id.txt_not_avail);

        sp_event = (Spinner)view.findViewById(R.id.booking_sp_event_name);

        txt_total_booking = (TextView)view.findViewById(R.id.booking_txt_total);
        txt_event_status = (TextView)view.findViewById(R.id.booking_txt_event_status);

        recyclerView = (RecyclerView)view.findViewById(R.id.bookings_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void Usuario_Eventos(){}

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

                            //evento.id = resultado.getString("evento_id");
                            evento.titulo = resultado.getString("evento_titulo");
                            evento.localizacao = resultado.getString("localizacao");
                            evento.tipo = resultado.getString("evento_tipo");
                            evento.data_inicio = resultado.getString("inicio_data");
                            evento.data_fim = resultado.getString("fim_data");
                            evento.tempo_inicio = resultado.getString("inicio_tempo");
                            evento.tempo_fim = resultado.getString("fim_tempo");
                            evento.preco_normal = resultado.getString("normal_preco");
                            evento.imagem = resultado.getString("add_flayer");
                            evento.lugares = resultado.getString("total_lugar");
                            evento.preco_vip = resultado.getString("vip_preco");
                            evento.descricao = resultado.getString("descricao");
                            evento.total_reservas = (Integer.parseInt(resultado.getString("total_reservas"))-1)+"";
                            //'evento_status'
                            evento.status = "promotor";
                            eventosLista.add(evento);

                        }
                        recyclerView.setVisibility(View.VISIBLE);
                        txt_not_avail.setVisibility(View.GONE);

                        adapter = new EventosAdaptador(getActivity(), eventosLista);
                        recyclerView.setAdapter(adapter);

                        adapter.setClickListner(new EventosAdaptador.ClickListnerForRecyclerView() {
                            @Override
                            public Void layout_clicked(View view, int position) {
                                switch (view.getId()){
                                    /*case R.id.home_lay_des:
                                        evento_id = "23"; //eventosLista.get(position).id;
                                        setFragment(new EventoDetalheFragmento(), evento_id);
                                        break;*/
                                }
                                return null;
                            }
                        });

                    } else {
                        txt_not_avail.setText(getResources().getString(R.string.sem_eventos));
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
                params.put("id", idUtilizador);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }
}
