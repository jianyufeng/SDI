package com.puyu.mobile.sdi.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.puyu.mobile.sdi.mvvm.model.DataRepository;

import java.lang.reflect.InvocationTargetException;

/**
 * 自定义的一个ViewModel工厂
 * @param <T> 因为有时候可能会有别的数据需要处理，一个DataRepository里的可能不够用，所以支持自定义
 */
public class ViewModelParamsFactory<T extends DataRepository> extends ViewModelProvider.AndroidViewModelFactory {
    private static ViewModelParamsFactory sInstance;
    private Application mApplication;
    private T repository;

    /**
     * Creates a {@code AndroidViewModelFactory}
     * @param application an application to pass in
     */
    public ViewModelParamsFactory(@NonNull Application application, T repository) {
        super(application);
        this.mApplication = application;
        this.repository = repository;
    }

    @NonNull
    @Override
    public <Model extends ViewModel> Model create(@NonNull Class<Model> modelClass) {
        if (AndroidViewModel.class.isAssignableFrom(modelClass)) {
            //noinspection TryWithIdenticalCatches
            try {
                return modelClass.getConstructor(Application.class, repository.getClass())
                        .newInstance(mApplication, repository);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (InstantiationException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            }
        }
        return super.create(modelClass);
    }

}
