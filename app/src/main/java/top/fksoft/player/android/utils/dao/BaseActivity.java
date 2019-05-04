package top.fksoft.player.android.utils.dao;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Object o = initLayout();
        if (o instanceof View){
            setContentView((View) o);
        }else{
            setContentView((Integer) o);
        }
    }

    protected abstract Object initLayout();


}
