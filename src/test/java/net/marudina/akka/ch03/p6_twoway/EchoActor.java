package net.marudina.akka.ch03.p6_twoway;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

public class EchoActor extends AbstractActor {
	ActorRef target = null;

	@Override
	public Receive createReceive() {
		return ReceiveBuilder.create().match(Object.class, msg -> {
			sender().tell(msg, self());
			if (target != null)
				target.forward(msg, getContext());
		}).match(ActorRef.class, actorRef -> {
			target = actorRef;
			//getSender().tell("done", getSelf());
		}).build();
	}
}
