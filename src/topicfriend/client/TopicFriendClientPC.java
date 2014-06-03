package topicfriend.client;

import java.io.IOException;

import org.apache.http.util.ByteArrayBuffer;

import topicfriend.client.netwrapper.BadConnectionHandler;
import topicfriend.client.netwrapper.NetMessageHandler;
import topicfriend.client.netwrapper.NetMessageReceiver;
import topicfriend.netmessage.NetMessage;
import topicfriend.netmessage.NetMessageID;
import topicfriend.netmessage.NetMessageLogin;
import topicfriend.network.Network;

public class TopicFriendClientPC
{
	public static void main(String[] args)
	{
		Network.initNetwork(1, 1, 10);
		try
		{
			int connection=Network.connectHostPort("localhost", 55555, 2000);
			
			if(connection!=Network.NULL_CONNECTION)
			{
				NetMessageReceiver.getInstance().setBadConnectionHandler(new BadConnectionHandler()
				{
					@Override
					public void handleBadConnection(int connection)
					{
						System.out.println("client pc disconnected");
					}
				});
				
				NetMessageReceiver.getInstance().setMessageHandler(NetMessageID.LOGIN, new NetMessageHandler()
				{
					@Override
					public void handleMessage(int connection, NetMessage msg)
					{
						System.out.println("received message: "+msg.toString());
					}
				});
				
				NetMessageLogin login=new NetMessageLogin("hello", "world");
				Network.sendDataOne(login.toByteArrayBuffer(), connection);
				
				ByteArrayBuffer badBuffer=new ByteArrayBuffer(10);
				byte[] byteArr=new String("good").getBytes();
				badBuffer.append(byteArr, 0, byteArr.length);
				Network.sendDataOne(badBuffer, connection);
			}

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.out.println("failed to connection host");
		}
		
		try
		{
			Thread.sleep(10000);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		NetMessageReceiver.getInstance().purgeInstance();
		Network.destroyNetwork();
	}
}
