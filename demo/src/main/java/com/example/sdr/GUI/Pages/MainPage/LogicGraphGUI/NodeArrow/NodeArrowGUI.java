package com.example.sdr.GUI.Pages.MainPage.LogicGraphGUI.NodeArrow;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class NodeArrowGUI extends Group{
    //Arrow Style
    private double height;
    private double width;
    
    //Arrow Angle
    private double angle;

    //Arrow Scale
    private Scale scale;
    private double scaleSize;

    public void setSize(double scaleSizeX,double scaleSizeY){
        scale.setX(scaleSizeX);
        scale.setY(scaleSizeY);
    }

    private void calculateSize(double width, double height){
        double scaleSizeX = width / 30;
        double scaleSizeY = height / 30;

        setSize(scaleSizeX, scaleSizeY);
    }

    private void setAngle(double angle){
        this.angle = angle;
        this.setRotate(angle);
    }

    public NodeArrowGUI(double height, double width, double angle){
        Double[] points = {15.0,0.0,0.0,30.0,30.0,30.0};

        //Create Triangle
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(points);
        triangle.setFill(Color.PURPLE);
        triangle.setStroke(Color.BLACK);
        triangle.setStrokeWidth(0.5);

        //Create Scale
        scale = new Scale();
        scale.setX(1);
        scale.setY(1);
        calculateSize(width, height);
        triangle.getTransforms().add(scale);

        //Set Angle
        setAngle(angle);

        this.getChildren().add(triangle);
    }
}
