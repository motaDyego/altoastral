package br.ufla.altoastral;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Realiza conexão HTTP com o servidor
 * @author Dyego
 * @since  20/11/2014
 */
public class ConexaoAssincrona extends AsyncTask {

    public JSONObject mensagemAEnviar = null;
    public JSONObject retorno = null;
    public int statusCode;
    public String destino = null;
    public Context context;
    private String ip, porta;
    public boolean putRequest = false;
    HttpPut put = null;
    HttpPost post = null;
    //public StringBuilder urlDestino = new StringBuilder("http://"); //10.1.1.3:8080/altoastral/usuario/login

    @Override
    protected Object doInBackground(Object[] params) {
        /* HTTP request */
        /* Monta o JSON da requisição  */
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000); //Timeout Limit
        HttpResponse response;
        byte[] buffer = new byte[1500];

        try {
            /* Obtem o IP e a porta da requisição */
            SharedPreferences preference = context.getSharedPreferences("prefConfig", Context.MODE_PRIVATE);
            ip = preference.getString("ip", "");
            porta = preference.getString("porta", "");

            /* Monta a requisição PUT*/
            if (putRequest) {
                put = new HttpPut("http://" + ip + ":" + porta + "/altoastral/" + destino);
                StringEntity se = new StringEntity(mensagemAEnviar.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                put.setEntity(se);
                put.setHeader("Origin", "http://10.1.1.5");
                /* Executando o request */
                response = client.execute(put);

            /* Monta requisição POST */
            } else {
                HttpPost post = new HttpPost("http://" + ip + ":" + porta + "/altoastral/" + destino);
                StringEntity se = new StringEntity(mensagemAEnviar.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                post.setHeader("Origin", "http://10.1.1.5");
                /* Executando o request */
                response = client.execute(post);
            }

            if (response != null) {
                statusCode = response.getStatusLine().getStatusCode();
                response.getEntity().getContent().read(buffer);
                System.out.println("buffer: " + new String(buffer));
                retorno = new JSONObject(new String(buffer));
                System.out.println("json: " + retorno);

            }
        }catch (ConnectTimeoutException cte) {
            cte.printStackTrace();
            statusCode = 500;
            retorno = new JSONObject();
            try {
                retorno.put("error", "Erro ao conectar ao ip: " + ip);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }catch (HttpHostConnectException htce){
            htce.printStackTrace();
            statusCode = 500;
            retorno = new JSONObject();
            try {
                retorno.put("error", "Erro ao conectar ao servidor, verifique sua conexão");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return retorno;

        /* Requisição usando socket */
        /*DataOutputStream out = null;
        DataInputStream input = null;
        Socket socket = null;

        try {
            socket = new Socket("10.1.1.2", 8888);

            out = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());

            out.writeUTF(mensagemAEnviar.toString());

            //retorno = input.readUTF();
            retorno = new JSONObject(input.readUTF());

            System.out.println("retorno recebido "+retorno.toString());

        } catch (IOException e) {
            System.out.println("**************deu erro****************");
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (socket != null) {
                    socket.close();
                }
                if (input != null) {
                    input.close();
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }*/

    }
}
