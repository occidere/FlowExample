package reactive;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.Flow.Processor;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

public class MyProcessor<T, R> extends SubmissionPublisher<R> implements Processor<T, R> {
    private Function function;
    private Subscription subscription;
    private String name;

    public MyProcessor(String name, Function<? super T, ? extends R> function){
        super();
        this.name = name;
        this.function = function;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        System.out.printf("[%s, (%s)] onSubscribe\n",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()), name);
        subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        R changed = (R) function.apply(item);
        submit(changed);

        int req = Runtime.getRuntime().availableProcessors();
        System.out.printf("[%s, %d, (%s)] %s => %s\n",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()), req, name, item, changed);
        subscription.request(req);
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
        subscription.cancel();
    }

    @Override
    public void onComplete() {
        System.out.printf("[%s, (%s)] Done!\n", name,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
    }
}
