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
import com.iracema.ffik.GestorDashboard;
import com.iracema.ffik.IOnBackPressed;
import com.iracema.ffik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adaptador.EventosAdaptador;
import Adaptador.PromotoresAdaptador;
import Utils.ConexaoBD;
import Utils.Evento;
import Utils.Usuario;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class GestorPromotoresFragmentos extends Fragment implements IOnBackPressed, View.OnClickListener{
    JSONObject json;
    EditText ed_search;
    LinearLayout lay_search;
    TextView txt_not_avail;
    RecyclerView recyclerView;
    EventosAdaptador adapter;
    PromotoresAdaptador adapterPromotor;
    RecyclerView.LayoutManager layoutManager;
    HashMap<String, String> user = new HashMap();
    ArrayList<Usuario> promotoresLista  = new ArrayList<>();
    private String jstr, user_id, idUtilizador, event_id, language, search_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestor_promotores, container, false);

        getIds(view);
        PromotoresLista();

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
                        PromotoresLista();
                    }
                }

            }

        });
    }

    private void setFragment(Fragment fragment, String event_id) {
        Bundle bundle = new Bundle();
        bundle.putString("event_id", event_id);
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
                PromotoresLista();
                break;
        }

    }

    private void PesquisaFav() {}
    private void PromotoresLista()
    {
        promotoresLista.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.ListarPromotoresURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso,msg;
                    sucesso = json.getString("sucesso");
                    msg = json.getString("msg");
                    JSONArray jsonArray = json.getJSONArray("usuarios");

                    if (sucesso.equals("1")){
                        for (int i = 0;  i < jsonArray.length(); i++){
                            JSONObject resultado = jsonArray.getJSONObject(i);

                            Usuario usuario = new Usuario();

                            usuario.id = resultado.getString("id");
                            usuario.username = resultado.getString("username");
                            usuario.nome = resultado.getString("nome");
                            usuario.email = resultado.getString("email");
                            usuario.acesso = resultado.getString("acesso");

                            promotoresLista.add(usuario);

                        }
                        recyclerView.setVisibility(View.VISIBLE);
                        txt_not_avail.setVisibility(View.GONE);


                        adapterPromotor = new PromotoresAdaptador(getActivity(), promotoresLista);
                        recyclerView.setAdapter(adapterPromotor);

                        adapterPromotor.setClickListner(new PromotoresAdaptador.ClickListnerForRecyclerView() {
                            @Override
                            public Void layout_clicked(View view, int position) {
                                switch (view.getId()){
                                    case R.id.home_btn_banir:
                                        user_id = promotoresLista.get(position).id;
                                        Banir(user_id);
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
                params.put("id", "");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }

    private void Banir(final String user_id)
    {
        this.user_id = user_id;
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.BanirPromotorURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso,msg;
                    sucesso = json.getString("sucesso");
                    msg = json.getString("msg");

                    if (sucesso.equals("1")){
                        Toast.makeText(getActivity(), "Promotor Banido! ", Toast.LENGTH_SHORT).show();
                        Intent dados = new Intent(getActivity(), GestorDashboard.class);
                        dados.putExtra("id",idUtilizador );
                        startActivity(dados);

                    } else { }
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
                params.put("user_id", user_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }

    @Override
    public boolean onBackPressed() {
        startActivity(new Intent(getActivity(), GestorDashboard.class));
        return true;
    }
}
