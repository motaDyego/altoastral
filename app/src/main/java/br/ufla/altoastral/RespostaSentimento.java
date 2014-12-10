package br.ufla.altoastral;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import br.ufla.altoastral.R;

public class RespostaSentimento extends Activity {

    Intent recebido = null;
    Bundle dados = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resposta_sentimento);

        recebido = getIntent();
        dados = recebido.getExtras();
        TextView usuario = (TextView) findViewById(R.id.tvUsuarioLogado);
        usuario.setText(dados.getString("usuario").toString());
        EditText txtPost = (EditText) findViewById(R.id.edtTextoPost);
        txtPost.setText(dados.getString("texto").toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resposta_sentimento, menu);
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

    public void responderPost(View v){
        System.out.println("chamou o responderPost(), dados: "+dados.toString());
        ConexaoAssincrona con = new ConexaoAssincrona();
        JSONObject retorno = null;
        EditText etResposta = (EditText) findViewById(R.id.etResposta);

        try {
            /* Monta o JSON da requisição */
            dados = recebido.getExtras();
            con.mensagemAEnviar = new JSONObject();
            con.context = this.getApplicationContext();
            con.destino = "resposta/save";
            con.mensagemAEnviar.put("texto", etResposta.getText().toString());
            con.mensagemAEnviar.put("token", dados.getString("tkn").toString());
            con.mensagemAEnviar.put("post", dados.getString("idPost").toString());
            System.out.println("mensagem a enviar: "+con.mensagemAEnviar.toString());

            /* Executa a requisição HTTP */
            retorno = (JSONObject) con.execute().get();

            /* Verificamos o status da resposta */
            if(con.statusCode == 200) {
                Toast toast = Toast.makeText(getBaseContext(), "resposta realizada com sucesso", Toast.LENGTH_SHORT);
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
