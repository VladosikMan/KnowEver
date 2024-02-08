package ru.vladgad.knowever;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity  {
    private FragmentTransaction fTrans;
    private MainMenu mainMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainMenu = new MainMenu();
        fTrans=getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.FragCont,mainMenu);
        fTrans.commit();
    }
}