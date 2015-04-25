package it.catta.spotshare;

import com.parse.ui.ParseLoginDispatchActivity;


public class DispatchActivity extends ParseLoginDispatchActivity {
    @Override
    protected Class<?> getTargetClass() {
        return MainActivity_.class;
    }
}