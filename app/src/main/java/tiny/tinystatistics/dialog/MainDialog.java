package tiny.tinystatistics.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import tiny.tinystatistics.R;

/**
 * 类描述
 * 创建者:tiny
 * 日期:18/5/5
 */

public class MainDialog extends Dialog {
    public MainDialog(@NonNull Context context) {
        super(context);
    }

    public MainDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MainDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dialog);
        TextView tv_dialog_test = findViewById(R.id.tv_dialog_test);
        tv_dialog_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "dialog test", Toast.LENGTH_SHORT).show();
            }
        });
        Log.i("lpc", "dialog onCreate");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lpc", "dialog onStop");
    }
}
