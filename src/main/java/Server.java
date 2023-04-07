import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Map;


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


    ArrayList<Map.Entry<String, Integer>> mapInfo;

    public void initialzeMap(){
        int playerNum = playerList.size();
        map.add(new Territory("Narnia", -1));
        map.add(new Territory("Narnia", -1));
        map.add(new Territory("Narnia", -1));
        map.add(new Territory("Narnia", -1));
        map.add(new Territory("Narnia", -1));
        map.add(new Territory("Narnia", -1));
        map.add(new Territory("Narnia", -1));
        map.add(new Territory("Narnia", -1));
        if(playerNum != 3 || playerNum != 4){
            map.add(new Territory("Narnia", -1));
        }
        if(playerNum != 4){
            map.add(new Territory("Narnia", -1));
        }



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
