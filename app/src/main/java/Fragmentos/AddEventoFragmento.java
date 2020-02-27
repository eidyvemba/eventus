package Fragmentos;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iracema.ffik.IOnBackPressed;
import com.iracema.ffik.PromotorDashboard;
import com.iracema.ffik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import android.support.design.widget.BottomSheetDialog;

import Adaptador.SpinnerAdaptador;
import Utils.ConexaoBD;
import Utils.Evento;
import Utils.MD5;


public class AddEventoFragmento extends Fragment implements View.OnClickListener, IOnBackPressed, DatePickerDialog.OnDateSetListener{
    JSONObject json;
    Calendar myCalendar;
    int ano, mes, dia, idEvento, totalLugar;
    Spinner sp_evento_tipo;
    private Uri mImageUri;
    private Bitmap bitmap;
    boolean start = false;
    HashMap<String, String> user;
    Button btn_cadastrar, btn_cancelar, btn_flyer;
    ImageView evento_imageview;
    private static final int RESULT_GALLERY = 0;
    private static final int CAMERA_REQUEST = 1;
    private static final int PLACE_PICKER_REQUEST = 2;
    private static final String IMAGE_DIRECTORY = "/ffik";
    EditText ed_nome, ed_total_lugares, ed_preco_normal, ed_preco_vip, ed_descricao, ed_localizacao;
    TextView txt_data_inicio, txt_data_fim, txt_tempo_inicio, ed_flyer, txt_tempo_fim;
    private String jstr, latlong, evento_id, imagem, idUtilizador, user_id, selectedFilePath, localizacao, latitude, longitude, titulo, tipo, data_inicio, data_fim, total_lugar, tempo_inicio, tempo_fim, preco_normal, preco_vip, descricao, linguagem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        getIds(view);


        txt_data_inicio.setOnClickListener(this);
        txt_data_fim.setOnClickListener(this);
        txt_tempo_inicio.setOnClickListener(this);
        btn_flyer.setOnClickListener(this);
        ed_flyer.setOnClickListener(this);
        txt_tempo_fim.setOnClickListener(this);
        btn_cadastrar.setOnClickListener(this);

        return view;
    }

    private void getIds(View view) {
        Intent dados = getActivity().getIntent();
        idUtilizador = dados.getStringExtra("id");
        sp_evento_tipo = (Spinner) view.findViewById(R.id.event_spinner_type);

        ed_nome = (EditText) view.findViewById(R.id.event_edxt_name);
        ed_total_lugares = (EditText) view.findViewById(R.id.event_edtxt_total_seats);
        ed_preco_normal = (EditText) view.findViewById(R.id.event_edtxt_normal_price);
        ed_preco_vip = (EditText) view.findViewById(R.id.event_edtxt_vip_price);
        ed_flyer = (TextView) view.findViewById(R.id.event_edtxt_flayer);
        ed_descricao = (EditText) view.findViewById(R.id.event_edtxt_detail);
        ed_localizacao = (EditText) view.findViewById(R.id.event_edxt_location);

        txt_data_inicio = (TextView) view.findViewById(R.id.event_txt_start_date);
        txt_tempo_fim = (TextView) view.findViewById(R.id.event_txt_endtime);
        txt_data_fim = (TextView) view.findViewById(R.id.event_txt_end_date);
        txt_tempo_inicio = (TextView) view.findViewById(R.id.event_txt_time);

        evento_imageview = (ImageView) view.findViewById(R.id.evento_imageview);

        btn_cancelar = (Button) view.findViewById(R.id.event_btn_cancelar);
        btn_cadastrar = (Button) view.findViewById(R.id.event_btn_create);
        btn_flyer = (Button) view.findViewById(R.id.event_btn_add_flayer);

    }

    private void getValues() {
        titulo = ed_nome.getText().toString();
        tipo = sp_evento_tipo.getSelectedItem().toString();
        data_inicio = txt_data_inicio.getText().toString();
        data_fim = txt_data_fim.getText().toString();
        total_lugar = ed_total_lugares.getText().toString();
        tempo_inicio = txt_tempo_inicio.getText().toString();
        tempo_fim = txt_tempo_fim.getText().toString();
        preco_normal = ed_preco_normal.getText().toString();
        preco_vip = ed_preco_vip.getText().toString();
        descricao = ed_descricao.getText().toString();
        localizacao = ed_localizacao.getText().toString();
        imagem = ed_flyer.getText().toString();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.event_txt_start_date:
                start = true;
                myCalendar = Calendar.getInstance();
                ano = myCalendar.get(Calendar.YEAR);
                mes = myCalendar.get(Calendar.MONTH);
                dia = myCalendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, ano, mes, dia);
                dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                dialog.show();
                break;

            case R.id.event_txt_end_date:
                start = false;
                if (data_inicio != null) {
                    SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        Date d = f.parse(data_inicio);
                        long milliseconds = d.getTime();

                        DatePickerDialog dialog1 = new DatePickerDialog(getActivity(), this, ano, mes - 1, dia);
                        dialog1.getDatePicker().setMinDate(milliseconds);
                        dialog1.show();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.select_data_inicio), Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.event_txt_time:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        boolean isPM = (selectedHour >= 12);
                        txt_tempo_inicio.setText(String.format("%02d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, selectedMinute, isPM ? "PM" : "AM"));
                    }
                }, hour, minute, false);
                mTimePicker.setTitle(getResources().getString(R.string.select_tempo));
                mTimePicker.show();
                break;

            case R.id.event_txt_endtime:
                Calendar currentTime = Calendar.getInstance();
                int h = currentTime.get(Calendar.HOUR_OF_DAY);
                int min = currentTime.get(Calendar.MINUTE);
                TimePickerDialog TimePicker;
                TimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        boolean isPM = (selectedHour >= 12);
                        txt_tempo_fim.setText(String.format("%02d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, selectedMinute, isPM ? "PM" : "AM"));
                    }
                }, h, min, false);
                TimePicker.setTitle(getResources().getString(R.string.select_tempo_fim));
                TimePicker.show();
                break;

            case R.id.event_btn_create:
                getValues();
                if (TextUtils.isEmpty(titulo) || TextUtils.isEmpty(tipo) || TextUtils.isEmpty(data_inicio) || TextUtils.isEmpty(data_fim) || TextUtils.isEmpty(total_lugar) || TextUtils.isEmpty(tempo_inicio)
                        || TextUtils.isEmpty(tempo_fim) || TextUtils.isEmpty(preco_normal) || TextUtils.isEmpty(preco_vip) || TextUtils.isEmpty(descricao) || TextUtils.isEmpty(localizacao)) {

                    Toast.makeText(getActivity(), getResources().getString(R.string.preencher), Toast.LENGTH_SHORT).show();
                }else if (txt_tempo_inicio.getText().equals(txt_tempo_fim.getText())){
                    Toast.makeText(getActivity(), "O Horário de início do evento não pode ser o mesmo que o horário de fim!", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(imagem)){
                    Toast.makeText(getActivity(), getResources().getString(R.string.preencherPanfleto), Toast.LENGTH_SHORT).show();
                }else
                    Add_Evento();

                break;

            case R.id.event_btn_add_flayer:
                addImagem();
                break;

            case R.id.event_edtxt_flayer:
                //addImage();
                break;

            //case R.id.event_edxt_location:
               // break;

            case R.id.event_btn_cancelar:
                startActivity(new Intent(getActivity(), PromotorDashboard.class));
                break;
        }
    }

    private void addImagem()
    {
        Intent carregarImagem = new Intent();
        carregarImagem.setType("image/*");
        carregarImagem.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(carregarImagem, "Selecionar  Panfleto"), 1);
    }

    private void Add_Evento()
    {
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.AddEventoURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso = json.getString("sucesso");

                    if (sucesso.equals("1")){
                        //startActivity(new Intent(getActivity(), Promotor_Dashboard.class));
                        Toast.makeText(getActivity(), "Cadastro com  sucesso! ", Toast.LENGTH_SHORT).show();
                        UploadImagem();
                        CarregarCodigos();

                        Intent dados = new Intent(new Intent(getActivity(), PromotorDashboard.class));
                        dados.putExtra("id",idUtilizador );
                        dados.putExtra("tipo_usuario","promotor" );
                        startActivity(dados);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Cadastro sem sucesso! " +e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("idioma", "pt");
                params.put("evento_titulo",titulo);
                params.put("evento_tipo", tipo);
                params.put("inicio_data",data_inicio);
                params.put("fim_data", data_fim);

                params.put("total_lugar", total_lugar);
                params.put("inicio_tempo",tempo_inicio);
                params.put("fim_tempo", tempo_fim);
                params.put("normal_preco",preco_normal);
                params.put("add_flayer", " ");


                params.put("vip_preco", preco_vip);
                params.put("total_earning"," ");
                params.put("usuario_id", idUtilizador);
                params.put("usuario_idfav"," ");
                params.put("latitude", " ");

                params.put("longitutde"," ");
                params.put("descricao", descricao);
                params.put("evento_status","n_aprovado");
                params.put("evento_total_reservado", " ");
                params.put("localizacao", localizacao);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);
    }

    @Override
    public boolean onBackPressed() {
        Intent dados = new Intent(new Intent(getActivity(), PromotorDashboard.class));
        dados.putExtra("id",idUtilizador );
        startActivity(dados);
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int y, int m, int dayOfMonth) {
        if (start == true) {
            txt_data_inicio.setText(Integer.toString(dayOfMonth) + "-" + Integer.toString(m + 1) + "-" + Integer.toString(y));

            ano = y;
            mes = m + 1;
            dia = dayOfMonth;

            data_inicio = txt_data_inicio.getText().toString();

        } else
            txt_data_fim.setText(Integer.toString(dayOfMonth) + "-" + Integer.toString(m + 1) + "-" + Integer.toString(y));

    }

    private void CarregarCodigos()
    {
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.GetLastEventoPromotorURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso,msg;
                    sucesso = json.getString("sucesso");
                    msg = json.getString("msg");
                    JSONArray jsonArray = json.getJSONArray("eventos");

                    if (sucesso.equals("1")){

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Busca sem sucesso! " +e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },  new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){ }
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

    private void UploadImagem()
    {
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.UploadURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso = json.getString("sucesso");

                    if (sucesso.equals("1")){
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    //Toast.makeText(getActivity(), "Cadastro sem sucesso! " +e.toString(), Toast.LENGTH_SHORT).show();
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
                String img = imageToString(bitmap);
                params.put("evento_titulo",titulo);
                params.put("imagem", img);
                params.put("idUtilizador", idUtilizador);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);
    }
    public String getStringImagem(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,byteArrayOutputStream);
        byte[] imagemByteArray = byteArrayOutputStream.toByteArray();

        String encodedImagem = Base64.encodeToString(imagemByteArray, Base64.DEFAULT);
        return encodedImagem;
    }

    private String imageToString (Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodeImg = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodeImg;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data );
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri caminhoPasta = data.getData();
            Bitmap bitmapAux;

            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(caminhoPasta);
                bitmapAux = BitmapFactory.decodeStream(inputStream);

                //bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), caminhoPasta );
                bitmap  = bitmapAux;
                ed_flyer.setText(data.getData().toString());
                evento_imageview.setImageBitmap(bitmap);
                //evento_imageview.se
            }catch (IOException e){ e.printStackTrace();}
        }
    }
}
