package com.example.sdr.GUI.Pages.MainPage.LogicGraphGUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LogicNodeGUIDebug extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        LogicNodeGUI logicNodeGUI = new LogicNodeGUI(300,200,"基本运算模块","This is a test");
        primaryStage.setTitle("Logic Node GUI");
        primaryStage.setScene(new Scene(logicNodeGUI));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
