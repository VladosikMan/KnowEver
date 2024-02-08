package ru.vladgad.knowever.ConstructMaterial;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import ru.vladgad.knowever.Constants;
import ru.vladgad.knowever.ConstructBook.RedactureBook;
import ru.vladgad.knowever.Essence.Block;
import ru.vladgad.knowever.Essence.Book_Stat;
import ru.vladgad.knowever.Essence.Chapter;
import ru.vladgad.knowever.Essence.Chapter_Stat;
import ru.vladgad.knowever.Essence.PostMan;
import ru.vladgad.knowever.MyCallback;
import ru.vladgad.knowever.R;
import ru.vladgad.knowever.TesterSystem.CreateTest;

public class MainConstruct extends Fragment implements View.OnClickListener{
    class Compar implements Comparator<Block> {
        @Override
        public int compare(Block o1, Block o2) {
            return Integer.compare(o1.position,o2.position);
        }
    }
    private Context context;
    private View v;
    private FragmentTransaction fTrans;
    private CreateBlock createBlock;
    private CreateTest createTest;
    private Button test,block;
    private ArrayList<Block> blocks;
    private DatabaseReference blockRef;
    private LinearLayout materialBase;
    private DatabaseReference chapterRef;
    private final  int USERID = 6000;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { v = inflater.inflate(R.layout.mainconstruct,null);
        v = inflater.inflate(R.layout.mainconstruct,null);
        context = container.getContext();
        Log.d("mLog","Да бля"+ Chapter_Stat.id);
        init();
        blocks.clear();
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
        sortBlock();
        Log.d("mLog","ТУпикачака");
        return v;
    }
    private  void createView(){
        int i =0;

        if(blocks.size()>0){
            //подготовить linearlayot

            for(i=0;i<blocks.size();i++){
                // выбор элемента
                switch (blocks.get(i).type){
                    case 1:{
                        //Простой тектс
                        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        lParams.topMargin = 30;
                        TextView text  = new TextView(context);
                        text.setText("Просто текст - "+blocks.get(i).value);
                        text.setId(USERID +1);
                        text.setOnClickListener(this);
                        materialBase.addView(text,lParams);
                        break;
                    }
                    case 2:{
                        //заголовок
                        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        lParams.topMargin = 30;
                        TextView head = new TextView(context);
                        head.setText("Заголовок 1 - "+blocks.get(i).value);
                        int typeface = Typeface.NORMAL;
                        typeface += Typeface.BOLD;
                        head.setTextColor(Color.BLACK);
                        head.setTextSize(24);
                        head.setId(USERID+2);
                        head.setOnClickListener(this);
                        materialBase.addView(head,lParams);
                        break;
                    }
                    case 3:{
                        //заголовок
                        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        lParams.topMargin = 30;
                        TextView head = new TextView(context);
                        head.setText("Заголовок 2 - "+blocks.get(i).value);
                        int typeface = Typeface.NORMAL;
                        typeface += Typeface.BOLD;
                        head.setTextColor(Color.BLACK);
                        head.setTextSize(18);
                        head.setId(USERID+3);
                        head.setOnClickListener(this);
                        materialBase.addView(head,lParams);
                        break;
                    }
                    case 4:{
                        //заголовок
                        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        lParams.topMargin = 30;
                        TextView head = new TextView(context);
                        head.setText("Заголовок 3 - "+blocks.get(i).value);
                        int typeface = Typeface.NORMAL;
                        typeface += Typeface.BOLD;
                        head.setTextColor(Color.BLACK);
                        head.setTextSize(14);
                        head.setId(USERID+4);
                        head.setOnClickListener(this);
                        materialBase.addView(head,lParams);
                        break;
                    }
                    case 5:{
                        //картинка
                        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,300);
                        lParams.topMargin = 30;
                        ImageView image = new ImageView(context);
                        materialBase.addView(image,lParams);
                        image.setId(USERID+5);
                        image.setOnClickListener(this);
                        Picasso.get().load(blocks.get(i).value).into(image);
                        break;
                    }
                    case 6:{
                        //ссылочка
                        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        lParams.topMargin = 30;
                        TextView foot = new TextView(context);
                        String str = blocks.get(i).value;
                        String s1 = "";
                        String s2="";;
                        int index = str.indexOf("Перенос91");
                        s1 = str.substring(0,index);
                        s2 =str.substring(index+9,str.length());
                        foot.setText("Cсылка "+s1);
                        foot.setTextColor(Color.BLUE);
                        foot.setId(USERID+6);
                        foot.setOnClickListener(this);
                        materialBase.addView(foot,lParams);
                    }
                    case 7:{
                        //вопрос
                        Log.d("mLog","Вопрос");
                        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        lParams.topMargin = 30;
                        TextView quen = new TextView(context);
                        String str = blocks.get(i).value;
                        String s1 = "";
                        String s2="";;
                        int index = str.indexOf("Перенос91");
                        s1 = str.substring(0,index);
                        s2 =str.substring(index+9,str.length());
                        quen.setText("Вопрос - "+s1);
                        quen.setTextColor(Color.RED);
                        quen.setId(USERID+7);
                        quen.setOnClickListener(this);
                        materialBase.addView(quen,lParams);
                        break;
                    }
                }
            }
        }
    }
    private void init() {
        blockRef = FirebaseDatabase.getInstance().getReference(getString(R.string.key_block));
        chapterRef = FirebaseDatabase.getInstance().getReference(getString(R.string.key_block));
        materialBase = v.findViewById(R.id.materialbase);
        blocks =  new ArrayList<Block>();
        test = v.findViewById(R.id.test);
        test.setOnClickListener(this);
        createBlock = new CreateBlock();
        createTest = new CreateTest();
        block = v.findViewById(R.id.block);
        block.setOnClickListener(this);
    }
    private void sortBlock(){
        Collections.sort(blocks,new Compar());
    }
    private void  getDataFromBD(MyCallback myCallback){
        Query phoneQuery = blockRef.orderByChild("chapter").equalTo(Chapter_Stat.id);
        phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    Block block = ds.getValue(Block.class);
                    blocks.add(block);
                   // Log.d("mLog",block.value);
                }
                Collections.sort(blocks,new Compar());
                createView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test:{
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragCont, createTest);
                fTrans.addToBackStack(null);
                fTrans.commit();
                break;
            }
            case R.id.block:{
                PostMan.lengBlock = blocks.size();
                Log.d("mLog",PostMan.lengBlock+" размер ");
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragCont, createBlock);
                fTrans.addToBackStack(null);
                fTrans.commit();
                break;
            }
            case USERID+1:{
                Toast.makeText(context,"Понасенков",Toast.LENGTH_SHORT).show();
                break;
            }
            case USERID+2:{
                Toast.makeText(context,"Невзоров",Toast.LENGTH_SHORT).show();
                break;
            }
            case USERID+3:{
                Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
                break;
            }
            case USERID+4:{
                Toast.makeText(context,"Понасенков",Toast.LENGTH_SHORT).show();
                break;
            }
            case USERID+5:{
                Toast.makeText(context,"Шевцов",Toast.LENGTH_SHORT).show();
                break;
            }
            case USERID+6:{
                Toast.makeText(context,"Гурием",Toast.LENGTH_SHORT).show();
                break;
            }
            case USERID+7:{
                Toast.makeText(context,"Баженов",Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}