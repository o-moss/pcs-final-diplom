import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static final int PORT = 8989;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        try (Socket clientSocket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
            out.println("бизнес");
            Gson gson = new Gson();
            String json = in.readLine();
            List<PageEntry> answerFromServer = gson.fromJson(json, List.class);
            System.out.println(answerFromServer);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}
