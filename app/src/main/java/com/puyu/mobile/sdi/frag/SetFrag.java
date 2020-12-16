package com.puyu.mobile.sdi.frag;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.puyu.mobile.sdi.BR;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.databinding.FragSetBinding;
import com.puyu.mobile.sdi.model.SetRepository;
import com.puyu.mobile.sdi.mvvm.BaseFragment;
import com.puyu.mobile.sdi.mvvm.ViewModelParamsFactory;
import com.puyu.mobile.sdi.viewmodel.SetViewModel;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/16 16:30
 * desc   :
 * version: 1.0
 */
public class SetFrag extends BaseFragment<FragSetBinding, SetViewModel> {
    private static final String TAG = "11111111SetFrag";
    public static SetFrag getInstance() {
        // Required empty public constructor
        return new SetFrag();
    }


    @Override
    public int initContentView() {
        return R.layout.frag_set;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public SetViewModel initViewModel() {
        return new ViewModelProvider(getActivity(), new ViewModelParamsFactory<>(getActivity()
                .getApplication(), new SetRepository())).get(SetViewModel.class);

    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView: " );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: " );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }
}
