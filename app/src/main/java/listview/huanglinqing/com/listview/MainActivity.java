package listview.huanglinqing.com.listview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {

    private LoadListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        listView.setAdapter(new ShouxinRenzhengDao().getAdapter(MainActivity.this));
        listView.setOnRefreshListener(new LoadListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainActivity.this, "下拉刷新", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.onRefreshComplete(true);
                    }
                },3000);

            }
        });
//        listView.setOnRefreshListener(new LoadListView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//            }
//        });


    }
}
