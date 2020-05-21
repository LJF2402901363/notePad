package com.moyisuiying.app;

import com.moyisuiying.view.NodePadPane;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author 陌意随影
 * @create 2020-03-23 14:34
 * @desc 程序入口
 **/
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("NotePad");
        NodePadPane nodePadPane = new NodePadPane();
        Scene scene = new Scene(nodePadPane,800,600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
