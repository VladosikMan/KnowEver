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

public class MainTest  extends Fragment implements View.OnClickListener {
    private MainConstruct mainConstruct;
    private View v;
    private Context context;
    private TestTest testTest;
    private FragmentTransaction fTrans;
//    private Button create;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.maintest,null);
        context = container.getContext();
        init();
        return v;
    }
    private void init(){
  //      create = v.findViewById(R.id.create);
    //    create.setOnClickListener(this);
        testTest = new TestTest();
        mainConstruct = new MainConstruct();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){


        }
    }
}
