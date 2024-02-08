package ru.vladgad.knowever.Blocks;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import ru.vladgad.knowever.Essence.Book_Stat;
import ru.vladgad.knowever.Essence.Chapter_Stat;
import ru.vladgad.knowever.Essence.PostMan;
import ru.vladgad.knowever.MyCallback;
import ru.vladgad.knowever.R;

public class SimpleText extends Fragment implements View.OnClickListener{
    private View v;
    private Context context;
    private FragmentTransaction fTrans;
    private EditText editText;
    private DatabaseReference blockRef;
    private Spinner size, font, spinner;
    private Button cancel, clear, create;
    private MainConstruct mainConstruct;
    private String data[];
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.simpletext,null);
        context= container.getContext();
        init();
        spin();
        createDataForSpinner();
        return v;
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
    private void  createDataForSpinner(){
        Log.d("mLog","Сука бля как заебался");
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
    private  void spin(){
        size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:{
                        Log.d("mLog","12");
                        editText.setTextSize(12);
                        break;
                    }
                    case 1:{
                        Log.d("mLog","14");
                        editText.setTextSize(14);
                        break;
                    }
                    case 2:{
                        Log.d("mLog","18");
                        editText.setTextSize(18);
                        break;
                    }
                    case 3:{
                        Log.d("mLog","24");
                        editText.setTextSize(24);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        font.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int typeface = Typeface.NORMAL;
                switch (position){
                    case 0:{
                        typeface = Typeface.NORMAL;
                        break;
                    }
                    case 1:{
                        typeface += Typeface.ITALIC;
                        break;
                    }
                    case 2:{
                        typeface += Typeface.BOLD;
                        break;
                    }
                }
                editText.setTypeface(null,typeface);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void init(){
        blockRef = FirebaseDatabase.getInstance().getReference(getString(R.string.key_block));
        mainConstruct = new MainConstruct();
        cancel = v.findViewById(R.id.cancel);
        clear = v.findViewById(R.id.clear);
        create = v.findViewById(R.id.create);
        cancel.setOnClickListener(this);
        clear.setOnClickListener(this);
        create.setOnClickListener(this);
        spinner = (Spinner)v.findViewById(R.id.spinner);
        font = v.findViewById(R.id.font);
        size = v.findViewById(R.id.size);
        editText = v.findViewById(R.id.editText);
    }
    private void saveBlock(){
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
        String id = blockRef.push().getKey();
        String value = editText.getText().toString();
        int type =1;
        Log.d("mLog","Что такое Пту"+ value);
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
                    editText.setText("");
                    break;
                }
                case R.id.create:{
                    if(editText.getText().toString().isEmpty()){
                        Toast.makeText(context,"Текстовое поле пустое", Toast.LENGTH_SHORT);
                    }else{
                        Log.d("mLog","Сукабля - цуацу");
                        saveBlock();
                        fTrans=getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.FragCont,mainConstruct);
                        fTrans.commit();
                    }
                    break;
                }
            }
    }
}