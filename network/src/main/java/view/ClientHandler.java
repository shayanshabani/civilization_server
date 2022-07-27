package view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.*;
import model.Request;
import model.Response;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClientHandler extends Thread{
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket;
    private Receiver receiver;
    private String username;
    private User user;

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
        while (true) {
            try {
                if (socket.isClosed())
                    return;
                String input = dataInputStream.readUTF();
                Request request = Request.fromJson(input);
                if (playing) {
                    Response response = process(request);
                    String output = response.toJson();
                    dataOutputStream.writeUTF(output);
                    dataOutputStream.flush();
                }
                else {
                    Response response = new Response();
                    response.setMessage("please wait...");
                    dataOutputStream.writeUTF(response.toJson());
                    dataOutputStream.flush();
                }
            } catch (IOException e) {
                System.out.println("client disconnected");
                receiver.kickClient(this);
                try {
                    dataInputStream.close();
                    socket.close();
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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
                            GameController.getInstance().nextPlayer(receiver.getPlayers().get(0));
                            for (ClientHandler clientHandler : receiver.getClientHandlers()) {
                                if (!clientHandler.getUser().equals(receiver.getPlayers().get(0)))
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
                    case "nearby tiles information":
                        return CityController.getInstance().nearbyTilesInformation(request);
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
}
