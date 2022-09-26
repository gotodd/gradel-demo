package gradle.demo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import com.google.gson.Gson;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.NetworkTableInstance;

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
		String msg;
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);

		System.out.printf("-- Running Server at " + InetAddress.getLocalHost() + ":%d\n",this.port);

		NetworkTableInstance inst = NetworkTableInstance.getDefault();
		NetworkTable table = inst.getTable("datatable");
		inst.startClient("127.0.0.1");  // where TEAM=190, 294, etc, or use inst.startClient("hostname") or similar
		NetworkTableEntry zEntry = table.getEntry("z");
		zEntry.setValue(NetworkTableValue.makeDouble(1.2));

		while (true) {

			// blocks until a packet is received
			udpSocket.receive(packet);
			msg = new String(packet.getData()).trim();
			System.out.println( "Message from " + packet.getAddress().getHostAddress() + ": " + msg);

			// Decode Json
			//String json = "{`value1`:1,`value2`:`abc`,`value3`:4.2}".replace('`','"'); //gson.toJson(obj); //System.out.println(json);
			Gson gson = new Gson();
			TagData obj = gson.fromJson(msg, TagData.class);
			System.out.printf("%.2f, %.2f, %.2f\n",obj.t0, obj.t1, obj.t2);

			//write data to network table
			table.getEntry("t0").setValue(NetworkTableValue.makeDouble(obj.t0));
			table.getEntry("t1").setValue(NetworkTableValue.makeDouble(obj.t1));
			table.getEntry("t2").setValue(NetworkTableValue.makeDouble(obj.t2));
			System.out.println("Z: " + zEntry.getDouble(0.0));
		}
	}
}
