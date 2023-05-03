package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class MyJavaFX extends Application {

    private static final String USERNAME = "123";
    private static final String PASSWORD = "123";
    boolean multiple = false;
    private String systemMessage = "The total number of unites is 50, please assign the units to the territories.\n";


    int firstInGame = 0;
    String joinGameMessage;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Player player = new Player(9999);
        player.connectToServerForFrontend();
//Obtain available game id list
        ArrayList<Integer> availableList = player.getAvailableList();
//set the title of the window
        primaryStage.setTitle("Risc Game");
// create login ui
        Label headerLabel = new Label("Login");
        headerLabel.setFont(new Font("Arial", 20));
        GridPane loginGridPane = new GridPane();
        loginGridPane.setAlignment(Pos.CENTER);
        loginGridPane.setHgap(10);
        loginGridPane.setVgap(10);
        loginGridPane.setPadding(new Insets(25, 25, 25, 25));

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button forgotPasswordButton = new Button("Find password");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.getChildren().addAll(loginButton, forgotPasswordButton);

        loginGridPane.add(usernameLabel, 0, 0);
        loginGridPane.add(usernameField, 1, 0);
        loginGridPane.add(passwordLabel, 0, 1);
        loginGridPane.add(passwordField, 1, 1);
        loginGridPane.add(buttonBox, 1, 2);
        Label statusLabel = new Label();

        BorderPane root1 = new BorderPane();
        BorderPane root2 = new BorderPane();

        Scene scene1 = new Scene(root1, 800, 600);
        Scene scene2 = new Scene(root2, 800, 600);
        GridPane joinGridPane = new GridPane();

// Create the Join button
        Button game1Button = new Button("Game 1");
        Button game2Button = new Button("Game 2");
        Button game1_2Button = new Button("Game 1&2");

// 登录按钮事件处理程序
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username.equals(USERNAME) && password.equals(PASSWORD)) {
// 登录成功，显示主界面
                if (availableList.contains(1)) {
                    game1Button.setDisable(false);
                } else {
                    game1Button.setDisable(true);
                }
                if (availableList.contains(2)) {
                    game2Button.setDisable(false);
                } else {
                    game2Button.setDisable(true);
                }
                if (availableList.contains(1) && availableList.contains(2)) {
                    game1_2Button.setDisable(false);
                } else {
                    game1_2Button.setDisable(true);
                }// primaryStage.setScene(new Scene(root1, 800, 600));
                primaryStage.setScene(new Scene(joinGridPane));
            } else {
// 显示错误消息
                statusLabel.setText("用户名或密码错误");
            }
        });

// 找回密码按钮事件处理程序
        forgotPasswordButton.setOnAction(event -> {
// 显示找回密码窗口
            Label messageLabel = new Label("请联系管理员找回密码。");
            messageLabel.setAlignment(Pos.CENTER);
            VBox vbox = new VBox(10);
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(messageLabel);
            Scene scene = new Scene(vbox, 300, 200);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        });
//login scene
        primaryStage.setScene(new Scene(loginGridPane, 300, 275));
//join game ui
        joinGridPane.setAlignment(Pos.CENTER);
        joinGridPane.setHgap(10);
        joinGridPane.setVgap(10);
        joinGridPane.setPadding(new Insets(25, 25, 25, 25));
        Label message = new Label("Which game you want to play?\n" +
                "If you want to join a game, select the game ID button you want to join.\n");


        Button backButton = new Button("Back");
        HBox buttonBox1 = new HBox(10);
        buttonBox1.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox1.getChildren().addAll(game1Button, game2Button, game1_2Button, backButton);
        joinGridPane.add(message, 0, 0);
        joinGridPane.add(buttonBox1, 1, 2);
// Create the Switch button
        Button switchButton1 = new Button("Switch to Game 2");
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
        Label messageGame1_1 = new Label("Territory 1");
        Label messageGame1_2 = new Label("Territory 2");
        Label messageGame1_3 = new Label("Territory 3");
        Label messageGame1_4 = new Label("Territory 4");
        Label messageGame1_5 = new Label("Territory 5");
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
        Label messageGame1_2_1 = new Label("Territory 1");
        Label messageGame1_2_2 = new Label("Territory 2");
        Label messageGame1_2_3 = new Label("Territory 3");
        Label messageGame1_2_4 = new Label("Territory 4");
        Label messageGame1_2_5 = new Label("Territory 5");
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
        Scene assignGame1Scene = new Scene(assignGridPaneGame1);
        Scene assignGame2Scene = new Scene(assignGridPaneGame2);
        Scene assignGame1_2Scene = new Scene(assignGridPaneGame1_2);
// join game按钮事件处理程序
        game1Button.setOnAction(event -> {
            try {
                player.connectToGameForFront("1");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String isStart = null;
            try {
//                sleep(5000);
                isStart = player.getGameStatueMessage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (isStart.equalsIgnoreCase("game start")) {
                primaryStage.setScene(assignGame1Scene);
            }
        });
        game2Button.setOnAction(event -> {
            try {
                player.connectToGameForFront("2");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String isStart = null;
            try {
                isStart = player.getGameStatueMessage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (isStart.equalsIgnoreCase("game start")) {
                primaryStage.setScene(assignGame2Scene);
                switchButton2.setDisable(true);
            }
        });
        game1_2Button.setOnAction(event -> {
            try {
                player.connectToGameForFront("1 2");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String isStart = null;
            try {
                isStart = player.getGameStatueMessage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (isStart.equalsIgnoreCase("game start")) {
                multiple = true;
                primaryStage.setScene(assignGame1_2Scene);
            }
        });

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

        errorButton2.setOnAction(event -> {
            primaryStage.setScene(assignGame2Scene);
        });

        assignButtonGame1.setOnAction(event -> {
            String result = "";
            String area1 = areaGame1_1.getText();
            String area2 = areaGame1_2.getText();
            String area3 = areaGame1_3.getText();
            String area4 = areaGame1_4.getText();
            String area5 = areaGame1_5.getText();

            joinGameMessage = area1 + " " + area2 + " " + area3 + " " + area4 + " " + area5;
            try {
                result = player.gameStartHandler(joinGameMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (result != null) {
                messageGame1.setText("Assign failure! Please re-enter units");
                primaryStage.setScene(assignErrorScene);
            } else {
                switchButton1.setDisable(true);
                try {
                    if (player.getGameStatueMessage().equalsIgnoreCase("turn start")) {
                        player.turnStartHandler();
                        set2Pane(player, switchButton1, root1, 1);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                switchButton2.setDisable(true);
                primaryStage.setScene(scene1);
            }
        });

        errorButton1.setOnAction(event1 -> {
            primaryStage.setScene(assignGame1Scene);
        });

        assignButtonGame2.setOnAction(event -> {
            String result = "";

            String area1 = areaGame2_1.getText();
            String area2 = areaGame2_2.getText();
            String area3 = areaGame2_3.getText();

            joinGameMessage = area1 + " " + area2 + " " + area3;
            try {
                result = player.gameStartHandler(joinGameMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (result != null) {
                messageGame2.setText("Assign failure! Please re-enter units");
                primaryStage.setScene(assignErrorScene2);
            } else {
                try {
                    if (player.getGameStatueMessage().equalsIgnoreCase("turn start")) {
                        player.turnStartHandler();
                        set2Pane(player, switchButton2, root2, 2);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(!multiple){
                    switchButton2.setDisable(true);
                }
                primaryStage.setScene(scene2);
            }
        });

        assignButtonGame1_2.setOnAction(event -> {
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (result != null) {
                messageGame1.setText("Assign failure! Please re-enter units");
                primaryStage.setScene(assignErrorScene);
            } else {
                try {
                    if (player.getGameStatueMessage().equalsIgnoreCase("turn start")) {
                        player.turnStartHandler();
                        set2Pane(player, switchButton1, root1, 1);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                primaryStage.setScene(scene1);
            }

        });

//click switch button1 or button2 to switch scene
        switchButton1.setOnAction(event -> {
            if (firstInGame == 0) {
                primaryStage.setScene(assignGame2Scene);
                switchButton1.setDisable(false);
                firstInGame = 1;
                System.out.println(11);
            } else {
                primaryStage.setScene(scene2);
                switchButton1.setDisable(false);
                System.out.println(22);
            }
        });

        switchButton2.setOnAction(event -> {
            switchButton2.setDisable(false);
            primaryStage.setScene(scene1);
// }
        });
        Button switchMap = new Button("Assign");
        switchMap.setOnAction(event -> {
            primaryStage.setScene(assignGame2Scene);
        });


        set2Pane(player, switchButton1, root1, 1);
        set2Pane(player, switchButton2, root2, 2);
//Attach the icon to the stage/window
        primaryStage.getIcons().add(new Image("logo.png"));
        primaryStage.show();

    }

    public void set2Pane(Player player, Button switchButton, BorderPane root, int id) {
// 创建主界面
        ImagePattern imagePattern = new ImagePattern(new Image("bg.jpg"), 0, 0, 0.5, 0.5, true);
        root.setBackground(new Background(new BackgroundFill(imagePattern, CornerRadii.EMPTY, Insets.EMPTY)));
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
        Label playerId = new Label("You are Player: " + player.getPlayerID());
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
        Button attackButton = new Button("Attack");
        attackButton.setFont(new Font("Cambria", 22));
        Button moveButton = new Button("Move");
        moveButton.setFont(new Font("Cambria", 25));
        Button upgradeButton = new Button("Upgrade");
        upgradeButton.setFont(new Font("Cambria", 19));
        rightBox.getChildren().addAll(attackButton, moveButton, upgradeButton);

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
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            doneButton.setDisable(true);
            refreshButton.setDisable(false);
            attackButton.setDisable(true);
            moveButton.setDisable(true);
            upgradeButton.setDisable(true);
        });
        refreshButton.setOnAction(event -> {
            try {
                player.getGameStatueMessage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                player.turnStartHandler();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            set2Pane(player, switchButton, root, id);
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
        } else {
            limit = 9;
        }
// 左侧按钮事件处理程序
        for (int i = 0; i < limit; i++) {
            Button button = buttons[i];
            int ownId, l0, l1, l2, l3, l4, l5, l6,l7;
            String name;
            if (player.getGlobalMap().isEmpty()) {
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

            } else {
                ArrayList<Territory> map = player.getGlobalMap();
                ownId = map.get(i).getOwnID();
                name = map.get(i).getName();
                l0 = map.get(i).getUnits().units.get(0);
                l1 = map.get(i).getUnits().units.get(1);
                l2 = map.get(i).getUnits().units.get(2);
                l3 = map.get(i).getUnits().units.get(3);
                l4 = map.get(i).getUnits().units.get(4);
                l5 = map.get(i).getUnits().units.get(5);
                l6 = map.get(i).getUnits().units.get(6);
                l7 = map.get(i).getUnits().units.get(7);
            }


            button.setOnAction(event -> {
                statusLabel.setText("Owner: Player" + ownId + "\nName: " + name + "\n" +
                        "Level:\n" + "Ants: " + l0 + "\n" + "Bees: " + l1 + "\n" + "Wasps: " + l2 + "\n" + "Bird: " + l3 + "\n" + "Snake: " +
                        l4 + "\n" + "Eagle: " + l5 + "\n" + "Human: " + l6 + "\n" + "Spy: " + l7);
                statusLabel.setFont(new Font("Cambria", 20));
                statusLabel.setStyle("-fx-font-weight: bold");
            });
        }// 右侧按钮事件处理程序
        attackButton.setOnAction(event -> {
            statusLabel.setText("Attack！");
        });

// 创建一个网格布局
        GridPane gridPaneDialog = new GridPane();
        gridPaneDialog.setPadding(new Insets(10));
        gridPaneDialog.setHgap(10);
        gridPaneDialog.setVgap(10);
//create a dialog to input attack information
        attackButton.setOnAction(event -> {
// create a dialog box
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Attack");
            dialog.setHeaderText("Please enter origin/destination/level/units");
//add button
            ButtonType DoneButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(DoneButtonType, ButtonType.CANCEL);

// create text box and label
            Label originLabel = new Label("From:");
            TextField originTextField = new TextField();
            Label destLabel = new Label("To:");
            TextField destTestField = new TextField();
            Label unitLevel = new Label("Level:");
            TextField unitLevelField = new TextField();
            Label unitLabel = new Label("Units:");
            TextField unitTestField = new TextField();

// add gridpane and
            gridPaneDialog.add(originLabel, 1, 1);
            gridPaneDialog.add(originTextField, 2, 1);
            gridPaneDialog.add(destLabel, 1, 2);
            gridPaneDialog.add(destTestField, 2, 2);
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
            originTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty() || destTestField.getText().isEmpty());
            });
            destTestField.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty() || originTextField.getText().isEmpty());
            });

// 获取用户输入
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == DoneButtonType) {
                    return "A " + unitLevelField.getText() + " " + unitTestField.getText() + " " +
                            originTextField.getText() + " " + destTestField.getText();
                }
                return null;
            });

// 显示对话框并等待用户响应
            dialog.showAndWait().ifPresent(result -> {
                player.createAndAddMoveOrAttack("A " + unitLevelField.getText() + " " + unitTestField.getText() + " " +
                        originTextField.getText() + " " + destTestField.getText());
                System.out.println(result);
            });
        });

        moveButton.setOnAction(event -> {
// create a dialog box
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Move");
            dialog.setHeaderText("Please enter origin/destination/units");
//add button
            ButtonType DoneButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(DoneButtonType, ButtonType.CANCEL);

// create text box and label
            Label originLabel = new Label("From:");
            TextField originTextField = new TextField();
            Label destLabel = new Label("To:");
            TextField destTextField = new TextField();
            Label unitLevel = new Label("Level:");
            TextField unitLevelField = new TextField();
            Label unitLabel = new Label("Units:");
            TextField unitTestField = new TextField();

// add grid pane and
            gridPaneDialog.add(originLabel, 1, 1);
            gridPaneDialog.add(originTextField, 2, 1);
            gridPaneDialog.add(destLabel, 1, 2);
            gridPaneDialog.add(destTextField, 2, 2);
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
            originTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty() || destTextField.getText().isEmpty());
            });
            destTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty() || originTextField.getText().isEmpty());
            });

// 获取用户输入
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == DoneButtonType) {
                    return "M " + unitLevelField.getText() + " " + unitTestField.getText() + " " +
                            originTextField.getText() + " " + destTextField.getText();
                }
                return null;
            });

// 显示对话框并等待用户响应
            dialog.showAndWait().ifPresent(result -> {
                player.createAndAddMoveOrAttack("M " + unitLevelField.getText() + " " + unitTestField.getText() + " "
                        + originTextField.getText() + " " + destTextField.getText());
                System.out.println(result);
            });
        });

        upgradeButton.setOnAction(event -> {
// create a dialog box
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Upgrade");
            dialog.setHeaderText("Please enter origin/level/units");
//add button
            ButtonType DoneButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(DoneButtonType, ButtonType.CANCEL);

// create text box and label
            Label originLabel = new Label("Origin:");
            TextField originTextField = new TextField();
            Label oldlevelLabel = new Label("old Level:");
            TextField oldlevelTextField = new TextField();
            Label unitLabel = new Label("Units:");
            TextField unitTestField = new TextField();
            Label newlevelLabel = new Label("new Level:");
            TextField newlevelTestField = new TextField();

// add grid pane and
            gridPaneDialog.add(originLabel, 1, 1);
            gridPaneDialog.add(originTextField, 2, 1);
            gridPaneDialog.add(oldlevelLabel, 1, 2);
            gridPaneDialog.add(oldlevelTextField, 2, 2);
            gridPaneDialog.add(unitLabel, 1, 3);
            gridPaneDialog.add(unitTestField, 2, 3);
            gridPaneDialog.add(newlevelLabel, 1, 4);
            gridPaneDialog.add(newlevelTestField, 2, 4);

// set content in gridpane
            dialog.getDialogPane().setContent(gridPaneDialog);

// 禁用确定按钮，除非文本框中有输入
            Node okButton = dialog.getDialogPane().lookupButton(DoneButtonType);
            okButton.setDisable(true);

            // 当文本框中有输入时，启用确定按钮
            originTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty() || originTextField.getText().isEmpty());
            });
            originTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty() || originTextField.getText().isEmpty());
            });

// 获取用户输入
            // 获取用户输入
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == DoneButtonType) {
                    return unitTestField.getText() + " " + originTextField.getText() + " "
                            + oldlevelTextField.getText() + " " + newlevelTestField.getText();
                }
                return null;
            });

// 显示对话框并等待用户响应
            dialog.showAndWait().ifPresent(result -> {
                player.createAndAddUpgrade(unitTestField.getText() + " " + originTextField.getText() + " "
                        + oldlevelTextField.getText() + " " + newlevelTestField.getText());
                System.out.println(result);
            });
        });
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

                    } else {
                        BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf("#f2e6ce"), CornerRadii.EMPTY, Insets.EMPTY);
                        Background background = new Background(backgroundFill);
                        button.setBackground(background);
                    }
                    BorderStroke borderStroke = new BorderStroke(Paint.valueOf("#3D3D3D"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1));
                    Border border = new Border(borderStroke);
                    button.setBorder(border);
                    rowBox.getChildren().add(button);

                }
            } else {
                for (; j < 4; j++) {
                    Button button = new Button("Map " + (i * 3 + j + 1));
                    button.setFont(new Font("Cambria", 16));
                    button.setPrefSize(75, 50);
                    buttons[index++] = button;

                    rowBox.getChildren().add(button);
                }
            }
            leftBox.getChildren().add(rowBox);
        }
        String[] colors;
        if (numsOfPlayer == 3) {
            colors = new String[]{"#f2e6ce", "#8ba583", "#8ba583", "#f2e6ce", "#f2e6ce", "#8ba583", "#74759b", "#74759b", "#74759b", "#A6A6A8"};
            buttons[9].setText("");
        } else {
            colors = new String[]{"#f2e6ce", "#f2e6ce", "#f2e6ce", "#f2e6ce", "#f2e6ce", "#8ba583", "#8ba583", "#8ba583", "#8ba583", "#8ba583"};
        }
        for (int i = 0; i < buttons.length; i++) {
            Button button = buttons[i];
            BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf(colors[i]), CornerRadii.EMPTY, Insets.EMPTY);
            Background background = new Background(backgroundFill);
            button.setBackground(background);
            BorderStroke borderStroke = new BorderStroke(Paint.valueOf("#3D3D3D"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1));
            Border border = new Border(borderStroke);
            button.setBorder(border);

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}