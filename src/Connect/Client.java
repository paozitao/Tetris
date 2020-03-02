/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author white
 */
public class Client {
    PrintWriter pw;
    BufferedReader br;
    Socket client;
    Boolean isConnect=false;
    Boolean isStart= false;
    Boolean end = false;
    String host;
    String self;
    String meg;
    
//    ���������������Լ��ĳ�ʼ���棬��ȡ�Է��ĳ�ʼ���棬�����ȴ��̣߳��ȴ��Է���ʼ
    public Client(String ip,String self) throws IOException{
        client = new Socket("127.0.0.1",50000);
        isConnect=true;
        this.self=self;
        pw=new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
        br=new BufferedReader(new InputStreamReader(client.getInputStream()));
        pw.println(self);
        pw.flush();
        host=br.readLine();
        new Thread(new Wait()).start();
        }
//            �ȴ��������Ϳ�ʼ֪ͨ������Ϸ��ʼ���������ͺͽ�����Ϣ�߳�
        private class Wait implements Runnable{
            public void run(){
                try {
                    meg = br.readLine();
                    System.out.println("�յ���ʼ��Ϸ����Ϣ��"+meg);
                    if(meg.equals("start")){
                        isStart = true;
                        new Thread(new SendMsg()).start();
                        new Thread(new RecieveMsg()).start();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }        
}
    //���ܶ��ֵ�ͼ�Σ������ַ���endʱ����Է��Ѿ��������رս����߳�
            private class RecieveMsg implements Runnable{
            @Override
            public void run(){
            while(isStart){
                try {
                    host= br.readLine();
                    if(host.equals("end")){
                        end=true;
                        isStart = false;
                    }
                    Thread.sleep(200);
                } catch (Exception ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("�ͻ���ֹͣ������Ϣ");
        }
}
            //�����Լ���������ڵ�֪�Է�������Ϸ������Լ���Ϸ����ʱ��ֹͣ����
        private class SendMsg implements Runnable{
        public void run(){
            int i=0;
            while(isStart){
                if(end){
                    isStart = false;
                    pw.println("end");
                    pw.flush();
                }
                pw.println(self);
                pw.flush();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("�ͻ���ֹͣ������Ϣ");
        }
}
                
//        get set
    public void setEnd(){
        this.end = true;
    }
    
    public Boolean getEnd(){
        return end;
    }

    public Boolean getIsConnect() {
        return isConnect;
    }

    public Boolean getIsStart() {
        return isStart;
    }

    public String getHost() {
        return host;
    }
    public void setSelf(String self) {
        this.self = self;
    }
}
