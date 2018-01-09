package net.marudina.akka.ch03.p1_silent_singleThread;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;

public class SilentActorSingleThreadTest {

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
	public void testInternalState() {
		final Props props = Props.create(SilentActor.class);
		final TestActorRef<SilentActor> ref = TestActorRef.create(system, props, "testA");
		ref.tell(new SilentActor.SilentMessage("whisper"), ActorRef.noSender());
		
		List<String> state = ref.underlyingActor().getState();
		
		assertTrue(state.contains("whisper"));
	}

}
