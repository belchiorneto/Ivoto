package com.lojapro.candidato;

import com.lojapro.can.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class CandidatoBackup extends TabActivity implements OnTabChangeListener
{
	private float lastX; 
	private TabHost tabHost;
    private static final int ANIMATION_TIME = 300;
    private View previousView;
    private View currentView;
    private int currentTab;

	/** Called when the activity is first created. */
            @Override

            public void onCreate(Bundle savedInstanceState)
            {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.candidato);

                    // create the TabHost that will contain the Tabs
                    tabHost = (TabHost)findViewById(android.R.id.tabhost);
                    

                    TabSpec tab1 = tabHost.newTabSpec("Perfil");
                    TabSpec tab2 = tabHost.newTabSpec("Propostas");
                    TabSpec tab3 = tabHost.newTabSpec("Realizações");
                    TabSpec tab4 = tabHost.newTabSpec("Agenda");
                    TabSpec tab5 = tabHost.newTabSpec("Mensagens");
                    TabSpec tab6 = tabHost.newTabSpec("Apoiadores");
                    TabSpec tab7 = tabHost.newTabSpec("Ger. Apoiadores");
                    TabSpec tab8 = tabHost.newTabSpec("Eleitores");

                   // Set the Tab name and Activity
                   // that will be opened when particular Tab will be selected
                    tab1.setIndicator(null,getResources().getDrawable(R.drawable.home));
                    tab2.setIndicator(null,getResources().getDrawable(R.drawable.idea));
                    tab3.setIndicator(null,getResources().getDrawable(R.drawable.realizado));
                    tab4.setIndicator(null,getResources().getDrawable(R.drawable.agenda));
                    tab5.setIndicator(null,getResources().getDrawable(R.drawable.msg));
                    tab6.setIndicator(null,getResources().getDrawable(R.drawable.apoio));
                    tab7.setIndicator(null,getResources().getDrawable(R.drawable.seguirapoio));
                    tab8.setIndicator(null,getResources().getDrawable(R.drawable.eleitores));
                    
                    tab1.setContent(new Intent(this,PerfilCandidato.class));
                    tab2.setContent(new Intent(this,Propostas.class));
                    tab3.setContent(new Intent(this,Realizado.class));
                    tab4.setContent(new Intent(this,Agenda.class));
                    tab5.setContent(new Intent(this,Mensagens.class));
                    tab6.setContent(new Intent(this,Apoiadores.class));
                    tab7.setContent(new Intent(this,SeguirApoiadores.class));
                    tab8.setContent(new Intent(this,Eleitores.class));
                    
                    
                    
                    /** Add the tabs  to the TabHost to display. */
                   
                    tabHost.addTab(tab1);
                    tabHost.addTab(tab2);
                    tabHost.addTab(tab3);
                    tabHost.addTab(tab4);
                    tabHost.addTab(tab5);
                    tabHost.addTab(tab6);
                    tabHost.addTab(tab7);
                    tabHost.addTab(tab8);
                    tabHost.setOnTabChangedListener(this);
            }
           
            @Override
            public boolean onTouchEvent(MotionEvent touchevent) {
                switch (touchevent.getAction()) {
                // when user first touches the screen to swap
                case MotionEvent.ACTION_DOWN: {
                    lastX = touchevent.getX();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    float currentX = touchevent.getX();

                    // if left to right swipe on screen
                    if (lastX < currentX) {

                        switchTabs(false);
                    }

                    // if right to left swipe on screen
                    if (lastX > currentX) {
                        switchTabs(true);
                    }

                    break;
                }
                }
                return false;
            }
            public void switchTabs(boolean direction) {
                if (!direction) // true = move left
                {
                    if (tabHost.getCurrentTab() == 0)
                        tabHost.setCurrentTab(tabHost.getTabWidget().getTabCount() - 1);
                    else
                        tabHost.setCurrentTab(tabHost.getCurrentTab() - 1);
                } else
                // move right
                {
                    if (tabHost.getCurrentTab() != (tabHost.getTabWidget()
                            .getTabCount() - 1))
                        tabHost.setCurrentTab(tabHost.getCurrentTab() + 1);
                    else
                        tabHost.setCurrentTab(0);
                }
            }
           
            /**
             * A custom OnTabChangeListener that uses the TabHost its related to to fetch information about the current and previous
             * tabs. It uses this information to perform some custom animations that slide the tabs in and out from left and right.
             * 
             * @author Daniel Kvist
             * 
             */
         
                /**
                 * Constructor that takes the TabHost as a parameter and sets previousView to the currentView at instantiation
                 * 
                 * @param tabHost
                 * @return 
                 */
                public void AnimatedTabHostListener(TabHost tabHost)
                {
                    this.tabHost = tabHost;
                    this.previousView = tabHost.getCurrentView();
                }

                /**
                 * When tabs change we fetch the current view that we are animating to and animate it and the previous view in the
                 * appropriate directions.
                 */
                @Override
                public void onTabChanged(String tabId)
                {
                	System.out.println("tab changed");
                    currentView = tabHost.getCurrentView();
                    if (tabHost.getCurrentTab() > currentTab)
                    {
                        if(previousView != null){
                    	previousView.setAnimation(outToLeftAnimation());
                    	}
                        currentView
                        .setAnimation(inFromRightAnimation());
                    }
                    else
                    {
                        previousView.setAnimation(outToRightAnimation());
                        currentView.setAnimation(inFromLeftAnimation());
                    }
                    previousView = currentView;
                    currentTab = tabHost.getCurrentTab();

                }

                /**
                 * Custom animation that animates in from right
                 * 
                 * @return Animation the Animation object
                 */
                private Animation inFromRightAnimation()
                {
                    Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
                    return setProperties(inFromRight);
                }

                /**
                 * Custom animation that animates out to the right
                 * 
                 * @return Animation the Animation object
                 */
                private Animation outToRightAnimation()
                {
                    Animation outToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
                    return setProperties(outToRight);
                }

                /**
                 * Custom animation that animates in from left
                 * 
                 * @return Animation the Animation object
                 */
                private Animation inFromLeftAnimation()
                {
                    Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
                    return setProperties(inFromLeft);
                }

                /**
                 * Custom animation that animates out to the left
                 * 
                 * @return Animation the Animation object
                 */
                private Animation outToLeftAnimation()
                {
                    Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
                    return setProperties(outtoLeft);
                }

                /**
                 * Helper method that sets some common properties
                 * @param animation the animation to give common properties
                 * @return the animation with common properties
                 */
                private Animation setProperties(Animation animation)
                {
                    animation.setDuration(ANIMATION_TIME);
                    animation.setInterpolator(new AccelerateInterpolator());
                    return animation;
                }
          
} 
