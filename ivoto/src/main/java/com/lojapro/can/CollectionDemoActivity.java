package com.lojapro.can;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.lojapro.can.R;

public class CollectionDemoActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
     * each object in a collection. We use a {@link android.support.v4.app.FragmentStatePagerAdapter}
     * derivative, which will destroy and re-create fragments as needed, saving and restoring their
     * state in the process. This is important to conserve memory and is a best practice when
     * allowing navigation between objects in a potentially large collection.
     */
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    
    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    ViewPager mViewPager;
    static ImageLoader imageLoader;
    ListaRegistros lista;
    static ArrayList<HashMap<String, String>> songsList;
    static ArrayList<HashMap<String, String>> DadosCandidato;
    ListaCandidato lista_unica;
    static Intent itent;
    static int position = 0;
    static List<TableMeusCandidatos> list;
    public static int idcandidato;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_demo);
        imageLoader = new ImageLoader(this);
        itent = getIntent();
        if(position == 0 && itent.getExtras().getInt("position") != 0){
        	position = itent.getExtras().getInt("position");
        	itent.putExtra("position", 0);
        }
        lista = new ListaRegistros(CollectionDemoActivity.this);
        songsList = LazyAdapter.filtrado;
        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
    }

    @SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, Swipe.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
    public static class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
        	if(itent.getExtras().get("nome") != "nulo"){
        	for(int a = 0; a<songsList.size(); a++){
        		if (songsList.get(a).get("title").equals(itent.getExtras().get("nome"))) {
        		   itent.putExtra("nome", "nulo");
        		   i = a;
        		}
        	}
        	}
        	System.out.println("Position: " + i);
        	Fragment fragment = new DemoObjectFragment();
            Bundle args = new Bundle();

            args.putString("nome", songsList.get(i).get("title"));
            args.putString("numero", songsList.get(i).get("duration"));
            args.putString("cargo", songsList.get(i).get("artist"));
            args.putString("url", songsList.get(i).get("thumb_url"));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
            return songsList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DemoObjectFragment extends Fragment {

        public static final String ARG_OBJECT = "object";
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	
            View lst_item_view = inflater.inflate(R.layout.fragment1, null);
            TextView numero = (TextView) lst_item_view.findViewById(R.id.numero);
            TextView cargo = (TextView) lst_item_view.findViewById(R.id.cargo);
            TextView nome = (TextView) lst_item_view.findViewById(R.id.nome);
            ImageView thumb_image = (ImageView) lst_item_view.findViewById(R.id.imageView1);
            Bundle args = getArguments();
            
            numero.setText(args.getString("numero"));
            cargo.setText(args.getString("cargo"));
            nome.setText(args.getString("nome"));
            imageLoader.DisplayImage(args.getString("url"), thumb_image);
           
            return lst_item_view;
        }
    }
}
