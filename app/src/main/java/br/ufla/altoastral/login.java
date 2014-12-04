package br.ufla.altoastral;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

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


    @Override
  public boolean onCreateOptionsMenu(Menu menu) {
     // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.menu_login, menu);
      return true;
  }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Configuracao.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickEntrar(View view) {
        ConexaoAssincrona con = new ConexaoAssincrona();
        JSONObject retorno = null;
        EditText edtUsuario = (EditText) findViewById(R.id.editTextLogin);
        EditText edtSenha = (EditText) findViewById(R.id.editTextSenha);

        try {
            /* Monta o JSON da requisição */
            con.mensagemAEnviar = new JSONObject();
            con.context = this.getApplicationContext();
            con.destino = "usuario/login";
            con.mensagemAEnviar.put("nome", edtUsuario.getText().toString());
            con.mensagemAEnviar.put("senha", edtSenha.getText().toString());

            /* Executa a requisição HTTP */
            retorno = (JSONObject) con.execute().get();

            /* Verificamos o status da resposta */
            if(con.statusCode == 200) {
                Toast toast = Toast.makeText(getBaseContext(), retorno.get("mensagem").toString(), Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(this, MainActivity.class);
                Bundle dados = new Bundle();
                String tokenAcesso = retorno.get("token").toString();
                System.out.println("tokenAcesso "+tokenAcesso);
                dados.putString("token", tokenAcesso);
                intent.putExtras(dados);
                startActivity(intent);
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
