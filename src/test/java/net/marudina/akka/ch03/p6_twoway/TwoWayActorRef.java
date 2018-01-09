package net.marudina.akka.ch03.p6_twoway;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;

public class TwoWayActorRef {

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
	@Ignore
	// Difficult with Java. With Scala it is a simple Mixin Trait added to the test class - with ImplicitSender
	public void test1() {
		new TestKit(system) {
			{
				Props props = Props.create(EchoActor.class);
				final ActorRef echoActor = system.actorOf(props, "echoActor");

				// “inject” the probe by passing it to the test subject
				// like a real resource would be passed in production
				echoActor.tell(getRef(), getRef());

				echoActor.tell("Some message", ActorRef.noSender());

				expectMsg("Some Message");
			}
		};
	}

}
