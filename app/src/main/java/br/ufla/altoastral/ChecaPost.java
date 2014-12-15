package br.ufla.altoastral;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Dyego Mota on 08/12/2014.
 */
public class ChecaPost extends Service {

    public Trabalho trabalho;
    Context context;
    Bundle dados = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        System.out.println("criando o serviço de checagem de post");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        System.out.println("startando o serviço de checagem de post");
        dados = intent.getExtras();
        context = this.getApplicationContext();
        trabalho = new Trabalho(startId);
        trabalho.start();
        return START_NOT_STICKY;
    }

    class Trabalho extends Thread{
        public int startId;
        public boolean ativo = true;

        public Trabalho(int startId){
            this.startId = startId;
        }
        public void run(){
            String token = dados.getString("token").toString();
            String usuario = dados.getString("usuario").toString();
            while(ativo){
                try {
                    System.out.println("checando postagens, token: "+ dados.getString("token").toString());
                    /* Monta o JSON da requisição */
                    ConexaoAssincrona con = new ConexaoAssincrona();
                    JSONObject retorno = null;
                    con.mensagemAEnviar = new JSONObject();
                    con.context = context;
                    con.destino = "post/check";
                    con.mensagemAEnviar.put("token", token);

                    /* Executa a requisição HTTP */
                    retorno = (JSONObject) con.execute().get();

                    /* Verificamos o status da resposta */
                    if(con.statusCode == 200) {
                        System.out.println("checagem realizada com sucesso");
                        System.out.println(retorno.toString());

                        createNotification(token, retorno.get("texto").toString(), usuario, retorno.get("id").toString());
                    }else if(con.statusCode == 404){
                        System.out.println("nenhum post encontrado");
                    }else{
                        System.out.println("erro ao checar postagem");
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
                try {
                    Thread.sleep(1000*60);
                } catch (InterruptedException e) {
                    System.out.println("erro ao dormir a thread");
                    e.printStackTrace();
                }
            }
            System.out.println("serviço concluído");
        }
    }

    @Override
    public void onDestroy(){
        trabalho.ativo = false;
        stopSelf(trabalho.startId);
        super.onDestroy();
        System.out.println("Destruindo tread");
    }

    private void createNotification(String tokenAcesso, String texto, String usuario, String idPost) {
        System.out.println("parametros createNotification(): "+tokenAcesso+", "+texto+", "+ usuario+", "+idPost);
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, RespostaSentimento.class);
        Bundle dados = new Bundle();

        /* Transferimos as informações necessárias para a próxima activity */
        dados.putString("tkn", tokenAcesso);
        dados.putString("texto", texto);
        dados.putString("usuario", usuario);
        dados.putString("idPost", idPost);
        System.out.println("dados createNotification: "+dados.toString());
        //System.out.println("tokenAcesso inserido em outraCoisa: "+tokenAcesso);
        //dados.putString("outraCoisa", tokenAcesso);
        intent.putExtras(dados);
        //System.out.println(intent.getExtras().get("outraCoisa").toString());
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Postagem Sentimento")
                .setContentText("um usuário realizou uma nova postagem de sentimento").setSmallIcon(R.drawable.ic_alto_astral2)
                .setContentIntent(pIntent)
                .addAction(R.drawable.ic_alto_astral, "Call", pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }
}
