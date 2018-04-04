package reactive;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class MySubscriber<T> implements Subscriber<T> {
    private Subscription subscription;
    private String name;

    public MySubscriber(String name){
        this.name = name;
    }

    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        System.out.printf("[%s, (%s)] onSubscribe\n", name,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
        subscription.request(1); // 처음엔 1
    }

    public void onNext(T item) {
        int req = Runtime.getRuntime().availableProcessors();
        System.out.printf("[%s, %d, (%s)] %s\n",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()), req, name, item.toString());
        subscription.request(req);
    }

    public void onError(Throwable t) {
        t.printStackTrace();
        subscription.cancel();
    }

    public void onComplete() {
        System.out.printf("[%s, (%s)] Done!\n",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()), name);
    }
}
