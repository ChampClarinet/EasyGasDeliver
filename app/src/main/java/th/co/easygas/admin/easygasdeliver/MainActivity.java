package th.co.easygas.admin.easygasdeliver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.easygas.admin.easygasdeliver.Adapters.TasksCardAdapter;
import th.co.easygas.admin.easygasdeliver.Model.Tasks;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Tasks> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mockGasTanks();

        mAmountTextView.setText(getString(R.string.tasks_amount, tasks.size()));

        TasksCardAdapter adapter = new TasksCardAdapter(tasks, this);
        mTankRV.setLayoutManager(new LinearLayoutManager(this));
        mTankRV.setHasFixedSize(true);
        mTankRV.setAdapter(adapter);

        bindData();

    }

    private void bindData() {

    }

    private void mockGasTanks() {
        tasks = new ArrayList<>();
        Tasks t;
        t = new Tasks(1, "Captain'O", 13.811291480369361, 100.04361244482197, 1, "both");
        tasks.add(t);
        t = new Tasks(2, "SitNee", 13.811291480369361, 100.04355745953717, 1, "send");
        tasks.add(t);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        }
        return super.onOptionsItemSelected(item);
    }

    @BindView(R.id.tanks_rv)
    RecyclerView mTankRV;
    @BindView(R.id.label_tasks_amount)
    TextView mAmountTextView;

}
