package dk.itu.mmad.bikeshare.controller;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;
import dk.itu.mmad.bikeshare.model.Transaction;
import dk.itu.mmad.bikeshare.model.User;
import io.realm.Realm;
import io.realm.Sort;

public class ListTransactionsActivity extends AppCompatActivity {

    private TextView mAccountBalanceText;
    private RecyclerView mRecyclerView;
    private TransactionAdaptor mAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transactions);

        mAccountBalanceText = (TextView) findViewById(R.id.account_balance_text);

        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    User user = bgRealm.where(User.class)
                            .equalTo("mEmail", PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                    .getString("user", null))
                            .findFirst();
                    String accountBalanceText = "Account balance: " + user.getBalance() + " kr";
                    mAccountBalanceText.setText(accountBalanceText);
                }
            });
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.transaction_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        try (Realm realm = Realm.getDefaultInstance()) {
            mAdaptor = new TransactionAdaptor(realm.where(Transaction.class)
                    .equalTo("mUserEmail", PreferenceManager.getDefaultSharedPreferences(this)
                            .getString("user", null))
                    .sort("mDate", Sort.DESCENDING)
                    .findAllAsync());
            mRecyclerView.setAdapter(mAdaptor);
        }

    }
}
