import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {

    public ArrayList<Player> playerList;
    public ArrayList<Territory> map;
    public int port;
    public ServerSocket serverSocket;
    private BasicChecker attackRuleChecker;
    private BasicChecker moveRuleChecker;

    public void acceptPlayer(){}

    public void gameStart(){}


    public void initialzeMapHelper(){

    }

    public void initialzeMap(){
        int playerNum = playerList.size();
        if(playerNum == 2){

        }else if(playerNum == 3){

        }else if(playerNum == 4){

        }else if(playerNum == 5){

        }
    }

    public void playTurn(){}

    private void checkBehavior(ArrayList<Behavior>){}

    public void sendPlayerStatus(){}

    public void gameOver(){}




}
