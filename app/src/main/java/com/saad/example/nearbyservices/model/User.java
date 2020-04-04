package com.saad.example.nearbyservices.model;

/**
 * Created by Team Mashwara on 12/29/2019.
 */

    public class User  {
        public String id;
        public String username;
        public String email;
        public String pass;
        public User(String Email, String Pass)
        {
            this.email=Email;
            this.pass=Pass;
        }
        public User(String uid, String email,String username)
        {
            this.id= uid;
            this.email=email;
            this.username= username;
            this.pass=pass;
        }
}
