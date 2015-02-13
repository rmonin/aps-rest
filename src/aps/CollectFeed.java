package aps;

import facebook4j.FacebookException;

public class CollectFeed extends Thread{

	public CollectFeed () {
		this.start() ;
	}
	
	
	public void run () {   
		Facebook facebook = new Facebook();
		try {
			facebook.getFriends();
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			facebook.getPosts();
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
