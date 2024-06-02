package com.example.demo;

import java.util.Random;

//класс игрового объекта, содержащий координаты и размер
public class GameObject {
    protected double x;
    protected double y;
    protected int size;

    public GameObject() {

    }

    //установка случайной позиции по заданным границам
    public void setRandomPos(int minWidth, int maxWidth, int minHeight,  int maxHeight){
        Random rand = new Random();
        x=rand.nextInt(maxWidth-minWidth)+minWidth;
        y=rand.nextInt(maxHeight-minHeight)+minHeight;
        if (x+size>maxWidth)x=maxWidth-size;
        if (y+size>maxHeight)y=maxHeight-size;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public int getSize() {
        return size;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setSize(int size) {
        this.size = size;
    }

}
