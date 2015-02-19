package aps;

import java.sql.SQLException;

import twitter4j.TwitterException;

public class ClassifyTweets extends Thread{

	public ClassifyTweets () {
		this.start() ;
	}
	
	
	public void run () {   
		// Traiter les tweets ici
		
		Twitter twitter = new Twitter();
		try {
			
			twitter.classifyTweets();	
			
		} catch (TwitterException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}