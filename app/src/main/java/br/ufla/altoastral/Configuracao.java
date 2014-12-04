package br.ufla.altoastral;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import br.ufla.altoastral.R;

public class Configuracao extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        SharedPreferences preference = getSharedPreferences("prefConfig",MODE_PRIVATE);
        EditText ip = (EditText) findViewById(R.id.etIP);
        EditText porta = (EditText) findViewById(R.id.etPorta);
        ip.setText(preference.getString("ip", ""));
        porta.setText(preference.getString("porta", ""));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuracao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void salvarConfiguracao(View v){
        /* Salvamos as configurações definidas pelo usuário */
        SharedPreferences preference = getSharedPreferences("prefConfig",MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        EditText ip = (EditText) findViewById(R.id.etIP);
        editor.putString("ip", ip.getText().toString());
        EditText porta = (EditText) findViewById(R.id.etPorta);
        editor.putString("porta", porta.getText().toString());
        editor.commit();

        finish();
    }
}
