package audiocast.audio;

import java.io.IOException;
import java.net.* ;
import java.util.concurrent.BlockingQueue;

import android.util.Log;


public class Client extends Thread
{
	private static MulticastSocket socket = null;
	private static InetAddress address;
    final static int MAXLEN = 1024;
    public static boolean ClientBool = false;
    final BlockingQueue<byte[]> queue;
    InetAddress host;
    
    public Client(BlockingQueue<byte[]> queue){
    	
    	this.queue=queue;
    	
    	try{
    		address = InetAddress.getByName("224.0.0.1");
    		socket = new MulticastSocket(6666);
    		socket.joinGroup(address);
    	}catch(Exception e){
    		Log.e("Audiocast", "error");
    	}
    	Log.d("Audiocast", "server created");
    	
    }

	public void run()
    {
    	try{
    		byte[] data = new byte[MAXLEN];
    		DatagramPacket pkt;
    		
    		while(!Thread.interrupted()){
    			pkt = new DatagramPacket(data, MAXLEN);
    			try{
    				if(ClientBool) socket.receive(pkt);
					data = pkt.getData();
					queue.put(data);
    			}catch(IOException e){
    				e.printStackTrace();
    			}
    		}
    	}catch(InterruptedException e){
    		
    	}finally{
    		try {
				socket.leaveGroup(address);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	} 

    }
}