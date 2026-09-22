package fr.ul.acl.start;

import fr.ul.acl.model.Greeter; 

public class Main { 
    public static void main(String[] args) { 
        Greeter greet = new Greeter(); 
        System.out.println(greet.greet("Couscous de la mer")); 
    } 
}