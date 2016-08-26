package yellow5a5.materialdesignlogin.IView;

import android.content.Context;

/**
 * Created by Weiwu on 16/8/26.
 */
public interface ICatchScroll {

    void initTitle(Context context);

    void addTitleItem(String ...item);

    void scrollLeft();

    void scrollRight();

}
