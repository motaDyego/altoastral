package br.ufla.altoastral;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import br.ufla.altoastral.R;


public class login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView texto = (TextView) findViewById(R.id.textoEsqueciSenha);
        texto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                funcaoNaoImplementada();

            }
        });
    }


  //  @Override
  //public boolean onCreateOptionsMenu(Menu menu) {
     // Inflate the menu; this adds items to the action bar if it is present.
    //  getMenuInflater().inflate(R.menu.login, menu);
      //return true;
  //}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickEntrar(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onClickCadastrar(View view){
        startActivity(new Intent(this, CadastroUsuario.class));
    }

    public void funcaoNaoImplementada() {
        // Context context = getApplicationContext();
        CharSequence text = "Função não implementada!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getBaseContext(), text, duration);
        toast.show();
    }
}
