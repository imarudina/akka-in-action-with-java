package net.marudina.akka.ch03.p2_sending;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;

public class SilentActorMultiThreadTest {

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
	public void testUsingProbe() {
		new TestKit(system) {
			{
				final Props props = Props.create(SilentActor.class);
				final ActorRef subject = system.actorOf(props);

				// can also use JavaTestKit “from the outside”
				final TestKit probe = new TestKit(system);

				subject.tell(new SilentActor.SilentMessage("whisper1"), ActorRef.noSender());
				subject.tell(new SilentActor.SilentMessage("whisper2"), ActorRef.noSender());

//				subject.tell(new SilentActor.GetState(probe.getTestActor()), ActorRef.noSender());
 				subject.tell(new SilentActor.GetState(probe.getRef()), ActorRef.noSender());

				List<String> expected = new ArrayList<>();
				expected.add("whisper1");
				expected.add("whisper2");
				probe.expectMsg(expected);
			}
		};
	}

	@Test
	public void testUsingTestActor() {
		new TestKit(system) {
			{
				final Props props = Props.create(SilentActor.class);
				final ActorRef subject = system.actorOf(props);

				subject.tell(new SilentActor.SilentMessage("whisper1"), ActorRef.noSender());
				subject.tell(new SilentActor.SilentMessage("whisper2"), ActorRef.noSender());

				subject.tell(new SilentActor.GetState(getRef()), ActorRef.noSender());

				List<String> expected = new ArrayList<>();
				expected.add("whisper1");
				expected.add("whisper2");
				expectMsg(expected);
			}
		};
	}

	@Test
	public void testExpectMessageClass() {
		new TestKit(system) {
			{
				final Props props = Props.create(SilentActor.class);
				final ActorRef subject = system.actorOf(props);

				subject.tell(new SilentActor.SilentMessage("whisper1"), ActorRef.noSender());
				subject.tell(new SilentActor.SilentMessage("whisper2"), ActorRef.noSender());

				subject.tell(new SilentActor.GetState(getRef()), ActorRef.noSender());

				List<String> expected = new ArrayList<>();
				expected.add("whisper1");
				expected.add("whisper2");
				expectMsgClass(ArrayList.class);
			}
		};
	}

}
