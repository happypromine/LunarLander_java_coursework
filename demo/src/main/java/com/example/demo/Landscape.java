package com.example.demo;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//класс ландшафта
public class Landscape {
    private List<Point2D> points;

    public Landscape(int width, int heightUp, int heightDown, int numPoints, int pointDiff, Platform platform) {
        points = new ArrayList<>();
        generateLandscape(width, heightUp, heightDown, numPoints, pointDiff, platform);
    }

    //генерация ландшафта в определенных рамках, с определенным количеством точек, относительно определенной платформы
    public void generateLandscape(int width, int heightUp, int heightDown, int numPoints, int pointDiff, Platform platform) {
        points.clear();
        Random rand = new Random();
        double platformStart = platform.getX();
        double platformEnd = platform.getX()+platform.getSize();
        int segmentWidth = width/(numPoints-1);
        double pointX,pointY;
        Point2D prevPoint;

        //генерируем ландшафт до начала платформы
        points.add(new Point2D(0,rand.nextInt(heightDown-heightUp)+heightUp));
        for (int i = 1; i < numPoints; i++) {
                pointX=i*segmentWidth;

                if (pointX>=platformStart) {
                    pointX=platformStart;
                    pointY=platform.getY()+Constants.PLATFORM_HEIGHT;

                    double tempX=points.get(i-1).getX();
                    double tempY=(points.get(i-1).getY()+pointY)/2;
                    points.remove(i-1);
                    points.add(new Point2D(tempX, tempY));
                    points.add(new Point2D(pointX,pointY));
                    break;
                } else {
                    prevPoint=points.get(i-1);
                    pointY=prevPoint.getY()+rand.nextInt(2 * pointDiff)-pointDiff;
                    if (pointY<heightUp)pointY=heightUp;
                    if (pointY>heightDown)pointY=heightDown;
                    points.add(new Point2D(pointX,pointY));
                }
        }

        //генерируем ландшафт после платформы и до края
        points.add(new Point2D(platformEnd, platform.getY()+Constants.PLATFORM_HEIGHT));
        prevPoint=points.getLast();
        double lastX = platformEnd;
        while (lastX <= width){
            lastX+=segmentWidth;
            pointY = prevPoint.getY()+rand.nextInt(2 * pointDiff)-pointDiff;
            if (pointY<heightUp)pointY=heightUp;
            if (pointY>heightDown)pointY=heightDown;
            points.add(new Point2D(lastX,pointY));
            prevPoint=points.getLast();
        }

        /*for (Point2D point : points) {
            System.out.println(point.getX()+" "+point.getY());
        }*/
    }

    public List<Point2D> getPoints() {
        return points;
    }

    public void setPoints(List<Point2D> points) {
        this.points = points;
    }
}
