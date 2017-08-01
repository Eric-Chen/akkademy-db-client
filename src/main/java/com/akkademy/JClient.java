package com.akkademy;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static scala.compat.java8.FutureConverters.toJava;

public class JClient {

    public static final AtomicBoolean started = new AtomicBoolean(false);

    private final ActorSystem system = ActorSystem.create("LocalSystem");
    private final ActorRef localActor;
       public JClient(String remoteAddress) {
           String path = "akka.tcp://akkademy@"+ remoteAddress +"/user/akkademy-db";
           localActor = system.actorOf(Props.create(JClientActor.class, path, this), "localActor");
           int i = 0, retryCount = 10;
           while(i < retryCount){
               if(!this.started.get()){
                   try {
                       TimeUnit.SECONDS.sleep(1);
                       i++;
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
               break;
           }
       }

       public CompletionStage set(String key, Object value) {
           return toJava(Patterns.ask(localActor, new ValueObj(key, value), 200));

       }
       public CompletionStage<Object> get(String key){
           return toJava(Patterns.ask(localActor, new GetValue(key), 200));
       }
}
