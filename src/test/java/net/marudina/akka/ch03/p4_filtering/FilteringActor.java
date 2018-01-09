package net.marudina.akka.ch03.p4_filtering;

import java.util.LinkedList;
import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class FilteringActor extends AbstractActor {

	public static class Event {
		private final long id;

		public Event(long id) {
			this.id = id;
		}

		public long getId() {
			return this.id;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof Event))
				return false;

			Event e2 = (Event) obj;
			return this.id == e2.id;
		}

		@Override
		public int hashCode() {
			return (int) (id ^ (id >>> 32));
		}

	}

	private final ActorRef receiver;
	private final int bufferSize;
	//private Set<Integer> processed = new HashSet<>();
	private List<Integer> processed = new LinkedList<>();

	public static Props getProps(ActorRef receiver, int bufferSize) {
		return Props.create(FilteringActor.class, receiver, bufferSize);
	}

	private FilteringActor(ActorRef receiver, int bufferSize) {
		this.receiver = receiver;
		this.bufferSize = bufferSize;
	}

	@Override
	public Receive createReceive() {
		return ReceiveBuilder.create().match(Event.class, e -> {
			final int id = (int) e.getId();
			if (!processed.contains(id)) {
				processed.add(id);
				receiver.tell(e, getSender());
		        if (processed.size() > bufferSize) {
		            // discard the oldest
		            processed.remove(0);
		          }
			}
		}).build();
	}

}
