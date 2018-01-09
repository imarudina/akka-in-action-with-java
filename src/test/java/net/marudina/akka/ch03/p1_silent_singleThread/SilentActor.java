package net.marudina.akka.ch03.p1_silent_singleThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import akka.actor.AbstractActor;
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
	
	@Override
	public Receive createReceive() {
		return ReceiveBuilder.create()
				.match(SilentMessage.class, m -> {
					msgs.add(m.getMessage());
				})
				.build();
	}

	public List<String> getState() {
		return Collections.unmodifiableList(msgs);
	}

}
