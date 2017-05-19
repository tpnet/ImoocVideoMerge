package com.tpnet.imoocvideomerge.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.tpnet.imoocvideomerge.R;
import com.tpnet.imoocvideomerge.util.DataHelper;
import com.tpnet.imoocvideomerge.util.FileUtils;
import com.tpnet.imoocvideomerge.util.LogUtil;
import com.tpnet.imoocvideomerge.view.BaseDialog;
import com.trello.rxlifecycle2.components.RxPreferenceFragment;

import java.util.List;

/**
 * 设置的Fragment
 * Created by litp on 2017/5/2.
 */

public class SettingFragment extends RxPreferenceFragment {


    private ListPreference mstroage;  //存儲卡

    String key;


    //当前选择保存的sd卡根路径 的sp的key
    public final static String STORAGE_PATH_KEY = "storage_path_key";

    //

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        initData();
    }

    private <T> T findPreference(@StringRes int id) {
        return (T) (findPreference(getString(id)));
    }

    protected void initData() {
        mstroage = findPreference(R.string.preference_key_storage);
        key = getString(R.string.preference_key_storage);

        //获取内存卡数量
        final List<String> list = FileUtils.getExtSDCardPath();
        if (list.size() > 0) {
            //有外置sd卡
            mstroage.setEntryValues(getResources().getTextArray(R.array.array_storage_all));
        }


        if (mstroage.getValue() == null) {
            mstroage.setValueIndex(0);
        }

        String currStroage = DataHelper.getStringSF(getActivity(), key);
        mstroage.setSummary("当前方式:" + currStroage);
        //mWxPay.setOnPreferenceClickListener(this);
        //mQQPay.setOnPreferenceClickListener(this);

        mstroage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.equals("内置")) {
                    //setFile("/sys/misc-config/usbsw", "0");
                    LogUtil.e("选择了第一个");
                    DataHelper.SetStringSF(getActivity(), STORAGE_PATH_KEY, FileUtils.getInnerRootPath());

                } else if (newValue.equals("外置")) {
                    DataHelper.SetStringSF(getActivity(), STORAGE_PATH_KEY, list.get(0));
                    LogUtil.e("选择了第二个");
                }

                return true;
            }
        });
    }


    private BaseDialog delDownTipDialog;    //提示是否删除的dialog

    ProgressDialog progressDialog;   //删除中的提示dialog


}
