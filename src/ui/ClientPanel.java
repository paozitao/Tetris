/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import Connect.Client;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.controller.BlocksController;

/**
 *
 * @author white
 */
public class ClientPanel extends javax.swing.JPanel {

    /**
     * Creates new form ClientPanel
     */
    int time = 1000;
    BlocksController blocksCt = new BlocksController();
    int [] shape = blocksCt.getAShape();
    int[] start =blocksCt.getLocal();
    int[] fix;
    int[] op;
    Boolean isStart = false;
    Boolean isRun = false;
    Boolean isOver = false;
    int opscore;
    Client client;
    ClientWait cw=new ClientWait();
    GameOver gameOver = new GameOver();
    Boolean color = false;
    Color con1 = new Color(204, 255, 255);
    Color con2 = new Color(0xFF,0xBB,0xFF);
    Color conCurrent = Color.BLUE;
    public ClientPanel(String ip) throws IOException {
        cw.setSize(400,200);
        cw.setLocation(100,40);
        gameOver.setSize(300,200);
        gameOver.setLocation(150,50);
        this.add(cw);
        this.add(gameOver);
        gameOver.setVisible(false);
        client=new Client(ip,blocksCt.getAll());
        new Thread(new CheckStart()).start();
        new Thread(new CheckOver()).start();
        new Thread(new ChangeColor()).start();
        initComponents();
    }
//    ����Ƿ�ʼ��Ϸ����ʼ���ͺͽ�����Ϸ
        public class CheckStart implements Runnable{
        public volatile boolean exit = false;
        public void run(){
            while(!exit){
                if(client.getIsStart()){
                    cw.setVisible(false);
                    startGame();
                    exit = true;
                }else{
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HostPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
            }
            }
        }
    }

//        �����Ϸ�Ƿ������������ʱ������end����ʾ�Է���Ϸ�Ѿ�������ͬʱ��ʾgameOver Panel ������ʾ��Ϸ��������ʾ�÷�
    public class CheckOver implements Runnable{
        public void run(){
            Boolean exit = false;
            while(!exit){
                if(blocksCt.isIsOver()||client.getEnd()){
                    System.out.println("��Ϸ����");
                    isRun=false;
                    client.setEnd();
                    gameOver.setText("<html>��Ϸ����<br><br>���յ÷�Ϊ:"+blocksCt.getScore());
                    gameOver.setVisible(true);
                    exit = true;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HostPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
}
        
//  ��ʼ��Ϸ       
    public void startGame(){
        isRun=true;
        new Thread(new MoveThread(this,1000)).start();
    }
    
    //ˢ����Ϸ���棬���������Ӻ���ʾ�Է���ʼ����
    public void paintComponent(Graphics g){
        super.paintComponent(g);
            client.setSelf(blocksCt.getAll());
            drawCon(g);
            drawBlocks(g);
            drawFix(g);
            drawOp(g);
    }
        //�߳��˶������»���
    public class MoveThread implements Runnable{
            private int time=1000;
            private ClientPanel self;

        public void setSelf(ClientPanel self) {
            this.self = self;
        }
        public void setTime(int time) {
            this.time = time;
        }
        MoveThread(ClientPanel self, int time){
            this.self = self;
            this.time = time;
        }
            
            public void run(){
                Boolean exit = false;
                while(!exit){
                while(isRun){
                        blocksCt.down();
                        client.setSelf(blocksCt.getAll());
                        selfScore.setText(""+blocksCt.getScore());
                        self.repaint();
                        try {
                            Thread.sleep(this.time);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }

                }
            }
    }
    
//    ���ݼ��̣�����˹�����ƶ�
    public void keyPressed(java.awt.event.KeyEvent evt){
        if(blocksCt.isIsOver()){
            isRun = false;
        }
        if(isRun){
            if(evt.getKeyCode()==37){
                blocksCt.set(start);
                blocksCt.left();
                this.repaint();
            }
            if(evt.getKeyCode()==38){
                blocksCt.turn();
                this.repaint();
            }
              if(evt.getKeyCode()==39){
                blocksCt.set(start);
                blocksCt.right();
                this.repaint();
            }
                if(evt.getKeyCode()==40){
                blocksCt.set(start);
                blocksCt.down();
                            if(blocksCt.isIsOver()){
                                isRun  = false;
                                 return ;
                            }
                this.repaint();
            }
               this.setFocusable(true);
        }

    }
    
    public void drawBlocks(Graphics g){
//        g.setColor(Color.blue);
//        g.fillPolygon(px1,py1,4);
        shape=blocksCt.getAShape();
        start=blocksCt.getLocal();
        int x = start[0];
        int y = start[1];
        for(int i =0;i<shape.length;i++){//ʮ�����ӵ���4*4
            if(shape[i]==1){
            g.setColor(Color.WHITE);
            g.drawRect(x*20, 20*y, 20, 20);
            g.setColor(Color.BLUE);
            g.fillRect(x*20+1, y*20+1, 19, 19);
            }
            if((i+1)%4==0){
                x=start[0];
                y=y+1;
            }
            else {
                x=x+1;
            }
        }
    }
    
    public void chockSpeed(){
        int score = blocksCt.getScore();
        if(score>=10&&score<30){
            this.time= 500;
        }
        else if(score>=30&&score<80){
            time = 300;
        }
        else if(score>=80){
            time = 100;
        }
    }
    
    //����������Ǵ���������ʱҪ��
    public void drawFix(Graphics g){
//        g.setColor(Color.blue);
//        g.fillPolygon(px1,py1,4);
            int[][] fix = blocksCt.getFix();
            for(int i = 0;i<20;i++){
                for(int j = 0;j<10;j++){
                    if(fix[i][j]==1){
                    g.setColor(Color.BLUE);
                    g.drawRect(j*20, 20*i, 20, 20);
                    g.setColor(Color.WHITE);
                    g.fillRect(j*20+1, i*20+1, 19, 19);
                    }
                }
            }
    }
//    �߳̿���ָʾ�Լ����Ƶ�һ��,��ɫ��ת,(��С�������Ŷ!)
    public class ChangeColor implements Runnable{
        public void run(){
            Boolean exit = false;
            while(!exit){
                try {
                    color = !color;
                    Thread.sleep(300);
                    if(color){
                        conCurrent = con1;
                    }else{
                        conCurrent = con2;
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    //���ߣ���������������
        public void drawCon(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(210, 0,180,400);
        
        g.setColor(conCurrent);
        g.fillRoundRect(201, 0,9, 403, 1, 1);
        
        g.setColor(Color.blue);
        g.fillRoundRect(391, 0,9, 403, 1, 1);
    }
        //�ַ���ת����
        public int[] parse(String msg){
        String[] s = msg.substring(1, msg.length() - 1).split(", ");
        int[] shape = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            shape[i] = Integer.parseInt(s[i]);
        }
        return shape;
    }
        
//        �������֣����Ȼ�ȡ���ַ������ַ������Ѿ�����Ϸ����ת��Ϊ���ִ���ת��Ϊ���飬����ʼ������x+20����ƫ��
    public void drawOp(Graphics g){
        op=parse(client.getHost());
        opscore = op[2];
        opScore.setText(String.valueOf(op[2]));
        int x = op[0]+20;
        int y = op[1];
        int[] opShape = new int[16];
        int[][] opFix=new int[20][10];
        System.arraycopy(op,3,opShape, 0, 16);
        for(int i =0;i<opShape.length;i++){//ʮ�����ӵ���4*4
            if(opShape[i]==1){
            g.setColor(Color.ORANGE);
            g.drawRect(x*20, 20*y, 20, 20);
            g.setColor(Color.YELLOW);
            g.fillRect(x*20+1, y*20+1, 19, 19);
            }
            if((i+1)%4==0){
                x=op[0]+20;
                y=y+1;
            }
            else {
                x=x+1;
            }
        }
//        ��������Ķ���˹�������������x������ҲҪ��20
        for(int i=0;i<20;i++){
            for(int j=0;j<10;j++){
                opFix[i][j]=op[i*10+j+18];
                    if(opFix[i][j]==1){
                    g.setColor(Color.black);
                    g.drawRect((j+20)*20, 20*i, 20, 20);
                    g.setColor(Color.red);
                    g.fillRect((j+20)*20+1, i*20+1, 19, 19);
                    }
        }
    }
    }
    
//                �̲߳�ͣ���ڼ��end,opEnd,selfScore,opScore �Ӷ�������Ϸ�Ƿ����
//                ��BUG����
//        public class CheckOverBug implements Runnable{
//        public volatile boolean exit = false;
//        String text="";
//        int[] location={0,0};
//        int[] size={0,0};
//        Boolean show = false;
//        public void run(){
//            while(!exit){
////                �����ֽ�����Ϸ���Լ�δ����ʱ
//                if(client.getOpEnd()&&!isOver){
////                  ������Ϸ����
//     System.out.println("���ֽ�����Ϸ���Լ�δ����ʱ");
//                    if(blocksCt.getScore()>opscore) {
//                        text="��ϲ��ȡ������Ϸ��ʤ�����÷�Ϊ��";
//                        client.selfOver();
//                        isOver = true;
//                        isRun = false;
//                        location[0] = 100;
//                        location[1] = 20;
//                        size[0] = 400;
//                        size[1] =400;
//                    }
////                    ������ʾ������ڶ�����Ϸ�ĵط�
//                    else{
//                        text="Ψһ�ĺ���Ϣ����Ķ��ֽ�������Ϸ��";
//                        location[0] = 400;
//                        location[1] = 20;
//                        size[0] = 200;
//                        size[1] =400;
//                    }
//                    show = true;
//                }
////                ����δ������Ϸ���Լ�������Ϸ
//                else if((!client.getOpEnd())&&isOver){
//                    System.out.println("����δ������Ϸ���Լ�������Ϸ");
//                    if(blocksCt.getScore()<opscore) {
//                        text="���������ֶ�ս";
//                        location[0] = 100;
//                        location[1] = 20;
//                        size[0] = 400;
//                        size[1] =400;
//                    }
//                    else{
//                        text="Ψһ�ĺ���Ϣ���㻹���ٳ�һ�ᣡ";
//                        location[0] = 400;
//                        location[1] = 20;
//                        size[0] = 200;
//                        size[1] =400;
//                    }
//                    show = true;
//                }
////                ���ֽ�����Ϸ���Լ�������Ϸ
//                else if((client.getOpEnd())&&isOver){
//                    if(blocksCt.getScore()<opscore) {
//                        text="���������ֶ�ս";
//                        location[0] = 100;
//                        location[1] = 20;
//                        size[0] = 400;
//                        size[1] =400;
//                    }
//                    else if(blocksCt.getScore()==opscore){
//                        text="ƽ�֣�С������";
//                        location[0] = 100;
//                        location[1] = 20;
//                        size[0] = 400;
//                        size[1] =400;
//                    }else{
//                        text="��ϲ������Ϸ��ʤ�����÷�Ϊ��";
//                        location[0] = 100;
//                        location[1] = 20;
//                        size[0] = 400;
//                        size[1] =400;
//                    }
//                    show = true;
//                }
//                gameOver.setSize(size[0],size[1]);
//                gameOver.setLocation(location[0],location[1]);
//                gameOver.setText(text);
//                gameOver.setVisible(show);
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(HostPanel.class.getName()).log(Level.SEVERE, null, ex);
//                }
//        }
//    }
//        }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        opScore = new javax.swing.JLabel();
        selfScore = new javax.swing.JLabel();
        aa = new javax.swing.JLabel();
        aa1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(51, 255, 255));

        opScore.setBackground(new java.awt.Color(255, 255, 255));
        opScore.setFont(new java.awt.Font("����", 0, 25)); // NOI18N
        opScore.setForeground(new java.awt.Color(0, 255, 255));
        opScore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        opScore.setText("0");
        opScore.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 3, true));
        opScore.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        selfScore.setBackground(new java.awt.Color(255, 255, 255));
        selfScore.setFont(new java.awt.Font("����", 0, 25)); // NOI18N
        selfScore.setForeground(new java.awt.Color(0, 255, 255));
        selfScore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selfScore.setText("0");
        selfScore.setToolTipText("");
        selfScore.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 3, true));
        selfScore.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        aa.setBackground(new java.awt.Color(255, 255, 255));
        aa.setFont(new java.awt.Font("����", 0, 25)); // NOI18N
        aa.setForeground(new java.awt.Color(0, 255, 255));
        aa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aa.setText("�Է�����");
        aa.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 3, true));
        aa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        aa1.setBackground(new java.awt.Color(255, 255, 255));
        aa1.setFont(new java.awt.Font("����", 0, 25)); // NOI18N
        aa1.setForeground(new java.awt.Color(0, 255, 255));
        aa1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aa1.setText("���ķ���");
        aa1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 3, true));
        aa1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(210, 210, 210)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(selfScore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(aa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addComponent(opScore, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(aa1, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addGap(210, 210, 210))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(aa1)
                .addGap(18, 18, 18)
                .addComponent(selfScore, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(aa)
                .addGap(18, 18, 18)
                .addComponent(opScore)
                .addContainerGap(157, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aa;
    private javax.swing.JLabel aa1;
    private javax.swing.JLabel opScore;
    private javax.swing.JLabel selfScore;
    // End of variables declaration//GEN-END:variables
}
