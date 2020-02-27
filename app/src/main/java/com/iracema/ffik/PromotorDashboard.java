package com.iracema.ffik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import Adaptador.ViewPagerAdaptador;
import Fragmentos.PromMaisFragmento;
import android.widget.FrameLayout;
import Fragmentos.AddEventoFragmento;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import Fragmentos.PromoPerfilFragmento;
import Fragmentos.TotalEventosFragmento;/*
import Fragments.Org_ProfileFragment;*/
import Fragmentos.TotalReservasFragmento;
import Fragmentos.TotalGanhosFragmento;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

public class PromotorDashboard extends AppCompatActivity implements View.OnClickListener{
    FrameLayout lay_frame;
    private boolean back = false;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView txt_home, txt_add_event, txt_more, txt_profile;
    ImageView img_home, img_add_event, img_more, img_profile;
    LinearLayout lay_home, lay_add_event, lay_more, lay_profile, lay_dash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotor_dashboard);

        getIds();

        lay_home.setOnClickListener(this);
        lay_more.setOnClickListener(this);
        lay_profile.setOnClickListener(this);
        lay_add_event.setOnClickListener(this);
    }

    private void getIds() {
        Intent dados = getIntent();
        String idUtilizador = dados.getStringExtra("id");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        txt_home = (TextView)findViewById(R.id.menu_txt_home);
        txt_more = (TextView)findViewById(R.id.menu_txt_more);
        txt_profile = (TextView)findViewById(R.id.menu_txt_profile);
        txt_add_event = (TextView)findViewById(R.id.menu_txt_add_event);

        img_home = (ImageView)findViewById(R.id.menu_img_home);
        img_more = (ImageView)findViewById(R.id.menu_img_more);
        img_profile = (ImageView)findViewById(R.id.menu_img_profile);
        img_add_event = (ImageView)findViewById(R.id.menu_img_add_event);

        lay_home = (LinearLayout)findViewById(R.id.menu_lay_home);
        lay_more = (LinearLayout)findViewById(R.id.menu_lay_more);
        lay_profile = (LinearLayout)findViewById(R.id.menu_lay_profile);
        lay_add_event = (LinearLayout)findViewById(R.id.menu_lay_add_event);
        lay_dash = (LinearLayout)findViewById(R.id.lay_dash);

        lay_frame = (FrameLayout) findViewById(R.id.content_frame);
    }

    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText(getResources().getString(R.string.total_reservas));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.total_bookings, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(getResources().getString(R.string.total_eventos));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.total_event, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText(getResources().getString(R.string.total_ganho));
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.total_earnings, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdaptador adapter = new ViewPagerAdaptador(getSupportFragmentManager());
        adapter.addFrag(new TotalReservasFragmento(), getResources().getString(R.string.total_reservas));
        adapter.addFrag(new TotalEventosFragmento(), getResources().getString(R.string.total_eventos));
        adapter.addFrag(new TotalGanhosFragmento(), getResources().getString(R.string.total_ganho));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_lay_home:
                lay_dash.setVisibility(View.VISIBLE);
                lay_frame.setVisibility(View.GONE);

                txt_more.setTextColor(getResources().getColor(R.color.black));
                txt_profile.setTextColor(getResources().getColor(R.color.black));
                txt_add_event.setTextColor(getResources().getColor(R.color.black));
                txt_home.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                img_more.setImageDrawable(getResources().getDrawable(R.drawable.more));
                img_profile.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home_hover));
                img_add_event.setImageDrawable(getResources().getDrawable(R.drawable.add_event));
                break;

            case R.id.menu_lay_add_event:
                lay_dash.setVisibility(View.GONE);
                lay_frame.setVisibility(View.VISIBLE);

                setFragment(new AddEventoFragmento());

                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_more.setTextColor(getResources().getColor(R.color.black));
                txt_profile.setTextColor(getResources().getColor(R.color.black));
                txt_add_event.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home));
                img_more.setImageDrawable(getResources().getDrawable(R.drawable.more));
                img_profile.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_add_event.setImageDrawable(getResources().getDrawable(R.drawable.add_event_hover));
                break;

            case R.id.menu_lay_more:
                lay_dash.setVisibility(View.GONE);
                lay_frame.setVisibility(View.VISIBLE);

                setFragment(new PromMaisFragmento());

                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_profile.setTextColor(getResources().getColor(R.color.black));
                txt_add_event.setTextColor(getResources().getColor(R.color.black));
                txt_more.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home));
                img_more.setImageDrawable(getResources().getDrawable(R.drawable.more_hover));
                img_profile.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                img_add_event.setImageDrawable(getResources().getDrawable(R.drawable.add_event));
                break;

            case R.id.menu_lay_profile:
                lay_dash.setVisibility(View.GONE);
                lay_frame.setVisibility(View.VISIBLE);

                setFragment(new PromoPerfilFragmento());

                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_more.setTextColor(getResources().getColor(R.color.black));
                txt_profile.setTextColor(getResources().getColor(R.color.black));
                txt_add_event.setTextColor(getResources().getColor(R.color.black));
                txt_profile.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home));
                img_more.setImageDrawable(getResources().getDrawable(R.drawable.more));
                img_profile.setImageDrawable(getResources().getDrawable(R.drawable.profile_hover));
                img_add_event.setImageDrawable(getResources().getDrawable(R.drawable.add_event));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (lay_frame.getVisibility()== View.GONE){
            if (back == false){
                back = true;
                Toast.makeText(PromotorDashboard.this, getResources().getString(R.string.deseja_sair), Toast.LENGTH_SHORT).show();
            }else {
                moveTaskToBack(true);
                back = false;
            }
        }else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) { }

        }

    }
}
