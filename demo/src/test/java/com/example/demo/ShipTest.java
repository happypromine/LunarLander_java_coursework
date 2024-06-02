package com.example.demo;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//тесты
class ShipTest {

    @Test
    void testSuccessfulLanding() {
        Ship ship=new Ship();
        Platform platform=new Platform();

        ship.setX(0);
        ship.setY(50);
        ship.setVX(Constants.VX_SHIP_CRASH/2);
        ship.setVY(0);

        platform.setX(0);
        platform.setY(100);
        platform.setSize(250);

        for(int i=0;i<1000;i++){
            ship.updatePosition();
            ship.platformCollision(platform);
            if (ship.hasLanded())break;
        }

        assertTrue(ship.hasLanded());
    }

    @Test
    void testFailedLanding() {
        Ship ship=new Ship();
        Platform platform=new Platform();

        ship.setX(0);
        ship.setY(50);
        ship.setVX(Constants.VX_SHIP_CRASH);
        ship.setVY(Constants.VY_SHIP_CRASH);

        platform.setX(0);
        platform.setY(100);
        platform.setSize(250);
        for (int i=0;i<1000;i++){
            ship.updatePosition();
            ship.platformCollision(platform);
            if (ship.hasCrashed())break;
        }

        assertTrue(ship.hasCrashed());

    }

    @Test
    void testOutOfBounds(){
        Ship ship=new Ship();
        ship.setX(0);
        ship.setY(50);
        ship.setVX(-1);
        ship.setVY(-1);

        while (!ship.hasBordersCollision()){
            ship.updatePosition();
        }
        assertTrue(ship.hasBordersCollision());
    }

    /* @Test
    void testLandscapeCollision(){
        Ship ship=new Ship();
        Platform platform=new Platform();

        platform.setX(900);
        platform.setY(300);
        platform.setSize(50);

        List<Point2D> points=new ArrayList<>();
        points.add(new Point2D(0,200));
        points.add(new Point2D(100,300));
        points.add(new Point2D(200,100));
        points.add(new Point2D(300,500));
        points.add(new Point2D(400,500));

        Landscape landscape= new Landscape(Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT/4, Constants.WINDOW_HEIGHT, 50, 30, platform);
        landscape.setPoints(points);
        ship.setX(280);
        ship.setY(300);
        ship.setVX(0);
        ship.setVY(0);

        for (Point2D point : landscape.getPoints()) {
            System.out.println(point.getX()+" "+point.getY());
        }

        for (int i=0; i<1000;i++){
            ship.updatePosition();
            if (ship.hasLandscapeCollision(landscape))break;
        }
        assertTrue(ship.hasLandscapeCollision(landscape));
    }*/

}