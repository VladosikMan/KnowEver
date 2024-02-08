package ru.vladgad.knowever.ConstructMaterial;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ru.vladgad.knowever.Blocks.CreateImage;
import ru.vladgad.knowever.Blocks.Foot;
import ru.vladgad.knowever.Blocks.Header;
import ru.vladgad.knowever.Blocks.Link;
import ru.vladgad.knowever.Blocks.NumEnum;
import ru.vladgad.knowever.Blocks.Quen;
import ru.vladgad.knowever.Blocks.SimpleEnum;
import ru.vladgad.knowever.Blocks.SimpleText;
import ru.vladgad.knowever.Blocks.Video;
import ru.vladgad.knowever.Essence.PostMan;
import ru.vladgad.knowever.R;

public class CreateBlock extends Fragment implements View.OnClickListener {
    private View v;
    private Context context;
    private FragmentTransaction fTrans;
    private ImageView simpText,bigHead,midHead,simpleHead,ima,foot,link,quen,simpleEnum,numEnum,video;
    private SimpleText simpleText;
    private Header header;
    private CreateImage createImage;
    private Foot footer;
    private Link linker;
    private NumEnum numEnumer;
    private Quen quener;
    private SimpleEnum simpleEnumer;
    private Video videoer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.createblock,null);
        context = container.getContext();
        init();
        Log.d("mLog","Размерчик - "+ PostMan.lengBlock);
        return v;
    }
    private void init() {
        videoer = new Video();
        simpleEnumer = new SimpleEnum();
        quener = new Quen();
        numEnumer = new NumEnum();
        linker = new Link();
        footer =new Foot();
        createImage = new CreateImage();
        header = new Header();
        simpleText = new SimpleText();
        simpText = v.findViewById(R.id.simpleText);
        bigHead = v.findViewById(R.id.bigHead);
        midHead = v.findViewById(R.id.midHead);
        simpleHead = v.findViewById(R.id.simpleHead);
        ima = v.findViewById(R.id.ima);
        foot = v.findViewById(R.id.foot);
        link = v.findViewById(R.id.link);
        quen = v.findViewById(R.id.quen);
        simpleEnum = v.findViewById(R.id.simpleEnum);
        numEnum = v.findViewById(R.id.numEnum);
        video = v.findViewById(R.id.video);
        simpText.setOnClickListener(this);
        bigHead.setOnClickListener(this);
        midHead.setOnClickListener(this);
        simpleHead.setOnClickListener(this);
        ima.setOnClickListener(this);
        foot.setOnClickListener(this);
        link.setOnClickListener(this);
        quen.setOnClickListener(this);
        simpleEnum.setOnClickListener(this);
        numEnum.setOnClickListener(this);
        video.setOnClickListener(this);
        fTrans=getFragmentManager().beginTransaction();
        fTrans.replace(R.id.FragBlock,simpleText);
        fTrans.commit();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.simpleText:{
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragBlock,simpleText);
                fTrans.commit();
                break;
            }
            case R.id.bigHead:{
                PostMan.caseHeader =3;
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragBlock,header);
                fTrans.commit();
                break;
            }
            case R.id.midHead:{
                PostMan.caseHeader =2;
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragBlock,header);
                fTrans.commit();
                break;
            }
            case R.id.simpleHead:{
                PostMan.caseHeader =1;
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragBlock,header);
                fTrans.commit();
                break;
            }
            case R.id.ima:{
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragBlock,createImage);
                fTrans.commit();
                break;
            }
            case R.id.foot:{
                Log.d("mLog","ЧТо такое Ftrna");
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragBlock,footer);
                fTrans.commit();
                break;
            }
            case R.id.link:{
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragBlock,linker);
                fTrans.commit();
                break;
            }
            case R.id.quen:{
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragBlock,quener);
                fTrans.commit();
                break;
            }
            case R.id.simpleEnum:{
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragBlock,simpleEnumer);
                fTrans.commit();
                break;
            }
            case R.id.numEnum:{
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragBlock,numEnumer);
                fTrans.commit();
                break;
            }
            case R.id.video:{
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragBlock,videoer);
                fTrans.commit();
                break;
            }
        }
    }
}
