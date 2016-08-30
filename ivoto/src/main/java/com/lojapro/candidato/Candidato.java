package com.lojapro.candidato;

import android.app.SearchManager;
import android.os.Bundle;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.lojapro.can.Eleitor;
import com.lojapro.can.R;

import analytcs.AnalyticsApplication;
import android.app.FragmentTransaction;
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
 
public class Candidato extends AppCompatActivity {
	Tracker t;
	ViewPager Tab;
    TabPageAdapter TabAdapter;
    ActionBar actionBar;
    SharedPreferences sharedPref;
    Context ctx;
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidato);
        
        sharedPref = this.getSharedPreferences("com.lojapro.can", Context.MODE_PRIVATE);
        
        
        
        ctx = this.getApplicationContext();
        
        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = new AnalyticsApplication(ctx);
        t = application.getDefaultTracker();
        // [END shared_tracker]
        //t = new MyTrack(ctx).getTracker();
        
        TabAdapter = new TabPageAdapter(getSupportFragmentManager());

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF7A01")));
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        Tab = (ViewPager)findViewById(R.id.pagercandidato);
      
        Tab.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {

                        actionBar = getSupportActionBar();
                        actionBar.setSelectedNavigationItem(position);                    }
                });
        Tab.setAdapter(TabAdapter);
 

        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
       // actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF7A01")));
    	actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF7A01")));
    	
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){

            @Override
            public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
                Tab.setCurrentItem(tab.getPosition());
                String tela = "Pagina " +  tab.getPosition() + " (can)";
                sendScreenImageName(tela);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

            }


            public void onTabUnselected(android.app.ActionBar.Tab tab,
                    FragmentTransaction ft) {
                // TODO Auto-generated method stub

            }};
            //Add New Tab
          
            actionBar.addTab(actionBar.newTab().setText("Inicio").setIcon(this.getResources().getDrawable(R.drawable.home)).setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText("Propostas").setIcon(this.getResources().getDrawable(R.drawable.idea)).setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText("Feitos").setIcon(this.getResources().getDrawable(R.drawable.realizado)).setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText("Agenda").setIcon(this.getResources().getDrawable(R.drawable.agenda)).setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText("Contatos").setIcon(this.getResources().getDrawable(R.drawable.msg)).setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText("Apoio").setIcon(this.getResources().getDrawable(R.drawable.apoio)).setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText("Gerir").setIcon(this.getResources().getDrawable(R.drawable.seguirapoio)).setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText("Eleitores").setIcon(this.getResources().getDrawable(R.drawable.eleitores)).setTabListener(tabListener));
    }
   
    
    private void sendScreenImageName(String tela) {
        // [START screen_view_hit]
    	t.setScreenName("Image~" + tela);
  		t.send(new HitBuilders.ScreenViewBuilder().build());
  		// [END screen_view_hit]
    }
    
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		sendScreenImageName("Inicio");
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
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
                     	
                  }	
               	
               	 if (item2.getItemId() == R.id.menueleitor) {
               		sharedPref.edit().putInt("perfil", 1).apply();
                	finish();
                	Intent intent = new Intent(ctx, Eleitor.class);
                	startActivity(intent);
                    }
                    if (item2.getItemId() == R.id.menucandidato) {
                   	
                   	SharedPreferences.Editor editor = sharedPref.edit();
                    	editor.putInt("perfil", 2);
                    	editor.commit();
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
    
    
    
}