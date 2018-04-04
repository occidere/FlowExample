package reactive;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class MySubscriber<T> implements Subscriber<T> {
    private Subscription subscription;

    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1); // 처음엔 1
    }

    public void onNext(T item) {
        int req = Runtime.getRuntime().availableProcessors();
        System.out.printf("[%s, %d, (MySubscriber)] %s\n",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()), req, item.toString());
        subscription.request(req);
    }

    public void onError(Throwable t) {
        t.printStackTrace();
    }

    public void onComplete() {
        System.out.printf("[%s, (MySubscriber)] Done!\n",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
    }
}
