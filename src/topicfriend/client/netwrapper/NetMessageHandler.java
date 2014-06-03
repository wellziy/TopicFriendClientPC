package topicfriend.client.netwrapper;

import topicfriend.netmessage.NetMessage;

public interface NetMessageHandler
{
	//remember that the callback is in another thread
	public void handleMessage(int connection,NetMessage msg);
}
