package aps;

public class ClassifyFeed extends Thread{

		public ClassifyFeed () {
			this.start() ;
		}
		
		
		public void run () {   
			
			Facebook facebook = new Facebook();
			System.out.println("ClassifyFeed active");
			
			try {
				
				facebook.classifyTweets();
				// Pause du thread pendant 5 heures
				System.out.println("ClassifyTweets sleeping");
				ClassifyTweets.sleep(18000000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
			this.run();
		}
	}