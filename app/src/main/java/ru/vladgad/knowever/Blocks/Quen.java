package ru.vladgad.knowever.Blocks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ru.vladgad.knowever.ConstructMaterial.MainConstruct;
import ru.vladgad.knowever.Essence.Block;
import ru.vladgad.knowever.Essence.Chapter_Stat;
import ru.vladgad.knowever.R;

public class Quen extends Fragment implements View.OnClickListener{
    private View v;
    private Context context;
    private FragmentTransaction fTrans;
    private Button create,clear,cancel;
    private EditText editTextName, editTextBase;
    private MainConstruct mainConstruct;
    private DatabaseReference blockRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,   @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.quen,null);
        context= container.getContext();
        init();
        return v;
    }
    private void saveQuen(){
        String id =blockRef.push().getKey();
        int type = 7;
        String value = editTextName.getText().toString();
        value += " Перенос91 ";
        value += editTextBase.getText().toString();
        int pos =4;
        String chapter = Chapter_Stat.id;
        Block block = new Block(id,chapter,value,type,pos);
        if(id!=null)blockRef.child(id).setValue(block);
    }
    private void init(){
        blockRef = FirebaseDatabase.getInstance().getReference(getString(R.string.key_block));
        mainConstruct = new MainConstruct();
        editTextName = v.findViewById(R.id.editTextName);
        editTextBase = v.findViewById(R.id.editTextBase);
        create = v.findViewById(R.id.create);
        clear = v.findViewById(R.id.clear);
        cancel = v.findViewById(R.id.cancel);
        create.setOnClickListener(this);
        clear.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:{
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragCont,mainConstruct);
                fTrans.commit();
                break;
            }
            case R.id.clear:{
                editTextName.setText("");
                editTextBase.setText("");
                break;
            }
            case R.id.create:{
                if(editTextName.getText().toString().isEmpty()||editTextBase.getText().toString().isEmpty()){

                }else {
                    saveQuen();
                    fTrans=getFragmentManager().beginTransaction();
                    fTrans.replace(R.id.FragCont,mainConstruct);
                    fTrans.commit();
                }
                break;
            }

        }
    }
}
