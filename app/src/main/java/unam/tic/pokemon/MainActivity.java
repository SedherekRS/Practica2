package unam.tic.pokemon;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import unam.tic.pokemon.Modelo.Pokemon;

public class MainActivity extends AppCompatActivity {
    ByteArrayInputStream inputStream;
    ArrayList<Pokemon> datos = new ArrayList<>();
    ListView lv;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv=findViewById(R.id.lv);
        progressBar=findViewById(R.id.progressBar);

        if(savedInstanceState==null){
            new ConexionHttp().execute("");
        }
    }
    //Asincrono
    public class ConexionHttp extends AsyncTask<String, Float,String>{

        boolean sinError = true;

        @Override
        protected String doInBackground(String... strings) {



            try{
                StringBuilder result = new StringBuilder();
                String urlString = "https://pokeapi.co/api/v2/pokemon";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                JSONObject myresponse = new JSONObject(result.toString());
                JSONArray pokemons = new JSONArray();
                pokemons = myresponse.getJSONArray("results");

                for (int i = 0; i < pokemons.length(); ++i) {
                    JSONObject rec = pokemons.getJSONObject(i);
                    String nameP = rec.getString("name");
                    String urlP = rec.getString("url");
                    //System.out.println(nameP+"<->"+urlP);
                    Map<String,String> pokeData = new HashMap<>();
                    Pokemon pokeTemp = new Pokemon(nameP);
                    //////////////////////////
                    pokeData= getPokeInfo(urlP);
                    pokeTemp.setType1(pokeData.get("type1"));
                    pokeTemp.setType2(pokeData.get("type2"));
                    pokeTemp.setUrlPokemon(pokeData.get("sprite"));
                    /////////////////////////////

                    datos.add(pokeTemp);
                    // ...
                }


            }catch(Exception e){
                e.printStackTrace();
                sinError = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(sinError){
                progressBar.setVisibility(View.GONE);
                lv.setAdapter(new Adaptador(MainActivity.this,datos));
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("nombre",datos.get(position).getName());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });


            }else{
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Aviso")
                        .setMessage("Servicio no disponible en estos momentos.")
                        .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new ConexionHttp().execute("");
                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        new ConexionHttp().execute("");
                    }
                }).setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                        .show();
            }

        }
    }


    private HashMap<String,String> getPokeInfo(String urlPoke) throws IOException, JSONException {
        HashMap<String,String> pokeInfo = new HashMap<>();
        //////////////////
        pokeInfo.put("type1","");
        pokeInfo.put("type2","");
        //////////////////
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlPoke);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        JSONObject myresponse = new JSONObject(result.toString());
        JSONArray pokemonsType = new JSONArray();
        pokemonsType = myresponse.getJSONArray("types");
        JSONObject pokemonsSprites = new JSONObject(myresponse.getString("sprites"));
        String sprite = pokemonsSprites.getString("front_default");
        //System.out.println(sprite);
        pokeInfo.put("sprite",sprite);
        //String pokemonsSprites;
        //pokemonsSprites = myresponse.getString("sprites");

        //System.out.println(pokemonsSprites);
        //////////////////
        for (int i = 0; i < pokemonsType.length(); ++i) {
            JSONObject rec = pokemonsType.getJSONObject(i);
            JSONObject type = new JSONObject(rec.getString("type"));
            String typeName = type.getString("name");
            String slot = rec.getString("slot");
            //System.out.println(slot+"<->"+typeName);
            if(slot.equals("1")){
                pokeInfo.put("type1",typeName);
            }
            if(slot.equals("2")){
                pokeInfo.put("type2",typeName);
            }
        }
        ////////////////////
        return  pokeInfo;

    }
    private static String obtenValor(String tag, Element elemento) {
        NodeList listaNodos = elemento.getElementsByTagName(tag).item(0).getChildNodes();
        Node nodo = listaNodos.item(0);
        return nodo.getNodeValue();
    }

}
