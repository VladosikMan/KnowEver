package ru.vladgad.knowever.Blocks;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ru.vladgad.knowever.ConstructMaterial.MainConstruct;
import ru.vladgad.knowever.Essence.Block;
import ru.vladgad.knowever.Essence.Chapter_Stat;
import ru.vladgad.knowever.Essence.PostMan;
import ru.vladgad.knowever.MyCallback;
import ru.vladgad.knowever.R;

public class Foot extends Fragment implements View.OnClickListener {
    private View v;
    private Context context;
    private FragmentTransaction fTrans;
    private Button cancel,clear,create;
    private EditText editTextName, editTextBase;
    private MainConstruct mainConstruct;
    private DatabaseReference blockRef;
    private Spinner spinner;
    String[] data;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.foot,null);
        context= container.getContext();
        init();
        createDataForSpinner();
        return v;
    }
    private void  createDataForSpinner(){
        Log.d("mLog","Сука бля как заебался в пизду нахуй");
        String[]data = new String[PostMan.lengBlock+1];
        for(int i=0;i<PostMan.lengBlock+1;i++){
            data[i]=Integer.toString(i+1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //заголовок
        spinner.setPrompt("Позиция");
        spinner.setSelection(0);
    }
    private void changePositionChapter(int pos, MyCallback myCallback){
        Query query = blockRef.orderByChild("chapter").equalTo(Chapter_Stat.id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    int tep = dataSnapshot.child("position").getValue(Integer.class);
                    Log.d("mLog", "Какого хрена"+ String.valueOf(tep));
                    if(tep>=pos){
                        dataSnapshot.getRef().child("position").setValue(tep+1);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void init(){
        spinner = (Spinner)v.findViewById(R.id.spinner);
        blockRef = FirebaseDatabase.getInstance().getReference(getString(R.string.key_block));
        mainConstruct = new MainConstruct();
        editTextName  = v.findViewById(R.id.editTextName);
        editTextBase = v.findViewById(R.id.editTextBase);
        cancel = v.findViewById(R.id.cancel);
        clear = v.findViewById(R.id.clear);
        create = v.findViewById(R.id.create);
        cancel.setOnClickListener(this);
        clear.setOnClickListener(this);
        create.setOnClickListener(this);
    }
    private void saveFoot() {
        int pos =spinner.getSelectedItemPosition()+1;
        changePositionChapter(pos,new MyCallback() {
            @Override
            public void onCallback(String value) {
                Log.d("mLog","Жозахун");
            }
        });
        try {
            Thread.sleep(1000); //Приостанавливает поток на 1 секунду
        } catch (Exception e) {

        }
        String  id = blockRef.push().getKey();
        int type = 6;
        int typeface = Typeface.NORMAL;
        typeface += Typeface.BOLD;
        String value = editTextName.getText().toString();
        value += " Перенос91 ";
        value += editTextBase.getText().toString();;
        String chapter = Chapter_Stat.id;
        Block block = new Block(id,chapter,value,type,pos);
        if(id!=null)blockRef.child(id).setValue(block);
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
                    saveFoot();
                    fTrans=getFragmentManager().beginTransaction();
                    fTrans.replace(R.id.FragCont,mainConstruct);
                    fTrans.commit();
                }
                break;
            }
        }
    }

}
