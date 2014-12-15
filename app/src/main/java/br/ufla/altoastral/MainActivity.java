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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import br.ufla.altoastral.R;


public class MainActivity extends Activity {

    Intent recebido = null;
    Bundle dados = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recebido = getIntent();
        dados = recebido.getExtras();
        TextView usuario = (TextView) findViewById(R.id.tvUsuarioLogado);
        usuario.setText(dados.getString("usuario").toString());
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
            startActivity(new Intent(this, Configuracao.class));
            return true;
        }else if(id == R.id.atualizar_senha){
            Intent intent = new Intent(this, EditarUsuario.class);
            //String tokenMain = dados.getString("token");
            //System.out.println("token no main: "+tokenMain);
            intent.putExtras(dados);
            startActivity(intent);
            return true;
        }else if(id == R.id.logout){
            Intent itService = new Intent("CHECA_POST");
            stopService(itService);
            startActivity(new Intent(this, login.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickLogo (View view) {
        startActivity(new Intent(this, MainActivity.class));
    }


    public void funcaoNaoImplementada() {
        CharSequence text = "Função não implementada!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getBaseContext(), text, duration);
        toast.show();
    }

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

    public void postarSentimento(View v){
        ConexaoAssincrona con = new ConexaoAssincrona();
        JSONObject retorno = null;
        EditText edtSentimento = (EditText) findViewById(R.id.dtPostSentimento);

        try {
            /* Monta o JSON da requisição */
            dados = recebido.getExtras();
            con.mensagemAEnviar = new JSONObject();
            con.context = this.getApplicationContext();
            con.destino = "post/save";
            con.mensagemAEnviar.put("texto", edtSentimento.getText().toString());
            con.mensagemAEnviar.put("token", dados.getString("token").toString());

            /* Executa a requisição HTTP */
            retorno = (JSONObject) con.execute().get();

            /* Verificamos o status da resposta */
            if(con.statusCode == 200) {
                Toast toast = Toast.makeText(getBaseContext(), "postagem realizada com sucesso", Toast.LENGTH_SHORT);
                toast.show();
            }else{
                AlertDialog.Builder alerta = new AlertDialog.Builder(this);
                alerta.setTitle("Erro");
                //define a mensagem
                alerta.setMessage(retorno.toString());
                alerta.create().show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex){
            System.out.println("---Erro geral----");
            ex.printStackTrace();
        }
    }
}
