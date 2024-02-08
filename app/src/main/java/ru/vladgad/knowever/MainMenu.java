package ru.vladgad.knowever;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.vladgad.knowever.ConstructBook.CreateBook;
import ru.vladgad.knowever.ConstructBook.RedactureBook;
import ru.vladgad.knowever.Essence.Book;
import ru.vladgad.knowever.Essence.Book_Stat;

public class MainMenu extends Fragment implements View.OnClickListener {
    private static Context context;
    private Button addBook;
    private  static ListView lvSimple;
    private SimpleAdapter sAdapter;
    private FragmentTransaction fTrans;
    private static View v;
    private CreateBook createBook;
    private DatabaseReference myRef;
    private ArrayList<Book>books;
    private RedactureBook redactureBook;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.mainmenu,null);
        context = container.getContext();
        init();
        books.clear();
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
        redactureBook = new RedactureBook();
        createBook = new CreateBook();
        addBook = v.findViewById(R.id.addBook);
        addBook.setOnClickListener(this);
        lvSimple = v.findViewById(R.id.lvSimple);
        myRef = FirebaseDatabase.getInstance().getReference(getString(R.string.key_book));
        books = new ArrayList<Book>();
    }
    private void listLoad() {
        lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book_Stat.id = books.get(position).id;
                Book_Stat.name = books.get(position).name;
                Book_Stat.annotation = books.get(position).annotation;
                Book_Stat.image = books.get(position).image;
                Book_Stat.createdata = books.get(position).createdata;
                Book_Stat.editingdate = books.get(position).editingdate;
                fTrans= getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragCont,redactureBook);
                 fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });
    }
    private void getDataFromBD(MyCallback myCallback){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    Book book= ds.getValue(Book.class);
                    books.add(book);
                }
                int i;
                ArrayList<Map<String,Object>>data = new ArrayList<Map<String, Object>>();
                Map<String,Object> m;
                for(i=0;i<books.size();i++){
                    m = new HashMap<String, Object>();
                    m.put(Constants.BOOK_ATTRIBUTE_NAME_ID,books.get(i).id);
                    m.put(Constants.BOOK_ATTRIBUTE_NAME_NAME,books.get(i).name);
                    m.put(Constants.BOOK_ATTRIBUTE_NAME_ANNOTATION,books.get(i).annotation);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    String s = sdf.format(Long.parseLong(books.get(i).createdata));
                    m.put(Constants.BOOK_ATTRIBUTE_NAME_CREATEDATA,s);
                    s = sdf.format(Long.parseLong(books.get(i).editingdate));
                    m.put(Constants.BOOK_ATTRIBUTE_NAME_EDITDATA,s);
                    data.add(m);
                }
                String[] from = {Constants.BOOK_ATTRIBUTE_NAME_NAME,Constants.BOOK_ATTRIBUTE_NAME_ANNOTATION,Constants.BOOK_ATTRIBUTE_NAME_CREATEDATA,Constants.BOOK_ATTRIBUTE_NAME_EDITDATA};
                int [] to={R.id.Name,R.id.Annot,R.id.CreateData,R.id.EditData};
                SimpleAdapter sAdapter = new SimpleAdapter(context,data,R.layout.item_book,from,to);
                lvSimple.setAdapter(sAdapter);
                Log.d("mLog","Вывели");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        myRef.addValueEventListener(vListener);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addBook:{
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragCont,createBook);
                fTrans.addToBackStack(null);
                fTrans.commit();
                break;
            }
        }
    }
}