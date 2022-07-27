package view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.*;
import model.Maps;
import model.Request;
import model.Response;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ClientHandler extends Thread{
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket;
    private Receiver receiver;
    private String username;
    private User user;
    private Maps map = ServerController.getInstance().getMap();
    private MapController mapController = MapController.getInstance();

    private boolean playing;

    public ClientHandler(Socket socket, Receiver receiver) throws IOException {
        this.socket = socket;
        this.receiver = receiver;
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        username = "user" + receiver.getClientHandlers().size();
        playing = true;
    }

    @Override
    public void run() {
        Response response;
        while (true) {
            try {
                if (socket.isClosed())
                    return;
                String input = dataInputStream.readUTF();
                Request request = Request.fromJson(input);
                if (playing) {

                    if (request.getMenu().equals("play game") && request.getAction().equals("show map")) {
                        String username = request.getParameters().get("username");
                        User user2 = UsersController.getInstance().getUserByUsername(username);
                        String string = showMap(user2);
                        for (int i = 0; i < 100; i++) {
                            response = new Response();
                            if (i == 0)
                                response.setStatusCode("fuck you");
                            response.setMessage(string.substring((string.length() * i) / 100, (string.length() * (i + 1)) / 100));
                            dataOutputStream.writeUTF(response.toJson());
                            dataOutputStream.flush();
                        }
                        //System.out.println(response.getMessage());
                    }

                    else {
                        response = process(request);

                        if (response == null) {
                            response = new Response();
                            response.setMessage("error");
                            System.out.println("error");
                        }
                        String output = response.toJson();
                        dataOutputStream.writeUTF(output);
                        dataOutputStream.flush();
                    }
                }
                else {
                    response = new Response();
                    response.setMessage("please wait...");
                    dataOutputStream.writeUTF(response.toJson());
                    dataOutputStream.flush();
                }
            } catch (IOException e) {
                System.out.println("client disconnected");
                receiver.kickClient(this);
            }
        }
    }

    public void terminateClient() {
        receiver.kickClient(this);
        try {
            dataInputStream.close();
            System.out.println("client terminated!");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response process(Request request) {
        Response response;
        switch (request.getMenu()) {
            case "register menu" :
                if (request.getAction().equals("signup"))
                    return UsersController.getInstance().signup(request);
                else {
                    response = UsersController.getInstance().login(request);
                    if (response.getStatusCode().equals("200")) {
                        username = response.getParameters().get("username");
                        user = UsersController.getInstance().getUserByUsername(username);
                    }
                    return response;
                }
            case "profile menu":
                if (request.getAction().equals("change password"))
                    return UsersController.getInstance().changePassword(request);
                else
                    return UsersController.getInstance().changeNickname(request);

            case "game menu":
                if (request.getAction().equals("play game"))
                    return UsersController.getInstance().startGame(request);

            case "play game" :
                switch (request.getAction()) {
                    case "man page":
                        return GameController.getInstance().manPage(request);
                    case "set players" :
                        response = GameController.getInstance().setPlayers(request);
                        if (Boolean.parseBoolean(response.getParameters().get("valid"))) {
                            //set players in receiver
                            receiver.setPlayers(new Gson().fromJson(response.getParameters().get("players"), new TypeToken<List<User>>(){}.getType()));
                            response.getParameters().remove("players");
                            //initialize client handler of players
                            LinkedList<ClientHandler> clientHandlers = new LinkedList<>();
                            for (ClientHandler clientHandler : receiver.getClientHandlers()) {
                                if (receiver.getPlayers().contains(clientHandler.getUser()))
                                    clientHandlers.add(clientHandler);
                            }
                            //set client handler of players
                            receiver.setClientHandlersPlayers(clientHandlers);
                            GameController.getInstance().nextPlayer(user);
                            for (ClientHandler clientHandler : receiver.getClientHandlers()) {
                                if (!clientHandler.getUser().equals(user))
                                    clientHandler.setPlaying(false);
                            }
                        }
                        response.getParameters().remove("valid");
                        return response;
                    case "next turn":
                        response = GameController.getInstance().nextTurn(request);
                        //assume the request accepted!
                        playing = false;
                        User you = UsersController.getInstance().getUserByUsername(username);
                        int indexOfYou = receiver.getPlayers().indexOf(you);
                        indexOfYou++;
                        if (indexOfYou == receiver.getPlayers().size())
                            indexOfYou = 0;
                        for (ClientHandler clientHandler : receiver.getClientHandlers()) {
                            if (clientHandler.getUsername().equals(receiver.getPlayers().get(indexOfYou).getUsername())) {
                                clientHandler.setPlaying(true);
                                GameController.getInstance().nextPlayer(receiver.getPlayers().get(indexOfYou));
                            }
                        }
                        return response;
                    case "increase turn":
                        return GameController.getInstance().increaseTurnRequest(request);
                    case "increase gold":
                        return GameController.getInstance().increaseGoldRequest(request);
                    case "increase food":
                        return GameController.getInstance().increaseFoodRequest(request);
                    case "increase faith":
                        return GameController.getInstance().increaseFaithRequest(request);
                    case "increase science":
                        return GameController.getInstance().increaseScienceRequest(request);
                    case "increase capital citizens":
                        return GameController.getInstance().increaseCapitalCitizensRequest(request);
                    case "increase capital defence":
                        return GameController.getInstance().increaseCapitalDefenceRequest(request);
                    case "increase culture":
                        return GameController.getInstance().increaseCultureRequest(request);
                    case "increase happiness":
                        return GameController.getInstance().increaseHappinessRequest(request);
                    case "decrease research turn":
                        return GameController.getInstance().decreaseResearchTurnRequest(request);
                    case "select tile":
                        return GameController.getInstance().selectTileRequest(request);
                    case "search friend":
                        return GameController.getInstance().searchFriend(request);
                    case "show map":
                        return GameController.getInstance().showMap(request);
                    case "show players":
                        return GameController.getInstance().setPlayers(request);
                }
            case "tile menu":
                switch(request.getAction()) {
                    case "move unit":
                        return MapController.getInstance().moveUnitConditions(ServerController.getInstance().getMap(), request);
                    case "place city":
                        return CityController.getInstance().conditionsForPlaceCity(request, ServerController.getInstance().getMap());
                    case "attack city":
                        return CityController.getInstance().conditionForAttackCity(request, ServerController.getInstance().getMap());
                    case "decision on what to do with city":
                        return CombatController.getInstance().decisionOnWhatDoDo(request, ServerController.getInstance().getMap());
                    case "attack unit":
                        return CombatController.getInstance().conditionForAttackUnit(request, ServerController.getInstance().getMap());
                    case "delete unit":
                        return UnitController.getInstance().deleteUnit(request, ServerController.getInstance().getMap());
                    case "sleep unit":
                        return UnitController.getInstance().sleepUnit(request, ServerController.getInstance().getMap());
                    case "alert unit":
                        return UnitController.getInstance().alertUnit(request, ServerController.getInstance().getMap());
                    case "garrison unit":
                        return UnitController.getInstance().garrisonUnit(request, ServerController.getInstance().getMap());
                    case "fortify unit":
                        return UnitController.getInstance().fortifyUnit(request, ServerController.getInstance().getMap());
                    case "pillage unit":
                        return UnitController.getInstance().pillageUnit(request, ServerController.getInstance().getMap());
                    case "repair improvement":
                        return UnitController.getInstance().repairImprovement(request, ServerController.getInstance().getMap());
                    case "pause improvement":
                        return UnitController.getInstance().pauseImprovement(request, ServerController.getInstance().getMap());
                    case "resume improvement":
                        return UnitController.getInstance().resumeImprovement(request, ServerController.getInstance().getMap());
                    case "improvement information":
                        return GameController.getInstance().improvementInformation(request);
                    case "add improvement":
                        return GameController.getInstance().addImprovement(request);
                    case "delete improvement":
                        return GameController.getInstance().deleteImprovement(request);
                    case "tile information":
                        return GameController.getInstance().tileInformation(request);
                    case "build road":
                        return GameController.getInstance().buildRoad(request);
                    case "select another tile":
                        return GameController.getInstance().selectAnotherTile(request);
                    case "show possible improvements":
                        return GameController.getInstance().showPossibleImprovements(request);
                }
            case "city menu":
                switch(request.getAction()) {
                    case "set production":
                        return CityController.getInstance().setProduction(request);
                    case "buy tile":
                        return CityController.getInstance().buyTile(request);
                    case "set citizen":
                        return CityController.getInstance().setCitizen(request);
                    case "user cities":
                        return CityController.getInstance().userCities(request);
                    case "nearby tiles information":
                        return CityController.getInstance().nearbyTilesInformation(request);
                    case "citizen information":
                        return CityController.getInstance().citizenInformation(request);
                    case "tile citizen information":
                        return CityController.getInstance().tileCitizenInformation(request);
                }
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String showMap(User user) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("user nickname : " + new ColorsController().getColorOfUser(user) + user.getNickname() + Colors.RESET + "\n");
        stringBuilder.append("user Happiness : " + user.getHappiness());
        stringBuilder.append("           user UnHappiness : " + user.getUnhappiness());
        stringBuilder.append("           user Food : " + user.getFood());
        stringBuilder.append("           user Gold : " + user.getGold());
        stringBuilder.append("           user Science : " + user.getScience() + "\n");
        //first top sides of left tiles of game board
        for (int j = 0; j < map.getWidth(); j++)
            stringBuilder.append("   " + mapController.riverFinder(map.getTileBoard()[0][j], 0) + "              ");
        stringBuilder.append("\n");

        //print the game board
        for (int i = 0; i < map.getHeight()/2; i++) {
            stringBuilder.append(leftCoordinatesPlace(i));
            stringBuilder.append(leftOwnerName(i));
            stringBuilder.append(leftTilesUnit(i));
            stringBuilder.append(leftResourceAndTerrain(i));
            stringBuilder.append(rightOwnerName(i));
            stringBuilder.append(leftBottomSides(i));
        }
        return stringBuilder.toString();

    }

    private String leftCoordinatesPlace(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        //Coordinates of left tiles and resource and terrain in right tiles
        for (int j = 0; j < map.getWidth(); j++) {
            if (i != 0) {
                if (j != 0)
                    stringBuilder.append(mapController.getColorOfTile(map.getTileBoard()[2 * i - 1][j - 1])
                            + "  " + Colors.RESET);
                else stringBuilder.append("  ");
                stringBuilder.append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 5)
                        + mapController.getColorOfTile(map.getTileBoard()[2 * i][j]) + leftCoordination(i , j)
                        + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i][j], 1)
                        + mapController.getColorOfTile(map.getTileBoard()[2 * i - 1][j]) + "   "
                        + mapController.tileResource(map.getTileBoard()[2 * i - 1][j], false)
                        + "   " + mapController.tileFeature(map.getTileBoard()[2 * i - 1][j], false)
                        + "  " + Colors.RESET);
            } else stringBuilder.append("  " + mapController.riverFinder(map.getTileBoard()[2 * i][j], 5)
                    + mapController.getColorOfTile(map.getTileBoard()[2 * i][j])
                    + leftCoordination(0 , j) + Colors.RESET
                    + mapController.riverFinder(map.getTileBoard()[2 * i][j], 1) + "             ");
        }
        if (i != 0) stringBuilder.append(mapController.getColorOfTile(map.getTileBoard()[2 * i - 1][map.getWidth() - 1])
                + "  " + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i - 1][map.getWidth() - 1], 2) + "\n");
        else stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String leftCoordination(int i, int j){
        if (2 * i < 10 && j < 10) return "  [0" + 2 * i + ",0" + j + "]  ";
        else if (2 * i > 9 && j < 10) return "  [" + 2 * i + ",0" + j + "]  ";
        else if (2 * i > 9) return "  [" + 2 * i + "," + j + "]  ";
        else return "  [0" + 2 * i + "," + j + "]  ";
    }

    private String leftOwnerName(int i) {
        String ANSI_COLOR;
        StringBuilder stringBuilder = new StringBuilder();
        //owner name and color of left tiles and right Improvement
        for (int j = 0; j < map.getWidth(); j++) {
            ANSI_COLOR = mapController.getColorOfTileOwner(map.getTileBoard()[2 * i][j]);
            if (i != 0 && j != 0)
                stringBuilder.append(mapController.getColorOfTile(map.getTileBoard()[2 * i - 1][j - 1]));
            stringBuilder.append(" " + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i][j], 5)
                    + mapController.getColorOfTile(map.getTileBoard()[2 * i][j]) + "     " + ANSI_COLOR
                    + mapController.getColorOfTile(map.getTileBoard()[2 * i][j])
                    + mapController.tileOwner(map.getTileBoard()[2 * i][j])
                    + mapController.getColorOfTile(map.getTileBoard()[2 * i][j]) + "     " + Colors.RESET
                    + mapController.riverFinder(map.getTileBoard()[2 * i][j], 1));
            if (i != 0)
                stringBuilder.append(mapController.getColorOfTileOwner(map.getTileBoard()[2 * i - 1][j])
                        + mapController.getColorOfTile(map.getTileBoard()[2 * i - 1][j])
                        + "     " + mapController.tileImprovement(map.getTileBoard()[2 * i - 1][j])
                        + "    " + Colors.RESET);
            else stringBuilder.append("            ");
        }
        if (i != 0) stringBuilder.append(mapController.getColorOfTile(map.getTileBoard()[2 * i - 1][map.getWidth() - 1])
                + " " + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i][0], 2) + "\n");
        else stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String leftTilesUnit(int i){
        String ANSI_COLOR;
        StringBuilder stringBuilder = new StringBuilder();
        //units on the left tiles and top sides of right tiles
        for (int j = 0; j < map.getWidth(); j++) {
            ANSI_COLOR = mapController.getColorOfTileOwner(map.getTileBoard()[2 * i][j]);
            stringBuilder.append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 5)
                    + mapController.getColorOfTile(map.getTileBoard()[2 * i][j]) + "    "
                    + ANSI_COLOR + mapController.getColorOfTile(map.getTileBoard()[2 * i][j])
                    + mapController.civilianUnit(map.getTileBoard()[2 * i][j])
                    + "   " + ANSI_COLOR + mapController.getColorOfTile(map.getTileBoard()[2 * i][j])
                    + mapController.militaryUnit(map.getTileBoard()[2 * i][j])
                    + mapController.getColorOfTile(map.getTileBoard()[2 * i][j]) + "    "
                    + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i][j], 1)
            );
            if (i != 0) stringBuilder.append(mapController.riverFinder(map.getTileBoard()[2 * i - 1][j], 3));
            else stringBuilder.append(mapController.riverFinder(map.getTileBoard()[1][j], 0));
        }
        if (i != 0) stringBuilder.append(mapController.riverFinder(map.getTileBoard()[2 * i][map.getWidth() - 1], 2) + "\n");
        else stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String leftResourceAndTerrain(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        //resource and terrain in left tiles and Coordinates of right tiles
        for (int j = 0; j < map.getWidth(); j++) {
            if (i != map.getHeight() / 2 - 1)
                stringBuilder.append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 4)
                        + mapController.getColorOfTile(map.getTileBoard()[2 * i][j]) + "   "
                        + mapController.tileResource(map.getTileBoard()[2 * i][j], false)
                        + "   " + mapController.tileFeature(map.getTileBoard()[2 * i][j], false)
                        + "    " + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i][j], 2)
                        + mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j])
                        + rightCoordination(i , j) + Colors.RESET);
            else stringBuilder.append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 4)
                    + mapController.getColorOfTile(map.getTileBoard()[2 * i][j])
                    + "   " + mapController.tileResource(map.getTileBoard()[2 * i][j], false)
                    + "   " + mapController.tileFeature(map.getTileBoard()[2 * i][0], false) + "    "
                    + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i][j], 2) + "           ");
        }
        if (i != map.getHeight() / 2 - 1) stringBuilder.append(mapController.riverFinder(map.getTileBoard()[2 * i + 1][map.getWidth() - 1], 1) + "\n");
        else stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String rightCoordination(int i, int j){
        if (2 * i + 1 < 10 && j < 10) return "  [0" + (2 * i + 1) + ",0" + j + "]  ";
        else if (2 * i + 1 > 9 && j < 10) return "  [" + (2 * i + 1) + ",0" + j + "]  ";
        else if (2 * i + 1 > 9) return "  [" + (2 * i + 1) + "," + j + "]  ";
        else return "  [0" + (2 * i + 1) + "," + j + "]  ";
    }

    private String rightOwnerName(int i) {
        String ANSI_COLOR;
        String ANSI_COLOR2;
        StringBuilder stringBuilder = new StringBuilder();
        //owner name and color of right tiles and left Improvement
        for (int j = 0; j < map.getWidth(); j++) {
            ANSI_COLOR = mapController.getColorOfTileOwner(map.getTileBoard()[2 * i][j]);
            ANSI_COLOR2 = mapController.getColorOfTileOwner(map.getTileBoard()[2 * i + 1][j]);;
            if (i != map.getHeight() / 2 - 1) {
                if (j != 0) stringBuilder.append(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j - 1]));
                stringBuilder.append(" " + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i][j], 4)
                        + ANSI_COLOR + mapController.getColorOfTile(map.getTileBoard()[2 * i][j])
                        + "     " + mapController.tileImprovement(map.getTileBoard()[2 * i][j])
                        + "     " + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i][j], 2)
                        + ANSI_COLOR2 + mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j])
                        + "     " + mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j])
                        + mapController.tileOwner(map.getTileBoard()[2 * i + 1][j])
                        + mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j]) + "    " + Colors.RESET);
            } else stringBuilder.append(" " + mapController.riverFinder(map.getTileBoard()[2 * i][j], 4)
                    + ANSI_COLOR + mapController.getColorOfTile(map.getTileBoard()[2 * i][j])
                    + "     " + mapController.tileImprovement(map.getTileBoard()[2 * i][j])
                    + "     " + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i][j], 2)
                    + "            ");
        }
        if (i != map.getHeight() / 2 - 1) stringBuilder.append(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][map.getWidth() - 1])
                + " " + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i + 1][map.getWidth() - 1], 1) + "\n");
        else stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String leftBottomSides(int i) {
        String ANSI_COLOR = Colors.WHITE;
        StringBuilder stringBuilder = new StringBuilder();
        //bottom sides of left tiles and units on the right tiles
        for (int j = 0; j < map.getWidth(); j++) {
            if (i != map.getHeight() / 2 - 1) ANSI_COLOR = mapController.getColorOfTileOwner(map.getTileBoard()[2 * i + 1][j]);

            if (i != map.getHeight() / 2 - 1) {
                if (j != 0) stringBuilder.append(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j - 1]));
                stringBuilder.append("  " + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i][j], 4)
                        + mapController.riverFinder(map.getTileBoard()[2 * i][j], 3)
                        + mapController.riverFinder(map.getTileBoard()[2 * i][j], 2)
                        + mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j])
                        + "    " + ANSI_COLOR + mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j])
                        + mapController.civilianUnit(map.getTileBoard()[2 * i + 1][0])
                        + mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j])
                        + "   " + ANSI_COLOR + mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j])
                        + mapController.militaryUnit(map.getTileBoard()[2 * i + 1][j]) + "  " + Colors.RESET);
            } else
                stringBuilder.append("  " + mapController.riverFinder(map.getTileBoard()[2 * i][j], 4)
                        + mapController.riverFinder(map.getTileBoard()[2 * i][j], 3)
                        + mapController.riverFinder(map.getTileBoard()[2 * i][j], 2) + "             ");
        }
        if (i != map.getHeight() / 2 - 1)
            stringBuilder.append(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][map.getWidth() - 1])
                    + "  " + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i + 1][map.getWidth() - 1], 1) + "\n");
        else stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
