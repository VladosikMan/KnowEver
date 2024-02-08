package ru.vladgad.knowever.ConstructBook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import ru.vladgad.knowever.Essence.Book;
import ru.vladgad.knowever.MainMenu;
import ru.vladgad.knowever.R;
import static android.app.Activity.RESULT_OK;

public class CreateBook extends Fragment implements View.OnClickListener{
    private static View v;
    private static Context context;
    private FragmentTransaction fTrans;
    private MainMenu mainMenu;
    private ImageView ima;
    private Button addIma,retur,back,ok;
    private EditText EditTextName, EditTextAnnot;
    private DatabaseReference bookRef;
    private String path;
    private Uri upload;
    private StorageReference mStorageRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.createbook,null);
        context= container.getContext();
        init();
        return v;
    }
    private void init() {
        mStorageRef = FirebaseStorage.getInstance().getReference("BookImage");
        path = "zero";
        bookRef = FirebaseDatabase.getInstance().getReference(getString(R.string.key_book));
        mainMenu= new MainMenu();
        EditTextName=(EditText)v.findViewById(R.id.EditTextName);
        EditTextAnnot=(EditText)v.findViewById(R.id.EditTextAnnot);
        ima = (ImageView)v.findViewById(R.id.addImage);
        addIma=(Button)v.findViewById(R.id.addbutt);
        retur=(Button)v.findViewById(R.id.retur);
        back=(Button)v.findViewById(R.id.back);
        ok = (Button)v.findViewById(R.id.ok);
        addIma.setOnClickListener(this);
        retur.setOnClickListener(this);
        back.setOnClickListener(this);
        ok.setOnClickListener(this);
    }
    private boolean zeroView(){
        if(EditTextName.getText().toString().isEmpty()||EditTextAnnot.getText().toString().isEmpty()){
            return false;
        }else {
            return true;
        }
    }
    private void uploadImage(){
        if(!zeroView()){
            Toast.makeText(context,"Не введено имя или аннотация", Toast.LENGTH_SHORT);
        }else{
            if(ima.getDrawable()==null){
                    String id = bookRef.push().getKey();
                    String name = EditTextName.getText().toString();
                    String annot = EditTextAnnot.getText().toString();
                    Long time = System.currentTimeMillis();
                    String create = String.valueOf(time);
                    String edit = create;
                    Book newBook = new Book(id,name,annot,path,create,edit);
                    if(id!=null)  bookRef.child(id).setValue(newBook);
                }
            else{
                Bitmap bitmap = ((BitmapDrawable)ima.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50,baos);
                byte [] byteArray = baos.toByteArray() ;
                final StorageReference mRef = mStorageRef.child(System.currentTimeMillis()+" "+ EditTextName.getText().toString());
                UploadTask up = mRef.putBytes(byteArray);
                Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return mRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(!zeroView()){
                            Toast.makeText(context,"Не введено имя или аннотация", Toast.LENGTH_SHORT);
                        }else{
                            String id = bookRef.push().getKey();
                            String name = EditTextName.getText().toString();
                            String annot = EditTextAnnot.getText().toString();
                            Long time = System.currentTimeMillis();
                            String create = String.valueOf(time);
                            String edit = create;
                            upload = task.getResult();
                            path = String.valueOf(upload);
                            Log.d("mLog","Хули хуюли "+ path);
                            Book newBook = new Book(id,name,annot,path,create,edit);
                            if(id!=null)  bookRef.child(id).setValue(newBook);
                        }
                    }
                });
            }
            fTrans= getFragmentManager().beginTransaction();
            fTrans.replace(R.id.FragCont,mainMenu);
            fTrans.commit();
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
                ima.setImageURI(data.getData());
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //слушатель нажатий
            case R.id.addbutt:{
                getImage();
                break;
            }
            case R.id.retur:{
                fTrans=getFragmentManager().beginTransaction();
                fTrans.replace(R.id.FragCont,mainMenu);
                fTrans.commit();
                break;
            }
            case R.id.back:{
                EditTextAnnot.setText("");
                EditTextName.setText("");
                ima.setImageURI(null);
                break;
            }
            case R.id.ok:{
                uploadImage();
                break;
            }
        }
    }
}