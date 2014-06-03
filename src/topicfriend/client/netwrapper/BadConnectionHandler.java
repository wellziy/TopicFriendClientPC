package topicfriend.client.netwrapper;

public interface BadConnectionHandler 
{
	//remember that the callback is in another thread
	//NOTICE: a bad connection will be detected only when reading or writing to it
	public void handleBadConnection(int connection);
}
