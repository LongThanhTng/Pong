package gui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import model.Court; //plus tard pour paramétrer taille, etc
import model.CourtDeuxContreDeux;
import model.CourtObstacles;
import model.CourtSpeed;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import model.RacketController;
import javafx.scene.layout.*;
import javafx.scene.effect.ImageInput;
import model.TimeMode;
import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.control.ChoiceDialog;



//*************************************TEST*********** */
import java.io.File; 

//***************************************************** */

//App, fichier du jeu in-game
//Implémentation du menu pause : Fait
//Implémentation du menu de fin de jeu : fait

public class App {

    public static Pane root;
    public Scene gameScene;
    public int limite;

    App(Pane root, Scene a, int limite){
        App.root = root;
        gameScene = a;
        this.limite=limite;
    }

    App(Pane root, Scene a){
        App.root = root;
        gameScene = a;
    }

    public static String[] commandes = {"A", "Q", "P", "M", "E", "R", "J", "K"};

    public static void setCommandes(String[] s){
        commandes[0] = s[0];
        commandes[1] = s[1];
        commandes[2] = s[2];
        commandes[3] = s[3];
        commandes[4] = s[4];
        commandes[5] = s[5];
        commandes[6] = s[6];
        commandes[7] = s[7];
    }

    public static Button Quitter= new Button("Quitter") ;
    public static Button Reprendre= new Button("Reprendre") ;
    public static Button Recommencer= new Button("Recommencer") ;



    public void start(Stage primaryStage)  {

                    class Player implements RacketController {
                        State state = State.IDLE;
                        @Override
                        public State getState() {
                            return state;
                        }
                    }
                    var playerA = new Player();
                    var playerB = new Player();
                    Image img = new Image("file:src/Pictures/fond.png");
                    BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                    Background bGround = new Background(bImg);
                    root.setBackground(bGround);
                    var court = new Court(playerA, playerB, 1000, 600, limite);
                    var gameView = new GameView(court, root, 1);


                    //Pour le menu de pause
                    Image image2 = new Image(new File("src/Pictures/pause1.gif").toURI().toString());
                    ImageView imageV = new ImageView(image2);
                    imageV.setX(290);
                    imageV.setY(200);
                    
                    Quitter.setLayoutX(320);
                    Quitter.setLayoutY(350);
                    Quitter.setMinSize(80, 80);
                    Quitter.setEffect(new ImageInput(new Image("file:src/Pictures/retourM.png")));
                    Quitter.setSkin(new MyButtonSkin(Quitter));
                  
 
                    Reprendre.setLayoutX(485);
                    Reprendre.setLayoutY(350);
                    Reprendre.setMinSize(80, 80);
                    Reprendre.setEffect(new ImageInput(new Image("file:src/Pictures/play.png")));
                    Reprendre.setSkin(new MyButtonSkin(Reprendre));
                   

                    Recommencer.setLayoutX(695);
                    Recommencer.setLayoutY(350);
                    Recommencer.setMinSize(80, 80);
                    Recommencer.setEffect(new ImageInput(new Image("file:src/Pictures/recommencer.png")));
                    Recommencer.setSkin(new MyButtonSkin(Recommencer));
                  
                    //Switch pour les boutons de jeu, in-game.
                    gameScene.setOnKeyPressed(ev -> {
                        String s = ev.getCode().toString();

                        if(s == commandes[0]){
                            playerA.state = RacketController.State.GOING_UP;
                        } else if(s == commandes[1]){
                            playerA.state = RacketController.State.GOING_DOWN;
                        } else if(s == commandes[2]){
                            playerB.state = RacketController.State.GOING_UP;
                        } else if(s == commandes[3]){
                            playerB.state = RacketController.State.GOING_DOWN;
                        } else if(s == "ESCAPE"){
                            if(!GameView.pause && !GameView.finGame){
                                root.getChildren().add(imageV);
                                root.getChildren().addAll(Quitter, Reprendre, Recommencer);
                                GameView.pause = true;
                           }else{
                                if(!GameView.finGame){
                                    root.getChildren().removeAll(imageV, Quitter, Reprendre, Recommencer);
                                    GameView.pause = false ; 
                                }
                            }    
                        }
                    });


                    //Switch bouton in-game, uniquement pour les boutons de jeu. 
                    gameScene.setOnKeyReleased(ev -> {
                        String s = ev.getCode().toString();

                        if(s == commandes[0]){
                            if (playerA.state == RacketController.State.GOING_UP) playerA.state = RacketController.State.IDLE;
                        } else if(s == commandes[1]){
                            if (playerA.state == RacketController.State.GOING_DOWN) playerA.state = RacketController.State.IDLE;
                        } else if(s == commandes[2]){
                            if (playerB.state == RacketController.State.GOING_UP) playerB.state = RacketController.State.IDLE;
                        } else if(s == commandes[3]){
                            if (playerB.state == RacketController.State.GOING_DOWN) playerB.state = RacketController.State.IDLE;
                        }
                    });


                    gameView.animate();
                    

                    //Action du bouton Quitter
                    Quitter.setOnAction(ev1 -> {
                        Pane root1 = new Pane();
                        gameScene.setRoot(root1);
                        Menu a = new Menu(root1, gameScene);
                        a.start(primaryStage);
                    });

                    //Action du bouton Reprendre
                    Reprendre.setOnAction(ev1 ->{
                        root.getChildren().removeAll(imageV, Quitter, Reprendre, Recommencer);
                        GameView.pause = false ; 
                    });

                    //Action du bouton Recommencer
                    Recommencer.setOnAction(ev1 ->{
                        Quitter.setLayoutX(320);
                        Recommencer.setLayoutX(695);
                        Recommencer.setLayoutY(350);
                        Quitter.setLayoutY(350);
                        root.getChildren().remove(imageV);
                        if (GameView.finGame){
                            root.getChildren().remove(root.getChildren().size()-3) ; 
                            root.getChildren().remove(root.getChildren().size()-3) ;  
                        } 
                        root.getChildren().removeAll(Quitter, Reprendre, Recommencer);
                        court.refresh(); 
                        GameView.pause = false ; 
                        GameView.finGame = false;
                    });			

    }

    //Start pour le 2 contre 2
    public void start2C2(Stage primaryStage)  {

        class Player implements RacketController {
            State state = State.IDLE;
            @Override
            public State getState() {return state;}
        }
        
        var playerA = new Player();
        var playerB = new Player();
        var playerC = new Player();
        var playerD = new Player();
        Image img = new Image("file:src/Pictures/fond.png");
        BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        root.setBackground(bGround);
        ArrayList<Integer> limiteS = new ArrayList<Integer>();
        limiteS.add(2);
        limiteS.add(4);
        limiteS.add(6);
        limiteS.add(8);

        ChoiceDialog<Integer> limiteScore = new ChoiceDialog<Integer>(2, limiteS);
        limiteScore.initOwner(primaryStage);
        limiteScore.setTitle("Limite de Score");
        limiteScore.setHeaderText("Veuillez choisir un nombre de points maximum");
        limiteScore.setContentText("Nombre : ");

        Optional<Integer> limitScore = limiteScore.showAndWait();
        limitScore.ifPresent(limite -> {
            this.limite = limite;
        });   

        ArrayList<String> pop = new ArrayList<String>();
        pop.add("COMPRIS");
        pop.add("PAS COMPRIS");
        ChoiceDialog<String> pop2 = new ChoiceDialog<String>("COMPRIS", pop);
        pop2.initOwner(primaryStage);
        pop2.setTitle("Touches du Joueur 3 & 4:");
        pop2.setHeaderText("TOUCHE DU J3 : E et R  |  TOUCHE DU J4: J et K");
        pop2.setResizable(false);
        Optional<String> popOk = pop2.showAndWait();
        if(popOk.isEmpty()){
            Pane root1 = new Pane();
            gameScene.setRoot(root1);
            ModeDeJeu a = new ModeDeJeu(root1, gameScene);
            a.start(primaryStage);
        }if (popOk.isPresent()){
            if(popOk.get().equals("COMPRIS")){
                var court = new CourtDeuxContreDeux(playerA, playerB, playerC, playerD, 1000, 600, limite);
                var gameView = new GameViewDeuxContreDeux(court, root, 1);

                //Pour le menu de pause
                Image image2 = new Image(new File("src/Pictures/pause1.gif").toURI().toString());
                ImageView imageV = new ImageView(image2);
                imageV.setX(290);
                imageV.setY(200);
                    
                Quitter.setLayoutX(320);
                Quitter.setLayoutY(350);
                Quitter.setMinSize(80, 80);
                Quitter.setEffect(new ImageInput(new Image("file:src/Pictures/retourM.png")));
                Quitter.setSkin(new MyButtonSkin(Quitter));
              
                Reprendre.setLayoutX(485);
                Reprendre.setLayoutY(350);
                Reprendre.setMinSize(80, 80);
                Reprendre.setEffect(new ImageInput(new Image("file:src/Pictures/play.png")));
                Reprendre.setSkin(new MyButtonSkin(Reprendre));
                   

                Recommencer.setLayoutX(695);
                Recommencer.setLayoutY(350);
                Recommencer.setMinSize(80, 80);
                Recommencer.setEffect(new ImageInput(new Image("file:src/Pictures/recommencer.png")));
                Recommencer.setSkin(new MyButtonSkin(Recommencer));
                
                //Switch pour les boutons de jeu, in-game.
                gameScene.setOnKeyPressed(ev -> {
                    String s = ev.getCode().toString();
                    if(s == commandes[0]){
                        playerA.state = RacketController.State.GOING_UP;
                    } else if(s == commandes[1]){
                        playerA.state = RacketController.State.GOING_DOWN;
                    } else if(s == commandes[2]){
                        playerB.state = RacketController.State.GOING_UP;
                    } else if(s == commandes[3]){
                        playerB.state = RacketController.State.GOING_DOWN;
                    } else if(s == commandes[4]){
                        playerC.state = RacketController.State.GOING_UP;
                    } else if(s == commandes[5]){
                        playerC.state = RacketController.State.GOING_DOWN;
                    } else if(s == commandes[6]){
                        playerD.state = RacketController.State.GOING_UP;
                    } else if(s == commandes[7]){
                        playerD.state = RacketController.State.GOING_DOWN;
                    } else if(s == "ESCAPE"){
                        if(!GameView.pause && !GameView.finGame){
                            root.getChildren().add(imageV);
                            root.getChildren().addAll(Quitter, Reprendre, Recommencer);
                            GameView.pause = true;
                       }else{
                            if(!GameView.finGame){
                                root.getChildren().removeAll(imageV, Quitter, Reprendre, Recommencer);
                                GameView.pause = false ; 
                            }
                        }    
                    }
                });
                gameScene.setOnKeyReleased(ev -> {
                String s = ev.getCode().toString();

                //Switch bouton in-game, uniquement pour les boutons de jeu. 
                if(s == commandes[0]){
                    if (playerA.state == RacketController.State.GOING_UP) playerA.state = RacketController.State.IDLE;
                } else if(s == commandes[1]){
                    if (playerA.state == RacketController.State.GOING_DOWN) playerA.state = RacketController.State.IDLE;
                } else if(s == commandes[2]){
                    if (playerB.state == RacketController.State.GOING_UP) playerB.state = RacketController.State.IDLE;
                } else if(s == commandes[3]){
                    if (playerB.state == RacketController.State.GOING_DOWN) playerB.state = RacketController.State.IDLE;
                } else if(s == commandes[4]){
                    if (playerC.state == RacketController.State.GOING_UP) playerC.state = RacketController.State.IDLE;
                } else if(s == commandes[5]){
                    if (playerC.state == RacketController.State.GOING_DOWN) playerC.state = RacketController.State.IDLE;
                } else if(s == commandes[6]){
                    if (playerD.state == RacketController.State.GOING_UP) playerD.state = RacketController.State.IDLE;
                } else if(s == commandes[7]){
                    if (playerD.state == RacketController.State.GOING_DOWN) playerD.state = RacketController.State.IDLE;
                }


            });

                gameView.animate();
                
            //Action du bouton Quitter
                Quitter.setOnAction(ev1 -> {
                    Pane root1 = new Pane();
                    gameScene.setRoot(root1);
                    Menu a = new Menu(root1, gameScene);
                    a.start(primaryStage);
                });
            //Action du bouton Reprendre
                Reprendre.setOnAction(ev1 ->{
                    root.getChildren().removeAll(imageV, Quitter, Reprendre, Recommencer);
                    GameView.pause = false ; 
                });

                    //Action du bouton Recommencer
                    Recommencer.setOnAction(ev1 ->{
                    Quitter.setLayoutX(320);
                    Recommencer.setLayoutX(695);
                    Recommencer.setLayoutY(350);
                    Quitter.setLayoutY(350);
                    root.getChildren().remove(imageV);
                    if (GameView.finGame){
                        root.getChildren().remove(root.getChildren().size()-3) ; 
                        root.getChildren().remove(root.getChildren().size()-3) ;  
                    } 
                    root.getChildren().removeAll(Quitter, Reprendre, Recommencer);
                    court.refresh(); 
                    GameView.pause = false ; 
                    GameView.finGame = false;
                });                
            }
        }     

    }
    
    //pour le timer de timermode
    public void startTimer(Stage primaryStage, int nbManche, int t)  {

        class Player implements RacketController {
            State state = State.IDLE;
            @Override
            public State getState() {
                return state;
            }
        }
        var playerA = new Player();
        var playerB = new Player();
        Image img = new Image("file:src/Pictures/fond.png");
        BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        root.setBackground(bGround);
        var court = new TimeMode(playerA, playerB, 1000, 600, nbManche, t);
        var gameView = new GameView(court, root, 1);

         //Pour le menu de pause
         Image image2 = new Image(new File("src/Pictures/pause1.gif").toURI().toString());
         ImageView imageV = new ImageView(image2);
         imageV.setX(290);
         imageV.setY(200);
         
         Quitter.setLayoutX(320);
         Quitter.setLayoutY(350);
         Quitter.setMinSize(80, 80);
         Quitter.setEffect(new ImageInput(new Image("file:src/Pictures/retourM.png")));
         Quitter.setSkin(new MyButtonSkin(Quitter));
       
         
         Reprendre.setLayoutX(485);
         Reprendre.setLayoutY(350);
         Reprendre.setMinSize(80, 80);
         Reprendre.setEffect(new ImageInput(new Image("file:src/Pictures/play.png")));
         Reprendre.setSkin(new MyButtonSkin(Reprendre));
        

         Recommencer.setLayoutX(695);
         Recommencer.setLayoutY(350);
         Recommencer.setMinSize(80, 80);
         Recommencer.setEffect(new ImageInput(new Image("file:src/Pictures/recommencer.png")));
         Recommencer.setSkin(new MyButtonSkin(Recommencer));
    
        //Switch pour les boutons de jeu, in-game.
        gameScene.setOnKeyPressed(ev -> {
            String s = ev.getCode().toString();

                if(s == commandes[0]){
                    playerA.state = RacketController.State.GOING_UP;
                } else if(s == commandes[1]){
                    playerA.state = RacketController.State.GOING_DOWN;
                } else if(s == commandes[2]){
                    playerB.state = RacketController.State.GOING_UP;
                } else if(s == commandes[3]){
                    playerB.state = RacketController.State.GOING_DOWN;
                } else if(s == "ESCAPE"){
                   if(!GameView.pause && !GameView.finGame){
                    root.getChildren().add(imageV);
                    root.getChildren().addAll(Quitter, Reprendre, Recommencer);
                    GameView.pause = true;
                   }else if(!GameView.finGame){
                    root.getChildren().removeAll(imageV, Quitter, Reprendre, Recommencer);
                    GameView.pause = false ; 
                   }
            }
        });


        //Switch bouton in-game, uniquement pour les boutons de jeu. 
        gameScene.setOnKeyReleased(ev -> {
            String s = ev.getCode().toString();

            if(s == commandes[0]){
                if (playerA.state == RacketController.State.GOING_UP) playerA.state = RacketController.State.IDLE;
            } else if(s == commandes[1]){
                if (playerA.state == RacketController.State.GOING_DOWN) playerA.state = RacketController.State.IDLE;
            } else if(s == commandes[2]){
                if (playerB.state == RacketController.State.GOING_UP) playerB.state = RacketController.State.IDLE;
            } else if(s == commandes[3]){
                if (playerB.state == RacketController.State.GOING_DOWN) playerB.state = RacketController.State.IDLE;
            }
        });


        gameView.animate();
        

        //Action du bouton Quitter
        Quitter.setOnAction(ev1 -> {
            Pane root1 = new Pane();
            gameScene.setRoot(root1);
            Menu a = new Menu(root1, gameScene);
            if(court instanceof TimeMode) {
                court.closeTimer();
                court.resetNbManche();
            }
            a.start(primaryStage);
        });

        //Action du bouton Reprendre
        Reprendre.setOnAction(ev1 ->{
            root.getChildren().removeAll(imageV, Quitter, Reprendre, Recommencer);
            GameView.pause = false ; 
        });

        //Action du bouton Recommencer
        Recommencer.setOnAction(ev1 ->{
            Quitter.setLayoutX(320);
            Recommencer.setLayoutX(695);
            Recommencer.setLayoutY(350);
            Quitter.setLayoutY(350);
            root.getChildren().remove(imageV);
            if (GameView.finGame){
                root.getChildren().remove(root.getChildren().size()-3) ; 
                root.getChildren().remove(root.getChildren().size()-3) ;  
            }           
            root.getChildren().removeAll(Quitter, Reprendre, Recommencer);
            court.reset() ;  
           court.getScore().reset();
           GameView.pause = false ; 
           GameView.finGame = false;
           if(court instanceof TimeMode) court.commencerTimer();
        });	
    }

    
    public void startSpeed(Stage primaryStage)  {

        class Player implements RacketController {
            State state = State.IDLE;
            @Override
            public State getState() {
                return state;
            }
        }
        var playerA = new Player();
        var playerB = new Player();
        Image img = new Image("file:src/Pictures/fond.png");
        BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        root.setBackground(bGround);
        var court = new CourtSpeed(playerA, playerB, 1000, 600, limite);
        var gameView = new GameView(court, root, 1);


        //Pour le menu de pause
        Image image2 = new Image(new File("src/Pictures/pause1.gif").toURI().toString());
        ImageView imageV = new ImageView(image2);
        imageV.setX(290);
        imageV.setY(200);
        
        Quitter.setLayoutX(320);
        Quitter.setLayoutY(350);
        Quitter.setMinSize(80, 80);
        Quitter.setEffect(new ImageInput(new Image("file:src/Pictures/retourM.png")));
        Quitter.setSkin(new MyButtonSkin(Quitter));
      
        
        Reprendre.setLayoutX(485);
        Reprendre.setLayoutY(350);
        Reprendre.setMinSize(80, 80);
        Reprendre.setEffect(new ImageInput(new Image("file:src/Pictures/play.png")));
        Reprendre.setSkin(new MyButtonSkin(Reprendre));
       

        Recommencer.setLayoutX(695);
        Recommencer.setLayoutY(350);
        Recommencer.setMinSize(80, 80);
        Recommencer.setEffect(new ImageInput(new Image("file:src/Pictures/recommencer.png")));
        Recommencer.setSkin(new MyButtonSkin(Recommencer));
      
        //Switch pour les boutons de jeu, in-game.
        gameScene.setOnKeyPressed(ev -> {
            String s = ev.getCode().toString();

                if(s == commandes[0]){
                    playerA.state = RacketController.State.GOING_UP;
                } else if(s == commandes[1]){
                    playerA.state = RacketController.State.GOING_DOWN;
                } else if(s == commandes[2]){
                    playerB.state = RacketController.State.GOING_UP;
                } else if(s == commandes[3]){
                    playerB.state = RacketController.State.GOING_DOWN;
                } else if(s == "ESCAPE"){
                   if(!GameView.pause && !GameView.finGame){
                    root.getChildren().add(imageV);
                    root.getChildren().addAll(Quitter, Reprendre, Recommencer);
                    GameView.pause = true;
                   }else if(!GameView.finGame){
                    root.getChildren().removeAll(imageV, Quitter, Reprendre, Recommencer);
                    GameView.pause = false ; 
                   }
            }
        });


        //Switch bouton in-game, uniquement pour les boutons de jeu. 
        gameScene.setOnKeyReleased(ev -> {
            String s = ev.getCode().toString();

            if(s == commandes[0]){
                if (playerA.state == RacketController.State.GOING_UP) playerA.state = RacketController.State.IDLE;
            } else if(s == commandes[1]){
                if (playerA.state == RacketController.State.GOING_DOWN) playerA.state = RacketController.State.IDLE;
            } else if(s == commandes[2]){
                if (playerB.state == RacketController.State.GOING_UP) playerB.state = RacketController.State.IDLE;
            } else if(s == commandes[3]){
                if (playerB.state == RacketController.State.GOING_DOWN) playerB.state = RacketController.State.IDLE;
            }
        });


        gameView.animate();
        

        //Action du bouton Quitter
        Quitter.setOnAction(ev1 -> {
            Pane root1 = new Pane();
        gameScene.setRoot(root1);
        Menu a = new Menu(root1, gameScene);
        a.start(primaryStage);
        });

        //Action du bouton Reprendre
        Reprendre.setOnAction(ev1 ->{
            root.getChildren().removeAll(imageV, Quitter, Reprendre, Recommencer);
            GameView.pause = false ; 
        });

        //Action du bouton Recommencer
        Recommencer.setOnAction(ev1 ->{
            Quitter.setLayoutX(320);
            Recommencer.setLayoutX(695);
            Recommencer.setLayoutY(350);
            Quitter.setLayoutY(350);
            root.getChildren().remove(imageV);
            if (GameView.finGame){
                root.getChildren().remove(root.getChildren().size()-3) ; 
                root.getChildren().remove(root.getChildren().size()-3) ;  
            } 
            root.getChildren().removeAll(Quitter, Reprendre, Recommencer);
            court.refresh();
            GameView.pause = false ; 
            GameView.finGame = false;
        });			

}

    public void startObstacles(Stage primaryStage , boolean vitesse)  {

        class Player implements RacketController {
            State state = State.IDLE;
            @Override
            public State getState() {
                return state;
            }
        }
        var playerA = new Player();
        var playerB = new Player();
        Image img = new Image("file:src/Pictures/fond.png");
        BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        root.setBackground(bGround);
        var court = new CourtObstacles(playerA, playerB, 1000, 600, this.limite) ; 
        var gameView = new GameView(court, root, 1);


        //Pour le menu de pause
        Image image2 = new Image(new File("src/Pictures/pause1.gif").toURI().toString());
        ImageView imageV = new ImageView(image2);
        imageV.setX(290);
        imageV.setY(200);
        
        Quitter.setLayoutX(320);
        Quitter.setLayoutY(350);
        Quitter.setMinSize(80, 80);
        Quitter.setEffect(new ImageInput(new Image("file:src/Pictures/retourM.png")));
        Quitter.setSkin(new MyButtonSkin(Quitter));
    

        Reprendre.setLayoutX(485);
        Reprendre.setLayoutY(350);
        Reprendre.setMinSize(80, 80);
        Reprendre.setEffect(new ImageInput(new Image("file:src/Pictures/play.png")));
        Reprendre.setSkin(new MyButtonSkin(Reprendre));
    

        Recommencer.setLayoutX(695);
        Recommencer.setLayoutY(350);
        Recommencer.setMinSize(80, 80);
        Recommencer.setEffect(new ImageInput(new Image("file:src/Pictures/recommencer.png")));
        Recommencer.setSkin(new MyButtonSkin(Recommencer));
    
        //Switch pour les boutons de jeu, in-game.
        gameScene.setOnKeyPressed(ev -> {
            String s = ev.getCode().toString();

            if(s == commandes[0]){
                playerA.state = RacketController.State.GOING_UP;
            } else if(s == commandes[1]){
                playerA.state = RacketController.State.GOING_DOWN;
            } else if(s == commandes[2]){
                playerB.state = RacketController.State.GOING_UP;
            } else if(s == commandes[3]){
                playerB.state = RacketController.State.GOING_DOWN;
            } else if(s == "ESCAPE"){
                if(!GameView.pause && !GameView.finGame){
                    root.getChildren().add(imageV);
                    root.getChildren().addAll(Quitter, Reprendre, Recommencer);
                    GameView.pause = true;
            }else{
                    if(!GameView.finGame){
                        root.getChildren().removeAll(imageV, Quitter, Reprendre, Recommencer);
                        GameView.pause = false ; 
                    }
                }    
            }
        });


        //Switch bouton in-game, uniquement pour les boutons de jeu. 
        gameScene.setOnKeyReleased(ev -> {
            String s = ev.getCode().toString();

            if(s == commandes[0]){
                if (playerA.state == RacketController.State.GOING_UP) playerA.state = RacketController.State.IDLE;
            } else if(s == commandes[1]){
                if (playerA.state == RacketController.State.GOING_DOWN) playerA.state = RacketController.State.IDLE;
            } else if(s == commandes[2]){
                if (playerB.state == RacketController.State.GOING_UP) playerB.state = RacketController.State.IDLE;
            } else if(s == commandes[3]){
                if (playerB.state == RacketController.State.GOING_DOWN) playerB.state = RacketController.State.IDLE;
            }
        });


        gameView.animate();
        

        //Action du bouton Quitter
        Quitter.setOnAction(ev1 -> {
            Pane root1 = new Pane();
            gameScene.setRoot(root1);
            Menu a = new Menu(root1, gameScene);
            a.start(primaryStage);
        });

        //Action du bouton Reprendre
        Reprendre.setOnAction(ev1 ->{
            root.getChildren().removeAll(imageV, Quitter, Reprendre, Recommencer);
            GameView.pause = false ; 
        });

        //Action du bouton Recommencer
        Recommencer.setOnAction(ev1 ->{
            Quitter.setLayoutX(320);
            Recommencer.setLayoutX(695);
            Recommencer.setLayoutY(350);
            Quitter.setLayoutY(350);
            root.getChildren().remove(imageV);
            if (GameView.finGame){
                root.getChildren().remove(root.getChildren().size()-3) ; 
                root.getChildren().remove(root.getChildren().size()-3) ;  
            } 
            root.getChildren().removeAll(Quitter, Reprendre, Recommencer);
            court.refresh();
            GameView.pause = false ; 
            GameView.finGame = false;
        });			

    }
}
