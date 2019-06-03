/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import static java.lang.System.currentTimeMillis;
import ui.Shape;

/**
 *
 * @author white
 */
public class Blocks extends Shape{
    int[][] blocks={
         {0,1,0,0,1,1,1,0,0,0,0,0,0,0,0,0},//凸字形方块上0
        {0,1,0,0,0,1,1,0,0,1,0,0,0,0,0,0},//右
        {0,0,0,0,1,1,1,0,0,1,0,0,0,0,0,0},//下
        {0,1,0,0,1,1,0,0,0,1,0,0,0,0,0,0},//左
        {0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0},//一条形上4
        {0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0},//右
        {0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0},//下
        {0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0},//左
        {0,0,0,0,0,1,0,0,0,1,1,1,0,0,0,0},//右√字形上8
        {0,0,0,0,0,1,1,0,0,1,0,0,0,1,0,0},//右
        {0,0,0,0,1,1,1,0,0,0,1,0,0,0,0,0},//下
        {0,0,1,0,0,0,1,0,0,1,1,0,0,0,0,0},//左
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//点字形上12
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//点字形
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//点字形
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//点字形
        {1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0},//2字形上16
        {0,0,1,1,0,1,1,0,0,0,0,0,0,0,0,0},//点字形
        {0,0,1,0,0,1,1,0,0,1,0,0,0,0,0,0},//点字形
        {0,1,0,0,0,1,1,0,0,0,1,0,0,0,0,0},//点字形
        {0,0,0,0,0,0,1,0,1,1,1,0,0,0,0,0},//左√字形上20
        {0,0,0,0,0,1,1,0,0,0,1,0,0,0,1,0},//右
        {0,0,0,0,0,1,1,1,0,1,0,0,0,0,0,0},//下
        {0,0,1,0,0,0,1,0,0,0,1,1,0,0,0,0},//左
    };

    public int[][] getBlocks() {
        return blocks;
    }
//    Random rd = new Random();
//    int state=rd.nextInt(23);
//    public void random(){
//        state = rd.nextInt(23);
//    }
//    
    long state = currentTimeMillis()%(blocks.length-1);
    public void random(){
        state = currentTimeMillis()%(blocks.length-1);
    }
    public void next(){
           if(state%4>=3){
               state = state-3;
           }else{
            state++;
           }

    }
    public void forward(){
        if(state%4<=0){
            state=state+3;
        }
        else {
            state--;
        }
    }
    public int[] getcurrent(){
        return blocks[(int)state];
    }
    
}
