package net.marudina.akka.ch04;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class ActorLifecycle {

	public static void main(String[] args) throws InterruptedException {
		ActorSystem system = ActorSystem.create();
		ActorRef actor = system.actorOf(Props.create(ParentActor.class));

		actor.tell("Hello", ActorRef.noSender());

		Thread.sleep(2000);

		System.out.println("Stopping Akka System");
		system.terminate();

		Thread.sleep(2000);
		
		System.out.println("Sending again message to actor");
		actor.tell("Hello again", ActorRef.noSender());
		
		Thread.sleep(2000);
	}

}
