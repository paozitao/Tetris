/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import java.util.Arrays;
import ui.Shape;

/**
 *
 * @author white
 */
public class BlocksController {
    int currentX = 3;
    int currentY = 0;
    int[][] fix= new int[20][10];
    Shape[] shape = {new Blocks()};
    Shape   currentShape = shape[0];
    int score = 0;
    boolean isOver = false;
    
    public BlocksController(){
        for(int i = 0;i<20;i++){
            for(int j = 0;j<10;j++){
                fix[i][j]=0;
            }
        }
    }

    //获取当前方块所在的坐标XY 2，得分 ，方块16，fix固定的矩阵总共2+1+16+10*20=219个数
    public String getAll(){
        int[] meg = new int[222];
        meg[0] = currentX;
        meg[1] = currentY;
        meg[2]=score;
        int[] current = currentShape.getcurrent();
        for(int i =3;i<19;i++){
            meg[i]=current[i-3];
        }
        for(int i = 0;i<20;i++){
            for(int j = 0;j<10;j++){
                meg[i*10+j+18]=fix[i][j];
            }
        }
        String t=Arrays.toString(meg);
        return t;
    }
    public int[][] getFix() {
        return fix;
    }

    public void setFix(int[][] fix) {
        this.fix = fix;
    }
    
    public void set(int[] local){
        currentX = local[0];
        currentY = local[1];
    }
    //获取坐标
    public int[] getLocal(){
        int[] local = {currentX,currentY};
        return local;
    }

    public int[] getAShape(){
        return currentShape.getcurrent();
    }
    
    public boolean checkOver(){
        if(isdown(currentY+1)||isTouch(currentX,currentY+1)){
            return true;
        }
        else return false;
    }
    
    public boolean isValid(int x,int y){
        
        int [] xSpan = getSpan();
        if(x+xSpan[0]<0||x+xSpan[1]>9)
        {
            return false;
        }
        return true;
        
    }
    
    public int[] getSpan(){
        int[] shape = getAShape();
        int xMin = 4;
        int xMax = 0;
        int yMin = 0;
        int yMax = 0;
        for(int i=0;i<15;i++)
        {
            if(shape[i]==1&&((i)%4>xMax)){
                xMax = (i)%4;
            }
            if(shape[i]==1&&((i)%4<xMin)){
                xMin = (i)%4;
            }
             if(shape[i]==1&&((i)/4>yMax)){
                yMax = (i)/4;
            }
            if(shape[i]==1&&((i)/4<yMin)){
                yMin = (i)/4;
            }
        }
        int[] x = {xMin,xMax,yMin,yMax};
        return x;
    }

    public boolean isTouch(int x,int y){
        int[] shape = getAShape();
         for(int i=0;i<16;i++){
             if(shape[i]==1&&fix[y+(i/4)][x+(i%4)]==1){
                 return true;
             }
         }
         return false;
        }
    
    public boolean isdown(int y){
        int[] span = getSpan();
        if(y+span[3]==20){
            return true;
        }
        return false;
    }
    
    public void clear(){
        int line = 0;
        boolean flag;
        for(int j =19;j>0;){
            flag=true;
            for(int i =0;i<10;i++){
                    if(fix[j][i]==0){
                        flag= false;
                    }
            }
              if(flag){
                line++;
                for(int t = j;t>1;t--){
                    for(int i = 0;i<10;i++){
                        fix[t][i]=fix[t-1][i];
                    }
                }
            }
              else{
                  j--;
              }
          }
        if(line>0){
            score = (int) (score + Math.pow(2, line));
        }

    }
    public void down(){
        int[] shape = getAShape();
        if(isdown(currentY+1)||isTouch(currentX,currentY+1)){
            for(int i=0;i<16;i++){
                if(shape[i]==1){
                    fix[currentY+i/4][currentX+i%4]=1;
                }
            }
         add();
         if(checkOver()){
             isOver = true;
         }
        }
        else{
            currentY++;
        }
    }
    
    public void add(){
        currentShape.random();
        currentX = 3;
        currentY = 0;
        clear();
    }
    
    public void left(){
        if(isValid(currentX-1,currentY)&&!isTouch(currentX-1,currentY)){
            currentX-=1;
        }
        
    }
    public void right(){
        if(isValid(currentX+1,currentY)&&!isTouch(currentX+1,currentY)){
            currentX++;
        }
    }
    public void turn(){
        currentShape.next();
        if(isValid(currentX,currentY))
        {
            
        }
        else{
            currentShape.forward();
        }

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isIsOver() {
        return isOver;
    }

}
