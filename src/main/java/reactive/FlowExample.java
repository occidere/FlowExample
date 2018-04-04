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
        MyProcessor<String, String[]> splitProcessor = new MyProcessor<>("SplitProcessor", s-> s.split(" "));
        MyProcessor<String[], Integer> countProcessor = new MyProcessor<>("CountProcessor", arr-> arr.length);
        MySubscriber<Integer> subscriber = new MySubscriber<>("MySubscriber");

        publisher.subscribe(splitProcessor);
        splitProcessor.subscribe(countProcessor);
        countProcessor.subscribe(subscriber);

        Arrays.stream(Lyrics.getLyrics())
                .forEach(publisher::submit);

        publisher.close();
        executor.shutdown();
    }
}
