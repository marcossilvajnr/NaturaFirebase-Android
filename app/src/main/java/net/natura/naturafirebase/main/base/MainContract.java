package net.natura.naturafirebase.main.base;

import net.natura.naturafirebase.base.view.BaseView;

/**
 * Created by marcos on 04/06/17.
 */

public interface MainContract {

    interface Presenter {
        void fetchUserInformation();
    }

    interface View extends BaseView {
        void openSplash();
    }
}
