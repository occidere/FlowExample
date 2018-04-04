package reactive;

import common.Lyrics;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class FlowExample {
    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        SubmissionPublisher<String> publisher = new SubmissionPublisher<>(executor, Flow.defaultBufferSize());
        MySubscriber<String> subscriber = new MySubscriber<>();

        publisher.subscribe(subscriber);

        Arrays.stream(Lyrics.getLyrics())
                .forEach(publisher::submit);

        publisher.close();
        executor.shutdown();
    }
}
