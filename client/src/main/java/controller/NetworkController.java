package controller;

import model.Request;
import model.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkController {
    private static NetworkController networkController;

    private static Socket socket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    public static NetworkController getInstance() {
        if (networkController == null)
            networkController = new NetworkController();
        return networkController;
    }

    public void initialize() {
        try {
            socket = new Socket("localhost", 3333);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response sendRequest(Request request) {
        try {
            String output = request.toJson();
            dataOutputStream.writeUTF(output);
            dataOutputStream.flush();
            String input = dataInputStream.readUTF();
            Response response = new Response().fromJson(input);
            if (request.getMenu().equals("play game") && request.getAction().equals("show map") && response.getStatusCode().equals("fuck you")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(response.getMessage());
                for (int i = 0; i < 99; i++) {
                    input = dataInputStream.readUTF();
                    response = new Response().fromJson(input);
                    stringBuilder.append(response.getMessage());
                }
                System.out.println(stringBuilder);
                response = new Response();
                response.setMessage("fuck");
                return response;
            }
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
