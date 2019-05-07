package top.fksoft.player.android.utils.dao;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment<T extends Activity>  extends Fragment implements View.OnClickListener {
    private View contentView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(initLayout(),container,false);
        return contentView;
    }




    @Override
    public void onStart() {
        super.onStart();
        initView();
        initData();
    }
    protected abstract int initLayout();
    protected abstract void initData();
    protected abstract void initView();

    @Nullable
    @Override
    public T getContext() {
        return (T) getActivity();
    }

    public <T extends View> T findViewById(int id) {
        T viewById = contentView.findViewById(id);
        return viewById;
    }


    @Override
    public void onClick(View v) {

    }
}
