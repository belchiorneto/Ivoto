package com.lojapro.can;

import android.app.SearchManager;
import android.os.Bundle;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lojapro.candidato.Candidato;
import com.lojapro.alertas.RegistraClique;

import analytcs.AnalyticsApplication;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

@SuppressWarnings("deprecation")
public class Eleitor extends AppCompatActivity implements
        ActionBar.TabListener {
    private Tracker mTracker;
    ViewPager Tab;
    TabPageAdapter TabAdapter;
    ActionBar actionBar;
    SharedPreferences sharedPref;
    Context ctx;
    private GoogleApiClient client;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eleitor);
        sharedPref = this.getSharedPreferences(
                "com.lojapro.can", Context.MODE_PRIVATE);

        TabAdapter = new TabPageAdapter(getSupportFragmentManager());

        ctx = this.getApplicationContext();
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = new AnalyticsApplication(ctx);
        mTracker = application.getDefaultTracker();
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF7A01")));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        Tab = (ViewPager) findViewById(R.id.pagereleitor);

        Tab.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar = getSupportActionBar();
                        actionBar.setSelectedNavigationItem(position);
                    }
                });
        Tab.setAdapter(TabAdapter);


        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF7A01")));

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {


            @Override
            public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
                Tab.setCurrentItem(tab.getPosition());
                String tela = "Pagina " + tab.getPosition();
                sendScreenImageName(tela);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

            }



        };
        //Add New Tab

        //actionBar.addTab(actionBar.newTab().setText("Indicar").setIcon(this.getResources().getDrawable(R.drawable.indicarcandidato)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Votar").setIcon(this.getResources().getDrawable(R.drawable.votarcandidato)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Propostas").setIcon(this.getResources().getDrawable(R.drawable.avaliarpropostas)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Feitos").setIcon(this.getResources().getDrawable(R.drawable.avaliarrealizacoes)).setTabListener(tabListener));
        // actionBar.addTab(actionBar.newTab().setText("Contato").setIcon(this.getResources().getDrawable(R.drawable.mensagemcandidato)).setTabListener(tabListener));

        //Intent i = this.getIntent();
        //processIntent(i);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
    }

    ;

    private void processIntent(Intent intent) {
        String ref;
        try {
            ref = intent.getExtras().getString("ref");
            String idnotif = intent.getExtras().getString("idnotif");
            if (ref.equals("notif")) {
                new RegistraClique(ctx, idnotif).execute();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void sendScreenImageName(String tela) {
        mTracker.setScreenName("Image~" + tela);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Tab.setCurrentItem(0);
        //Indicar.mostracidadeescolha();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        super.onCreateOptionsMenu(menu);
        MenuItem searchItem = menu.findItem(R.id.search_field_eleitor);
        SearchView searchview = new SearchView(this);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchview.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        MenuItemCompat.setShowAsAction(searchItem,
                MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW |
                        MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(searchItem, searchview);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.abremenu) {
            View menuItemView = this.findViewById(R.id.abremenu);
            PopupMenu popupMenu = new PopupMenu(this, menuItemView);
            popupMenu.inflate(R.menu.eleitor);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item2) {

                    if (item2.getItemId() == R.id.alteracidade) {
                        sharedPref.edit().putInt("cidade", 0).apply();
                        Indicar.mostracidadeescolha();
                        Tab.setCurrentItem(0);
                    }

                    if (item2.getItemId() == R.id.menueleitor) {
                        finish();
                        Intent intent = new Intent(ctx, Eleitor.class);
                        startActivity(intent);
                    }
                    if (item2.getItemId() == R.id.menucandidato) {

                        sharedPref.edit().putInt("perfil", 2).apply();
                        finish();
                        Intent intent = new Intent(ctx, Candidato.class);
                        startActivity(intent);
                    }
                    return true;
                }
            });

            popupMenu.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
}