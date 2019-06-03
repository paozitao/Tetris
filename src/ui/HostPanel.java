/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import Connect.Server;
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
public class HostPanel extends javax.swing.JPanel {

    /**
     * Creates new form HostPanel
     */
    int time = 1000;
    HostPanel.MoveThread movey = new HostPanel.MoveThread(this,1000);
    Thread thready = new Thread(movey);
    BlocksController blocksCt = new BlocksController();
    int [] shape = blocksCt.getAShape();
    int[] start =blocksCt.getLocal();
    int[] op=new int[219];
    Boolean isRun = false;
    Boolean isConnect = false;
    Boolean isOver=false;
    Ready ready = new Ready();
    Server server = new Server();
    GameOver gameOver = new GameOver();
    int opscore=0;
    Boolean color=false;
    Color con1 = new Color(204, 255, 255);
    Color con2 = new Color(0xFF,0xBB,0xFF);
    Color conCurrent = Color.BLUE;
    //初始化房间，显示等待对手加入的Panel，开启等待对方加入线程
    public HostPanel() throws IOException {
        ready.setSize(400,200);
        ready.setLocation(100,50);
        gameOver.setSize(300,200);
        gameOver.setLocation(150,50);
        this.add(ready);
        this.add(gameOver);
        gameOver.setVisible(false);
        server.setHost(blocksCt.getAll());
        new Thread(new CheckReady()).start();
        new Thread(new CheckStart()).start();
        new Thread(new CheckOver()).start();
        new Thread(new ChangeColor()).start();
        initComponents();
    }
    
//    等待对方加入线程，当对手连接上，通知准备界面，对手已加入，同时刷新显示对手的方块
    public class CheckReady implements Runnable{
        public volatile boolean exit = false;
        public void run(){
            while(!exit){
                if(server.getIsConnect()){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HostPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ready.find();
                    isConnect = true;
                    exit = true;
                    repaint();
                    System.out.println("画出对手");
                }else{
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HostPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
//    检查是否开始，当准备界面点击开始，其消失，游戏开始，同时通知Server想Client发送开始消息，然后开始发送和接受游戏
        public class CheckStart implements Runnable{
        public volatile boolean exit = false;
        public void run(){
            while(!exit){
                if(ready.getStart()){
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

//        开始游戏
        public void startGame(){
            isRun=true;
            server.stratGame();
            thready.start();
    }
        
    //线程运动加重新画线
    public class MoveThread implements Runnable{
            private int time=1000;
            private HostPanel self;

        public void setSelf(HostPanel self) {
            this.self = self;
        }
        public void setTime(int time) {
            this.time = time;
        }
        MoveThread(HostPanel self, int time){
            this.self = self;
            this.time = time;
        }
            
            public void run(){
                Boolean exit = false;
                while(!exit){
                while(isRun){
                        blocksCt.down();
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
    
//        检查游戏是否结束以及对手是否结束，当结束时，发送end，提示对方游戏已经结束，同时显示gameOver Panel 出现提示游戏结束，显示得分
    public class CheckOver implements Runnable{
        public void run(){
            Boolean exit = false;
            while(!exit){
                if(blocksCt.isIsOver()||server.getEnd()){
                    System.out.println("游戏结束");
                    isRun = false;
                    server.setEnd(true);
                    gameOver.setText("<html>游戏结束<br><br>最终得分为 :"+blocksCt.getScore());
                    gameOver.setVisible(true);
                    exit  = true;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HostPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
}

        
    //刷新游戏界面，当对手连接后，显示对方初始界面
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(!isOver){
            server.setHost(blocksCt.getAll());
            drawBlocks(g);
            drawFix(g);
            drawCon(g);
        }
        if(isConnect&&(!isOver))drawOp(g);
    }
    
    public void keyPressed(java.awt.event.KeyEvent evt){
        if(isRun){
            if(blocksCt.isIsOver()){
                return ;
            }
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
        for(int i =0;i<shape.length;i++){//十六格子点阵4*4
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
    
    //当下落或者是触碰到方块时要画
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
    
    //    线程控制指示自己控制的一方,颜色反转,(是小嘟嘟提出的哦!)
    public class ChangeColor implements Runnable{
        public void run(){
            Boolean exit = false;
            while(!exit){
                try {
                    color = !color;
                    if(color){
                        conCurrent = con1;
                    }else{
                        conCurrent = con2;
                    }
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    //画线，将各个部分区别开
        public void drawCon(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(211, 0,180,403);
    
        g.setColor(conCurrent);
        g.fillRoundRect(201, 0,9, 403, 1, 1);
        
        g.setColor(Color.blue);
        g.fillRoundRect(391, 0,9, 403, 1, 1);
    }
        //字符串转数组
        public int[] parse(String msg){
        String[] s = msg.substring(1, msg.length() - 1).split(", ");
        int[] shape = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            shape[i] = Integer.parseInt(s[i]);
        }
        return shape;
    }
//        画出对手，首先获取对手发来的字符串（已经将游戏数组转化为数字串）转换为数组，将起始的坐标x+20代表偏移
    public void drawOp(Graphics g){
        op=parse(server.getOp());
        opscore = op[2];
        opScore.setText(String.valueOf(op[2]));
        int x = op[0]+20;
        int y = op[1];
        int[] opShape = new int[16];
        int[][] opFix=new int[20][10];
        System.arraycopy(op,3,opShape, 0, 16);
        for(int i =0;i<opShape.length;i++){//十六格子点阵4*4
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
//        对手下落的俄罗斯方块坐标数组的x轴坐标也要加20
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
//                线程不停地在检查end,opEnd,selfScore,opScore 从而控制游戏是否结束
//    有bug决定放弃这个
//        public class CheckOverBug implements Runnable{
//        public volatile boolean exit = false;
//        String text="";
//        int[] location={0,0};
//        int[] size={0,0};
//        Boolean show = false;
//        public void run(){
//            while(!exit&&isConnect){
////                当对手结束游戏，自己未结束时
//                if(server.getOpEnd()&&!isOver){
////                  控制游戏结束
//                    if(blocksCt.getScore()>opscore) {
//                        text="恭喜你取得了游戏的胜利！得分为：";
//                        server.selfOver();
//                        isOver = true;
//                        isRun = false;
//                        location[0] = 100;
//                        location[1] = 20;
//                        size[0] = 400;
//                        size[1] =400;
//                    }
////                    设置提示框出现在对手游戏的地方
//                    else{
//                        text="唯一的好消息是你的对手结束了游戏！";
//                        location[0] = 400;
//                        location[1] = 20;
//                        size[0] = 200;
//                        size[1] =400;
//                    }
//                    show = true;
//                }
////                对手未结束游戏，自己结束游戏
//                else if((!server.getOpEnd())&&isOver){
//System.out.println("对手未结束游戏，自己结束游戏");
//                    if(blocksCt.getScore()<opscore) {
//                        text="你输掉了这局对战";
//                        location[0] = 100;
//                        location[1] = 20;
//                        size[0] = 400;
//                        size[1] =400;
//                    }
//                    else{
//                        text="唯一的好消息是你还能再撑一会！";
//                        location[0] = 400;
//                        location[1] = 20;
//                        size[0] = 200;
//                        size[1] =400;
//                    }
//                    show = true;
//                }
////                对手结束游戏，自己结束游戏
//                else if((server.getOpEnd())&&isOver){
//                    if(blocksCt.getScore()<opscore) {
//                        text="你输掉了这局对战";
//                        location[0] = 100;
//                        location[1] = 20;
//                        size[0] = 400;
//                        size[1] =400;
//                    }
//                    else if(blocksCt.getScore()==opscore){
//                        text="平局，小伙子们";
//                        location[0] = 100;
//                        location[1] = 20;
//                        size[0] = 400;
//                        size[1] =400;
//                    }else{
//                        text="恭喜你获得游戏的胜利！得分为：";
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

        setBackground(new java.awt.Color(0, 255, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setFocusCycleRoot(true);
        setFocusTraversalPolicyProvider(true);
        setPreferredSize(new java.awt.Dimension(600, 500));

        opScore.setBackground(new java.awt.Color(255, 255, 255));
        opScore.setFont(new java.awt.Font("楷体", 0, 25)); // NOI18N
        opScore.setForeground(new java.awt.Color(0, 255, 255));
        opScore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        opScore.setText("0");
        opScore.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 3, true));
        opScore.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        selfScore.setBackground(new java.awt.Color(255, 255, 255));
        selfScore.setFont(new java.awt.Font("楷体", 0, 25)); // NOI18N
        selfScore.setForeground(new java.awt.Color(0, 255, 255));
        selfScore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selfScore.setText("0");
        selfScore.setToolTipText("");
        selfScore.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 3, true));
        selfScore.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        aa.setBackground(new java.awt.Color(255, 255, 255));
        aa.setFont(new java.awt.Font("楷体", 0, 25)); // NOI18N
        aa.setForeground(new java.awt.Color(0, 255, 255));
        aa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aa.setText("对方分数");
        aa.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 3, true));
        aa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        aa1.setBackground(new java.awt.Color(255, 255, 255));
        aa1.setFont(new java.awt.Font("楷体", 0, 25)); // NOI18N
        aa1.setForeground(new java.awt.Color(0, 255, 255));
        aa1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aa1.setText("您的分数");
        aa1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 3, true));
        aa1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(210, 210, 210)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(opScore, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(selfScore, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(aa1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addComponent(aa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(208, Short.MAX_VALUE))
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
                .addContainerGap(259, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aa;
    private javax.swing.JLabel aa1;
    private javax.swing.JLabel opScore;
    private javax.swing.JLabel selfScore;
    // End of variables declaration//GEN-END:variables
}
