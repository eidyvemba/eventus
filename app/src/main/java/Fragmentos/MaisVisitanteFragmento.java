package Fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iracema.ffik.ClienteDashboard;
import com.iracema.ffik.IOnBackPressed;
import com.iracema.ffik.Login;
import com.iracema.ffik.R;

public class MaisVisitanteFragmento extends Fragment implements View.OnClickListener, IOnBackPressed{
    LinearLayout lay_account, lay_feedback, lay_change_password, lay_change_language, lay_setting;
    TextView txt_logout, txt_setting;
    int setting_click = 0;
    String id, idUtilizador;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mais_visitante, container, false);


        getIds(view);
        lay_account.setOnClickListener(this);
        lay_feedback.setOnClickListener(this);
        txt_logout.setOnClickListener(this);
        //lay_change_password.setOnClickListener(this);
        //txt_setting.setOnClickListener(this);
        //lay_change_language.setOnClickListener(this);

        return view;
    }

    private void getIds(View view) {
        Intent dados = getActivity().getIntent();
        idUtilizador = "0";
        lay_account = (LinearLayout)view.findViewById(R.id.more_lay_account);
        lay_feedback = (LinearLayout)view.findViewById(R.id.more_lay_feedback);
        //lay_change_password = (LinearLayout)view.findViewById(R.id.more_lay_change_password);
        txt_logout = (TextView) view.findViewById(R.id.more_txt_logout);
       // txt_setting = (TextView) view.findViewById(R.id.more_txt_setting);
       // lay_change_language = (LinearLayout) view.findViewById(R.id.more_lay_change_language);
        //lay_setting = (LinearLayout) view.findViewById(R.id.more_lay_settng);
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

            case R.id.more_lay_feedback:
                setFragment(new ComentarioVisitanteFragmento());
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
        Intent dados = new Intent(new Intent(getActivity(), ClienteDashboard.class));
        dados.putExtra("id",idUtilizador );
        startActivity(dados);
        return true;
    }

}
