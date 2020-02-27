package com.iracema.ffik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import Fragmentos.HomeFragmento;
import Fragmentos.MaisFragmento;
import Fragmentos.ClientePerfilFragmento;
import android.support.v4.app.Fragment;
import Fragmentos.ClienteReservasFragmentos;
import Fragmentos.ClienteFavoritosFragmentos;
import Utils.Evento;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

public class ClienteDashboard extends AppCompatActivity implements View.OnClickListener{
    TextView txt_home, txt_favoritos, txt_reservas, txt_mais, txt_perfil;
    ImageView img_home, img_favoritos, img_reservas, img_mais, img_perfil;
    LinearLayout lay_home, lay_favoritos, lay_reservas, lay_mais, lay_perfil;
    private boolean voltar = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_dashboard);

        getIds();

        lay_home.setOnClickListener(this);
        lay_mais.setOnClickListener(this);
        lay_perfil.setOnClickListener(this);
        lay_reservas.setOnClickListener(this);
        lay_favoritos.setOnClickListener(this);

        lay_home.performClick();
    }

    private void getIds() {
        Intent dados = getIntent();
        String idUtilizador = dados.getStringExtra("id");

        txt_home = (TextView)findViewById(R.id.menu_txt_home);
        txt_mais = (TextView)findViewById(R.id.menu_txt_mais);
        txt_perfil = (TextView)findViewById(R.id.menu_txt_perfil);
        txt_reservas = (TextView)findViewById(R.id.menu_txt_reservas);
        txt_favoritos = (TextView)findViewById(R.id.menu_txt_favoritos);

        img_home = (ImageView)findViewById(R.id.menu_img_home);
        img_mais = (ImageView)findViewById(R.id.menu_img_mais);
        img_perfil = (ImageView)findViewById(R.id.menu_img_perfil);
        img_reservas = (ImageView)findViewById(R.id.menu_img_reservas);
        img_favoritos = (ImageView)findViewById(R.id.menu_img_favoritos);

        lay_home = (LinearLayout)findViewById(R.id.menu_lay_home);
        lay_mais= (LinearLayout)findViewById(R.id.menu_lay_mais);
        lay_perfil = (LinearLayout)findViewById(R.id.menu_lay_perfil);
        lay_reservas = (LinearLayout)findViewById(R.id.menu_lay_reservas);
        lay_favoritos = (LinearLayout)findViewById(R.id.menu_lay_favoritos);
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
                setFragment(new HomeFragmento());

                txt_mais.setTextColor(getResources().getColor(R.color.black));
                txt_perfil.setTextColor(getResources().getColor(R.color.black));
                txt_reservas.setTextColor(getResources().getColor(R.color.black));
                txt_favoritos.setTextColor(getResources().getColor(R.color.black));
                txt_home.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                img_mais.setImageDrawable(getResources().getDrawable(R.drawable.more));
                img_perfil.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home_hover));
                img_reservas.setImageDrawable(getResources().getDrawable(R.drawable.my_bookings));
                img_favoritos.setImageDrawable(getResources().getDrawable(R.drawable.favourites));
                break;

            case R.id.menu_lay_favoritos:
                setFragment(new ClienteFavoritosFragmentos());

                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_mais.setTextColor(getResources().getColor(R.color.black));
                txt_perfil.setTextColor(getResources().getColor(R.color.black));
                txt_reservas.setTextColor(getResources().getColor(R.color.black));
                txt_favoritos.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home));
                img_mais.setImageDrawable(getResources().getDrawable(R.drawable.more));
                img_perfil.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_reservas.setImageDrawable(getResources().getDrawable(R.drawable.my_bookings));
                img_favoritos.setImageDrawable(getResources().getDrawable(R.drawable.favourites_hover));
                break;

            case R.id.menu_lay_reservas:
                setFragment(new ClienteReservasFragmentos());

                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_mais.setTextColor(getResources().getColor(R.color.black));
                txt_perfil.setTextColor(getResources().getColor(R.color.black));
                txt_favoritos.setTextColor(getResources().getColor(R.color.black));
                txt_reservas.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home));
                img_mais.setImageDrawable(getResources().getDrawable(R.drawable.more));
                img_perfil.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_favoritos.setImageDrawable(getResources().getDrawable(R.drawable.favourites));
                img_reservas.setImageDrawable(getResources().getDrawable(R.drawable.my_bookings_hover));
                break;

            case R.id.menu_lay_mais:
                setFragment(new MaisFragmento());

                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_perfil.setTextColor(getResources().getColor(R.color.black));
                txt_reservas.setTextColor(getResources().getColor(R.color.black));
                txt_favoritos.setTextColor(getResources().getColor(R.color.black));
                txt_mais.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home));
                img_mais.setImageDrawable(getResources().getDrawable(R.drawable.more_hover));
                img_perfil.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_reservas.setImageDrawable(getResources().getDrawable(R.drawable.my_bookings));
                img_favoritos.setImageDrawable(getResources().getDrawable(R.drawable.favourites));
                break;

            case R.id.menu_lay_perfil:
                setFragment(new ClientePerfilFragmento());

                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_mais.setTextColor(getResources().getColor(R.color.black));
                txt_perfil.setTextColor(getResources().getColor(R.color.black));
                txt_reservas.setTextColor(getResources().getColor(R.color.black));
                txt_favoritos.setTextColor(getResources().getColor(R.color.black));
                txt_perfil.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home));
                img_mais.setImageDrawable(getResources().getDrawable(R.drawable.more));
                img_reservas.setImageDrawable(getResources().getDrawable(R.drawable.my_bookings));
                img_perfil.setImageDrawable(getResources().getDrawable(R.drawable.profile_hover));
                img_favoritos.setImageDrawable(getResources().getDrawable(R.drawable.favourites));
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
