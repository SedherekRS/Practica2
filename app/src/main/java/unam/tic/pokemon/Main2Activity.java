package unam.tic.pokemon;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Main2Activity extends AppCompatActivity {
    ProgressBar progressBar2;
    TextView tvPokeName2;
    TextView tvPokePeso;

    String name;
    String peso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tvPokeName2=findViewById(R.id.tvPokeName2);
        tvPokePeso=findViewById(R.id.tvPokePeso);
        progressBar2 = findViewById(R.id.progressBar2);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("nombre", "Pokemon");

        if(savedInstanceState==null){
            new ConexionHttp().execute("");
        }
    }
    public class ConexionHttp extends AsyncTask<String, Float, String> {

        boolean sinError = true;


        @Override
        protected String doInBackground(String... strings) {
            try

            {

                StringBuilder result = new StringBuilder();
                URL sourceURL = new URL("https://pokeapi.co/api/v2/pokemon/" + name);
                /////////////////
                HttpURLConnection conn = (HttpURLConnection) sourceURL.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                JSONObject myresponse = new JSONObject(result.toString());

                peso = myresponse.getString("weight");

                /////////////////
            }catch(
                    Exception e)

            {
                e.printStackTrace();
                sinError = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar2.setVisibility(View.GONE);
            if(sinError)

            {
                //ivImagen.setImageBitmap(imagen);
                tvPokeName2.setText(name);
                tvPokePeso.setText(peso);
            }else

            {
                new AlertDialog.Builder(Main2Activity.this)
                        .setTitle("Aviso")
                        .setMessage("Servicio no disponible en estos momentos o no existe imagen para el elemento seleccionado")
                        .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new ConexionHttp().execute("");
                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        new ConexionHttp().execute("");
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                        .show();
            }

        }

    }
}
