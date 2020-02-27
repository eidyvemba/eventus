package Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iracema.ffik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adaptador.EventosAdaptador;
import Adaptador.GanhosAdaptador;
import Utils.ConexaoBD;
import Utils.Evento;
public class TotalGanhosFragmento extends Fragment{
    JSONObject json;
    RecyclerView recyclerView;
    EventosAdaptador adapter;
    TextView txt_not_avail;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Evento> earnings = new ArrayList<>();
    ArrayList<Evento> eventosLista = new ArrayList<>();
    HashMap<String, String> user;
    private String jstr, idUtilizador, language;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_ganhos, container, false);

        getIds(view);
        Ganhos();

        return view;
    }

    private void getIds(View view) {
        Intent dados = getActivity().getIntent();
        idUtilizador = dados.getStringExtra("id");

        recyclerView = (RecyclerView)view.findViewById(R.id.earnings_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        txt_not_avail = (TextView)view.findViewById(R.id.txt_not_avail);
    }

    private void Ganhos(){
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
                            evento.ganho = resultado.getString("ganho");
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
