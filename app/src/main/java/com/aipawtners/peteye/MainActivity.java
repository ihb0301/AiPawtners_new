package com.aipawtners.peteye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    MainFragment main_fragment;
    InfoFragment info_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_fragment=new MainFragment();
        info_fragment=new InfoFragment();



        getSupportFragmentManager().beginTransaction().replace(R.id.container,main_fragment).commit();

        BottomNavigationView bottomNavigation=findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if(item.getItemId()==R.id.tab_main){
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, main_fragment).commit();
                        }else if(item.getItemId()==R.id.tab_information){
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, info_fragment).commit();
                        }
                        return true;
            }
        }
        );
    }
}