package com.example.demo;

import javafx.geometry.Point2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Ship extends GameObject{
    private double ax,ay; //acceleration
    private double vx,vy; //hor/vert speed
    private int mass; //massa
    private double g; //gravity
    private int f; //ship power
    private double fuel; //amount of fuel
    private double thrust;
    private boolean isLanded;
    private boolean isCrashed;
    private int altitude;

    public Ship() {
        super();
        isLanded=false;
        isCrashed=false;
        size = Constants.SHIP_SIZE;
        x=0;
        y=0;
        g=6.8;
        thrust=1;
        f=15000;
        mass=1000;
        fuel=5000;
    }

    //установка значений из файла
    public void setFromFile(String physfilename, String shipfilename){
        Scanner scanphys, scanship;
        try {
            scanphys = new Scanner(new File(physfilename));
            scanship = new Scanner(new File(shipfilename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        mass = scanphys.nextInt();
        f= scanphys.nextInt();
        g = scanphys.nextDouble();
        thrust = scanphys.nextDouble();

        x= scanship.nextDouble();
        y= scanship.nextDouble();
        vx= scanship.nextDouble();
        vy= scanship.nextDouble();
        fuel= scanship.nextDouble();
        scanship.close();
        scanphys.close();
    }

    //полет влево
    public void goLeft(){
        if (fuel>0){
            ax = -(double)(f/mass) / 2;
            fuel-=0.5;
        }
    }

    //полет вправо
    public void goRight(){
        if (fuel>0){
            ax = (double)(f/mass) / 2;
            fuel-=0.5;
        }
    }

    //полет вверх
    public void goUp(){
        if (fuel>0){
            ay= -(double)(f/mass) * thrust ;
            //System.out.println(ay);
            fuel-=0.5*thrust;
        }
    }

    //настройка вертикальной тяги
    public void thrustAdjustment(double level){
        if (thrust>=0) thrust += level;
        if (thrust<0) thrust=0;
    }


    //обновление позиции корабля
    public void updatePosition(){
        vx+=ax/1000;
        vy+=(ay+g)/1000;

        x+= vx;
        y+= vy;

        ax=0;
        ay=0;
    }

    //обновление высоты над платформой
    public void updateAltitude(Platform platform){
        altitude=(int)(platform.getY()-y-size);
    }

    public int getAltitude(){
        return altitude;
    }

    public double getFuel(){
        return fuel;
    }

    public double getVX(){
        return vx;
    }

    public double getVY(){
        return vy;
    }

    public double getThrust(){
        return thrust;
    }

    public boolean hasLanded(){
        return isLanded;
    }

    public boolean hasCrashed(){
        return isCrashed;
    }

    public void setCrashed(boolean crashed){
        isCrashed=crashed;
    }

    public void setLanded(boolean landed){
        isLanded=landed;
    }

    public void setVX(double vx){
        this.vx=vx;
    }

    public void setVY(double vy){
        this.vy=vy;
    }

    public void collisionFloor(){
        if (y+size>=Constants.WINDOW_HEIGHT){
            y=Constants.WINDOW_HEIGHT-size;
            vy=0;
        }
    }

    //проверка коллизии с платформой, мягкая она или нет
    public void platformCollision(Platform platform){
        if (y+size>=platform.getY() && x>=platform.getX()-(double)size/3 && x<=platform.getX()+platform.getSize()+(double)size/3){
            y=platform.getY()-size;
            if (Math.abs(vy)<=Constants.VY_SHIP_CRASH && Math.abs(vx)<=Constants.VX_SHIP_CRASH){
                isLanded=true;
            }else{
                isCrashed=true;
            }
        }
    }

    //проверка коллизии с границами
    public boolean hasBordersCollision() {
            if (x >= Constants.WINDOW_WIDTH || x + size <= 0 || y + size <= 0){
                return true;
            } else { return false;}
    }

    //проверка коллизии с ландшафтом
    public boolean hasLandscapeCollision(Landscape landscape){
        for (int i=1; i<landscape.getPoints().size();i++){
            Point2D p1=landscape.getPoints().get(i-1);
            Point2D p2=landscape.getPoints().get(i);
            Point2D rectPos= new Point2D(x,y);

            if (lineIntersectsLine(p1,p2, rectPos, new Point2D(rectPos.getX()+size,rectPos.getY())) ||
                    lineIntersectsLine(p1,p2, rectPos, new Point2D(rectPos.getX(), rectPos.getY()+size)) ||
                    lineIntersectsLine(p1, p2, new Point2D(rectPos.getX()+size,rectPos.getY()), new Point2D(rectPos.getX()+size, rectPos.getY()+size)) ||
                    lineIntersectsLine(p1, p2, new Point2D(rectPos.getX(), rectPos.getY()+size), new Point2D(rectPos.getX()+size, rectPos.getY()+size))){
                return true;
            }
        }
        return false;
    }

    //проверка пересечения двух линий по их заданным точкам
    private boolean lineIntersectsLine(Point2D p1, Point2D p2, Point2D p3, Point2D p4){
        double denominator, num_a, num_b, Ua, Ub;
        denominator = (p4.getY()-p3.getY()) * (p1.getX()-p2.getX())-(p4.getX()-p3.getX())*(p1.getY()-p2.getY());

        if (denominator == 0){
            if ((p1.getX() * p2.getY()-p2.getX()*p1.getY())*(p4.getX()-p3.getX()) - (p3.getX()*p4.getY()-p4.getX()*p3.getY())*(p2.getX()-p1.getX()) == 0 && (p1.getX() * p2.getY()-p2.getX()*p1.getY())*(p4.getY()-p3.getY()) - (p3.getX()*p4.getY()-p4.getX()*p3.getY())*(p2.getY()-p1.getY()) == 0)
                return true;
            else
                return false;
        } else{
            num_a=(p4.getX()-p2.getX())*(p4.getY()-p3.getY())-(p4.getX()-p3.getX())*(p4.getY()-p2.getY());
            num_b=(p1.getX()-p2.getX())*(p4.getY()-p2.getY())-(p4.getX()-p2.getX())*(p1.getY()-p2.getY());
            Ua=num_a/denominator;
            Ub=num_b/denominator;
            return Ua>=0 && Ua<=1 && Ub>=0 && Ub<=1;
        }
    }
}
