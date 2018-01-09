package net.marudina.akka.ch03.p5_sideeffecting;

import akka.actor.AbstractLoggingActor;
import akka.japi.pf.ReceiveBuilder;

public class Greeter extends AbstractLoggingActor {

	public static class Greeting {
		private final String msg;
		
		public Greeting(String msg) {
			this.msg = msg;
		}
		
		public String getMsg() {
			return msg;
		}
	}
	

	@Override
	public Receive createReceive() {
		return ReceiveBuilder.create()
				.match(Greeting.class, m -> {
					log().info("Hello {}!", m.getMsg());
				})
				.build();
	}

}
