package com.followme;

import com.followme.BD.UsuarioDA;
import com.followme.adapter.CustomListAdapter;
import com.followme.library.HttpConnection;
import com.followme.model.Grupo;
import com.followme.model.Usuario;
 
import java.util.ArrayList;
import java.util.List;
 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
 
public class MainActivity extends Activity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
 
    // Grupos json url
    private ProgressDialog pDialog;
    private List<Grupo> grupoList = new ArrayList<Grupo>();
    private ListView listView;
    private CustomListAdapter adapter;
    
    //Data base
    private UsuarioDA bd;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // verifica login
		bd = new UsuarioDA(getApplicationContext());
		bd.open();
		if (bd.getUsuario() == null) {
			bd.close();
			Intent itLogin = new Intent(this, LoginActivity.class);
			startActivity(itLogin);
			finish();
		}
		else{ 
			int id_logado = bd.getUsuario().getId();
			bd.close();
	        
	        listView = (ListView) findViewById(R.id.list);
	        adapter = new CustomListAdapter(this, grupoList);
	        listView.setAdapter(adapter);
	 
	        pDialog = new ProgressDialog(this);
	        // Showing progress dialog before making http request
	        pDialog.setMessage("Carregando grupos...");
	        pDialog.show();
	 
	        // changing action bar color
	        getActionBar().setBackgroundDrawable(
	                new ColorDrawable(Color.parseColor("#1b1b1b")));
	 
	        String json = generateSendJSON(id_logado);
	        new GetGruposAsyncTask().execute(json);
	        
	        //click listener
	        listView.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {

	            	TextView id_grupo = (TextView) view.findViewById(R.id.id_grupo);
	            	TextView nome = (TextView) view.findViewById(R.id.nome_grupo);

	            	Intent it = new Intent(MainActivity.this, MapaActivity.class);
	    			it.putExtra("id_grupo", id_grupo.getText().toString());
	    			it.putExtra("nome_grupo", nome.getText().toString());
	    			startActivity(it);
	            }
	        });
		}
    }
    
    private class GetGruposAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String api = getResources().getString(R.string.api_url);
			String url = api + "grupo/get";
			Log.e(TAG, url);
			return HttpConnection.getSetDataWeb(url, "send-json", params[0]);
		}

		protected void onPostExecute(String result) {
			Log.e(TAG, result);
            hidePDialog();
            
            try {
	            JSONArray jArray = new JSONArray(result);
	
	            // Parsing json
	            for (int i = 0; i < jArray.length(); i++) {
	                JSONObject obj = jArray.getJSONObject(i);
	                Usuario admin = new Usuario();
	                admin.setId(obj.getInt("admin"));
	                admin.setEmail(obj.getString("email"));
	                
	                Grupo grupo = new Grupo(obj.getInt("id_grupo"), obj.getString("nome_grupo"), obj.getString("descricao"), obj.getString("foto_patch"), admin);
	
	                // adding grupo to grupos array
	                grupoList.add(grupo);
	            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // notifying list adapter about data changes
            // so that it renders the list view with updated data
            adapter.notifyDataSetChanged();
		}
	}
 
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }
 
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.main_logoff:
				logoff();
				break;
		}
		return true;
	}
    
    private void logoff(){
    	bd.open();
		bd.logoffUsuario();
		bd.close();

		Intent itLogoff = new Intent(this, LoginActivity.class);
		startActivity(itLogoff);
    }
    
    private String generateSendJSON(int id) {
		JSONObject jo = new JSONObject();
		String chave = getResources().getString(R.string.api_key);
		try {
			jo.put("api_key", chave);
			jo.put("id_usuario", id);

		} catch (JSONException e1) {
			Log.e("Script", "erro Json");
		}
		return jo.toString();
	}
}