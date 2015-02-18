package aps;

import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Friend;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

public class Facebook {

		// Tokens for identification
		private String app_id = "1597055770513413";
		private String app_secret = "d9e1589864612fac8190f2024710dbad";			
		private facebook4j.Facebook facebook;
		// Get an access token from: 
		// https://developers.facebook.com/tools/explorer
		// Copy and paste it below.
		private String accessTokenString = "CAACEdEose0cBALLQMrGjk44OYPnx6TdZAo2MqWLn0YUGlmK8N54f0ZBDOfZCb0WIytomNZBZAQXZCPMc2qrtV8dk2ZCE0MvlIAsLMUCE8ZAb6xQQwePlPsHML0OowPZC5g37ZCxd1DmwqJ5qXjiiRhiwniq67VZBTjcgZBxU7QfvOaQ8NZByihTzKDSCbTSYu6YA7j4BA9texv0FZAFn5ngTDCRNq4MgbblkEyFsAZD";
		
		public Facebook () {
			// Generate facebook instance.
			facebook = new FacebookFactory().getInstance();
			facebook.setOAuthAppId(app_id, app_secret);
			// Set access token.
			AccessToken token = new AccessToken(accessTokenString);
			facebook.setOAuthAccessToken(token);
		}
		
			   
		public void getFriends () throws FacebookException {
			    ResponseList<Friend> liste_Friends = facebook.getFriends();
			    for (int i=0;i<liste_Friends.size();i++){
					Friend friend = liste_Friends.get(i);
					System.out.println("id = " + friend.getId());
					System.out.println("name = " + friend.getName());
					System.out.println("last name = " + friend.getLastName());
					System.out.println("gender = " + friend.getGender());
					System.out.println("birthday = " + friend.getBirthday());
					System.out.println("username = " + friend.getUsername());
					System.out.println("email = " + friend.getEmail()+"\n");
				}
		}
			    
		public void getPosts () throws FacebookException {
				ResponseList<Post> liste_Posts = facebook.getPosts();
				for (int i=0;i<liste_Posts.size();i++){
					Post post = liste_Posts.get(i);
					System.out.println("id = " + post.getId());
					System.out.println("type = " + post.getType());
					System.out.println("description = " + post.getDescription());
					System.out.println("picture = " + post.getFullPicture());
					System.out.println("link = " + post.getLink());
					System.out.println("source = " + post.getSource());
					System.out.println("nbr de likes = " + post.getLikes().size());
					System.out.println("message = " + post.getMessage()+"\n");
				}
		}
}
