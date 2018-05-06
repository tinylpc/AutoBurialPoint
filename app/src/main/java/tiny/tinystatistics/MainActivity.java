package tiny.tinystatistics;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import tiny.tinystatistics.dialog.MainDialog;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainDialog md = new MainDialog(MainActivity.this);
                md.show();
            }
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.vg_container_main, new HomeFragment());
        ft.commit();

        findViewById(R.id.vg_container_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
