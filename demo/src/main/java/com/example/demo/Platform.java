package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

//класс платформы
public class Platform extends GameObject {

    public Platform() {
        super();
    }

    //установка значений из файла
    public void setFromFile(String filename){
        Scanner scan;
        try {
            scan = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        x=scan.nextInt();
        y=scan.nextInt();
        size=scan.nextInt();

        if (size>Constants.MAX_PLATFORM_SIZE)size=Constants.MAX_PLATFORM_SIZE;
        if(x+size>Constants.WINDOW_WIDTH)x=Constants.WINDOW_WIDTH-size;
        if(x<0)x=0;
        if(y+Constants.PLATFORM_HEIGHT>Constants.WINDOW_HEIGHT)y=Constants.WINDOW_HEIGHT-Constants.PLATFORM_HEIGHT;
    }

    @Override
    public void setRandomPos(int minWidth, int maxWidth, int minHeight,  int maxHeight){
        Random rand = new Random();
        x=rand.nextInt(maxWidth-minWidth)+minWidth;
        y=rand.nextInt(maxHeight-minHeight)+minHeight;
        size=rand.nextInt(Constants.MAX_PLATFORM_SIZE-Constants.SHIP_SIZE)+Constants.SHIP_SIZE;

        if (x+size>maxWidth)x=maxWidth-size;
        if (y+Constants.PLATFORM_HEIGHT>maxHeight)y=maxHeight-Constants.PLATFORM_HEIGHT;
    }

    @Override
    public void setSize(int size){
        if (size>Constants.MAX_PLATFORM_SIZE)this.size=Constants.MAX_PLATFORM_SIZE;
            else this.size=size;
    }

}
