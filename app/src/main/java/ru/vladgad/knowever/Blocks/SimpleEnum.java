package ru.vladgad.knowever.Blocks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ru.vladgad.knowever.R;

public class SimpleEnum extends Fragment {
    private View v;
    private Context context;
    private FragmentTransaction fTrans;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.simpleenum,null);
        context= container.getContext();
        init();
        return v;
    }
    private void init(){

    }
}
