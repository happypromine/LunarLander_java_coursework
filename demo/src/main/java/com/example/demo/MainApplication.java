package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import java.io.IOException;

//класс запускающий окно и сам проект
public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        stage.setTitle("Lunar Lander");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        //проверка нажатий клавиш
        scene.setOnKeyPressed(e->{
            if (e.getCode() == KeyCode.LEFT)
                Controller.left = true;
            if (e.getCode() == KeyCode.RIGHT)
                Controller.right = true;
            if (e.getCode() == KeyCode.UP)
                Controller.up = true;
        });

        //проверка отжатия клавиш
        scene.setOnKeyReleased(e->{
            if (e.getCode() == KeyCode.EQUALS)
                Controller.plusThrust=true;
            if (e.getCode() == KeyCode.MINUS)
                Controller.minusThrust=true;
            if (e.getCode() == KeyCode.LEFT)
                Controller.left = false;
            if (e.getCode() == KeyCode.RIGHT)
                Controller.right = false;
            if (e.getCode() == KeyCode.UP)
                Controller.up = false;
        });

    }


    public static void main(String[] args) {
        launch();
    }
}
