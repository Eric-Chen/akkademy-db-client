package com.akkademy;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import scala.concurrent.duration.Duration;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;
import static scala.compat.java8.FutureConverters.toJava;

/**
 * Created by eric on 31/07/2017.
 */
public class JClientActor extends AbstractActor {

    protected final LoggingAdapter log = Logging.getLogger(context().system(), this);

    private String path;
    private ActorRef remoteActor;
    private JClient parent;

    public JClientActor(String path, JClient parent){
        this.path = path;
        this.parent = parent;
        sendIdentifyRequest();
    }


    private void sendIdentifyRequest() {
        getContext().actorSelection(path).tell(new Identify(path), self());
        getContext()
                .system()
                .scheduler()
                .scheduleOnce(Duration.create(3, SECONDS), self(),
                        ReceiveTimeout.getInstance(), getContext().dispatcher(), self());
    }

    Receive active = receiveBuilder().match(ValueObj.class, msg -> {
        log.info(">>>>>>>>>>>trigger message: {} -> {}", msg.getKey(), msg.getValue());
        Patterns.ask(remoteActor, new SetRequest(msg.getKey(), msg.getValue()), 2000);
    }).match(GetValue.class, msg -> {
        log.info("<<<<<<<<<<<trigger get message: {} ", msg.getKey());
        Object result = ((CompletableFuture) toJava(Patterns.ask(remoteActor, new GetRequest(msg.getKey()), 2000))).get();
        log.info(">>>>>>>>>>>>>result: {}", result);
        sender().tell(result, self());
    }).build();

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(ActorIdentity.class, identity -> {
                    remoteActor = identity.getActorRef().get();
                    if (remoteActor == null) {
                        System.out.println("Remote actor not available: " + path);
                    } else {
                        log.info("start watching.....");
                        getContext().watch(remoteActor);
                        getContext().become(active, true);
                        parent.started.set(true);
                    }
                })
                .match(ReceiveTimeout.class, x -> {
                    log.info("got receiveTimeout....");
                    sendIdentifyRequest();
                })
                .matchAny(x -> {
                    log.info("==============Not initialized....................."+x);
                })
                .build();

    }


}
