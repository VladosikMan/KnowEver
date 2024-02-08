package ru.vladgad.knowever.TesterSystem;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.vladgad.knowever.R;

public class TestTest extends Fragment {
    private Context context;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.testtest,null);
        context = container.getContext();
        init();
        return v;
    }

    private void init() {

    }
}

