package dk.itu.mmad.bikeshare.controller;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;
import dk.itu.mmad.bikeshare.model.Transaction;

public class TransactionHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "TransactionHolder";

    private TextView mDescriptionView;
    private TextView mIdView;
    private TextView mAmountView;

    public TransactionHolder(View itemView) {
        super(itemView);

        mDescriptionView = itemView.findViewById(R.id.description_text);
        mIdView = itemView.findViewById(R.id.id_text);
        mAmountView = itemView.findViewById(R.id.amount_text);
    }

    public void bind(Transaction transaction) {
        mDescriptionView.setText(transaction.getDescription());
        mIdView.setText(transaction.getId());

        String amount = transaction.getAmount() + " kr";
        mAmountView.setText(amount);
    }
}
