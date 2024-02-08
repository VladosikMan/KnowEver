package ru.vladgad.knowever;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import ru.vladgad.knowever.ConstructMaterial.MainConstruct;


public class RedactureMaterial extends AppCompatActivity implements View.OnClickListener {
    private TextView nameChapter;
    private FragmentTransaction fTrans;
    private EditText annot;
    private MainConstruct mainConstruct;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redacturematerial);
        mainConstruct = new MainConstruct();
        fTrans=getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.FragCont,mainConstruct);
        fTrans.commit();
    }
    @Override
    public void onClick(View v) {

    }
}
