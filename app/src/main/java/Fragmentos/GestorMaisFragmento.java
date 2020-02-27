package Fragmentos;

import com.iracema.ffik.CadastroGestor;
import com.iracema.ffik.ClienteDashboard;
import com.iracema.ffik.GestorDashboard;
import com.iracema.ffik.IOnBackPressed;
import com.iracema.ffik.Login;
import com.iracema.ffik.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

public class GestorMaisFragmento extends Fragment implements View.OnClickListener, IOnBackPressed{
    LinearLayout lay_editar, lay_edit_gestor , lay_edit_promotor, lay_edit_usuario, lay_feedback, lay_cad_promotor, lay_cad_usuario, lay_change_password, lay_change_language, lay_setting;
    TextView txt_logout, txt_password, txt_setting, txt_editarUsuarios;
    int setting_click = 0;
    String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gestor_mais, container, false);



        getIds(view);
        txt_logout.setOnClickListener(this);
        lay_change_password.setOnClickListener(this);
        txt_setting.setOnClickListener(this);
        lay_change_language.setOnClickListener(this);
        lay_cad_promotor.setOnClickListener(this);
        lay_cad_usuario.setOnClickListener(this);
        txt_editarUsuarios.setOnClickListener(this);
        lay_edit_gestor.setOnClickListener(this);
        lay_edit_promotor.setOnClickListener(this);
        lay_edit_usuario.setOnClickListener(this);
        txt_password.setOnClickListener(this);
        return view;
    }

    private void getIds(View view) {
        lay_change_password = (LinearLayout)view.findViewById(R.id.more_lay_cadastrar_gestor);
        txt_password = (TextView)view.findViewById(R.id.more_lay_change_password);
        txt_logout = (TextView) view.findViewById(R.id.more_txt_logout);
        txt_setting = (TextView) view.findViewById(R.id.more_txt_setting);
        lay_change_language = (LinearLayout) view.findViewById(R.id.more_lay_editar_usuarios);
        lay_setting = (LinearLayout) view.findViewById(R.id.more_lay_settng);
        lay_cad_promotor = (LinearLayout) view.findViewById(R.id.more_lay_cadastrar_promotor);
        lay_cad_usuario= (LinearLayout) view.findViewById(R.id.more_lay_cadastrar_usuario);


        lay_edit_gestor = (LinearLayout) view.findViewById(R.id.more_lay_editar_gestor);
        lay_edit_promotor = (LinearLayout) view.findViewById(R.id.more_lay_editar_promotor);
        lay_edit_usuario= (LinearLayout) view.findViewById(R.id.more_lay_editar_usuario);


        txt_editarUsuarios = (TextView) view.findViewById(R.id.more_txt_editar);

        lay_editar = (LinearLayout) view.findViewById(R.id.more_lay_editar);


    }

    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.more_txt_logout:
                logout();
                break;

            case R.id.more_lay_change_password:
                setFragment(new MudarPasswordFragmento());
                break;

            //case R.id.more_lay_editar_usuarios:
                //setFragment(new ChangePasswordFragment());
               // break;

            case R.id.more_lay_cadastrar_gestor:
                setFragment(new CadastrarGestorFragmento());
                break;

            case R.id.more_lay_cadastrar_usuario:
                setFragment(new CadastrarUsuarioFragmento());
                break;

            case R.id.more_lay_cadastrar_promotor:
                setFragment(new CadastrarPromotorFragmento());
                break;

                // Editar Eliminar
            case R.id.more_lay_editar_gestor:
                setFragment(new GestorEditGestorFragmentos());
                break;

            case R.id.more_lay_editar_usuario:
                setFragment(new GestorEditUsuarioFragmentos());
                break;

            case R.id.more_lay_editar_promotor:
                setFragment(new GestorEditPromotorFragmentos());
                break;


            case R.id.more_txt_setting:

                if (setting_click == 1){

                    lay_setting.setVisibility(View.VISIBLE);
                    setting_click = 0;

                }else{

                    lay_setting.setVisibility(View.GONE);
                    setting_click = 1;
                }

                break;


            case R.id.more_txt_editar:

                if (setting_click == 1){

                    lay_editar.setVisibility(View.VISIBLE);
                    setting_click = 0;

                }else{

                    lay_editar.setVisibility(View.GONE);
                    setting_click = 1;
                }

                break;

            case R.id.more_lay_change_language:
                break;

        }
    }

    private void logout(){
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.alerta))
                .setMessage(getResources().getString(R.string.deseja_sair))
                .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), Login.class));
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
                }).show();
    }

    @Override
    public boolean onBackPressed() {
        startActivity(new Intent(getActivity(), GestorDashboard.class));
        return true;
    }
}
