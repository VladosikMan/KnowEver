package ru.vladgad.knowever.Blocks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import ru.vladgad.knowever.ConstructMaterial.MainConstruct;
import ru.vladgad.knowever.Essence.Block;
import ru.vladgad.knowever.Essence.Book;
import ru.vladgad.knowever.Essence.Chapter_Stat;
import ru.vladgad.knowever.R;

import static android.app.Activity.RESULT_OK;
public class CreateImage extends Fragment implements View.OnClickListener {
    private View v;
    private Context context;
    private FragmentTransaction fTrans;
    private Button cancel,clear,create,addIma;
    private MainConstruct mainConstruct;
    private ImageView image;
    private StorageReference mStorageRef;
    private DatabaseReference blockRef;
    private Uri upload;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.createimage,null);
        context= container.getContext();
        init();
        return v;
    }
    private void uploadImage(){
        if(image.getDrawable()==null){
            Toast.makeText(context,"Не выбрали картинку", Toast.LENGTH_SHORT);
        }else{
            Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50,baos);
            byte [] byteArray = baos.toByteArray() ;
            final StorageReference mRef = mStorageRef.child(String.valueOf(System.currentTimeMillis()));
            UploadTask up = mRef.putBytes(byteArray);
            Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return mRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                        String id = blockRef.push().getKey();
                        int pos =3;
                        String chapter = Chapter_Stat.id;
                        upload = task.getResult();
                        String path = String.valueOf(upload);
                        int type = 5;
                        Block block = new Block(id,chapter,path,type,pos);
                        if(id!=null)  blockRef.child(id).setValue(block);
                }
            });
        }
    }
    private void getImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && data!=null && data.getData()!=null){
            if(resultCode==RESULT_OK){
                image.setImageURI(data.getData());
            }
        }
    }
    private void init(){
        mStorageRef =FirebaseStorage.getInstance().getReference("BlockImage");
        blockRef = FirebaseDatabase.getInstance().getReference(getString(R.string.key_block));
        image = v.findViewById(R.id.image);
        mainConstruct = new MainConstruct();
        cancel = v.findViewById(R.id.cancel);
        clear = v.findViewById(R.id.clear);
        create = v.findViewById(R.id.create);
        addIma = v.findViewById(R.id.addIma);
        cancel.setOnClickListener(this);
        clear.setOnClickListener(this);
        create.setOnClickListener(this);
        addIma.setOnClickListener(this);
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
                image.setImageURI(null);
                break;
            }
            case R.id.create:{
                uploadImage();
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragCont,mainConstruct);
                fTrans.commit();
                break;
            }
            case R.id.addIma:{
                getImage();
                break;
            }
        }
    }
}