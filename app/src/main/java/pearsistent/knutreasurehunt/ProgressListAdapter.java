package pearsistent.knutreasurehunt;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Chau Pham on 01.05.2017.
 */

public class ProgressListAdapter extends RecyclerView.Adapter<ProgressListAdapter.ViewHolder> {

    private ArrayList<Item> allItems = new ArrayList<>();
    private Context context;
    private String teamName = null;
    ArrayList<Item> updateKeyList = new ArrayList<>();
    private int point;

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView selfieCardView;
        TextView selfieTitle;
        TextView selfiePoints;
        ImageView selfieImage;
        ImageButton selfieTrashBtn;



        public ViewHolder(View itemView) {

            super(itemView);
            this.selfieCardView = (CardView) itemView.findViewById(R.id.selfieCardView);
            this.selfieTitle = (TextView) itemView.findViewById(R.id.selfieTitle);
            this.selfiePoints = (TextView) itemView.findViewById(R.id.selfiePoints);
            this.selfieImage = (ImageView) itemView.findViewById(R.id.selfieImage);
            this.selfieTrashBtn = (ImageButton) itemView.findViewById(R.id.selfieTrashBtn);

            this.selfieTrashBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder myBuilder = new AlertDialog.Builder(context);
                    myBuilder.setMessage("Are you sure you want to delete this picture?");
                    myBuilder.setCancelable(true);

                    myBuilder.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int position = getAdapterPosition();
                                    int minusPoint = allItems.get(position).getPoints();

                                    String itemName = allItems.get(position).getName()+".jpg";
                                    deleteItemFromStorage(itemName);
                                    deleteItemFromRecyclerView(position);
                                    deleteItemFromDatabase(position);
                                    updatePoint(minusPoint);

                                }
                            });

                    myBuilder.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog deleteDialog = myBuilder.create();
                    deleteDialog.show();
                }
            });
        }
    }

    private void updatePoint(final int point) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Team").child(teamName).child("teamPoint").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int currentPoint = dataSnapshot.getValue(int.class);

                int changePoint = currentPoint - point;

                dataSnapshot.getRef().setValue(changePoint);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public ProgressListAdapter(ArrayList<Item> itemArrayList, String teamName, Context context) {
        this.allItems = itemArrayList;
        this.teamName = teamName;
        this.context = context;
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
        holder.selfieTitle.setText(allItems.get(position).getName());
        holder.selfiePoints.setText(allItems.get(position).getText() + "  ( " + String.valueOf(allItems.get(position).getPoints()) + " pts )");
        Glide.with(context).using(new FirebaseImageLoader()).load(allItems.get(position).getImageReference()).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).error(R.drawable.marker).into(holder.selfieImage);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return allItems.size();
    }


    private void deleteItemFromDatabase(int position) {

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://knutreasurehunt.firebaseio.com/");
        final String stringPosition = String.valueOf(position);

        //final ArrayList<Item> updateKeyList = new ArrayList<>();

        updateKeyList.clear();
        mDatabase.child("Team").child(teamName).child("itemList").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot tempSnapShot : dataSnapshot.getChildren()){
                    Item item = tempSnapShot.getValue(Item.class);

                    if(!tempSnapShot.getKey().equals(stringPosition)) {
                        updateKeyList.add(item);
                    }
                }

                if(updateKeyList.size()==0){
                    Item iniItem = new Item();
                    updateKeyList.add(iniItem);
                }

                mDatabase.child("Team").child(teamName).child("itemList").setValue(updateKeyList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void deleteItemFromRecyclerView(int position) {
        allItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, allItems.size());
        notifyDataSetChanged();
    }

    private void deleteItemFromStorage(String imageFileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference childRef = storageRef.child(teamName + "/" + imageFileName);

        childRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "File deleted successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "File couldn't be deleted!", Toast.LENGTH_SHORT).show();
            }
        });



    }
}
