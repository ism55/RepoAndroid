package com.example.myapplication3;




import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE=42;
    private static final int PERMISSION_REQUEST_STORAGE=1000;


    TextView probar;
    TextView etiqueta;
    Button getFile;
    EditText cuadro;
    EditText repetir;
    Button getButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getButton=(Button)findViewById(R.id.button);
        etiqueta=(TextView)findViewById(R.id.txtoutput);
        cuadro=(EditText)findViewById(R.id.txtinput1);

        //final String[] response = {""};
        final String url="http://192.168.4.1:8266";
//        ClassConnection connection = new ClassConnection();
//        try {
//            response[0] = connection.execute(url).get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        etiqueta.setText("Status: "+response[0]);
        getButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                //ClassConnection connection = new ClassConnection();

//                StringBuilder query = new StringBuilder(url);
//                query.append("?");
//                ////query.append("85");
                repetir =(EditText)findViewById(R.id.repid);
                cuadro=(EditText)findViewById(R.id.txtinput1);

                String valor_rep= repetir.getText().toString();
                for (int i=0; i<Integer.parseInt(valor_rep)+1;i++){

                    String valor=cuadro.getText().toString();
                    Scanner scanner=new Scanner(valor);


                    while(scanner.hasNextLine()){
                        String line=scanner.nextLine();
                        String[] arreglo = line.split(" ");

                        comparar(arreglo[0],arreglo[1],url);



                    }
                }


                //Toast.makeText(getApplicationContext(), "Multiline: "+valor, Toast.LENGTH_LONG).show();
                //String valor = cuadro1.getText().toString().toLowerCase();
                //query.append(valor);




//                try {
//                    response[0] = connection.execute(query.toString()).get();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                //etiqueta.setText("Status: "+response[0]);
            }
        });

        //PERMISOS
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_STORAGE);
        }

        getFile =(Button)findViewById(R.id.cargar);

        getFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performFileSearch();
            }
        });

    }


    //LEER ARCHIVO

    protected StringBuilder readText(String input){


        StringBuilder text = new StringBuilder();
        try{
            File file=new File(Environment.getExternalStorageDirectory(), input);

            FileInputStream fis = new FileInputStream(file);

            if(fis != null){
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                String line=null;
                while((line =br.readLine())!=null){
                    text.append(line);
                    text.append("\n");


                }
                fis.close();

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    //SELECCIONAR ARCHIVO

    private void performFileSearch(){
        Intent  intent =new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent,READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==READ_REQUEST_CODE && resultCode== Activity.RESULT_OK){
            if(data!= null){
                Uri uri =data.getData();
                String path=uri.getPath();
                path = path.substring(path.indexOf(":")+1);
                Toast.makeText(this,""+path,Toast.LENGTH_SHORT).show();
                cuadro.setText(readText(path));
                //cuadro.setText("Hola mundo");

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        if(requestCode==PERMISSION_REQUEST_STORAGE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permiso concedido!",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Permiso no concedido", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void comparar(String s0, String s1,String url){
        ClassConnection connection = new ClassConnection();
        StringBuilder query = new StringBuilder(url);
        query.append("?");

        final String[] response = {""};
        switch (s0){
            case "q1":
                query.append("num1");
                query.append("=");
                if(Float.parseFloat(s1)>135){query.append("135");}
                if(Float.parseFloat(s1)<-135){query.append("-135");}
                else{query.append(s1);}

                try {
                    response[0] = connection.execute(query.toString()).get();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;

            case "q2":
                query.append("num2");
                query.append("=");
                if(Float.parseFloat(s1)>135){query.append("135");}
                if(Float.parseFloat(s1)<-135){query.append("-135");}
                else{query.append(s1);}

                try {
                    response[0] = connection.execute(query.toString()).get();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;

            case "q3":
                query.append("num3");
                query.append("=");
                if(Float.parseFloat(s1)>135){query.append("135");}
                if(Float.parseFloat(s1)<-135){query.append("-135");}
                else{query.append(s1);}

                try {
                    response[0] = connection.execute(query.toString()).get();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;

            case "q4":
                query.append("num4");
                query.append("=");
                if(Float.parseFloat(s1)>60){query.append("60");}
                if(Float.parseFloat(s1)<-60){query.append("-60");}
                else{query.append(s1);}
                try {
                    response[0] = connection.execute(query.toString()).get();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;

            case "q5":
                query.append("num5");
                query.append("=");
                if(Float.parseFloat(s1)>60){query.append("60");}
                if(Float.parseFloat(s1)<-60){query.append("-60");}
                else{query.append(s1);}
                try {
                    response[0] = connection.execute(query.toString()).get();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;

            case "q6":
                query.append("num6");
                query.append("=");
                if(Float.parseFloat(s1)>60){query.append("60");}
                if(Float.parseFloat(s1)<-60){query.append("-60");}
                else{query.append(s1);}
                try {
                    response[0] = connection.execute(query.toString()).get();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;

            case "grip":
                if(s1.equals("on")){
                    query.append("gripon");

                }
                if(s1.equals("off")){
                    query.append("gripoff");
                }


                try {
                    response[0] = connection.execute(query.toString()).get();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;

            case "wait":

                try {
                    Thread.sleep(Integer.parseInt(s1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }



}

