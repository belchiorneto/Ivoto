package com.lojapro.candidato;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPageAdapter extends FragmentStatePagerAdapter {
    public TabPageAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }
 
    @Override
    public Fragment getItem(int i) {
        switch (i) {
        case 0:
            return new PerfilCandidato();
        case 1:
           //Fragment for Ios Tab
            return new Propostas();
        case 2:
            //Fragment for Windows Tab
            return new Realizado();
        case 3:
            //Fragment for Windows Tab
            return new Agenda();
        case 4:
            //Fragment for Windows Tab
            return new Mensagens();
        case 5:
            //Fragment for Windows Tab
            return new Apoiadores();
        case 6:
            //Fragment for Windows Tab
            return new SeguirApoiadores();
        case 7:
            //Fragment for Windows Tab
            return new Eleitores();
        }
        return null;
 
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 8; //No of Tabs
    }
 
    }
