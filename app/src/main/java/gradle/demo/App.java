/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package gradle.demo;

import java.net.SocketException;

public class App {
    public static void main(String[] args) {

		//start the network table server
        (new Thread(new MyRunnable())).start();

		//start UDP socket listener
		UDPServer client;
		try{
			client = new UDPServer(4001);
			client.listen();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}
}
