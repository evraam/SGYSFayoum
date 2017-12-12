package com.example.andrew.goodreads;

import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    /*private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Home Fragment1 = new Home();
                    FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                    transaction1.replace(android.R.id.content, Fragment1);
                    transaction1.disallowAddToBackStack();

                    transaction1.commit();
                    return true;
                case R.id.navigation_pdfs:
                    MyBooks Fragment2 = new MyBooks();
                    FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
                    transaction2.replace(android.R.id.content, Fragment2);
                    transaction2.disallowAddToBackStack();

                    transaction2.commit();
                    return true;
                case R.id.navigation_search:
                    Search Fragment3 = new Search();
                    FragmentTransaction transaction3 = getFragmentManager().beginTransaction();
                    transaction3.replace(android.R.id.content, Fragment3);
                    transaction3.disallowAddToBackStack();

                    transaction3.commit();
                    return true;
                case R.id.navigation_more:
                    More Fragment4 = new More();
                    FragmentTransaction transaction4 = getFragmentManager().beginTransaction();
                    transaction4.replace(android.R.id.content, Fragment4);
                    transaction4.disallowAddToBackStack();

                    transaction4.commit();


                    return true;
                case R.id.navigation_library:
                    reviews Fragment5 = new reviews();
                    FragmentTransaction transaction5 = getFragmentManager().beginTransaction();
                    transaction5.replace(android.R.id.content, Fragment5);
                    transaction5.disallowAddToBackStack();

                    transaction5.commit();


                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        disableShiftMode(navigation);
        navigation.setBackgroundResource(android.R.color.darker_gray);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Home Fragment1 = new Home();
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, Fragment1);
        transaction.commit();
    }

    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }*/


}
