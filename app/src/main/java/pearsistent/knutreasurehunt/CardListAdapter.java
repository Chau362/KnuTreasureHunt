package pearsistent.knutreasurehunt;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.util.ArrayList;

/**
 * Created by Chau Pham on 25.04.2017.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    ArrayList<Item> allItems = new ArrayList<>();
    private Context context;
    private static String teamName;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView cardTitle;
        TextView cardPoints;
        ImageView selfie;

        public ViewHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
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

    public CardListAdapter(ArrayList<Item> itemArrayList, String teamName) {
        this.allItems = itemArrayList;
        this.teamName = teamName;
    }

    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        ViewHolder myViewHolder = new ViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(CardListAdapter.ViewHolder holder, int position) {

        holder.cardTitle.setText(allItems.get(position).getName());
        holder.cardPoints.setText(allItems.get(position).getText()+"  ( " +String.valueOf(allItems.get(position).getPoints())+" pts )");
        //holder.cardPoints.setText(String.valueOf(10));


        //Seulki : if loading has error then I will instead it to marker image in ImageView
        Glide.with(context).using(new FirebaseImageLoader()).load(allItems.get(position).getImageReference()).error(R.drawable.marker).into(holder.selfie);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return allItems.size();
    }


    public Object getItem(int position){
        return allItems.get(position);
    }
}
