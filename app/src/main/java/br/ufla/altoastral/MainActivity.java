package br.ufla.altoastral;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.ufla.altoastral.R;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView texto = (TextView) findViewById(R.id.tvPerfil);
        texto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editarConta(v);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onClickLogo (View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onClickTriste(View view) {
        //tipoDeConselho(view, "Eu estou triste, pois ");
        funcaoNaoImplementada();
    }

    public void onClickFeliz(View view) {
        //tipoDeConselho(view, "Eu estou feliz, pois ");
        funcaoNaoImplementada();
    }

    public void onClickPensativo(View view) {
        //tipoDeConselho(view, "Eu estou com dúvida, pois ");
        funcaoNaoImplementada();
    }

    public void tipoDeConselho(View view, String msg) {
        Intent intent = new Intent("com.ihc.PEDIRCONSELHOACTIVITY");
        Bundle params = new Bundle();

        params.putString("mensagem", msg);
        intent.putExtras(params);
        startActivity(intent);

    }

    //public void onClickPedidosAjuda (View view) {
    //    startActivity(new Intent(this, ReceberPedidoDeConselhoActivity.class));
    //}

    public void funcaoNaoImplementada() {
        // Context context = getApplicationContext();
        CharSequence text = "Função não implementada!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getBaseContext(), text, duration);
        toast.show();
    }

    //public void clickInStars() {
        // Context context = getApplicationContext();
    //    CharSequence text = "Nível de confiança";
    //    CaixaDialogo caixa = new CaixaDialogo(text, context);

    //    caixa.show(getFragmentManager(), "fewa");
    //}

    public void editarConta(View v){
        startActivity(new Intent(this, EditarUsuario.class));
    }
    class CaixaDialogo extends android.app.DialogFragment
    {
        CharSequence textoDialogo;
        Context context;

        public CaixaDialogo(CharSequence texto, Context contexto)
        {
            textoDialogo = texto;
            this.context = contexto;

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle("Aviso");
            builder.setMessage(textoDialogo);

            return builder.create();
        }
    }
}
