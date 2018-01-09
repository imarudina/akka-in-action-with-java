package net.marudina.akka.ch04;

import java.util.Optional;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

public class ChildActor extends AbstractActor {

	@Override
	public void postStop() throws Exception {
		System.out.println("ChildActor postStop()");
		super.postStop();
	}

	@Override
	public void preStart() throws Exception {
		System.out.println("ChildActor preStart()");
		super.preStart();
	}

	@Override
	public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
		System.out.println("ChildActor preRestart()");
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
					System.out.println("ChildActor String received: " + m);
				})
				.build();
	}

}
