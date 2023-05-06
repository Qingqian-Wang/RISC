package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class MyJavaFX extends Application {

    private static final String USERNAME = "";

    private static final String PASSWORD = "";
    boolean multiple = false;
    private String systemMessage = "The total number of unites is 50,\n please assign the units to the territories.\n";

    String fileName = "users.txt";
    String newUserName = "";
    String newPassword = "";

    boolean userExists = false;
    String line;

    int firstInGame = 0;
    String joinGameMessage;

    public MyJavaFX() throws FileNotFoundException {
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Player player = new Player(9999);
        player.connectToServerForFrontend();
//Obtain available game id list
        ArrayList<Integer> availableList = player.getAvailableList();
//set the title of the window
        primaryStage.setTitle("Risc Game");

        Media click = new Media(new File("src/main/resources/Click.mp3").toURI().toString());
        MediaPlayer clickPlayer = new MediaPlayer(click);

// create login ui
        Label headerLabel = new Label("Login");
        headerLabel.setFont(new Font("Arial", 20));
        GridPane loginGridPane = new GridPane();
        //set background image
        Image image = new Image("file:src/main/resources/bg1.jpg");
        //背景自适应


        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        loginGridPane.setBackground(background);
        loginGridPane.setAlignment(Pos.CENTER);
        loginGridPane.setHgap(10);
        loginGridPane.setVgap(10);
//        loginGridPane.setPadding(new Insets(25, 25, 25, 25));

// FileReader fileReader = new FileReader(fileName);
// BufferedReader bufferedReader = new BufferedReader(fileReader);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setTextFill(Color.WHITE);
        usernameLabel.setStyle("-fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Please enter your username");
        Label passwordLabel = new Label("Password:");
        passwordLabel.setTextFill(Color.WHITE);
        passwordLabel.setStyle("-fx-font-weight: bold;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Please enter your password");
        Button loginButton = new Button("Login");
        Button forgotPasswordButton = new Button("Find password");
        Button registerButton = new Button("Register");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.getChildren().addAll(loginButton, forgotPasswordButton, registerButton);

        loginGridPane.add(usernameLabel, 0, 0);
        loginGridPane.add(usernameField, 1, 0);
        loginGridPane.add(passwordLabel, 0, 1);

        //        play backgroud music in javafx
        Media media = new Media(new File("src/main/resources/He'S A Pirate.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        ToggleButton muteButton = new ToggleButton("Mute");
        muteButton.selectedProperty().bindBidirectional(mediaPlayer.muteProperty());
        loginGridPane.add(muteButton, 2, 3);

        //set the volume of the music
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(1);
        slider.setValue(0.5);
        slider.valueProperty().bindBidirectional(mediaPlayer.volumeProperty());
        loginGridPane.add(slider, 1, 3);

        //exit the game
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event -> {

            System.exit(0);
        });


        loginGridPane.add(passwordField, 1, 1);
        loginGridPane.add(buttonBox, 1, 2);
        loginGridPane.add(exitButton, 2, 2);
        Label statusLabel = new Label();

        BorderPane root1 = new BorderPane();
        BorderPane root2 = new BorderPane();

        Scene scene1 = new Scene(root1, 800, 800);
        Scene scene2 = new Scene(root2, 800, 800);
        GridPane joinGridPane = new GridPane();

// Create the Join button
        Button game1Button = new Button("Game 1");
        Button game2Button = new Button("Game 2");
        Button game1_2Button = new Button("Game 1&2");

// 登录按钮事件处理程序
        loginButton.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username.equals(USERNAME) && password.equals(PASSWORD)) {
// 登录成功，显示主界面
                if (availableList.contains(1)) {
                    game1Button.setDisable(false);
                }else {
                    game1Button.setDisable(true);
                }if (availableList.contains(2)) {
                    game2Button.setDisable(false);
                }else {
                    game2Button.setDisable(true);
                }if (availableList.contains(1) && availableList.contains(2)) {
                    game1_2Button.setDisable(false);
                }else {
                    game1_2Button.setDisable(true);
                }// primaryStage.setScene(new Scene(root1, 800, 600));
                //add background picture
                Image image1 = new Image("file:src/main/resources/bg2.jpg");
                BackgroundSize backgroundSize1 = new BackgroundSize(100, 100, true, true, true, false);
                BackgroundImage backgroundImage1 = new BackgroundImage(image1, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize1);
                Background background1 = new Background(backgroundImage1);
                joinGridPane.setBackground(background1);
                Scene joinGame = new Scene(joinGridPane, 800, 800);
                primaryStage.setScene(joinGame);


            }else {
// 显示错误消息
                statusLabel.setText("Invalid username or password.");
            }});

// 找回密码按钮事件处理程序
        forgotPasswordButton.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
            clickPlayer1.play();
// 显示找回密码窗口
            Label messageLabel = new Label("Please contact the administrator to reset your password.");
            messageLabel.setAlignment(Pos.CENTER);
            VBox vbox = new VBox(10);
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(messageLabel);
//确认
            Button confirmButton = new Button("Confirm");
            vbox.getChildren().add(confirmButton);
//close the window
            confirmButton.setOnAction(event1 -> {
                Stage stage = (Stage) confirmButton.getScene().getWindow();
                stage.close();
            });
            Scene scene = new Scene(vbox, 400, 300);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        });


// 注册按钮事件处理程序
        registerButton.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
            clickPlayer1.play();
// 显示注册窗口
            Label messageLabel = new Label("Please enter your username and password.");
            messageLabel.setAlignment(Pos.CENTER);
            TextField usernameField1 = new TextField();
            TextField passwordField1 = new TextField();
            Button registerButton1 = new Button("Register");
            Button cancelButton = new Button("Cancel");
            HBox buttonBox1 = new HBox(10);
            buttonBox1.setAlignment(Pos.BOTTOM_RIGHT);
            buttonBox1.getChildren().addAll(registerButton1, cancelButton);
            GridPane registerGridPane = new GridPane();
            registerGridPane.setAlignment(Pos.CENTER);
            registerGridPane.setHgap(10);
            registerGridPane.setVgap(10);
            registerGridPane.setPadding(new Insets(25, 25, 25, 25));
            registerGridPane.add(messageLabel, 0, 0);
            registerGridPane.add(usernameField1, 0, 1);
            registerGridPane.add(passwordField1, 0, 2);
            registerGridPane.add(buttonBox1, 0, 3);
            Scene scene = new Scene(registerGridPane, 300, 200);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
//注册按钮事件处理程序
            registerButton1.setOnAction(event1 -> {
                MediaPlayer clickPlayerq = new MediaPlayer(click);
                clickPlayerq.play();
                String username = usernameField1.getText();
                String password = passwordField1.getText();
                if (username.equals("") || password.equals("")) {
                    messageLabel.setText("Username or password cannot be empty.");
                }else {
//注册成功
                    messageLabel.setText("Register successfully.");
//关闭注册窗口
                    Stage stage1 = (Stage) registerButton1.getScene().getWindow();
                    stage1.close();
                }});
//取消按钮事件处理程序
            cancelButton.setOnAction(event1 -> {
                MediaPlayer clickPlayer2 = new MediaPlayer(click);
                clickPlayer2.play();

                Stage stage1 = (Stage) cancelButton.getScene().getWindow();

                stage1.close();
            });

        });
//login scene
        primaryStage.setScene(new Scene(loginGridPane, 600, 600));
//join game ui
        joinGridPane.setAlignment(Pos.CENTER);
        joinGridPane.setHgap(10);
        joinGridPane.setVgap(10);
        joinGridPane.setPadding(new Insets(25, 25, 25, 25));
        Label message = new Label("Which game you want to play?\n" +
                "If you want to join a game, select \nthe game ID button you want to join.\n");
        //set this label white
        message.setTextFill(Color.WHITE);
        message.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        Button backButton = new Button("Back");
        HBox buttonBox1 = new HBox(10);
        buttonBox1.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox1.getChildren().addAll(game1Button, game2Button, game1_2Button, backButton);
        joinGridPane.add(message, 0, 0);
        joinGridPane.add(buttonBox1, 1, 2);
// Create the Switch button
        Button switchButton1 = new Button("Switch to Game 2");
        switchButton1.autosize();
        switchButton1.setFont(new Font("Cambria", 25));
        Button switchButton2 = new Button("Switch to Game 1");
        switchButton2.setFont(new Font("Cambria", 25));


//assign the units of game1
        GridPane assignGridPaneGame1 = new GridPane();
        assignGridPaneGame1.setAlignment(Pos.CENTER);
        assignGridPaneGame1.setHgap(10);
        assignGridPaneGame1.setVgap(10);
        assignGridPaneGame1.setPadding(new Insets(25, 25, 25, 25));
        Label messageGame1 = new Label(systemMessage);
        messageGame1.setTextFill(Color.WHITE);
        messageGame1.setFont(Font.font("Arial", FontWeight.BOLD, 19));
        Label messageGame1_1 = new Label("Territory 1");
        messageGame1_1.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        messageGame1_1.setTextFill(Color.WHITE);
        Label messageGame1_2 = new Label("Territory 2");
        messageGame1_2.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        messageGame1_2.setTextFill(Color.WHITE);
        Label messageGame1_3 = new Label("Territory 3");
        messageGame1_3.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        messageGame1_3.setTextFill(Color.WHITE);
        Label messageGame1_4 = new Label("Territory 4");
        messageGame1_4.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        messageGame1_4.setTextFill(Color.WHITE);
        Label messageGame1_5 = new Label("Territory 5");
        messageGame1_5.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        messageGame1_5.setTextFill(Color.WHITE);
        TextField areaGame1_1 = new TextField();
        TextField areaGame1_2 = new TextField();
        TextField areaGame1_3 = new TextField();
        TextField areaGame1_4 = new TextField();
        TextField areaGame1_5 = new TextField();
        assignGridPaneGame1.add(messageGame1, 0, 0);
        assignGridPaneGame1.add(messageGame1_1, 0, 1);
        assignGridPaneGame1.add(areaGame1_1, 1, 1);
        assignGridPaneGame1.add(messageGame1_2, 0, 2);
        assignGridPaneGame1.add(areaGame1_2, 1, 2);
        assignGridPaneGame1.add(messageGame1_3, 0, 3);
        assignGridPaneGame1.add(areaGame1_3, 1, 3);
        assignGridPaneGame1.add(messageGame1_4, 0, 4);
        assignGridPaneGame1.add(areaGame1_4, 1, 4);
        assignGridPaneGame1.add(messageGame1_5, 0, 5);
        assignGridPaneGame1.add(areaGame1_5, 1, 5);
        Button assignButtonGame1 = new Button("Assign");
        HBox buttonBoxGame1 = new HBox(10);
        buttonBoxGame1.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBoxGame1.getChildren().addAll(assignButtonGame1);
        assignGridPaneGame1.add(buttonBoxGame1, 1, 6);

//assign the units of game2
        GridPane assignGridPaneGame2 = new GridPane();
        assignGridPaneGame2.setAlignment(Pos.CENTER);
        assignGridPaneGame2.setHgap(10);
        assignGridPaneGame2.setVgap(10);
        assignGridPaneGame2.setPadding(new Insets(25, 25, 25, 25));
        Label messageGame2 = new Label(systemMessage);
        Label messageGame2_1 = new Label("Territory 1");
        Label messageGame2_2 = new Label("Territory 2");
        Label messageGame2_3 = new Label("Territory 3");
        TextField areaGame2_1 = new TextField();
        TextField areaGame2_2 = new TextField();
        TextField areaGame2_3 = new TextField();
        assignGridPaneGame2.add(messageGame2, 0, 0);
        assignGridPaneGame2.add(messageGame2_1, 0, 1);
        assignGridPaneGame2.add(areaGame2_1, 1, 1);
        assignGridPaneGame2.add(messageGame2_2, 0, 2);
        assignGridPaneGame2.add(areaGame2_2, 1, 2);
        assignGridPaneGame2.add(messageGame2_3, 0, 3);
        assignGridPaneGame2.add(areaGame2_3, 1, 3);
        Button assignButtonGame2 = new Button("Assign");
        HBox buttonBoxGame2 = new HBox(10);
        buttonBoxGame2.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBoxGame2.getChildren().addAll(assignButtonGame2);
        assignGridPaneGame2.add(buttonBoxGame2, 1, 4);

//assign the units of game1&2
        GridPane assignGridPaneGame1_2 = new GridPane();
        assignGridPaneGame1_2.setAlignment(Pos.CENTER);
        assignGridPaneGame1_2.setHgap(10);
        assignGridPaneGame1_2.setVgap(10);
        assignGridPaneGame1_2.setPadding(new Insets(25, 25, 25, 25));
        Label messageGame1_2_ = new Label(systemMessage);
        messageGame1_2_.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        messageGame1_2_.setTextFill(Color.WHITE);
        Label messageGame1_2_1 = new Label("Territory 1");
        messageGame1_2_1.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        messageGame1_2_1.setTextFill(Color.WHITE);
        Label messageGame1_2_2 = new Label("Territory 2");
        messageGame1_2_2.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        messageGame1_2_2.setTextFill(Color.WHITE);
        Label messageGame1_2_3 = new Label("Territory 3");
        messageGame1_2_3.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        messageGame1_2_3.setTextFill(Color.WHITE);
        Label messageGame1_2_4 = new Label("Territory 4");
        messageGame1_2_4.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        messageGame1_2_4.setTextFill(Color.WHITE);
        Label messageGame1_2_5 = new Label("Territory 5");
        messageGame1_2_5.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        messageGame1_2_5.setTextFill(Color.WHITE);
        TextField areaGame1_2_1 = new TextField();
        TextField areaGame1_2_2 = new TextField();
        TextField areaGame1_2_3 = new TextField();
        TextField areaGame1_2_4 = new TextField();
        TextField areaGame1_2_5 = new TextField();
        assignGridPaneGame1_2.add(messageGame1_2_, 0, 0);
        assignGridPaneGame1_2.add(messageGame1_2_1, 0, 1);
        assignGridPaneGame1_2.add(areaGame1_2_1, 1, 1);
        assignGridPaneGame1_2.add(messageGame1_2_2, 0, 2);
        assignGridPaneGame1_2.add(areaGame1_2_2, 1, 2);
        assignGridPaneGame1_2.add(messageGame1_2_3, 0, 3);
        assignGridPaneGame1_2.add(areaGame1_2_3, 1, 3);
        assignGridPaneGame1_2.add(messageGame1_2_4, 0, 4);
        assignGridPaneGame1_2.add(areaGame1_2_4, 1, 4);
        assignGridPaneGame1_2.add(messageGame1_2_5, 0, 5);
        assignGridPaneGame1_2.add(areaGame1_2_5, 1, 5);
        Button assignButtonGame1_2 = new Button("Assign");
        HBox buttonBoxGame1_2 = new HBox(10);
        buttonBoxGame1_2.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBoxGame1_2.getChildren().addAll(assignButtonGame1_2);
        assignGridPaneGame1_2.add(buttonBoxGame1_2, 1, 6);
        Image image2 = new Image("file:src/main/resources/bg3.jpg");
        //背景自适应
        BackgroundSize backgroundSize2 = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage2 = new BackgroundImage(image2, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize2);
        Background background2 = new Background(backgroundImage2);
        loginGridPane.setBackground(background2);
        loginGridPane.setAlignment(Pos.CENTER);
        loginGridPane.setHgap(10);
        loginGridPane.setVgap(10);
        assignGridPaneGame1.setBackground(background2);
        assignGridPaneGame2.setBackground(background2);
        assignGridPaneGame1_2.setBackground(background2);

        Scene assignGame1Scene = new Scene(assignGridPaneGame1, 600, 600);
        Scene assignGame2Scene = new Scene(assignGridPaneGame2, 600, 600);
        Scene assignGame1_2Scene = new Scene(assignGridPaneGame1_2, 600, 600);
// join game按钮事件处理程序
        game1Button.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
            try {
                player.connectToGameForFront("1");
            }catch (IOException e) {
                throw new RuntimeException(e);
            }String isStart = null;
            try {
// sleep(5000);
                isStart = player.getGameStatueMessage();
            }catch (IOException e) {
                throw new RuntimeException(e);
            }if (isStart.equalsIgnoreCase("game start")) {
                primaryStage.setScene(assignGame1Scene);
            }});
        game2Button.setOnAction(event -> {
            try {
                player.connectToGameForFront("2");
            }catch (IOException e) {
                throw new RuntimeException(e);
            }String isStart = null;
            try {
                isStart = player.getGameStatueMessage();
            }catch (IOException e) {
                throw new RuntimeException(e);
            }if (isStart.equalsIgnoreCase("game start")) {
                primaryStage.setScene(assignGame2Scene);
                switchButton2.setDisable(true);
            }});
        game1_2Button.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();

            try {
                player.connectToGameForFront("1 2");
            }catch (IOException e) {
                throw new RuntimeException(e);
            }String isStart = null;
            try {
                isStart = player.getGameStatueMessage();
            }catch (IOException e) {
                throw new RuntimeException(e);
            }if (isStart.equalsIgnoreCase("game start")) {
                multiple = true;
                primaryStage.setScene(assignGame1_2Scene);
            }});

// creat a gridpane to tell user the input is wrong
        GridPane assignErrorGridPane = new GridPane();
        assignErrorGridPane.setAlignment(Pos.CENTER);
        assignErrorGridPane.setHgap(10);
        assignErrorGridPane.setVgap(10);
        assignErrorGridPane.setPadding(new Insets(25, 25, 25, 25));
        Label assignErrorMessage = new Label("The number of units you assign is wrong!");
        assignErrorGridPane.add(assignErrorMessage, 0, 0);
        Button errorButton1 = new Button("OK");
        assignErrorGridPane.add(errorButton1, 1, 1);
        Scene assignErrorScene = new Scene(assignErrorGridPane);
// System.out.println("The number of units you assign is wrong!");

        GridPane assignErrorGridPane2 = new GridPane();
        assignErrorGridPane2.setAlignment(Pos.CENTER);
        assignErrorGridPane2.setHgap(10);
        assignErrorGridPane2.setVgap(10);
        assignErrorGridPane2.setPadding(new Insets(25, 25, 25, 25));
        Label assignErrorMessage2 = new Label("The number of units you assign is wrong!");
        assignErrorGridPane2.add(assignErrorMessage2, 0, 0);
        Button errorButton2 = new Button("OK");
        assignErrorGridPane2.add(errorButton2, 1, 1);
        Scene assignErrorScene2 = new Scene(assignErrorGridPane2);

        Button attackButton = new Button("Attack");
        attackButton.setFont(new Font("Cambria", 22));
        Button moveButton = new Button("Move");
        moveButton.setFont(new Font("Cambria", 25));
        Button upgradeButton = new Button("Upgrade");
        upgradeButton.setFont(new Font("Cambria", 19));
        Button cloakButton = new Button("Cloak");
        cloakButton.setFont(new Font("Cambria", 23));
        //rightBox.getChildren().addAll(attackButton, moveButton, upgradeButton, CloakButton);

        errorButton2.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
            primaryStage.setScene(assignGame2Scene);
        });

        assignButtonGame1.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
            String result = "";
            String area1 = areaGame1_1.getText();
            String area2 = areaGame1_2.getText();
            String area3 = areaGame1_3.getText();
            String area4 = areaGame1_4.getText();
            String area5 = areaGame1_5.getText();

            joinGameMessage = area1 + " " + area2 + " " + area3 + " " + area4 + " " + area5;
            try {
                result = player.gameStartHandler(joinGameMessage);
            }catch (IOException e) {
                throw new RuntimeException(e);
            }if (result != null) {
                messageGame1.setText("Assign failure! Please re-enter units");
                primaryStage.setScene(assignErrorScene);
            }else {
                switchButton1.setDisable(true);
                try {
                    if (player.getGameStatueMessage().equalsIgnoreCase("turn start")) {
                        player.turnStartHandler();
                        set2Pane(player, switchButton1, attackButton, moveButton, upgradeButton, cloakButton, root1, 1);
                    }}catch (IOException e) {
                    throw new RuntimeException(e);
                }switchButton2.setDisable(true);
                primaryStage.setScene(scene1);
            }});

        errorButton1.setOnAction(event1 -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
            primaryStage.setScene(assignGame1Scene);
        });

        assignButtonGame2.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
            String result = "";

            String area1 = areaGame2_1.getText();
            String area2 = areaGame2_2.getText();
            String area3 = areaGame2_3.getText();

            joinGameMessage = area1 + " " + area2 + " " + area3;
            try {
                result = player.gameStartHandler(joinGameMessage);
            }catch (IOException e) {
                throw new RuntimeException(e);
            }if (result != null) {
                messageGame2.setText("Assign failure! Please re-enter units");
                primaryStage.setScene(assignErrorScene2);
            }else {
                try {
                    if (player.getGameStatueMessage().equalsIgnoreCase("turn start")) {
                        player.turnStartHandler();
                        set2Pane(player, switchButton2, attackButton, moveButton, upgradeButton, cloakButton,  root2, 2);
                    }}catch (IOException e) {
                    throw new RuntimeException(e);
                }if (!multiple) {
                    switchButton2.setDisable(true);
                }primaryStage.setScene(scene2);
            }});

        assignButtonGame1_2.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
            String area1 = areaGame1_2_1.getText();
            String area2 = areaGame1_2_2.getText();
            String area3 = areaGame1_2_3.getText();
            String area4 = areaGame1_2_4.getText();
            String area5 = areaGame1_2_5.getText();
            joinGameMessage = area1 + " " + area2 + " " + area3 + " " + area4 + " " + area5;
            switchButton1.setDisable(false);
            switchButton2.setDisable(false);
            String result = "";

            try {
                result = player.gameStartHandler(joinGameMessage);
            }catch (IOException e) {
                throw new RuntimeException(e);
            }if (result != null) {
                messageGame1.setText("Assign failure! Please re-enter units");
                primaryStage.setScene(assignErrorScene);
            }else {
                try {
                    if (player.getGameStatueMessage().equalsIgnoreCase("turn start")) {
                        player.turnStartHandler();
                        set2Pane(player, switchButton1, attackButton, moveButton, upgradeButton, cloakButton, root1, 1);
                    }}catch (IOException e) {
                    throw new RuntimeException(e);
                }primaryStage.setScene(scene1);
            }});

//click switch button1 or button2 to switch scene
        switchButton1.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
            if (firstInGame == 0) {
                primaryStage.setScene(assignGame2Scene);
                switchButton1.setDisable(false);
                firstInGame = 1;
                System.out.println(11);
            }else {
                primaryStage.setScene(scene2);
                switchButton1.setDisable(false);
                System.out.println(22);
            }});

        switchButton2.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
            switchButton2.setDisable(false);
            primaryStage.setScene(scene1);
// }
        });
        Button switchMap = new Button("Assign");
        switchMap.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
            primaryStage.setScene(assignGame2Scene);
        });

        ObservableList<String> levels = FXCollections.observableArrayList("0","1", "2", "3", "4", "5", "6", "spy");

        attackButton.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
// create a dialog box
            Dialog<String> dialog = new Dialog<>();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Attack");
            dialog.setHeaderText("Please enter origin/destination/level/units");
//add button
            ButtonType DoneButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(DoneButtonType, cancelButtonType);

            ObservableList<String> origins = FXCollections.observableArrayList("Narnia", "Midkemia", "Oz", "Gondor", "Mordor", "Hogwarts", "Elantris", "Roshar", "Scadrial", "Duke");
// create text box and label
            Label originLabel = new Label("From:");
            ComboBox<String> originComboBox = new ComboBox<>(origins);
            Label destLabel = new Label("To:");
            ComboBox<String> destComboBox = new ComboBox<>(origins);
            Label unitLevel = new Label("Level:");
            ComboBox<String> unitLevelBox = new ComboBox<>(levels);
            Label unitLabel = new Label("Units:");
            TextField unitTestField = new TextField();

            GridPane gridPaneDialog = new GridPane();
            gridPaneDialog.setPadding(new Insets(10));
            gridPaneDialog.setHgap(10);
            gridPaneDialog.setVgap(10);
// add gridpane and
            gridPaneDialog.add(originLabel, 1, 1);
            gridPaneDialog.add(originComboBox, 2, 1);
            gridPaneDialog.add(destLabel, 1, 2);
            gridPaneDialog.add(destComboBox, 2, 2);
            gridPaneDialog.add(unitLevel, 1, 3);
            gridPaneDialog.add(unitLevelBox, 2, 3);
            gridPaneDialog.add(unitLabel, 1, 4);
            gridPaneDialog.add(unitTestField, 2, 4);

// set content in gridpane
            dialog.getDialogPane().setContent(gridPaneDialog);

// 禁用确定按钮，除非文本框中有输入
            Node okButton = dialog.getDialogPane().lookupButton(DoneButtonType);
            okButton.setDisable(true);

// 当文本框中有输入时，启用确定按钮
//当文本框中有输入时，启用确定按钮

            unitTestField.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty() || unitLevelBox.getValue().isEmpty());
            });

// 获取用户输入
            dialog.setResultConverter(dialogButton -> {
                String temp = unitLevelBox.getValue();
                if (unitLevelBox.getValue().equals("spy")) {
                    temp = "7";
                }

                if (dialogButton == DoneButtonType) {
                    return "A " + temp + " " + unitTestField.getText() + " " +
                            originComboBox.getValue() + " " + destComboBox.getValue();
                }return null;
            });

// 显示对话框并等待用户响应
            dialog.showAndWait().ifPresent(result -> {
                String temp = unitLevelBox.getValue();
                if (unitLevelBox.getValue().equals("spy")) {
                    temp = "7";
                }

                player.createAndAddMoveOrAttack("A " + temp + " " + unitTestField.getText() + " " +
                        originComboBox.getValue() + " " + destComboBox.getValue());
                System.out.println(result);
            });
        });

        moveButton.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
// create a dialog box
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Move");
            dialog.setHeaderText("Please enter origin/destination/units");

//add button
            ButtonType DoneButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(DoneButtonType, cancelButtonType);

            ObservableList<String> origins = FXCollections.observableArrayList("Narnia", "Midkemia", "Oz", "Gondor", "Mordor", "Hogwarts", "Elantris", "Roshar", "Scadrial", "Duke");

// create text box and label
            Label originLabel = new Label("From:");
            ComboBox<String> originComBox = new ComboBox<>(origins);
            Label destLabel = new Label("To:");
            ComboBox<String> destComBox = new ComboBox<>(origins);
            Label unitLevel = new Label("Level:");
            ComboBox<String> unitLevelField = new ComboBox<>(levels);
            Label unitLabel = new Label("Units:");
            TextField unitTestField = new TextField();

            GridPane gridPaneDialog = new GridPane();
            gridPaneDialog.setPadding(new Insets(10));
            gridPaneDialog.setHgap(10);
            gridPaneDialog.setVgap(10);
// add grid pane and
            gridPaneDialog.add(originLabel, 1, 1);
            gridPaneDialog.add(originComBox, 2, 1);
            gridPaneDialog.add(destLabel, 1, 2);
            gridPaneDialog.add(destComBox, 2, 2);
            gridPaneDialog.add(unitLevel, 1, 3);
            gridPaneDialog.add(unitLevelField, 2, 3);
            gridPaneDialog.add(unitLabel, 1, 4);
            gridPaneDialog.add(unitTestField, 2, 4);

// set content in gridpane
            dialog.getDialogPane().setContent(gridPaneDialog);

// 禁用确定按钮，除非文本框中有输入
            Node okButton = dialog.getDialogPane().lookupButton(DoneButtonType);
            okButton.setDisable(true);

// 当文本框中有输入时，启用确定按钮
            unitTestField.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty() || unitLevelField.getValue().isEmpty());
            });

// 获取用户输入
            dialog.setResultConverter(dialogButton -> {
                String temp = unitLevelField.getValue();
                if(unitLevelField.getValue().equals("spy")) {
                    temp = "7";
                }
                if (dialogButton == DoneButtonType) {
                    return "M " + temp + " " + unitTestField.getText() + " " +
                            originComBox.getValue() + " " + destComBox.getValue();
                }return null;
            });

// 显示对话框并等待用户响应

                dialog.showAndWait().ifPresent(result -> {
                    String temp = unitLevelField.getValue();
                    if(unitLevelField.getValue().equals("spy")) {
                        temp = "7";
                    }
                player.createAndAddMoveOrAttack("M " + temp + " " + unitTestField.getText() + " "
                + originComBox.getValue() + " " + destComBox.getValue());
                System.out.println(result);
                });
        });

        upgradeButton.setOnAction(event -> {
            MediaPlayer clickPlayer1 = new MediaPlayer(click);
            clickPlayer1.play();
// create a dialog box
            Dialog<String> dialog = new Dialog<>();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Upgrade");
            dialog.setHeaderText("Please enter origin/level/units");
//add button
            ButtonType DoneButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(DoneButtonType, cancelButtonType);

// create text box and label

            ObservableList<String> origins = FXCollections.observableArrayList("Narnia", "Midkemia", "Oz", "Gondor", "Mordor", "Hogwarts", "Elantris", "Roshar", "Scadrial", "Duke");

            Label originLabel = new Label("Origin:");
            ComboBox<String> originComBox = new ComboBox<>(origins);
            Label oldlevelLabel = new Label("old Level:");
            ComboBox<String> oldlevelBox = new ComboBox<>(levels);
            Label unitLabel = new Label("Units:");
            TextField unitTestField = new TextField();
            Label newlevelLabel = new Label("new Level:");
            ComboBox<String> newlevelTestField = new ComboBox<>(levels);

// add grid pane and

            GridPane gridPaneDialog = new GridPane();
            gridPaneDialog.setPadding(new Insets(10));
            gridPaneDialog.setHgap(10);
            gridPaneDialog.setVgap(10);

            gridPaneDialog.add(originLabel, 1, 1);
            gridPaneDialog.add(originComBox, 2, 1);
            gridPaneDialog.add(oldlevelLabel, 1, 2);
            gridPaneDialog.add(oldlevelBox, 2, 2);
            gridPaneDialog.add(unitLabel, 1, 3);
            gridPaneDialog.add(unitTestField, 2, 3);
            gridPaneDialog.add(newlevelLabel, 1, 4);
            gridPaneDialog.add(newlevelTestField, 2, 4);

// set content in gridpane
            dialog.getDialogPane().setContent(gridPaneDialog);

// 禁用确定按钮，除非文本框中有输入
//            Node okButton = dialog.getDialogPane().lookupButton(DoneButtonType);
//            okButton.setDisable(true);

// 当文本框中有输入时，启用确定按钮
//            unitTestField.textProperty().addListener((observable, oldValue, newValue) -> {
//                okButton.setDisable(newValue.trim().isEmpty() || newlevelTestField.getValue().isEmpty());
//            });
// 获取用户输入
            dialog.setResultConverter(dialogButton -> {
                if(oldlevelBox.getValue().equals("spy")) {
                    oldlevelBox.setValue("7");
                }
                if(newlevelTestField.getValue().equals("spy")) {
                    newlevelTestField.setValue("7");
                }
                if (dialogButton == DoneButtonType) {
                    return unitTestField.getText() + " " + originComBox.getValue() + " "
                            + oldlevelBox.getValue() + " " + newlevelTestField.getValue();
                }return null;
            });

// 显示对话框并等待用户响应
            dialog.showAndWait().ifPresent(result -> {
                String temp = "", temp1 = "";

                if(oldlevelBox.getValue().equals("spy")) {
                    temp = "7";
                }
                else {
                    temp = oldlevelBox.getValue();
                }
                if(newlevelTestField.getValue().equals("spy")) {
                    temp1 = "7";
                }
                else {
                    temp1 = newlevelTestField.getValue();
                }


                player.createAndAddUpgrade(unitTestField.getText() + " " + originComBox.getValue() + " "
                        + temp + " " + temp1);
                System.out.println(result);
            });
        });
        ObservableList<String> origins = FXCollections.observableArrayList("Narnia", "Midkemia", "Oz", "Gondor", "Mordor", "Hogwarts", "Elantris", "Roshar", "Scadrial", "Duke");

        cloakButton.setOnAction(event -> {
// create a dialog box
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Cloak");
            dialog.setHeaderText("Please enter a territory to Cloak");
//add button
            ButtonType DoneButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(DoneButtonType, cancelButtonType);

// create text box and label
            Label originLabel = new Label("Cloak Territory:");
            ComboBox<String> cloakTextField = new ComboBox<>(origins);
            GridPane gridPaneDialog = new GridPane();
// add grid pane and
            gridPaneDialog.add(originLabel, 1, 1);
            gridPaneDialog.add(cloakTextField, 2, 1);

            dialog.getDialogPane().setContent(gridPaneDialog);

            // 禁用确定按钮，除非文本框中有输入
            Node okButton = dialog.getDialogPane().lookupButton(DoneButtonType);
            okButton.setDisable(true);

// 当文本框中有输入时，启用确定按钮
            cloakTextField.valueProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty());
            });
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == DoneButtonType) {
                    return cloakTextField.getValue();
                }
                return null;
            });

            System.out.println(cloakTextField.getValue());
            dialog.showAndWait().ifPresent(result -> {
                player.createAndAddCloak(cloakTextField.getValue());
                System.out.println(result);
            });
        });
        set2Pane(player, switchButton1, attackButton, moveButton, upgradeButton, cloakButton, root1, 1);
        set2Pane(player, switchButton2, attackButton, moveButton, upgradeButton, cloakButton, root2, 2);
//Attach the icon to the stage/window
        primaryStage.getIcons().add(new Image("logo.png"));
        primaryStage.show();

    }
    public void set2Pane(Player player, Button switchButton, Button attackButton, Button moveButton, Button upgradeButton, Button cloakButton,BorderPane root, int id) {
// 创建主界面
        BackgroundSize backgroundSize1 = new BackgroundSize(100, 100, true, true, true, false);
        Image image1 = new Image("file:src/main/resources/bg4.jpg");
        BackgroundImage backgroundImage1 = new BackgroundImage(image1, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize1);
        Background background1 = new Background(backgroundImage1);
        root.setBackground(background1);
        VBox colBox = new VBox(0);
        HBox rowBow = new HBox(0);
        rowBow.setPadding(new Insets(10, 10, 10, 10));
//
        Label gameId = new Label("Game id: " + id);
        gameId.setFont(new Font("Cambria", 22));
        Label food = new Label(" Food: " + player.getFood());
        food.setFont(new Font("Cambria", 22));
        Label playerRank = new Label(" Your Rank: " + player.getTechLevel());
        playerRank.setFont(new Font("Cambria", 22));
        Label techPoints = new Label(" Tech Points: " + player.getCost());
        Button upgrade = new Button("↑");
        rowBow.getChildren().addAll(gameId, food, playerRank, upgrade, techPoints);
        rowBow.setAlignment(Pos.CENTER);
        root.setTop(rowBow);

        colBox.getChildren().add(rowBow);
        HBox rowBow2 = new HBox(0);
        rowBow2.setPadding(new Insets(10, 10, 10, 10));
//
        Label playerId = new Label("You are Player " + player.getPlayerID()+"\n Your color is green.");
        playerId.setFont(new Font("Cambria", 19));
        rowBow2.getChildren().add(playerId);
        rowBow2.setAlignment(Pos.CENTER);
        colBox.getChildren().add(rowBow2);
        root.setTop(colBox);

//add map button
        VBox leftBox = new VBox(0);
        leftBox.setPadding(new Insets(10, 10, 10, 10));
        leftBox.setAlignment(Pos.CENTER);
        Button[] buttons = new Button[10];
        generateMap(leftBox, buttons, id + 1);

        Label statusLabel = new Label();
        statusLabel.setAlignment(Pos.CENTER);

        VBox rightBox = new VBox(10);
        rightBox.setPadding(new Insets(10, 10, 10, 10));
        rightBox.setAlignment(Pos.CENTER);

        rightBox.getChildren().addAll(attackButton, moveButton, upgradeButton, cloakButton);

        HBox lastRowBox = new HBox(20);
        lastRowBox.setPadding(new Insets(10, 10, 10, 10));
        Button doneButton = new Button("Done");
        doneButton.setFont(new Font("Cambria", 25));

        Button refreshButton = new Button("Refresh");
        refreshButton.setFont(new Font("Cambria", 25));


        lastRowBox.setAlignment(Pos.CENTER);
        lastRowBox.getChildren().add(doneButton);
        lastRowBox.getChildren().add(refreshButton);
        lastRowBox.getChildren().add(switchButton);
        root.setBottom(lastRowBox);
        root.setLeft(leftBox);
        root.setCenter(statusLabel);
        root.setRight(rightBox);
        refreshButton.setDisable(true);
        doneButton.setOnAction(event -> {
            try {
                player.endTurn();
            }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }doneButton.setDisable(true);
            refreshButton.setDisable(false);
            attackButton.setDisable(true);
            moveButton.setDisable(true);
            upgradeButton.setDisable(true);
        });
        refreshButton.setOnAction(event -> {
            try {
                player.getGameStatueMessage();
            }catch (IOException e) {
                throw new RuntimeException(e);
            }try {
                player.turnStartHandler();
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
            set2Pane(player, switchButton, attackButton, moveButton, upgradeButton, cloakButton,  root, id);
            doneButton.setDisable(false);
            refreshButton.setDisable(true);
            attackButton.setDisable(false);
            moveButton.setDisable(false);
            upgradeButton.setDisable(false);
        });
        upgrade.setOnAction(event -> {
            player.evloveTech();
        });
        int limit;
        if (id == 1) {
            limit = 10;
        }else {
            limit = 9;
        }// 左侧按钮事件处理程序

//        for(int i = 0; i < limit; i++){
//
//        }
        for (int i = 0; i < limit; i++) {
            Button button = buttons[i];
            int ownId, l0, l1, l2, l3, l4, l5, l6,l7, spynum;
            String name;
            if (player.getGlobalMap() == null) {
                ownId = -1;
                name = "Name";
                l0 = -1;
                l1 = -1;
                l2 = -1;
                l3 = -1;
                l4 = -1;
                l5 = -1;
                l6 = -1;
                l7 = -1;
                spynum = -1;

            } else {
                ArrayList<Territory> map = player.getGlobalMap();
                ownId = map.get(i).getOwnID();
                name = map.get(i).getName();
                button.setText(name);
                if(ownId == player.getPlayerID()){
                        BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf("#00CC99"), CornerRadii.EMPTY, Insets.EMPTY);
                        Background background = new Background(backgroundFill);
                        button.setBackground(background);
                }else{
                    if(map.get(i).isAbleToSee()){
                        BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf("#FF5050"), CornerRadii.EMPTY, Insets.EMPTY);
                        Background background = new Background(backgroundFill);
                        button.setBackground(background);
                    }
                }
                l0 = map.get(i).getUnits().units.get(0);
                l1 = map.get(i).getUnits().units.get(1);
                l2 = map.get(i).getUnits().units.get(2);
                l3 = map.get(i).getUnits().units.get(3);
                l4 = map.get(i).getUnits().units.get(4);
                l5 = map.get(i).getUnits().units.get(5);
                l6 = map.get(i).getUnits().units.get(6);
                l7 = map.get(i).getUnits().units.get(7);
                spynum = player.getSpyNumOnTerritory(map.get(i));
            }
            String spyInfo;
            if (spynum == 0) {
                spyInfo = "You got no spy on this territory";
            } else {
                spyInfo = "You got " + spynum + " spy on this territory";
            }

            button.setOnAction(event -> {
                String s0, s1, s2, s3, s4, s5, s6, s7, sownId;
                if (l0 == -1) {
                    s0 = "?";
                } else {
                    s0 = String.valueOf(l0);
                }
                if (l1 == -1) {
                    s1 = "?";
                } else {
                    s1 = String.valueOf(l1);
                }
                if (l2 == -1) {
                    s2 = "?";
                } else {
                    s2 = String.valueOf(l2);
                }
                if (l3 == -1) {
                    s3 = "?";
                } else {
                    s3 = String.valueOf(l3);
                }
                if (l4 == -1) {
                    s4 = "?";
                } else {
                    s4 = String.valueOf(l4);
                }
                if (l5 == -1) {
                    s5 = "?";
                } else {
                    s5 = String.valueOf(l5);
                }
                if (l6 == -1) {
                    s6 = "?";
                } else {
                    s6 = String.valueOf(l6);
                }
                if (l7 == -1) {
                    s7 = "?";
                } else {
                    s7 = String.valueOf(l7);
                }
                if (ownId == -1) {
                    sownId = "?";
                } else {
                    sownId = String.valueOf(ownId);
                }

                statusLabel.setText("Owner: Player" + sownId + "\nName: " + name + "\n" +
                        "Level:\n" + "Ants: " + s0 + "\n" + "Bees: " + s1 + "\n" + "Wasps: " + s2 + "\n" + "Bird: " + s3 + "\n" + "Snake: " +
                        s4 + "\n" + "Eagle: " + s5 + "\n" + "Human: " + s6 + "\n" + "Spy: " + s7 + "\n" + spyInfo);
                statusLabel.setFont(new Font("Cambria", 20));
                statusLabel.setStyle("-fx-font-weight: bold");
            });
        }// 右侧按钮事件处理程序

//create a dialog to input attack information

    }//id is used to check how many map in this game, reserved

    public void generateMap(VBox leftBox, Button[] buttons, int numsOfPlayer) {

        int index = 0;
        for (int i = 0; i < 3; i++) {
            HBox rowBox = new HBox(0);
            rowBox.setAlignment(Pos.CENTER);
            int j = 0;
            if (i < 2) {
                for (; j < 3; j++) {
                    Button button = new Button("Map " + (i * 3 + j + 1));
                    button.setFont(new Font("Cambria", 16));
                    button.setPrefSize(100, 50);
                    buttons[index++] = button;
                    if (index < 6) {
                        BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf("#8ba583"), CornerRadii.EMPTY, Insets.EMPTY);
                        Background background = new Background(backgroundFill);
                        button.setBackground(background);

                    }else {
                        BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf("#8ba583"), CornerRadii.EMPTY, Insets.EMPTY);
                        Background background = new Background(backgroundFill);
                        button.setBackground(background);
                    }
                    BorderStroke borderStroke = new BorderStroke(Paint.valueOf("#3D3D3D"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1));
                    Border border = new Border(borderStroke);
                    button.setBorder(border);
                    rowBox.getChildren().add(button);

                }}else {
                for (; j < 4; j++) {
                    Button button = new Button("Map " + (i * 3 + j + 1));
                    button.setFont(new Font("Cambria", 16));
                    button.setPrefSize(75, 50);
                    buttons[index++] = button;

                    rowBox.getChildren().add(button);
                }}leftBox.getChildren().add(rowBox);
        }
        if (numsOfPlayer == 3) {
            buttons[9].setText("");
        }
        for (int i = 0; i < buttons.length; i++) {
            Button button = buttons[i];
            BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf("#A6A6A8"), CornerRadii.EMPTY, Insets.EMPTY);
            Background background = new Background(backgroundFill);
            button.setBackground(background);
            BorderStroke borderStroke = new BorderStroke(Paint.valueOf("#3D3D3D"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1));
            Border border = new Border(borderStroke);
            button.setBorder(border);

        }}
    public static void main(String[] args) {
        launch(args);
    }}