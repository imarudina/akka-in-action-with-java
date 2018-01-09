package net.marudina.akka.ch03.p2_sending;

import java.util.ArrayList;
import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

public class SilentActor extends AbstractActor {
	
	private List<String> msgs = new ArrayList<>();

	public static class SilentMessage {
		private final String msg;
		
		public SilentMessage(String msg) {
			this.msg = msg;
		}
		
		public String getMessage() {
			return msg;
		}
	}
	
	public static class GetState {
		private final ActorRef receiver;
		
		public GetState(ActorRef receiver) {
			this.receiver = receiver;
		}
		
		public ActorRef getReceiver() {
			return this.receiver;
		}
	}
	
	@Override
	public Receive createReceive() {
		return ReceiveBuilder.create()
				.match(SilentMessage.class, m -> {
					msgs.add(m.getMessage());
				})
				.match(GetState.class, m -> {
					m.getReceiver().tell(getState(), getSelf());
				})
				.build();
	}

	public List<String> getState() {
		return new ArrayList<>(msgs);
	}

}
