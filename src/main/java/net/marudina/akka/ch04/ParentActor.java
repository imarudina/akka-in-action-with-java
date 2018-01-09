package net.marudina.akka.ch04;

import java.util.Optional;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class ParentActor extends AbstractActor {
	
	private ActorRef child;
	
	public ParentActor() {
		child = context().actorOf(Props.create(ChildActor.class));
	}

	@Override
	public void postStop() throws Exception {
		System.out.println("ParentActor postStop()");
		super.postStop();
	}

	@Override
	public void preStart() throws Exception {
		System.out.println("ParentActor preStart()");
		super.preStart();
	}

	@Override
	public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
		System.out.println("ParentActor preRestart()");
		super.preRestart(reason, message);
	}

	@Override
	public void postRestart(Throwable reason) throws Exception {
		super.postRestart(reason);
	}

	@Override
	public Receive createReceive() {
		return ReceiveBuilder.create()
				.match(String.class, m -> {
					System.out.println("ParentActor String received: " + m);
				})
				.build();
	}

}
