package com.example.sdr.GUI.Pages.MainPage.LogicGraphGUI.BaseALUNode;

import com.example.sdr.GUI.Pages.MainPage.LogicGraphGUI.LogicNodeGUI;
import com.example.sdr.GUI.Pages.MainPage.LogicGraphGUI.NodeArrow.NodeArrowGUI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class BaseALUNodeGUI extends LogicNodeGUI{
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
    private String title = "基本运算模块";
    private String description = "工作模式: ";

    //Node Connection
    private int inputCount;
    private int outputCount;

    public BaseALUNodeGUI(double width, double height,int inputCount,int outputCount) {
        super(width, height);
        this.width = width;
        this.height = height;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        createGUI();
    }

    public void createGUI(){
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

        //Create the Input and Output
        NodeArrowGUI nodeArrowGUI = new NodeArrowGUI(30,30,90);
        nodeArrowGUI.setLayoutX(0);
        nodeArrowGUI.setLayoutY(85);   
        mainLayout.getChildren().add(nodeArrowGUI);     

        this.getChildren().add(nodeLayout);
    }
}
