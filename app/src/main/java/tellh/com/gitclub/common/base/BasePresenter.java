package tellh.com.gitclub.common.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.common.wrapper.Note;

public class BasePresenter<T extends BaseView> implements MvpPresenter<T> {

    private CompositeSubscription subscriptions;
    private T view;

    @Override
    public void attachView(T mvpView) {
        view = mvpView;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        view = null;
        subscriptions.unsubscribe();
        subscriptions = null;
        Utils.leakWatch(this);
    }

    public T getView() {
        return view;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) {
            throw new MvpViewNotAttachedException();
        }
    }

    public boolean isViewAttached() {
        return view != null;
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(BaseView) before" + " requesting data to the Presenter");
        }
    }

    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }
    public boolean checkNetwork() {
        if (!Utils.isNetworkAvailable(AndroidApplication.getInstance())){
            Note.show(Utils.getString(R.string.error_no_network));
            return false;
        }
        return true;
    }
}