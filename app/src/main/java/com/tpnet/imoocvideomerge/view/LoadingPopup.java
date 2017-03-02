package com.tpnet.imoocvideomerge.view;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.tpnet.imoocvideomerge.R;
import com.tpnet.imoocvideomerge.base.basepopup.BasePopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 正在加载列表的等待框
 * Created by Litp on 2017/2/22.
 */

public class LoadingPopup extends BasePopupWindow  {


    @BindView(R.id.tv_load_tip)
    TextView tvLoadTip;

    public LoadingPopup(Activity context) {
        super(context);

        //绑定ButterKnife
        ButterKnife.bind(getPopupWindowView());


/*        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                tvLoadTip.setText("");
            }
        });*/

    }

    /**
     * 初始化一个进入动画，该动画将会用到initAnimaView()返回的view
     *
     * @return
     */
    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    /**
     * 如果有需要的话，可以使用这个方法返回一个点击dismiss popupwindow的view(也许是遮罩层也许是某个view，这个随您喜欢)
     *
     * @return
     */
    @Override
    public View getClickToDismissView() {
        return null;
    }

    /**
     * 初始化您的popupwindow界面，建议直接使用createPopupById()
     *
     * @return
     */
    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popup_loading_layout);
    }

    /**
     * 得到用于展示动画的view，一般为了做到蒙层效果，我们的xml都会给一个灰色半透明的底层然后才是给定展示的popup（详情见demo）
     *
     * @return
     */
    @Override
    public View initAnimaView() {
        return null;
    }


    public void setText(String text){
        tvLoadTip.setText(text);
    }


}
