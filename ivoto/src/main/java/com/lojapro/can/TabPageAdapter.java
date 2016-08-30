package com.lojapro.can;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPageAdapter extends FragmentStatePagerAdapter {
    public TabPageAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }
 
    @Override
    public String getPageTitle(int position) {
    	switch (position) {
        //case 0:
          //return "Indicar";
        case 0:
          return "Votar";
        case 1:
          return "Avaliar Proposta";
        case 2:
          return "Feitos";
        //case 4:
        //  return "Contato";
        }
        return null;
    
    }
    @Override
    public Fragment getItem(int i) {
        switch (i) {
        //case 0:
        //  return new Indicar();
        case 0:
          return new Votar();
        case 1:
          return new AvaliarProposta();
        case 2:
          return new Feitos();
       // case 4:
         // return new Contato();
        	
       }
        return null;
 
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 4; //No of Tabs
    }
 
    }
