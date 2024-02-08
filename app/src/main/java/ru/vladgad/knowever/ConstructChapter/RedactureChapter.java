package ru.vladgad.knowever.ConstructChapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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

import ru.vladgad.knowever.ConstructBook.RedactureBook;
import ru.vladgad.knowever.Essence.Book_Stat;
import ru.vladgad.knowever.Essence.Chapter;
import ru.vladgad.knowever.Essence.Chapter_Stat;
import ru.vladgad.knowever.Essence.PostMan;
import ru.vladgad.knowever.MainActivity;
import ru.vladgad.knowever.MyCallback;
import ru.vladgad.knowever.R;
import ru.vladgad.knowever.RedactureMaterial;

import static ru.vladgad.knowever.R.*;

public class RedactureChapter extends Fragment implements View.OnClickListener {
    private static Context context;
    private static View v;
    private FragmentTransaction fTrans;
    private Button delete,retur,change,cancel, material;
    private EditText nam,anno;
    private ImageView ima;
    private DatabaseReference myRef;
    private DatabaseReference chapterRef;
    private RedactureBook redactureBook;
    private Spinner spinner;
    private String[] data;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(layout.redacturechapter,null);
        context = container.getContext();
        init();
        Log.d("mLog","Где подломака?");
        createDataForSpinner();
        spinner.setSelection(Chapter_Stat.position-1);
        return v;
    }
    private void  createDataForSpinner(){
        String[]data = new String[PostMan.lengChap];
        for(int i=0;i<PostMan.lengChap;i++){
            data[i]=Integer.toString(i+1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Позиция");
    }
    private void init() {
        material = v.findViewById(id.matereial);
        material.setOnClickListener(this);
        cancel = v.findViewById(id.cancel);
        cancel.setOnClickListener(this);
        Log.d("mLog","Приветик, держи букетик");
        spinner = v.findViewById(id.spinner);
        redactureBook= new RedactureBook();
        delete = (Button)v.findViewById(id.delete);
        retur = (Button)v.findViewById(id.retur);
        change = (Button)v.findViewById(id.change);
        delete.setOnClickListener(this);
        retur.setOnClickListener(this);
        change.setOnClickListener(this);
        nam = (EditText)v.findViewById(id.header);
        nam.setText(Book_Stat.name);
        anno = (EditText)v.findViewById(id.anno);
        anno.setText(Book_Stat.annotation);
        ima = (ImageView) v.findViewById(id.imaher);
        chapterRef = FirebaseDatabase.getInstance().getReference(getString(R.string.key_chapter));
        nam.setText(Chapter_Stat.name);
        anno.setText(Chapter_Stat.annotation);
    }
    private void deleteDataFromBD(){
        myRef = FirebaseDatabase.getInstance().getReference(getString(R.string.key_book)).child(Book_Stat.id);
        myRef.removeValue();
    }
    private void changePositionChapter(int pos, MyCallback myCallback){
        Log.d("mLog","Позиция - "+ pos);
        Query query = chapterRef.orderByChild("book").equalTo(Book_Stat.id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    int tep = dataSnapshot.child("position").getValue(Integer.class);
                    Log.d("mLog", String.valueOf(tep));
                    if(tep==Chapter_Stat.position){
                        dataSnapshot.getRef().child("position").setValue(tep);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private  void updateFromDB(int pos){
        changePositionChapter(pos, new MyCallback() {
            @Override
            public void onCallback(String value) {

            }
        });
        String s = nam.getText().toString();
        String s1 = anno.getText().toString();
        Query query = chapterRef.orderByChild("id").equalTo(Chapter_Stat.id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    dataSnapshot.getRef().child("name").setValue(s);
                    dataSnapshot.getRef().child("annotation").setValue(s1);
                    dataSnapshot.getRef().child("position").setValue(pos);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case id.cancel:{
               nam.setText(Chapter_Stat.name);
               anno.setText(Chapter_Stat.annotation);
               spinner.setSelection(Chapter_Stat.position);
               break;
           }
           case id.delete:
               deleteDataFromBD();
               fTrans=getFragmentManager().beginTransaction();
               fTrans.replace(R.id.FragCont,redactureBook);
               fTrans.commit();
           case id.retur:{
               fTrans=getFragmentManager().beginTransaction();
               fTrans.replace(R.id.FragCont,redactureBook);
               fTrans.commit();
               break;
           }
           case id.change:{
               int pos = spinner.getSelectedItemPosition()+1;
               updateFromDB(pos);
               fTrans=getFragmentManager().beginTransaction();
               fTrans.replace(R.id.FragCont,redactureBook);
               fTrans.commit();
               break;
           }
           case id.matereial:{
               Intent intent = new Intent (RedactureChapter.this.getActivity(), RedactureMaterial.class);
               startActivity(intent);
               break;
           }
       }
    }
}
