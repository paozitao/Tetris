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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Server {
    ServerSocket server;
    PrintWriter pw;
    BufferedReader br;
    Socket client;
    Boolean isConnect=false;
    Boolean isStart= false;


    Boolean end=false;
    
    String host;
    String op;


    //��ʼ��������Socket���������ȴ����ֽ���
    public Server() throws IOException {
             server = new ServerSocket(50000);
             new Thread(new Wait()).start();   
}
    //��ʼ��Ϸ����֪ͨClient��Ϸ��ʼ����������������Ϸ����ͽ��ܶ��ֽ����߳�
    public void stratGame(){
            isStart = true;
            pw.println("start");
            pw.flush();
            new Thread(new SendMsg()).start();
            new Thread(new RecieveMsg()).start();
    }
    
    //���ܶ��ֵ�ͼ�Σ������ַ���endʱ����Է��Ѿ��������رս����߳�
            private class RecieveMsg implements Runnable{
            @Override
            public void run(){
            while(isStart){
                try {
                    op= br.readLine();
                    if(op.equals("end")){
                         end=true;
                         isStart=false;
                    }
                    Thread.sleep(200);
                } catch (Exception ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("����ֹͣ������Ϣ");
        }
}
            //�����Լ���������ڵ�֪�Է�������Ϸ������Լ���Ϸ����ʱ��ֹͣ����
        private class SendMsg implements Runnable{
            public void run(){
                while(isStart){
                    if(end){
                        pw.println("end");
                        pw.flush();
                        isStart=false;
                    }
                    pw.println(host);
                    pw.flush();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("����ֹͣ������Ϣ");
            }
}
    //�ȴ����ֽ��룬�����ֽ���󣬽����ֳ�ʼ�Ľ������������ʾ��ͬʱ�����Լ��ĳ�ʼ���棬����isConnect֪ͨHost����������
    private class Wait implements Runnable{
        public void run(){
            try {
                client = server.accept();
                pw=new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
                br=new BufferedReader(new InputStreamReader(client.getInputStream()));
                isConnect = true;
                op = br.readLine();
                pw.println(host);
                pw.flush();
                System.out.println("1.�������ӵ����䣬Client��Ϸ���棺"+op);
                System.out.println("1.�������ӵ����䣬������Ϸ���棺"+host);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}
    
    
//�����������������    
    public Boolean getEnd(){
        return end;
    }
    public Boolean getIsConnect() {
        return isConnect;
    }
    public void setStart(Boolean is){
        isStart=is;
    }
    public String getOp(){
        return op;
    }
    public void setHost(String host){
        this.host = host;
    }
    public void setEnd(Boolean set){
        this.end = set;
    }
    
    
    
    //��bug ����������Լ���Ϸ���������̽��������Լ�����Ϣ���̣߳�֪ͨ�Է��Լ���Ϸ����
    public void selfOver(){
            end=true;
            pw.println("end");
            pw.flush(); 
            pw.close();
    }

    
}
