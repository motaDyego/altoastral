package br.ufla.altoastral;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import br.ufla.altoastral.R;


public class CadastroUsuario extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro_usuario, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    /* Envia requisição de criação de novo usuário */
    public void onClickSalvar(View v) throws UnsupportedEncodingException {
        ConexaoAssincrona con = new ConexaoAssincrona();
        JSONObject retorno = null;
        EditText edtUsuario = (EditText) findViewById(R.id.etUsuario);
        EditText edtSenha = (EditText) findViewById(R.id.etSenha);

        try {
            /* Monta o JSON da requisição */
            con.mensagemAEnviar = new JSONObject();
            con.context = this.getApplicationContext();
            con.destino = "usuario/save";
            con.mensagemAEnviar.put("nome", edtUsuario.getText().toString());
            con.mensagemAEnviar.put("senha", edtSenha.getText().toString());

            /* Executa a requisição HTTP */
            retorno = (JSONObject) con.execute().get();

            /* Verificamos o status da resposta */
            if(con.statusCode == 200) {
                Toast toast = Toast.makeText(getBaseContext(), "usuário "+edtUsuario.getText().toString()+" criado com sucesso!", Toast.LENGTH_LONG);
                toast.show();
                startActivity(new Intent(this, login.class));
            }else{
                AlertDialog.Builder alerta = new AlertDialog.Builder(this);
                alerta.setTitle("Erro");
                //define a mensagem
                alerta.setMessage("Erro ao criar usuário: "+retorno.toString());
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
