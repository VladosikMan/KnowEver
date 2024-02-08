package ru.vladgad.knowever.TesterSystem;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import ru.vladgad.knowever.ConstructMaterial.MainConstruct;
import ru.vladgad.knowever.R;

public class CreateTest extends Fragment implements View.OnClickListener{
    private MainConstruct mainConstruct;
    private View v;
    private Context context;
    private Button createTest;
    private FragmentTransaction fTrans;
    private MainTest mainTest;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.createtest,null);
        context = container.getContext();


        init();
        return v;
    }
    private void init(){
        createTest = v.findViewById(R.id.createTest);
        createTest.setOnClickListener(this);
        mainTest = new MainTest();
    }
    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.createTest:{
               fTrans = getFragmentManager().beginTransaction();
               fTrans.replace(R.id.FragCont, mainTest);
               fTrans.addToBackStack(null);
               fTrans.commit();
               break;
           }

       }
    }
}
