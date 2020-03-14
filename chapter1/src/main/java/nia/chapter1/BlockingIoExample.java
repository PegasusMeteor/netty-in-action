package nia.chapter1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by kerr.
 *
 * <p>Listing 1.1 Blocking I/O example
 */
public class BlockingIoExample {

  /** Listing 1.1 Blocking I/O example */
  public void serve(int portNumber) throws IOException {
    //        建立连接
    ServerSocket serverSocket = new ServerSocket(portNumber);
    Socket clientSocket = serverSocket.accept();
    //        阻塞式读取数据
    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
    String request, response;
    //        返回 response
    while ((request = in.readLine()) != null) {
      if ("Done".equals(request)) {
        break;
      }
      response = processRequest(request);
      out.println(response);
    }
  }

  private String processRequest(String request) {
    return "Processed";
  }
}
