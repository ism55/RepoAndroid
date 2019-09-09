package com.example.myapplication3;




import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.io.FileOutputStream;
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
    Button guarda_archivo;
    EditText archivoNombre;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getButton=(Button)findViewById(R.id.button);
        etiqueta=(TextView)findViewById(R.id.txtoutput);
        cuadro=(EditText)findViewById(R.id.txtinput1);
        guarda_archivo=(Button)findViewById(R.id.guardar);
        archivoNombre = (EditText)findViewById(R.id.archivoname);
        //final String[] response = {""};
        final String url="http://192.168.4.1:8266";


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }


//        ClassConnection connection = new ClassConnection();
//        try {
//            response[0] = connection.execute(url).get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        etiqueta.setText("Status: "+response[0]);

        guarda_archivo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String nombrearchivo=archivoNombre.getText().toString();
                String contenido=cuadro.getText().toString();
                if(!contenido.equals("")    &&  !nombrearchivo.equals("")){

                    saveTextAsFile(nombrearchivo,contenido);

                } else {
                    Toast.makeText(MainActivity.this, "No deje espacios en blanco", Toast.LENGTH_SHORT).show();
                }
            }
        }


        );

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
                        if(!line.isEmpty()){
                            String[] arreglo = line.split(" ");
                            comparar(arreglo[0],arreglo[1],url);
                        }





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


    //GUARDAR ARCHIVO

    private void saveTextAsFile(String archivo, String content){
        String fileName= archivo + ".txt";

        //CREAR
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),fileName);

        ///ESCRIBIR


        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();

            Toast.makeText(this, "Guardado",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Archivo no encontrado",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error guardando",Toast.LENGTH_SHORT).show();
        }

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
                Toast.makeText(this,"¡Permiso concedido!",Toast.LENGTH_SHORT).show();
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
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("num1");
                query.append("=");


                if(Float.parseFloat(s1)>135){query.append("135");}
                if(Float.parseFloat(s1)<-135){query.append("-135");}
                else{query.append(s1);}

                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;

            case "q2":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("num2");
                query.append("=");
                if(Float.parseFloat(s1)>135){query.append("135");}
                if(Float.parseFloat(s1)<-135){query.append("-135");}
                else{query.append(s1);}

                try {
                    response[0] = connection.execute(query.toString()).get();
                    AsyncTask<String, String, String> test=connection.execute(query.toString());
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;

            case "q3":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("num3");
                query.append("=");
                if(Float.parseFloat(s1)>135){query.append("135");}
                if(Float.parseFloat(s1)<-135){query.append("-135");}
                else{query.append(s1);}

                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;

            case "q4":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("num4");
                query.append("=");
                if(Float.parseFloat(s1)>60){query.append("60");}
                if(Float.parseFloat(s1)<-60){query.append("-60");}
                else{query.append(s1);}
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;

            case "q5":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("num5");
                query.append("=");
                if(Float.parseFloat(s1)>60){query.append("60");}
                if(Float.parseFloat(s1)<-60){query.append("-60");}
                else{query.append(s1);}
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;

            case "q6":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("num6");
                query.append("=");
                if(Float.parseFloat(s1)>60){query.append("60");}
                if(Float.parseFloat(s1)<-60){query.append("-60");}
                else{query.append(s1);}
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

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
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;

            case "wait":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }

                //Thread.sleep(Integer.parseInt(s1));
                long antes = System.currentTimeMillis();
                long despues = System.currentTimeMillis();
                long resultado=0;
                while(resultado < Integer.parseInt(s1)){
                    despues = System.currentTimeMillis();
                    resultado=despues-antes;
                }

                break;
            case "pr1":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("PR1");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "kp1":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Kp1");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "ki1":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Ki1");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "kd1":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Kd1");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "kp2":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Kp2");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "ki2":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Ki2");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "kd2":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Kd2");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "kp3":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Kp3");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "ki3":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Ki3");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "kd3":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Kd3");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "kpro1":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Kpro1");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "kpro2":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Kpro2");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "kpro3":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Kpro3");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "koffset1":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Koffset1");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "koffset2":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Koffset2");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "koffset3":
                try {
                    Float.parseFloat(s1);
                }
                catch (NumberFormatException e) {
                    break;
                }
                query.append("Koffset3");
                query.append("=");
                query.append(s1);
                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "puente1":
                if(s1.equals("on")){
                    query.append("=ON");

                }
                if(s1.equals("off")){
                    query.append("=OFF");
                }


                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "puente2":
                if(s1.equals("on")){
                    query.append("=ON");

                }
                if(s1.equals("off")){
                    query.append("=OFF");
                }


                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;
            case "puente3":
                if(s1.equals("on")){
                    query.append("=ON");

                }
                if(s1.equals("off")){
                    query.append("=OFF");
                }


                try {
                    response[0] = connection.execute(query.toString()).get();
                    if (!response[0].equals("200")){
                        response[0]="404";
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                etiqueta.setText("Status: "+response[0]);
                break;



            default:
                etiqueta.setText("Status: "+ "404");
                break;
        }

    }



}

