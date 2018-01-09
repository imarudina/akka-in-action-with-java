package net.marudina.akka.ch03.p3_sending;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class SendingActor extends AbstractActor {

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
	
	public static class Event {
		private final long id;
		
		public Event(long id) {
			this.id = id;
		}
		
		public long getId() {
			return this.id;
		}
	}
	
	public static class SortEvents {
		private final List<Event> events;
		
		public SortEvents(List<Event> unsorted) {
			this.events = unsorted;
		}
		
		public List<Event> getEvents() {
			return this.events;
		}
	}
	
	public static class SortedEvents {
		private final List<Event> events;
		
		public SortedEvents(List<Event> sorted) {
			this.events = sorted;
		}
		
		public List<Event> getEvents() {
			return this.events;
		}
	}
	
	private List<String> msgs = new ArrayList<>();
	
	private ActorRef receiver;
	
	public static Props getProps(ActorRef receiver) {
		return Props.create(SendingActor.class, receiver);
	}
	
	private SendingActor(ActorRef receiver) {
		this.receiver = receiver;
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
				.match(SortEvents.class, m -> {
					List<Event> unsorted = m.getEvents();
					List<Event> sorted = new ArrayList<>(unsorted);
					sorted.sort(new Comparator<SendingActor.Event>() {
						@Override
						public int compare(SendingActor.Event e1, SendingActor.Event e2) {
							return (int) e1.getId() - (int) e2.getId();
						}
					});
					
					receiver.tell(new SortedEvents(sorted), getSelf());
				})
				.build();
	}

	public List<String> getState() {
		return new ArrayList<>(msgs);
	}

}
