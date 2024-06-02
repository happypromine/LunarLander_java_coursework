package com.example.demo;


//класс реализующий бота
public class Bot {
    private Ship ship;
    private Platform platform;

    public Bot(Ship ship, Platform platform) {
        this.ship = ship;
        this.platform = platform;
    }

    public void update(){
        double x=ship.getX();
        double vx=ship.getVX();
        double vy=ship.getVY();
        double platformX=platform.getX();

        //если правая сторона корабля не дошла до платформы, то перемещаемся вправо
        //иначе если скорость больше нуля, то идем влево для выравнивания
        if (x+ship.getSize()<platformX){
            if (vx<1)ship.goRight();
            if (vy>0)ship.goUp();
        } else{

            if (vx>0)ship.goLeft();
                else if (x<platformX)ship.goRight();
        }

        //если правая сторона корабля ушла за платформу + некоторую погрешность, то идем влево и вверх чтобы левитировать на месте
        if (x+ship.getSize()>=platformX+platform.getSize()+(double)ship.getSize()/3){
            if (vx>-Constants.VX_SHIP_CRASH)ship.goLeft();
            if (vy>0)ship.goUp();
        }

        //если корабль находится в пределах платформы, то начинаем медленное снижение, и скорость по X держим у нуля
        if (x>=platformX-(double)ship.getSize()/3 && x+ship.getSize()<platformX+platform.getSize()+(double)ship.getSize()/3){
            if (vx>0)ship.goLeft();
            if (vx<0)ship.goRight();
            if (vy>=Constants.VY_SHIP_CRASH/1.2)ship.goUp();
        }
    }
}
