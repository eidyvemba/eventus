package Fragmentos;

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
import com.iracema.ffik.ClienteDashboard;
import com.squareup.picasso.Picasso;
import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import android.support.annotation.RequiresApi;

import Utils.ConexaoBD;
import de.hdodenhof.circleimageview.CircleImageView;
import android.support.design.widget.BottomSheetDialog;
public class GestorPerfilFragmento extends Fragment implements View.OnClickListener, IOnBackPressed{
    Uri mImageUri;
    Bitmap bitmap;
    JSONObject json;
    Button btn_editar;
    TextView txt_nome;
    LinearLayout lay_edit;
    FrameLayout frameLayout;
    CircleImageView imageView;
    EditText ed_nome, ed_email;
    HashMap<String, String> user;
    private static final int RESULT_GALLERY = 2;
    private static final int CAMERA_REQUEST = 1 ;
    private static final String IMAGE_DIRECTORY = "/ffik";
    private String selectedFilePath, jstr, user_id, email, nome, profile_image, language, idUtilizador;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestor_profile, container, false);

        getIds(view);

        Perfil();

        frameLayout.setOnClickListener(this);
        lay_edit.setOnClickListener(this);
        btn_editar.setOnClickListener(this);

        return view;
    }

    private void getIds(View view) {
        Intent dados = getActivity().getIntent();
        idUtilizador = dados.getStringExtra("id");

        txt_nome = (TextView)view.findViewById(R.id.profile_txt_name);

        btn_editar = (Button)view.findViewById(R.id.profile_btn_edit);

        lay_edit = (LinearLayout)view.findViewById(R.id.profile_lay_edit);

        frameLayout = (FrameLayout)view.findViewById(R.id.profile_lay_image);

        imageView = (CircleImageView)view.findViewById(R.id.profile_img_user);

        ed_nome = (EditText)view.findViewById(R.id.profile_edtxt_nome);
        ed_email = (EditText)view.findViewById(R.id.profile_edtxt_email);

        setValues();
    }

    private void getValues(){
        email = ed_email.getText().toString();
        nome = ed_nome.getText().toString();
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

                            String nomeUsuario =  resultado.getString("nome");
                            String emailUsuario =  resultado.getString("email");

                            ed_email.setText(emailUsuario);
                            ed_nome.setText(nomeUsuario);
                            txt_nome.setText(nomeUsuario);

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
            case R.id.profile_btn_edit:
                getValues();
                Editar_Perfil();
                break;

            case R.id.profile_lay_image:
                //addImage();
                break;

            case R.id.profile_lay_edit:
                ed_nome.setEnabled(true);
                btn_editar.setVisibility(View.VISIBLE);
                break;

        }
    }

    private void Perfil() {}
    private void Editar_Perfil()
    {
        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.EditaUtilizadorURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso,msg;
                    sucesso = json.getString("sucesso");
                    msg = json.getString("msg");

                    if (sucesso.equals("1")){
                        Intent dados = new Intent(getActivity(), GestorDashboard.class);
                        dados.putExtra("id",idUtilizador );
                        startActivity(dados);
                        Toast.makeText(getActivity(), "Editado com sucesso!  "+msg, Toast.LENGTH_SHORT).show();

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
                params.put("nome",nome);
                params.put("id", idUtilizador);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }

    @Override
    public boolean onBackPressed() {
        Intent dados = new Intent(getActivity(), GestorDashboard.class);
        dados.putExtra("id",idUtilizador );
        startActivity(dados);
        return true;
    }
}
