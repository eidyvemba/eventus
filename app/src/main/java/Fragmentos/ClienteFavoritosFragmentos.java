package Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iracema.ffik.ClienteDashboard;
import com.iracema.ffik.IOnBackPressed;
import com.iracema.ffik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adaptador.EventosAdaptador;
import Adaptador.EventosResAdaptador;
import Utils.ConexaoBD;
import Utils.Evento;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class ClienteFavoritosFragmentos extends Fragment implements IOnBackPressed, View.OnClickListener {
    JSONObject json;
    EditText ed_search;
    LinearLayout lay_search;
    TextView txt_not_avail;
    RecyclerView recyclerView;
    EventosResAdaptador adapter;
    RecyclerView.LayoutManager layoutManager;
    HashMap<String, String> user = new HashMap();
    ArrayList<Evento> eventosLista = new ArrayList<>();
    private String jstr, search_text, evento_id, idUtilizador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventos_fav_usuarios, container, false);

        getIds(view);
        EventosLista();

        lay_search.setOnClickListener(this);
        return view;
    }

    private void getIds(View view) {
        Intent dados = getActivity().getIntent();
        idUtilizador = dados.getStringExtra("id");
        ed_search = (EditText) view.findViewById(R.id.favourites_edtxt_search);
        lay_search = (LinearLayout) view.findViewById(R.id.favourites_lay_search);
        recyclerView = (RecyclerView) view.findViewById(R.id.favourites_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        txt_not_avail = (TextView)view.findViewById(R.id.txt_not_avail);

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (s!=null){
                    if (s.toString().equals("")){
                        EventosLista();
                    }
                }

            }

        });
    }

    private void setFragment(Fragment fragment, String event_id) {
        Bundle bundle = new Bundle();
        bundle.putString("evento_id", event_id);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.favourites_lay_search:
                search_text = ed_search.getText().toString().trim();
                if (!search_text.equals("")){ PesquisaFav(); }
                EventosLista();
                break;
        }

    }

    private void PesquisaFav() {}
    private void EventosLista()
    {
        eventosLista.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.ListarEventoFavURL, new Response.Listener<String>() {
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
                            //'evento_status'
                            evento.status = "cliente";
                            eventosLista.add(evento);

                        }
                        recyclerView.setVisibility(View.VISIBLE);
                        txt_not_avail.setVisibility(View.GONE);

                        adapter = new EventosResAdaptador(getActivity(), eventosLista);
                        recyclerView.setAdapter(adapter);

                        adapter.setClickListner(new EventosResAdaptador.ClickListnerForRecyclerView() {
                            @Override
                            public Void layout_clicked(View view, int position) {
                                switch (view.getId()){
                                    case R.id.home_lay_des:
                                        evento_id = eventosLista.get(position).id;
                                        setFragment(new EventoDetFragmento(), evento_id);
                                        break;

                                    case R.id.home_btn_cancelar:
                                        evento_id = eventosLista.get(position).id;
                                        CancelarReserva(evento_id);
                                        break;
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
                params.put("idUtilizador", idUtilizador);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }

    private void CancelarReserva(final String evento_id)
    {
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.CancelarFavoritoURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso = json.getString("sucesso");

                    if (sucesso.equals("1")){
                        Toast.makeText(getActivity(), "Reserva Cancelada!", Toast.LENGTH_SHORT).show();
                        Intent dados = new Intent(new Intent(getActivity(), ClienteDashboard.class));
                        dados.putExtra("id",idUtilizador );
                        startActivity(dados);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Envio sem sucesso! " +e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("evento_id",evento_id);
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
        return true;
    }

}
