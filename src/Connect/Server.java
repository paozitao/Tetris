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


    //初始化，建立Socket服务器，等待对手接入
    public Server() throws IOException {
             server = new ServerSocket(50000);
             new Thread(new Wait()).start();   
}
    //开始游戏，先通知Client游戏开始，启动发送主机游戏界面和接受对手界面线程
    public void stratGame(){
            isStart = true;
            pw.println("start");
            pw.flush();
            new Thread(new SendMsg()).start();
            new Thread(new RecieveMsg()).start();
    }
    
    //接受对手的图形，当对手发来end时代表对方已经结束，关闭接受线程
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
            System.out.println("房主停止接收信息");
        }
}
            //发送自己的情况，在得知对方结束游戏后或者自己游戏结束时，停止发送
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
                System.out.println("房主停止发送信息");
            }
}
    //等待对手接入，当对手接入后，将对手初始的界面过来并且显示，同时发送自己的初始界面，设置isConnect通知Host对手已连接
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
                System.out.println("1.对手连接到房间，Client游戏界面："+op);
                System.out.println("1.对手连接到房间，主机游戏界面："+host);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}
    
    
//基本的输入输出函数    
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
    
    
    
    //有bug 不用了如果自己游戏结束，立刻结束发送自己的信息的线程，通知对方自己游戏结束
    public void selfOver(){
            end=true;
            pw.println("end");
            pw.flush(); 
            pw.close();
    }

    
}
