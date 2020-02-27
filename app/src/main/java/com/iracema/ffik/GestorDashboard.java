package com.iracema.ffik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import Fragmentos.GestorValidaEventosFragmentos;
import Fragmentos.GestorMaisFragmento;
import Fragmentos.GestorPerfilFragmento;
import Fragmentos.GestorPromotoresFragmentos;
import Fragmentos.HomeFragmento;
import Fragmentos.HomeGestorFragmento;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;

public class GestorDashboard extends AppCompatActivity implements View.OnClickListener{
    TextView txt_home, txt_validar, txt_promotores, txt_mais, txt_perfil;
    ImageView img_home, img_validar, img_promotores, img_mais, img_perfil;
    LinearLayout lay_home, lay_validar, lay_promotores, lay_mais, lay_perfil;
    private boolean voltar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestor_dashboard);

        getIds();

        lay_home.setOnClickListener(this);
        lay_mais.setOnClickListener(this);
        lay_perfil.setOnClickListener(this);
        lay_promotores.setOnClickListener(this);
        lay_validar.setOnClickListener(this);

        lay_home.performClick();
    }

    private void getIds() {
        Intent dados = getIntent();
        String idUtilizador = dados.getStringExtra("id");

        txt_home = (TextView)findViewById(R.id.menu_txt_home);
        txt_mais = (TextView)findViewById(R.id.menu_txt_mais);
        txt_perfil = (TextView)findViewById(R.id.menu_txt_perfil);
        txt_promotores = (TextView)findViewById(R.id.menu_txt_promotor);
        txt_validar = (TextView)findViewById(R.id.menu_txt_validar);

        img_home = (ImageView)findViewById(R.id.menu_img_home);
        img_mais = (ImageView)findViewById(R.id.menu_img_mais);
        img_perfil = (ImageView)findViewById(R.id.menu_img_perfil);
        img_promotores= (ImageView)findViewById(R.id.menu_img_promotor);
        img_validar = (ImageView)findViewById(R.id.menu_img_validar);

        lay_home = (LinearLayout)findViewById(R.id.menu_lay_home);
        lay_mais= (LinearLayout)findViewById(R.id.menu_lay_mais);
        lay_perfil = (LinearLayout)findViewById(R.id.menu_lay_perfil);
        lay_promotores = (LinearLayout)findViewById(R.id.menu_lay_promotor);
        lay_validar = (LinearLayout)findViewById(R.id.menu_lay_validar);
    }

    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_lay_home:
                setFragment(new HomeGestorFragmento());

                txt_mais.setTextColor(getResources().getColor(R.color.black));
                txt_perfil.setTextColor(getResources().getColor(R.color.black));
                txt_promotores.setTextColor(getResources().getColor(R.color.black));
                txt_validar.setTextColor(getResources().getColor(R.color.black));
                txt_home.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                img_mais.setImageDrawable(getResources().getDrawable(R.drawable.more));
                img_perfil.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home_hover));
                img_promotores.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_validar.setImageDrawable(getResources().getDrawable(R.drawable.add_event));
                break;

            case R.id.menu_lay_validar:
                setFragment(new GestorValidaEventosFragmentos());

                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_mais.setTextColor(getResources().getColor(R.color.black));
                txt_perfil.setTextColor(getResources().getColor(R.color.black));
                txt_promotores.setTextColor(getResources().getColor(R.color.black));
                txt_validar.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home));
                img_mais.setImageDrawable(getResources().getDrawable(R.drawable.more));
                img_perfil.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_promotores.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_validar.setImageDrawable(getResources().getDrawable(R.drawable.add_event_hover));
                break;

            case R.id.menu_lay_promotor:
                setFragment(new GestorPromotoresFragmentos());

                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_mais.setTextColor(getResources().getColor(R.color.black));
                txt_perfil.setTextColor(getResources().getColor(R.color.black));
                txt_validar.setTextColor(getResources().getColor(R.color.black));
                txt_promotores.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home));
                img_mais.setImageDrawable(getResources().getDrawable(R.drawable.more));
                img_perfil.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_validar.setImageDrawable(getResources().getDrawable(R.drawable.add_event));
                img_promotores.setImageDrawable(getResources().getDrawable(R.drawable.profile_hover));
                break;

            case R.id.menu_lay_mais:
                setFragment(new GestorMaisFragmento());

                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_perfil.setTextColor(getResources().getColor(R.color.black));
                txt_promotores.setTextColor(getResources().getColor(R.color.black));
                txt_validar.setTextColor(getResources().getColor(R.color.black));
                txt_mais.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home));
                img_mais.setImageDrawable(getResources().getDrawable(R.drawable.more_hover));
                img_perfil.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_promotores.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_validar.setImageDrawable(getResources().getDrawable(R.drawable.add_event));
                break;

            case R.id.menu_lay_perfil:
                setFragment(new GestorPerfilFragmento());

                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_mais.setTextColor(getResources().getColor(R.color.black));
                txt_perfil.setTextColor(getResources().getColor(R.color.black));
                txt_promotores.setTextColor(getResources().getColor(R.color.black));
                txt_validar.setTextColor(getResources().getColor(R.color.black));
                txt_perfil.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home));
                img_mais.setImageDrawable(getResources().getDrawable(R.drawable.more));
                img_promotores.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_perfil.setImageDrawable(getResources().getDrawable(R.drawable.profile_hover));
                img_validar.setImageDrawable(getResources().getDrawable(R.drawable.add_event));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) { }
        else { }
    }

}
