package pearsistent.knutreasurehunt;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chau Pham on 01.05.2017.
 */

public class ProgressListAdapter extends RecyclerView.Adapter<ProgressListAdapter.ViewHolder> {

    ArrayList<Item> allItems = new ArrayList<>();
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView selfieCardView;
        TextView selfieTitle;
        TextView selfiePoints;
        ImageButton selfieTrashBtn;


        public ViewHolder(View itemView) {
            super(itemView);
            this.selfieCardView = (CardView) itemView.findViewById(R.id.selfieCardView);
            this.selfieTitle = (TextView) itemView.findViewById(R.id.selfieTitle);
            this.selfiePoints = (TextView) itemView.findViewById(R.id.selfiePoints);
            this.selfieTrashBtn = (ImageButton) itemView.findViewById(R.id.selfieTrashBtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                }
            });

        }

    }

    @Override
    public ProgressListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selfieview, parent, false);
        ProgressListAdapter.ViewHolder myViewHolder = new ProgressListAdapter.ViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ProgressListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
