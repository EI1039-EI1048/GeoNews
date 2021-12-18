package es.uji.geonews.controller.tasks;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class UserTask extends AppCompatActivity {
    
    public abstract void execute();

    protected void lockUI(Context context, ConstraintLayout layout){
        layout.setVisibility(View.VISIBLE);
        layout.bringToFront();
        ((Activity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void unlockUI(Context context, ViewGroup layout){
        layout.setVisibility(View.GONE);
        ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
