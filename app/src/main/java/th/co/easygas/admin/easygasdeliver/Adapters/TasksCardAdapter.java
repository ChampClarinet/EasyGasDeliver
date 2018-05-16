package th.co.easygas.admin.easygasdeliver.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.easygas.admin.easygasdeliver.DestinationActivity;
import th.co.easygas.admin.easygasdeliver.GasTankActivity;
import th.co.easygas.admin.easygasdeliver.Model.Tasks;
import th.co.easygas.admin.easygasdeliver.R;

import static th.co.easygas.admin.easygasdeliver.Core.Utils.createLoadDialog;

public class TasksCardAdapter extends RecyclerView.Adapter<TasksCardAdapter.GenericHolder> {

    private static final String TAG = TasksCardAdapter.class.getSimpleName();

    private ArrayList<Tasks> dataSet;
    private Context context;

    public TasksCardAdapter(ArrayList<Tasks> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public GenericHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_tasks_list, parent, false);
        return new ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(GenericHolder holder, int position) {
        Tasks tasks = dataSet.get(position);
        holder.setViewData(tasks);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public ArrayList getData() {
        return dataSet;
    }

    public abstract static class GenericHolder extends RecyclerView.ViewHolder {

        public GenericHolder(View itemView) {
            super(itemView);
        }

        abstract public void setViewData(Tasks tasks);

    }

    public static class ViewHolder extends GenericHolder {

        //bind cardview's widgets
        @BindView(R.id.cardview_tasks)
        CardView tasksCardView;
        @BindView(R.id.cardview_store_name)
        TextView storeNameTextView;
        @BindView(R.id.cardview_tasks_amount)
        TextView tasksAmountTextView;
        @BindView(R.id.cardview_status_back)
        TextView backTextView;
        @BindView(R.id.cardview_status_send)
        TextView sendTextView;
        private Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        private void openDestinationActivity(Tasks tasks) {
            Intent i = new Intent(context, DestinationActivity.class);
            i.putExtra(context.getString(R.string.model_name_taks), tasks);
            createLoadDialog(context, context.getString(R.string.loading));
            context.startActivity(i);
        }

        @Override
        public void setViewData(final Tasks tasks) {
            storeNameTextView.setText(tasks.getStoreName());
            tasksAmountTextView.setText(Integer.toString(tasks.getTanksQuantity()));

            if(tasks.getTaskType().equalsIgnoreCase("back")){
                backTextView.setBackgroundColor(context.getResources().getColor(R.color.back));
                sendTextView.setBackgroundColor(context.getResources().getColor(R.color.disabled));
            }else if(tasks.getTaskType().equalsIgnoreCase("send")){
                backTextView.setBackgroundColor(context.getResources().getColor(R.color.disabled));
                sendTextView.setBackgroundColor(context.getResources().getColor(R.color.send));
            }else if(tasks.getTaskType().equalsIgnoreCase("both")) {
                backTextView.setBackgroundColor(context.getResources().getColor(R.color.back));
                sendTextView.setBackgroundColor(context.getResources().getColor(R.color.send));
            }else Log.d(TAG, "invalid tasks type");

            tasksCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        openDestinationActivity(tasks);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }

    public String printList() {
        String out = "";
        for (Tasks s : dataSet) out += s.getTaskId() + ", ";
        return out;
    }

}
