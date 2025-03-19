package com.example.sdr.GUI.Pages.MainPage.LogicGraphGUI.BaseALUNode;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BaseALUNodeGUIDebug extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        BaseALUNodeGUI baseALUNodeGUI = new BaseALUNodeGUI(300,200,2,1);
        primaryStage.setTitle("Base ALU Node GUI");
        primaryStage.setScene(new Scene(baseALUNodeGUI));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
