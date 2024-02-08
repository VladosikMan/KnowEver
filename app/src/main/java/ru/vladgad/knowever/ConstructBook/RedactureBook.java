package ru.vladgad.knowever.ConstructBook;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import ru.vladgad.knowever.Constants;
import ru.vladgad.knowever.ConstructChapter.CreateChapter;
import ru.vladgad.knowever.ConstructChapter.RedactureChapter;
import ru.vladgad.knowever.Essence.Book;
import ru.vladgad.knowever.Essence.Book_Stat;
import ru.vladgad.knowever.Essence.Chapter;
import ru.vladgad.knowever.Essence.Chapter_Stat;
import ru.vladgad.knowever.Essence.PostMan;
import ru.vladgad.knowever.MainMenu;
import ru.vladgad.knowever.MyCallback;
import ru.vladgad.knowever.R;
import ru.vladgad.knowever.RedactureMaterial;
public class
RedactureBook extends Fragment implements View.OnClickListener {
    class Compar implements Comparator<Chapter>{
        @Override
        public int compare(Chapter o1, Chapter o2) {
            return Integer.compare(o1.position,o2.position);
        }
    }
    private  static ListView lvSimple;
    private SimpleAdapter sAdapter;
    private static View v;
    private static Context context;
    private Button delete,retur,change,chap,cancel;
    private EditText nam,anno;
    private FragmentTransaction fTrans;
    private CreateChapter createChapter;
    private ImageView ima;
    private String path;
    private int sizeChap;
    private MainMenu mainMenu;
    private DatabaseReference myRef;
    private DatabaseReference myRefBook;
    private DatabaseReference chapterRef;
    private ArrayList<Chapter> chapters;
    private RedactureChapter redactureChapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.redacturebook,null);
        context = container.getContext();
        init();
        getDataFromBD(new MyCallback() {
            @Override
            public void onCallback(String value) {
                Log.d("mLog","Жозахун");
            }
        });
        try {
            Thread.sleep(1000); //Приостанавливает поток на 1 секунду
        } catch (Exception e) {

        }
        listLoad();
        return v;
    }
    private void init() {
        redactureChapter = new RedactureChapter();
        chapters = new ArrayList<Chapter>();
        myRef = FirebaseDatabase.getInstance().getReference(getString(R.string.key_chapter));
        myRefBook = FirebaseDatabase.getInstance().getReference(getString(R.string.key_book));
        chapterRef = FirebaseDatabase.getInstance().getReference(getString(R.string.key_chapter));
        mainMenu = new MainMenu();
        createChapter = new CreateChapter();
        lvSimple = (ListView)v.findViewById(R.id.lvSimple);
        cancel = (Button)v.findViewById(R.id.cancel);
        delete = (Button)v.findViewById(R.id.delete);
        retur = (Button)v.findViewById(R.id.retur);
        change = (Button)v.findViewById(R.id.change);
        chap = (Button)v.findViewById(R.id.addChapter);
        cancel.setOnClickListener(this);
        delete.setOnClickListener(this);
        retur.setOnClickListener(this);
        change.setOnClickListener(this);
        chap.setOnClickListener(this);
        nam = (EditText)v.findViewById(R.id.header);
        nam.setText(Book_Stat.name);
        anno = (EditText)v.findViewById(R.id.anno);
        anno.setText(Book_Stat.annotation);
        ima = (ImageView) v.findViewById(R.id.imaher);
        Picasso.get().load(Book_Stat.image).into(ima);
    }
    private void updateDataFromBD() {
        Log.d("mLog","Ан дарк");
        Log.d("mLog",Book_Stat.id);
        Log.d("mLog", String.valueOf(nam.getText()));
        String s = nam.getText().toString();
        String s1 = anno.getText().toString();
        Query query = myRefBook.orderByChild("id").equalTo(Book_Stat.id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        dataSnapshot.getRef().child("name").setValue(s);
                        dataSnapshot.getRef().child("annotation").setValue(s1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void deleteDataFromBD(){
        myRefBook = FirebaseDatabase.getInstance().getReference(getString(R.string.key_book)).child(Book_Stat.id);
        myRefBook.removeValue();
        Query query = chapterRef.orderByChild("book").equalTo(Book_Stat.id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Log.d("mLog","Мы на Марсе");
                    dataSnapshot.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void getDataFromBD(MyCallback myCallback){
        Query phoneQuery = myRef.orderByChild("book").equalTo(Book_Stat.id);
        phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    Chapter chapter = ds.getValue(Chapter.class);
                    chapters.add(chapter);
                    Log.d("mLog",chapter.name);
                }
                Collections.sort(chapters,new Compar());
                int i;
                ArrayList<Map<String,Object>>data = new ArrayList<Map<String, Object>>();
                Map<String,Object> m;
                for(i=0;i<chapters.size();i++){
                    m = new HashMap<String, Object>();
                    m.put(Constants.CHAPTER_ATTRIBUTE_NAME_ID,chapters.get(i).id);
                    m.put(Constants.CHAPTER_ATTRIBUTE_NAME_NAME,chapters.get(i).name);
                    m.put(Constants.CHAPTER_ATTRIBUTE_NAME_ANNOTATION,chapters.get(i).annotation);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    String s = sdf.format(Long.parseLong(chapters.get(i).createdata));
                    m.put(Constants.CHAPTER_ATTRIBUTE_NAME_CREATEDATA,s);
                    s = sdf.format(Long.parseLong(chapters.get(i).editingdata));
                    m.put(Constants.CHAPTER_ATTRIBUTE_NAME_EDITDATA,s);
                    m.put(Constants.CHAPTER_ATTRIBUTE_NAME_POSITION,chapters.get(i).position);
                    data.add(m);
                }
                String[] from ={Constants.CHAPTER_ATTRIBUTE_NAME_NAME,Constants.CHAPTER_ATTRIBUTE_NAME_ANNOTATION,Constants.CHAPTER_ATTRIBUTE_NAME_CREATEDATA,Constants.CHAPTER_ATTRIBUTE_NAME_EDITDATA,Constants.CHAPTER_ATTRIBUTE_NAME_POSITION};
                int []to={R.id.Name,R.id.Annot,R.id.CreateData,R.id.EditData,R.id.position};
                SimpleAdapter sAdapter = new SimpleAdapter(context,data,R.layout.item_chapter,from,to);
                lvSimple.setAdapter(sAdapter);
                Log.d("mLog","Вывели");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void listLoad(){
        lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chapter_Stat.id = chapters.get(position).id;
                Chapter_Stat.name = chapters.get(position).name;
                Chapter_Stat.annotation = chapters.get(position).annotation;
                Chapter_Stat.book = chapters.get(position).book;
                Chapter_Stat.createdata = chapters.get(position).createdata;
                Chapter_Stat.editingdata = chapters.get(position).editingdata;
                Chapter_Stat.position = chapters.get(position).position;
                PostMan.lengChap = chapters.size();
                Log.d("mLog", " Размер массива"+ String.valueOf(PostMan.lengChap));
                fTrans= getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragCont,redactureChapter);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:{
                nam.setText(Book_Stat.name);
                anno.setText(Book_Stat.annotation);
                break;
            }
            case R.id.delete:{
                // Вы уверены?
                deleteDataFromBD();
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragCont,mainMenu);
                fTrans.commit();
                //удаление информации глав
                break;
            }
            case R.id.retur:{
                fTrans= getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragCont,mainMenu);
                fTrans.commit();
                break;
            }
            case R.id.change:{
                //вы уверены?
                updateDataFromBD();
                break;
            }
            case  R.id.addChapter: {
                //добавление главы
                PostMan.lengChap = chapters.size();
                Log.d("mLog", " Размер массива"+ String.valueOf(PostMan.lengChap));
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragCont, createChapter);
                fTrans.addToBackStack(null);
                fTrans.commit();
                break;
            }
        }
    }
}