package net.marudina.akka.ch03.p5_sideeffecting;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging.Info;
import akka.testkit.CallingThreadDispatcher;
import akka.testkit.javadsl.EventFilter;
import akka.testkit.javadsl.TestKit;

public class SideEffectingActorTest {

	static ActorSystem system;

	@BeforeClass
	public static void setup() {
		Config config = ConfigFactory.parseString("akka.loggers = [akka.testkit.TestEventListener]");
		system = ActorSystem.create("testsystem", config);
		// system = ActorSystem.create(); - doesn't work without the TestEventListener setup
	}

	@AfterClass
	public static void teardown() {
		TestKit.shutdownActorSystem(system);
		system = null;
	}

	@Test
	public void testLogging() {
		new TestKit(system) {
			{
				String dispatcherId = CallingThreadDispatcher.Id();
				Props props = Props.create(Greeter.class).withDispatcher(dispatcherId);
				final ActorRef greeter = system.actorOf(props, "greeter");
				
				new EventFilter(Info.class, system)
						.occurrences(1)
						.message("Hello World!")
						.intercept(() -> { 
							greeter.tell(new Greeter.Greeting("World"), ActorRef.noSender());
							return 0;
						});
			}
		};
	}

}
