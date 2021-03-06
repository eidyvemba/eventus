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
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.iracema.ffik.Cadastro;
import com.iracema.ffik.GestorDashboard;
import com.iracema.ffik.IOnBackPressed;
import com.iracema.ffik.Login;
import com.iracema.ffik.PromotorDashboard;
import com.iracema.ffik.R;
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

import Utils.ConexaoBD;
public class CadastrarGestorFragmento extends Fragment implements View.OnClickListener, IOnBackPressed{
    Button btn_cadastro;
    TextView txt_login;
    EditText ed_username, ed_email, ed_password, ed_confirma_password;
    private String jstr, username, email, password, confirma_password,acesso, idUtilizador;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_gestor, container, false);

        getIds(view);

        btn_cadastro.setOnClickListener(this);

        return view;
    }

    private void getIds(View view){
        Intent dados = getActivity().getIntent();
        idUtilizador = dados.getStringExtra("id");
        btn_cadastro = (Button) view.findViewById(R.id.cadastro_btn_cadastro);

        ed_username = (EditText) view.findViewById(R.id.cadastro_edtxt_username);
        ed_email = (EditText) view.findViewById(R.id.cadastro_edtxt_email);
        ed_password = (EditText) view.findViewById(R.id.cadastro_edtxt_password);
        ed_confirma_password = (EditText) view.findViewById(R.id.cadastro_edtxt_confirma_password);
    }

    private void getValues(){
        username = ed_username.getText().toString();
        email = ed_email.getText().toString();
        password = ed_password.getText().toString();
        confirma_password = ed_confirma_password.getText().toString();

        acesso = getResources().getString(R.string.gestor);

    }

    private boolean isValidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.cadastro_btn_cadastro:
                getValues();

                if (TextUtils.isEmpty(username))
                    Toast.makeText(getContext(), getResources().getString(R.string.enter_username), Toast.LENGTH_SHORT).show();
                else if (!isValidEmail(email))
                    Toast.makeText(getContext(), getResources().getString(R.string.email_nao_valido), Toast.LENGTH_SHORT).show();
                else if (!password.equals(confirma_password))
                    Toast.makeText(getContext(), getResources().getString(R.string.password_nao_confere), Toast.LENGTH_SHORT).show();
                else if (acesso==null)
                    Toast.makeText(getContext(), getResources().getString(R.string.seleciona_tipo_usuario), Toast.LENGTH_SHORT).show();
                else
                    Cadastrar();

                break;

        }
    }

    private void Cadastrar() {

        StringRequest strReq = new StringRequest(Request.Method.POST, ConexaoBD.CadastroURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String sucesso = json.getString("sucesso");

                    if (sucesso.equals("1")){
                        Intent dados = new Intent(new Intent(getActivity(), GestorDashboard.class));
                        dados.putExtra("id",idUtilizador );
                        startActivity(dados);
                        Toast.makeText(getContext(), "Cadastro com  sucesso!", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Cadastro sem sucesso! " +e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },  new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getContext(), "Conexão erro! " +error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nome", username);
                params.put("username",username);
                params.put("email", email);
                params.put("password",password);
                params.put("acesso", acesso);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);

    }

    @Override
    public boolean onBackPressed() {
        Intent dados = new Intent(new Intent(getActivity(), GestorDashboard.class));
        dados.putExtra("id",idUtilizador );
        startActivity(dados);
        return true;
    }


}
