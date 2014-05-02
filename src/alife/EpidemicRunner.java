package alife;

import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.Location;

public class EpidemicRunner {
	
	public static void main(String[] args)
    {
		ActorWorld world = new ActorWorld();
	    Human alice = new Human();
	    Human bob = new Human();
	    Doctor reggia = new Doctor();
	    world.add(new Location(7, 8), alice);
	    world.add(new Location(5, 5), bob);
	    world.add(new Location(4, 5), reggia);
	    world.show();
    }
    
}
