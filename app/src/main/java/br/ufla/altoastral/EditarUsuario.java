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

import java.util.concurrent.ExecutionException;

import br.ufla.altoastral.R;


public class EditarUsuario extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editar_usuario, menu);
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
        }else if(id == R.id.logout){
            startActivity(new Intent(this, login.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void funcaoNaoImplementada() {
        // Context context = getApplicationContext();
        CharSequence text = "Função não implementada!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getBaseContext(), text, duration);
        toast.show();
    }

    public void onClickEditar(View v){
        ConexaoAssincrona con = new ConexaoAssincrona();
        JSONObject retorno = null;
        EditText edtSenhaAtual;
        EditText edtSenhaNova;
        //JSONObject jsonEnviado = null;
        try {
            edtSenhaAtual = (EditText) findViewById(R.id.etSenhaAtual);
            edtSenhaNova = (EditText) findViewById(R.id.etSenha);

            con.mensagemAEnviar = new JSONObject();
            con.context = this.getApplicationContext();
            con.putRequest = true;
            con.destino = "usuario/update";
            con.mensagemAEnviar.put("senhaAtual", edtSenhaAtual.getText().toString());
            con.mensagemAEnviar.put("senhaNova", edtSenhaNova.getText().toString());
            /* Recupera o token para enviar na requisição */
            Intent recebido = getIntent();
            Bundle dados = recebido.getExtras();
            if(dados != null) {
                String tokenAcesso = dados.getString("token");
                System.out.println("*************dados NOT null*************\n token: "+"\""+tokenAcesso+"\"");
                con.mensagemAEnviar.put("token", "\""+tokenAcesso+"\"");
                System.out.println("*************gerou o JSON da requisição*************");
            }else{
                System.out.println("dados null*************");
            }

            retorno = (JSONObject) con.execute().get();

            /* Verificamos o status da resposta */
            if(con.statusCode == 200) {
                Toast toast = Toast.makeText(getBaseContext(), retorno.get("mensagem").toString(), Toast.LENGTH_LONG);
                toast.show();
                finish();
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
