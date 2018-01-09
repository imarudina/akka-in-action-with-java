package net.marudina.akka.ch03.p3_sending;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import net.marudina.akka.ch03.p3_sending.SendingActor.SortedEvents;

public class SendingActorTest {

	static ActorSystem system;

	@BeforeClass
	public static void setup() {
		system = ActorSystem.create();
	}

	@AfterClass
	public static void teardown() {
		TestKit.shutdownActorSystem(system);
		system = null;
	}

	@Test
	public void testSendingActor() {
		new TestKit(system) {
			{
				final Props props = SendingActor.getProps(getRef());
				final ActorRef sendingActor = system.actorOf(props);
				
				final int size = 1000;
				final int maxInclusive = 100000;
				
				Random random = new Random(System.currentTimeMillis());
				List<SendingActor.Event> unsorted = IntStream.range(0, size)
					.mapToObj((int i) -> {
						return new SendingActor.Event(random.nextInt(maxInclusive));
					})
					.collect(Collectors.toList());
				
				
				sendingActor.tell(new SendingActor.SortEvents(unsorted), ActorRef.noSender());
				
				expectMsgPF("SendingActor should have sent SortedEvent with the sorted collection to the TestActor", (e) -> {
					if (e instanceof SortedEvents) {
						List<SendingActor.Event> sorted = ((SortedEvents) e).getEvents();
						assertEquals(size, sorted.size());
						unsorted.sort(new Comparator<SendingActor.Event>() {
							@Override
							public int compare(SendingActor.Event e1, SendingActor.Event e2) {
								return (int) e1.getId() - (int) e2.getId();
							}
						});
						assertEquals(unsorted, sorted);
					}
					return null;
				});
			}
		};
	}

}
