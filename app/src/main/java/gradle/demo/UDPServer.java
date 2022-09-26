package gradle.demo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import com.google.gson.Gson;

class TagData {
	public double t0= 0.0;
	public double t1= 0.0;
	public double t2= 0.0;
	TagData() {
		// no-args constructor
	}
}


public class UDPServer {
	private DatagramSocket udpSocket;
	private int port;

	public UDPServer(int port) throws SocketException, IOException {
		this.port = port;
		this.udpSocket = new DatagramSocket(this.port);
	}
	public void listen() throws Exception {
		System.out.println("-- Running Server at " + InetAddress.getLocalHost() + "--");
		String msg;

		while (true) {
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			// blocks until a packet is received
			udpSocket.receive(packet);
			msg = new String(packet.getData()).trim();
			System.out.println( "Message from " + packet.getAddress().getHostAddress() + ": " + msg);

			// Decode Json
			//String json = "{`value1`:1,`value2`:`abc`,`value3`:4.2}".replace('`','"'); //gson.toJson(obj); //System.out.println(json);
			Gson gson = new Gson();
			TagData obj = gson.fromJson(msg, TagData.class);
			System.out.printf("%.2f, %.2f, %.2f\n",obj.t0, obj.t1, obj.t2);
		}
	}

	/*
	public static void main(String[] args) throws Exception {
		UDPServer client = new UDPServer(Integer.parseInt(args[0]));
		client.listen();
	}
	*/
}
