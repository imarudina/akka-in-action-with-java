package net.marudina.akka.ch03.p4_filtering;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.JavaPartialFunction;
import akka.testkit.javadsl.TestKit;
import net.marudina.akka.ch03.p4_filtering.FilteringActor.Event;
import scala.concurrent.duration.Duration;

public class FilteringActorTest {

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
	public void test1() {
		new TestKit(system) {
			{
				final Props props = FilteringActor.getProps(getRef(), 5);
				final ActorRef filter = system.actorOf(props, "filter-1");

				filter.tell(new Event(1), ActorRef.noSender());
				filter.tell(new Event(2), ActorRef.noSender());
				filter.tell(new Event(1), ActorRef.noSender());
				filter.tell(new Event(3), ActorRef.noSender());
				filter.tell(new Event(1), ActorRef.noSender());
				filter.tell(new Event(4), ActorRef.noSender());
				filter.tell(new Event(5), ActorRef.noSender());
				filter.tell(new Event(5), ActorRef.noSender());
				filter.tell(new Event(6), ActorRef.noSender());

				List<Integer> actual = receiveWhile(Duration.create("3 seconds"), (msg) -> {
					long id = ((Event) msg).getId();
					if (id <= 5)
						return (int) id;
					else
						throw JavaPartialFunction.noMatch();
				});

				List<Integer> expectedIds = Arrays.asList(1, 2, 3, 4, 5);
				assertEquals(expectedIds, actual);

				// For the comparison to work, Event must have equals()
				expectMsg(new Event(6));
			}
		};
	}

	@Test
	public void test2() {
		new TestKit(system) {
			{
				final Props props = FilteringActor.getProps(getRef(), 5);
				final ActorRef filter = system.actorOf(props, "filter-2");
				
				filter.tell(new Event(1), ActorRef.noSender());
				filter.tell(new Event(2), ActorRef.noSender());
				expectMsg(new Event(1));
				expectMsg(new Event(2));
				filter.tell(new Event(1), ActorRef.noSender());
				expectNoMsg();
				filter.tell(new Event(3), ActorRef.noSender());
				expectMsg(new Event(3));
				filter.tell(new Event(1), ActorRef.noSender());
				expectNoMsg();
				filter.tell(new Event(4), ActorRef.noSender());
				filter.tell(new Event(5), ActorRef.noSender());
				filter.tell(new Event(5), ActorRef.noSender());
				expectMsg(new Event(4));
				expectMsg(new Event(5));
				expectNoMsg();
			}
		};
	}

}
