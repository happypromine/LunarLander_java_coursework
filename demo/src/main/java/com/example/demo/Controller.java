package com.example.demo;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Controller {
    private Ship ship;
    private Platform platform;
    private Landscape landscape;
    private Bot bot;
    @FXML
    private ImageView shipRectangle;
    @FXML
    private Label labelAttitude;
    @FXML
    private Label labelFuel;
    @FXML
    private Label labelHorizontal;
    @FXML
    private Label labelVertical;
    @FXML
    private Label labelX;
    @FXML
    private Label labelY;
    @FXML
    private Label labelThrust;
    @FXML
    private Label labelWinLose;
    @FXML
    private Button restartButton;
    @FXML
    private Rectangle platformRectangle;
    @FXML
    private Canvas landscapeCanvas;
    @FXML
    private ChoiceBox<String> levelLoad;
    @FXML
    private Pane menuPane;
    @FXML
    private Button startBtn;
    @FXML
    private ChoiceBox<String> whoPlayer;

    private Image shipImg, shipUpImg, shipRightImg, shipLeftImg, crashedShipImg;
    public static boolean right, left, up, plusThrust, minusThrust, isBotPlaying;


    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
                boolean movement=false;

                //Играет либо бот, либо игрок
                if (isBotPlaying){
                    bot.update();
                } else {
                    if (plusThrust){
                        ship.thrustAdjustment(0.1);
                        plusThrust = false;
                    }
                    if (minusThrust){
                        ship.thrustAdjustment(-0.1);
                        minusThrust = false;
                    }
                    if (right){
                        ship.goRight();
                        shipRectangle.setImage(shipRightImg);
                        movement=true;
                    }
                    if (left){
                        ship.goLeft();
                        shipRectangle.setImage(shipLeftImg);
                        movement=true;
                    }
                    if (up){
                        ship.goUp();
                        shipRectangle.setImage(shipUpImg);
                        movement=true;
                    }
                    if (!movement)shipRectangle.setImage(shipImg);
                }

                //обновление позиции корабля и высоты над платформой
                ship.updatePosition();
                ship.updateAltitude(platform);

                //обновление позиции изображения
                shipRectangle.setLayoutX(ship.getX());
                shipRectangle.setLayoutY(ship.getY());

            //проверка коллизии с платформой землей и границами окна
                ship.platformCollision(platform);
                if (ship.hasLanded() || ship.hasCrashed() || ship.hasBordersCollision() || ship.hasLandscapeCollision(landscape)){
                    labelWinLose.setVisible(true);
                    restartButton.setVisible(true);

                    if (ship.hasLanded())labelWinLose.setText("Вы успешно призелились!");
                    if (ship.hasCrashed())labelWinLose.setText("Вы разбились!");
                    if (ship.hasBordersCollision())labelWinLose.setText("Вы улетели в небытие!");
                    if (ship.hasLandscapeCollision(landscape))labelWinLose.setText("Вы разбились о землю!");
                    if(!ship.hasLanded())shipRectangle.setImage(crashedShipImg);
                    ship.setVX(0.3);
                    ship.setVY(0);
                    ship.setCrashed(false);
                    ship.setLanded(false);
                    timer.stop();
                }

                //проверка на конец топлива
                if (ship.getFuel()<=0){
                    labelWinLose.setVisible(true);
                    labelWinLose.setText("ТОПЛИВО ЗАКОНЧИЛОСЬ! GAME OVER!");
                    timer.stop();
                }

            //обновление параметров для вывода игроку
                labelAttitude.setText("Высота над платформой: " + ship.getAltitude());
                labelVertical.setText("Вертикальная скорость: " + String.format("%.0f", ship.getVY()*100));
                labelHorizontal.setText("Горизонтальная скорость: " + String.format("%.0f", ship.getVX()*100));
                labelX.setText("X: " + String.format("%.0f", ship.getX()));
                labelY.setText("Y: " + String.format("%.0f", ship.getY()));
                labelFuel.setText("Количество топлива: " + String.format("%.0f", ship.getFuel()));
                labelThrust.setText("Тяга: "+ String.format("%.0f",ship.getThrust()*100)+"%");
        }
    };

    public void initialize() {

        ship = new Ship();
        platform = new Platform();

        //подгрузка изображений для корабля
        shipImg=new Image("file:images/ship_idle.png");
        shipLeftImg=new Image("file:images/ship_left.png");
        shipUpImg=new Image("file:images/ship_up.png");
        shipRightImg=new Image("file:images/ship_right.png");
        crashedShipImg=new Image("file:images/ship_crash.png");

        platformRectangle.setVisible(false);

        whoPlayer.getItems().addAll("Player", "Bot");
        whoPlayer.setValue("Player");

        levelLoad.getItems().addAll("Random","File");
        levelLoad.setValue("Random");
    }

    //кнопка рестарта
    public void restartBtnClick(){
        ship.setRandomPos(0, Constants.WINDOW_WIDTH/6,0,Constants.WINDOW_HEIGHT/6);

        platform.setRandomPos(Constants.WINDOW_WIDTH/3, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT/2, Constants.WINDOW_HEIGHT);
        platformRectangle.setLayoutX(platform.getX());
        platformRectangle.setLayoutY(platform.getY());
        platformRectangle.setWidth(platform.getSize());

        landscape.generateLandscape(Constants.WINDOW_WIDTH,(int)(Constants.WINDOW_HEIGHT/4), Constants.WINDOW_HEIGHT, 41, 30, platform);
        drawLandscape();

        restartButton.setVisible(false);
        labelWinLose.setVisible(false);

        shipRectangle.setImage(shipImg);
        timer.start();
    }

    //кнопка начала
    public void startBtnClick(){
        menuPane.setVisible(false);

        if (levelLoad.getValue()=="File") {
            ship.setFromFile("data/physdata.txt","data/shipdata.txt");
            platform.setFromFile("data/platformdata.txt");
        }else{
            ship.setRandomPos(0, Constants.WINDOW_WIDTH/6,0,Constants.WINDOW_HEIGHT/6);
            platform.setRandomPos(Constants.WINDOW_WIDTH/3, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT/2, Constants.WINDOW_HEIGHT);
        }

        isBotPlaying = whoPlayer.getValue() == "Bot";

        landscape = new Landscape(Constants.WINDOW_WIDTH,(int)(Constants.WINDOW_HEIGHT/4), Constants.WINDOW_HEIGHT, 50, 30, platform);
        bot = new Bot(ship, platform);

        platformRectangle.setLayoutX(platform.getX());
        platformRectangle.setLayoutY(platform.getY());
        platformRectangle.setWidth(platform.getSize());
        platformRectangle.setHeight(Constants.PLATFORM_HEIGHT);
        platformRectangle.setVisible(true);
        shipRectangle.setLayoutX(ship.getX());
        shipRectangle.setLayoutY(ship.getY());
        shipRectangle.setFitWidth(ship.getSize());
        shipRectangle.setFitHeight(ship.getSize());
        shipRectangle.setImage(shipImg);

        drawLandscape();
        timer.start();
    }

    //метод отрисовки ландшафта
    public void drawLandscape(){
        GraphicsContext gc = landscapeCanvas.getGraphicsContext2D();
        gc.setStroke(Color.web("#bfbfbf"));
        gc.setLineWidth(2);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, landscapeCanvas.getWidth(), landscapeCanvas.getHeight());
        Point2D prevPoint=landscape.getPoints().getFirst();
        for (Point2D point : landscape.getPoints()){
            gc.strokeLine(prevPoint.getX(), prevPoint.getY(), point.getX(), point.getY());
            prevPoint=point;
        }
    }

}