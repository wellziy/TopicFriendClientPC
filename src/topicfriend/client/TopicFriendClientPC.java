package topicfriend.client;

import java.io.IOException;
import java.util.Scanner;

import topicfriend.client.netwrapper.BadConnectionHandler;
import topicfriend.client.netwrapper.NetMessageHandler;
import topicfriend.client.netwrapper.NetMessageReceiver;
import topicfriend.netmessage.NetMessage;
import topicfriend.netmessage.NetMessageChatFriend;
import topicfriend.netmessage.NetMessageID;
import topicfriend.netmessage.NetMessageLogin;
import topicfriend.network.Network;

public class TopicFriendClientPC
{
	public static void main(String[] args)
	{
		Network.initNetwork(1, 1, 10);
		int connection=Network.NULL_CONNECTION;
		try
		{
			connection=Network.connectHostPort("localhost", 55555, 2000);
			
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
				
				NetMessageReceiver.getInstance().setMessageHandler(NetMessageID.ERROR, new NetMessageHandler() {
					@Override
					public void handleMessage(int connection, NetMessage msg)
					{
						System.out.println("receive ERROR: "+msg.toJsonString());
					}
				});
				
				NetMessageReceiver.getInstance().setMessageHandler(NetMessageID.LOGIN_SUCCEED, new NetMessageHandler()
				{
					@Override
					public void handleMessage(int connection, NetMessage msg)
					{
						System.out.println("receive LOGIN_SUCCEED: "+msg.toString());
					}
				});
				
				NetMessageReceiver.getInstance().setMessageHandler(NetMessageID.CHAT_FRIEND, new NetMessageHandler()
				{
					@Override
					public void handleMessage(int connection, NetMessage msg)
					{
						System.out.println("receive CHAT_FRIEND: "+msg.toJsonString());
					}
				});
				
//				NetMessageLogin login=new NetMessageLogin("hello", "world");
//				Network.sendDataOne(login.toByteArrayBuffer(), connection);
//				
//				NetMessageRegister msgRegister=new NetMessageRegister("hhhhhhhhhhhhh", "hhhhhhhhhhhhh", UserInfo.SEX_MALE);
//				Network.sendDataOne(msgRegister.toByteArrayBuffer(), connection);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.out.println("failed to connect host");
		}
		
		Scanner sc=new Scanner(System.in);
		while(connection!=Network.NULL_CONNECTION&&!Thread.interrupted())
		{
			int msgCode=sc.nextInt();
			if(msgCode==NetMessageID.CHAT_FRIEND)
			{
				System.out.println("fid,content");
				int fid=sc.nextInt();
				String content=sc.nextLine();
				NetMessageChatFriend msgChatFriend=new NetMessageChatFriend(fid, content);
				Network.sendDataOne(msgChatFriend.toByteArrayBuffer(), connection);
			}
			
			if(msgCode==NetMessageID.LOGIN)
			{
				System.out.println("name,password");
				String name=sc.nextLine();
				String password=sc.nextLine();
				NetMessageLogin msgLogin=new NetMessageLogin(name,password);
				Network.sendDataOne(msgLogin.toByteArrayBuffer(), connection);
			}
		}
		
		System.out.println("after the thread was interrupted");
		NetMessageReceiver.getInstance().purgeInstance();
		Network.destroyNetwork();
	}
}
