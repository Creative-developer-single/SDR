package com.example.sdr.GUI.Pages.MainPage.LogicGraphGUI;

import javax.swing.GroupLayout.Alignment;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class LogicNodeGUI extends Group{
    //Node Style
    private Rectangle background;//background
    private Border border;//border

    //Node Size
    private double width;
    private double height;

    //Node Position
    private double x;
    private double y;

    //Node Layout
    private Pane nodeLayout;
    private Pane mainLayout;
    private Pane titleContainer;
    private ImageView imageView;
    private Image image;

    private FlowPane titleLayout;

    //Node Properties
    private Image nodeIcon;
    private Text nodeTitle;//text
    private Text nodeDescription;//text

    //Node Data
    private String title;
    private String description;

    private void createLayoutBasedOnCordinates(){
        //Create the nodeLayout
        nodeLayout = new Pane();
        nodeLayout.setPrefSize(width, height);

        //Create the Background
        background = new Rectangle(width,height);
        background.setFill(Color.WHITE);
        nodeLayout.getChildren().add(background);

        //Create the MainLayout
        mainLayout = new Pane();
        mainLayout.setPrefSize(width, height);
        mainLayout.setLayoutX(0);
        mainLayout.setLayoutY(0);
        nodeLayout.getChildren().add(mainLayout);

        //Create the TitleContainer
        titleContainer = new Pane();
        mainLayout.getChildren().add(titleContainer);
        
        //Create the Image
        image = new Image(this.getClass().getResourceAsStream("/Images/ALU.png"));
        imageView = new ImageView(image);
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        imageView.setLayoutX(50);
        imageView.setLayoutY(25);
        titleContainer.getChildren().add(imageView);

        //Create the Title
        nodeTitle = new Text(title);
        nodeTitle.setTextAlignment(TextAlignment.CENTER);
        nodeTitle.setFont(new Font("Simsun", 14));
        nodeTitle.setLayoutX(150);
        nodeTitle.setLayoutY(60);
        titleContainer.getChildren().add(nodeTitle);

        this.getChildren().add(nodeLayout);
    }

    /*
    private void createMainLayoutBasedOnCordinates(){

        //Create the nodeLayout
        nodeLayout = new StackPane();

        //Create the Background
        background = new Rectangle(width,height);
        background.setFill(Color.RED);
        nodeLayout.getChildren().add(background);

        //Create the MainLayout
        mainLayout = new VBox();
        mainLayout.setAlignment(Pos.CENTER);
        nodeLayout.getChildren().add(mainLayout);

        //Create the TitleContainer
        titleContainer = new HBox();
        titleContainer.setLayoutX(200);
        titleContainer.setLayoutY(0);
        mainLayout.getChildren().add(titleContainer);

        //Create the Image
        image = new Image(this.getClass().getResourceAsStream("/Images/ALU.png"));
        imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        titleContainer.getChildren().add(imageView);

        //Create the Title
        nodeTitle = new Text(title);
        nodeTitle.setTextAlignment(TextAlignment.CENTER);
        titleContainer.getChildren().add(nodeTitle);

        //Create the Description
        nodeDescription = new Text(description);
        nodeDescription.setTextAlignment(TextAlignment.CENTER);

        mainLayout.getChildren().add(nodeDescription);

        this.getChildren().add(nodeLayout);

    }*/

    /* 
    private void createMainLayout(){
        //Create the Layout
        nodeLayout = new StackPane();

        //Create the Background
        background = new Rectangle(width,height);
        background.setFill(Color.WHITE);
        nodeLayout.getChildren().add(background);

        //Create the MainLayout
        mainLayout = new VBox();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setSpacing(10);
        nodeLayout.getChildren().add(mainLayout);

        //create the titleContainer
        titleContainer = new HBox();
        image = new Image(this.getClass().getResourceAsStream("/Images/ALU.png"));
        imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        titleContainer.getChildren().add(imageView);

        //Create the Title
        nodeTitle = new Text("Test");
        nodeTitle.setTextAlignment(TextAlignment.CENTER);
        titleContainer.getChildren().add(nodeTitle);

        //Create the Description
        nodeDescription = new Text(description);

        //Properties
        mainLayout.setMargin(titleContainer, new Insets(20,20,20,20));
        mainLayout.setMargin(nodeDescription, new Insets(20,20,20,20));
        
        ObservableList list = mainLayout.getChildren();
        list.addAll(titleContainer,nodeDescription);

        this.getChildren().add(nodeLayout);

    }
        */

    //Generate Constructor for Child
    public LogicNodeGUI(double width,double height){
        this.width = width;
        this.height = height;
    }

    public LogicNodeGUI(double width,double height,String title,String descripString){
        this.width = width;
        this.height = height;
        this.title = title;
        this.description = descripString;

        //createMainLayout();
        //createMainLayoutBasedOnCordinates();
        createLayoutBasedOnCordinates();
        /*
        //Create the Layout
        nodeLayout = new StackPane();
        mainLayout = new FlowPane();
        titleLayout = new FlowPane();

        this.getChildren().add(nodeLayout);

        //Set Background
        background = new Rectangle(width,height);
        background.setFill(Color.WHITE);
        this.nodeLayout.getChildren().add(background);

        
        //Add FlowPane
        nodeLayout.getChildren().add(mainLayout);

        mainLayout.setOrientation(javafx.geometry.Orientation.VERTICAL);
        nodeTitle = new Text(this.title);
        nodeTitle.setTextAlignment(TextAlignment.CENTER);
        mainLayout.getChildren().add(nodeTitle);

        mainLayout.getChildren().add(titleLayout);
        titleLayout.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
        nodeDescription = new Text(this.description);
        titleLayout.getChildren().add(nodeDescription);
        */
    }

}
