package com.login;

public class User {
	private long id;
	private String login;
	private String password;
    
	public User (long id, String login, String password) {
		    super();
		    this.id = id;
		    this.login = login;
		    this.password = password;
		  }
   public User ( String login , String password){
	   super();
	   this.login = login;
	   this.password = password;
   }
		  public long getId() {
		    return id;
		  }

		  public void setId(long id) {
		    this.id = id;
		  }

		  public String getLogin() {
		    return this.login;
		  }

		  public void setIntitule(String login) {
		    this.login = login;
		  }

		  public String getPassword() {
		    return this.password;
		  }

		  public void setPassword(String password) {
		    this.password = password;
		  }

}
