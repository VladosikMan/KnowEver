package ru.vladgad.knowever.ConstructChapter;

import android.content.ContentResolver;
import android.content.Context;
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
import com.google.firebase.storage.StorageReference;

import ru.vladgad.knowever.ConstructBook.RedactureBook;
import ru.vladgad.knowever.Essence.Block_Stat;
import ru.vladgad.knowever.Essence.Book_Stat;
import ru.vladgad.knowever.Essence.Chapter;
import ru.vladgad.knowever.Essence.Chapter_Stat;
import ru.vladgad.knowever.Essence.PostMan;
import ru.vladgad.knowever.MainMenu;
import ru.vladgad.knowever.MyCallback;
import ru.vladgad.knowever.R;

public class CreateChapter extends Fragment implements View.OnClickListener {
    private static View v;
    private Context context;
    private FragmentTransaction fTrans;
    private Button retur, back, ok;
    private EditText name, anno;
    private Spinner spinner;
    private RedactureBook redactureBook;
    private DatabaseReference chapterRef;
    private  String[] data;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.createchapter,null);
        context = container.getContext();
        init();
        Log.d("mLog", String.valueOf(PostMan.lengChap));
        createDataForSpinner();
        return v;
    }
    private void  createDataForSpinner(){
        String[]data = new String[PostMan.lengChap+1];
        for(int i=0;i<PostMan.lengChap+1;i++){
            data[i]=Integer.toString(i+1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //заголовок
        spinner.setPrompt("Позиция");
        spinner.setSelection(0);
    }
    private void saveChapter(){
        if(!zeroView()){
            Toast.makeText(context,"Не введено имя или аннотация", Toast.LENGTH_SHORT);
        }else{
                int pos =spinner.getSelectedItemPosition()+1;
                Log.d("mLog","Это позиция ля ля ля");
                Log.d("mLog",Integer.toString(pos));
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
                String namE = name.getText().toString();
                String annot  = anno.getText().toString();
                String booker = Book_Stat.id;
                Long time = System.currentTimeMillis();
                String create = String.valueOf(time);
                String edit = create;
                String id = chapterRef.push().getKey();
                Log.d("mLog","Сука бля ебанный сссука " +pos);
                Chapter newChapter = new Chapter(id,namE,annot, booker,create,edit,pos);
                if(id!=null)  chapterRef.child(id).setValue(newChapter);
                // теперть надо изменить положеин глав
                fTrans= getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragCont,redactureBook);
                fTrans.commit();
        }
    }
    private void changePositionChapter(int pos, MyCallback myCallback){
        Query query = chapterRef.orderByChild("book").equalTo(Book_Stat.id);
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
    private boolean zeroView() {
        if(name.getText().toString().isEmpty()||anno.getText().toString().isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }
    private void init() {
        chapterRef = FirebaseDatabase.getInstance().getReference(getString(R.string.key_chapter));
        redactureBook = new RedactureBook();
        name = (EditText)v.findViewById(R.id.EditTextName);
        anno = (EditText)v.findViewById(R.id.EditTextAnnot);
        retur = (Button)v.findViewById(R.id.retur);
        back = (Button)v.findViewById(R.id.back);
        ok = (Button)v.findViewById(R.id.ok);
        spinner = (Spinner)v.findViewById(R.id.spinner);
        retur.setOnClickListener(this);
        back.setOnClickListener(this);
        ok.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.retur:{
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragCont,redactureBook);
                fTrans.commit();
                break;
            }
            case R.id.back:{
                name.setText("");
                anno.setText("");
                break;
            }
            case R.id.ok:{
                //проверка на схожесть имён
                saveChapter();
                break;
            }
        }
    }
}
