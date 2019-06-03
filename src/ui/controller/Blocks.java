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
         {0,1,0,0,1,1,1,0,0,0,0,0,0,0,0,0},//͹���η�����0
        {0,1,0,0,0,1,1,0,0,1,0,0,0,0,0,0},//��
        {0,0,0,0,1,1,1,0,0,1,0,0,0,0,0,0},//��
        {0,1,0,0,1,1,0,0,0,1,0,0,0,0,0,0},//��
        {0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0},//һ������4
        {0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0},//��
        {0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0},//��
        {0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0},//��
        {0,0,0,0,0,1,0,0,0,1,1,1,0,0,0,0},//�ҡ�������8
        {0,0,0,0,0,1,1,0,0,1,0,0,0,1,0,0},//��
        {0,0,0,0,1,1,1,0,0,0,1,0,0,0,0,0},//��
        {0,0,1,0,0,0,1,0,0,1,1,0,0,0,0,0},//��
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//��������12
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//������
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//������
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//������
        {1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0},//2������16
        {0,0,1,1,0,1,1,0,0,0,0,0,0,0,0,0},//������
        {0,0,1,0,0,1,1,0,0,1,0,0,0,0,0,0},//������
        {0,1,0,0,0,1,1,0,0,0,1,0,0,0,0,0},//������
        {0,0,0,0,0,0,1,0,1,1,1,0,0,0,0,0},//���������20
        {0,0,0,0,0,1,1,0,0,0,1,0,0,0,1,0},//��
        {0,0,0,0,0,1,1,1,0,1,0,0,0,0,0,0},//��
        {0,0,1,0,0,0,1,0,0,0,1,1,0,0,0,0},//��
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
