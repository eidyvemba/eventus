package Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.iracema.ffik.ClienteDashboard;
import com.iracema.ffik.IOnBackPressed;
import com.iracema.ffik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Utils.ConexaoBD;
import Utils.Evento;
import Utils.GetImageURL;


public class EventoDetFragmento extends Fragment implements View.OnClickListener, IOnBackPressed {
    JSONObject json;
    Button btn_reservar;
    int ticket = 1, amount = 0;
    ImageView img_share, imageView;
    RadioButton rb_normal, rb_vip;
    HashMap<String, String> user = new HashMap<>();
    public static final int PAYPAL_REQUEST_CODE = 123;
    LinearLayout lay_back, lay_share, lay_minus, lay_plus, lay_location;
    TextView txt_show_name, txt_seats, txt_location, txt_title, txt_description, txt_price, txt_no_of_tickets, txt_vip_price;
    private String jstr, idUtilizador, evento_id, show_name, seats, location, description, price, vip_price, image, url = "", paymentAmount, latitude, longitude, ticket_type = "", language;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_evento_det, container, false);

        //
        if (getArguments()!=null)
        {

            evento_id = getArguments().getString("evento_id").toString();
            //Toast.makeText(getActivity(), evento_id, Toast.LENGTH_SHORT).show();
        }

        getIds(view);
        Detalhe();


        lay_back.setOnClickListener(this);
        btn_reservar.setOnClickListener(this);
        lay_share.setOnClickListener(this);
        lay_location.setOnClickListener(this);
        lay_minus.setOnClickListener(this);
        lay_plus.setOnClickListener(this);
        rb_normal.setOnClickListener(this);
        rb_vip.setOnClickListener(this);

        return view;
    }

    private void getIds(View view) {

        Intent dados = getActivity().getIntent();
        idUtilizador = dados.getStringExtra("id");

        btn_reservar = (Button) view.findViewById(R.id.btn_reservar);

        rb_normal = (RadioButton)view.findViewById(R.id.detail_btn_normal);
        rb_vip = (RadioButton)view.findViewById(R.id.detail_btn_vip);

        imageView = (ImageView) view.findViewById(R.id.detail_imageview);
        img_share = (ImageView) view.findViewById(R.id.detail_img_share);

        lay_back = (LinearLayout) view.findViewById(R.id.detail_toolbar_back);
        lay_share = (LinearLayout) view.findViewById(R.id.detail_lay_share);
        lay_location = (LinearLayout) view.findViewById(R.id.detail_lay_loc);
        lay_minus = (LinearLayout)view.findViewById(R.id.detail_lay_minus);
        lay_plus = (LinearLayout)view.findViewById(R.id.detail_lay_plus);

        txt_title = (TextView) view.findViewById(R.id.detail_txt_title);
        txt_price = (TextView) view.findViewById(R.id.detail_txt_price);
        txt_vip_price = (TextView)view.findViewById(R.id.detail_txt_vipprice);
        txt_no_of_tickets = (TextView)view.findViewById(R.id.detail_txt_no_of_tickets);
        txt_location = (TextView) view.findViewById(R.id.detail_txt_location);
        txt_show_name = (TextView) view.findViewById(R.id.detail_txt_show_name);
        txt_seats = (TextView) view.findViewById(R.id.detail_txt_seats_available);
        txt_description = (TextView) view.findViewById(R.id.detail_txt_description);


    }

    private void Detalhe()
    {
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.PesquisarEventoURL, new Response.Listener<String>() {
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
                            evento.favourito = resultado.getString("favourito");

                            evento.reserva = resultado.getString("reserva");
                            evento.status = "cliente";
                            //'evento_status'

                            if (!evento.imagem.equals("") && !evento.imagem.equals(" ")) new GetImageURL(imageView).execute(ConexaoBD.Host+"/webservice/"+evento.imagem);

                            txt_description.setText("Detalhe: "+ evento.descricao);
                            txt_location.setText(getResources().getString(R.string.localizaçao)+" : " +evento.localizacao);

                                //txt_seats.setText(getResources().getString(R.string.seat_available) +evento.lugares);

                            txt_show_name.setText("Titulo: " + evento.titulo);
                            txt_title.setText("Titulo: " + evento.titulo);

                            txt_price.setText(getResources().getString(R.string.preco_normal) + " :" +evento.preco_normal + " Kz");
                            txt_vip_price.setText(getResources().getString(R.string.preco_vip)+": " +evento.preco_vip+" Kz");

                        }


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
                params.put("evento_id", evento_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }

    private void ReservarEvento()
    {
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.ReservarEventoURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso, codigo, msg;
                    sucesso = json.getString("sucesso");
                    codigo = json.getString("codigo");
                    msg = json.getString("msg");

                    if (sucesso.equals("1")){
                        Toast.makeText(getActivity(), "Reserva Marcada! \n Codigo: "+codigo, Toast.LENGTH_LONG).show();
                        Intent dados = new Intent(new Intent(getActivity(), ClienteDashboard.class));
                        dados.putExtra("id",idUtilizador );
                        dados.putExtra("tipo_usuario","usuario" );
                        startActivity(dados);

                    } else { Toast.makeText(getActivity(), "Erro: "+msg, Toast.LENGTH_SHORT).show(); }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Busca sem sucesso! " +e.toString(), Toast.LENGTH_LONG).show();
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
                params.put("evento_id", evento_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) { /*
            case R.id.detail_btn_book_now:
                if (ticket_type.equals(""))
                    Toast.makeText(getActivity(), getResources().getString(R.string.ticket_type), Toast.LENGTH_SHORT).show();
                else
                    // getPayment();

                    break; */
            case R.id.btn_reservar:
                getFragmentManager().popBackStack();
                break;


            case R.id.detail_toolbar_back:
                getFragmentManager().popBackStack();
                break;

            /* case R.id.detail_lay_share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Event");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey Checkout " +show_name + " at FFIK app. " +"\n\n https://play.google.com/store/apps/details?id=com.google.android.apps.plus&hl=en");
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
                break;

            case R.id.detail_lay_loc:

                Fragment fragment = new ViewMap();
                Bundle bundle = new Bundle();
                bundle.putString("event_name", show_name);
                bundle.putString("lat", latitude);
                bundle.putString("lng", longitude);
                bundle.putString("location", location);
                fragment.setArguments(bundle);
                setFragment(fragment);
                break;

            case R.id.detail_lay_plus:
                if (!ticket_type.equals("")) {
                    if (ticket >= 1) {
                        ticket = Integer.parseInt(txt_no_of_tickets.getText().toString());
                        ticket = ticket + 1;
                        txt_no_of_tickets.setText(String.valueOf(ticket));

                        if (ticket_type.equals(getResources().getString(R.string.normal))) {
                            amount = Integer.parseInt(price);
                            amount = ticket * amount;
                            txt_price.setText(getResources().getString(R.string.normal_dollar) +String.valueOf(amount));
                            paymentAmount = String.valueOf(amount);
                        } else if (ticket_type.equals(getResources().getString(R.string.vip))) {
                            amount = Integer.parseInt(vip_price);
                            amount = ticket * amount;
                            txt_vip_price.setText(getResources().getString(R.string.vip_dollar) +String.valueOf(amount));
                            paymentAmount = String.valueOf(amount);

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.ticket_type), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else
                    Toast.makeText(getActivity(), getResources().getString(R.string.ticket_type), Toast.LENGTH_SHORT).show();

                break;

            case R.id.detail_lay_minus:
                if (!ticket_type.equals("")) {
                    if (ticket >= 2) {
                        ticket = Integer.parseInt(txt_no_of_tickets.getText().toString());
                        ticket = ticket - 1;
                        txt_no_of_tickets.setText(String.valueOf(ticket));

                        if (ticket_type.equals(getResources().getString(R.string.normal))) {
                            amount = Integer.parseInt(price);
                            amount = ticket * amount;
                            txt_price.setText(getResources().getString(R.string.normal_price) +String.valueOf(amount));
                            paymentAmount = String.valueOf(amount);

                        } else if (ticket_type.equals(getResources().getString(R.string.vip))) {
                            amount = Integer.parseInt(vip_price);
                            amount = ticket * amount;
                            txt_vip_price.setText(getResources().getString(R.string.vip_price) +String.valueOf(amount));
                            paymentAmount = String.valueOf(amount);

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.ticket_type), Toast.LENGTH_SHORT).show();
                        }

                    }
                }else
                    Toast.makeText(getActivity(), getResources().getString(R.string.ticket_type), Toast.LENGTH_SHORT).show();

                break;

            case R.id.detail_btn_normal:
                ticket_type = getResources().getString(R.string.normal);
                txt_no_of_tickets.setText("1");
                txt_vip_price.setText(getResources().getString(R.string.vip_price) +vip_price);
                txt_price.setText(getResources().getString(R.string.normal_price) +price);
                paymentAmount = price;
                break;

            case R.id.detail_btn_vip:
                ticket_type = getResources().getString(R.string.vip);
                txt_no_of_tickets.setText("1");
                txt_vip_price.setText(getResources().getString(R.string.vip_price) +vip_price);
                txt_price.setText(getResources().getString(R.string.normal_price) +price);
                paymentAmount = vip_price;
                break;  */

        }
    }

    private void Reservar() {

    }

    private void Evento_Detalhe() {
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.PesquisarEventoURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso,msg;
                    sucesso = json.getString("sucesso");
                    msg = json.getString("msg");
                    JSONArray jsonArray = json.getJSONArray("eventos");

                    if (sucesso.equals("1")){
                        Evento evento = new Evento();
                        for (int i = 0;  i < jsonArray.length(); i++){
                            JSONObject resultado = jsonArray.getJSONObject(i);

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

                        }

                        if (!evento.imagem.equals("") && !evento.imagem.equals(" ")) new GetImageURL(imageView).execute(ConexaoBD.Host+"/webservice/"+evento.imagem);
                        /*txt_description.setText(evento.descricao);
                        txt_price.setText(getResources().getString(R.string.preco_normal) + ":" +price + " Kz");
                        txt_location.setText(getResources().getString(R.string.localizaçao) +evento.localizacao);*/

                        //txt_seats.setText(getResources().getString(R.string.seat_available) +evento.lugares);

                        /*txt_show_name.setText(evento.titulo);
                        txt_title.setText(evento.titulo);
                        txt_vip_price.setText(getResources().getString(R.string.preco_vip) +evento.preco_vip+" Kz");*/

                        /*if (seats.equals("0")){
                            btn_book_now.setBackground(getResources().getDrawable(R.drawable.btn_bg_red));
                            btn_book_now.setText(getResources().getString(R.string.already_booked));
                            btn_book_now.setEnabled(false);
                            lay_plus.setEnabled(false);
                            lay_minus.setEnabled(false);
                        }*/




                    } else { Toast.makeText(getActivity(), "Busca sem sucesso! ", Toast.LENGTH_SHORT).show();}
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Busca sem sucesso!" +e.toString(), Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);

    }

    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onBackPressed() {
        getFragmentManager().popBackStack();
        return false;
    }
}
