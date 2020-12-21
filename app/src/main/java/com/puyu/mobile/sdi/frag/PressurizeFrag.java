package com.puyu.mobile.sdi.frag;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.puyu.mobile.sdi.BR;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.databinding.FragPressurizeBinding;
import com.puyu.mobile.sdi.model.PressurizeRepository;
import com.puyu.mobile.sdi.mvvm.BaseFragment;
import com.puyu.mobile.sdi.mvvm.ViewModelParamsFactory;
import com.puyu.mobile.sdi.viewmodel.PressurizeViewModel;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/16 16:30
 * desc   :
 * version: 1.0
 */
public class PressurizeFrag extends BaseFragment<FragPressurizeBinding, PressurizeViewModel> {
    private static final String TAG = "11111111PressurizeConfigFrag";
    public static PressurizeFrag getInstance() {
        // Required empty public constructor
        return new PressurizeFrag();
    }


    @Override
    public int initContentView() {
        return R.layout.frag_pressurize;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public PressurizeViewModel initViewModel() {
        return new ViewModelProvider(getActivity(), new ViewModelParamsFactory<>(getActivity()
                .getApplication(), new PressurizeRepository())).get(PressurizeViewModel.class);

    }

    @Override
    protected void initData() {
        binding.spAddPressure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    binding.multipleRg.setVisibility(View.VISIBLE);
                    binding.gAbsPressure.setVisibility(View.INVISIBLE);
                }else {
                    binding.gAbsPressure.setVisibility(View.VISIBLE);
                    binding.multipleRg.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
