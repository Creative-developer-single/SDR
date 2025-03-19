package com.example.sdr.GUI.Pages.MainPage.LogicGraphGUI.NodeArrow;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NodeArrowGUIDebug extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        NodeArrowGUI nodeArrowGUI = new NodeArrowGUI(120,120,90);
        primaryStage.setTitle("Node Arrow GUI");
        primaryStage.setScene(new Scene(nodeArrowGUI));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
