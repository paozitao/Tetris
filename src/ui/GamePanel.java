/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.controller.BlocksController;

/**
 *
 * @author white
 */
public class GamePanel extends javax.swing.JPanel{//实现线程的接口

    /**
     * Creates new form GamePanel
     */
    
    
     int time = 1000;
    MoveThread movey = new MoveThread(this,1000);
    Thread thready = new Thread(movey);
    BlocksController blocksCt = new BlocksController();
    int [] shape = blocksCt.getAShape();
    int[] start =blocksCt.getLocal();
    int[] fix;
    boolean isRun = true;
    
    
    public GamePanel() {
        initComponents();
        jButton1.setLocation(time, time);
       thready.start();
       jLabel3.setVisible(false);
    }
    
    //线程运动加重新画线
    public class MoveThread implements Runnable{
            private int time=1000;
            private GamePanel self;

        public void setSelf(GamePanel self) {
            this.self = self;
        }
        public void setTime(int time) {
            this.time = time;
        }
        MoveThread(GamePanel self, int time){
            this.self = self;
            this.time = time;
        }
        public void run(){
            while(true){
            while(isRun){
                    blocksCt.down();
                    if(blocksCt.isIsOver()){
                        isRun  = false;
                         jLabel3.setVisible(true);
                         jLabel3.setText("<html>游戏结束<br><br>终得得分为<br>    "+blocksCt.getScore());
                         return ;
                    }
                    self.repaint();
                    jLabel2.setText(String.valueOf(blocksCt.getScore()));
                    try {
                        Thread.sleep(this.time);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }

            }
        }
    }

    //
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawBlocks(g);
        drawFix(g);
        drawCon(g);
    }
    
    public void keyPressed(java.awt.event.KeyEvent evt){
        if(blocksCt.isIsOver()){
                isRun=false;
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
                                 jLabel3.setVisible(true);
                                 jLabel3.setText("<html>游戏结束<br><br>终得得分为<br>    "+blocksCt.getScore());
                                 return ;
                            }
                this.repaint();
                jLabel2.setText(String.valueOf(blocksCt.getScore()));
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
    
    public void drawCon(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(201, 0, 199, 410);
        g.setColor(Color.GREEN);
        g.fillRoundRect(200, 0, 5, 405, 1, 1);
    }
    
    public void restart(){
        blocksCt = new BlocksController();
        jLabel2.setText("0");
        this.repaint();
        isRun = true;
        jLabel3.setVisible(false);
    }
    public void stop(){
        isRun=false;
        jButton2.setText("继续");
    }
    public void start(){
        isRun = true;
        jButton2.setText("暂停");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(51, 255, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setFocusCycleRoot(true);
        setFocusTraversalPolicyProvider(true);

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("楷体", 0, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 255, 255));
        jButton1.setText("重新开始");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 255, 255), new java.awt.Color(0, 255, 255), new java.awt.Color(0, 255, 255), new java.awt.Color(0, 255, 255)));
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("楷体", 0, 25)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("分数");
        jLabel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 3, true));
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel2.setFont(new java.awt.Font("楷体", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("0");
        jLabel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 3, true));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("楷体", 0, 24)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 255, 255));
        jButton2.setText("暂停");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 255, 255), new java.awt.Color(0, 255, 255), new java.awt.Color(0, 255, 255), new java.awt.Color(0, 255, 255)));
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("华文行楷", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(255, 255, 0), new java.awt.Color(0, 51, 255)));
        jLabel3.setEnabled(false);
        jLabel3.setFocusable(false);
        jLabel3.setInheritsPopupMenu(false);
        jLabel3.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(jButton2)
                        .addGap(41, 41, 41)
                        .addComponent(jButton1)))
                .addContainerGap(517, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
//        this.requestFocus();
        restart();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
//        this.requestFocus();
            if(isRun==false){
                start();
            }
            else{
                stop();
            }
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
