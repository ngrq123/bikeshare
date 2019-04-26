package dk.itu.mmad.bikeshare.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Transaction;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class TransactionAdaptor extends RealmRecyclerViewAdapter<Transaction, TransactionHolder> {
    // Adapted from
    // https://github.com/realm/realm-android-adapters/blob/master/example/src/main/java/io/realm/examples/adapters/ui/recyclerview/MyRecyclerViewAdapter.java

    // Logging variable
    private static final String TAG = "TransactionAdaptor";

    TransactionAdaptor(OrderedRealmCollection<Transaction> data) {
        super(data,true);
        setHasStableIds(true);
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_transaction, parent, false);
        return new TransactionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransactionHolder holder, int position) {
        Transaction transaction = getItem(position);
        holder.bind(transaction);
    }
}
