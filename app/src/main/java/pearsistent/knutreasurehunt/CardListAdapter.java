package pearsistent.knutreasurehunt;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chau Pham on 25.04.2017.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    ArrayList<Item> allItems = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cardTitle;
        public TextView cardPoints;
        public ImageView selfie;

        public ViewHolder(View itemView) {
            super(itemView);
            this.cardTitle = (TextView) itemView.findViewById(R.id.cardTitle);
            this.cardPoints = (TextView) itemView.findViewById(R.id.cardSubtext);
            this.selfie = (ImageView) itemView.findViewById(R.id.cardImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                }
            });
        }
    }

    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        ViewHolder myViewHolder = new ViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(CardListAdapter.ViewHolder holder, int position) {
        holder.cardTitle.setText(allItems.get(position).getName());
        holder.cardPoints.setText(allItems.get(position).getPoints());
        holder.selfie.setImageResource(allItems.get(position).getImage_i());
    }

    @Override
    public int getItemCount() {
        return allItems.size();
    }
}
