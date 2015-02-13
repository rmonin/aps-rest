package aps;

import twitter4j.TwitterException;

public class CollectTweets extends Thread {

	public CollectTweets () {
		this.start() ;
	}
	
	
	public void run () {   
		Twitter twitter = new Twitter();
		try {
			twitter.getTimeline();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			twitter.getFriendsAndFollowers();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
