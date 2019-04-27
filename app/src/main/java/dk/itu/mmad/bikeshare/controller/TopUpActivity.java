package dk.itu.mmad.bikeshare.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Transaction;
import dk.itu.mmad.bikeshare.model.User;
import dk.itu.mmad.bikeshare.util.ValidationUtils;
import io.realm.Realm;

public class TopUpActivity extends AppCompatActivity {

    private TextView mAmountText;
    private TextView mCardNumberText;
    private TextView mCardCVVText;
    private Button mTopUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        mAmountText = (TextView) findViewById(R.id.amount_text);
        mCardNumberText = (TextView) findViewById(R.id.card_number_text);
        mCardCVVText = (TextView) findViewById(R.id.card_cvv_text);
        mTopUpButton = (Button) findViewById(R.id.top_up_button);

        mTopUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String amountText = mAmountText.getText().toString().trim();
                String cardNumberText = mCardNumberText.getText().toString().trim();
                String cardCVVText = mCardCVVText.getText().toString().trim();

                if (!ValidationUtils.isValidDouble(amountText)) {
                    showDialogBox(view, "Please enter a valid amount.");
                    return;
                }

                if (!ValidationUtils.isValidCreditCardNumber(cardNumberText) ||
                        !ValidationUtils.isValidCreditCardCVVNumber(cardCVVText)) {
                    showDialogBox(view, "Top up error.\nPlease check your card details.");
                    return;
                }

                try (Realm realm = Realm.getDefaultInstance()) {
                    final String email = PreferenceManager.getDefaultSharedPreferences(view.getContext()).getString("user", null);

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            User user = bgRealm.where(User.class)
                                    .equalTo("mEmail", email)
                                    .findFirst();
                            double amount = Double.parseDouble(amountText);
                            user.addBalance(amount);

                            bgRealm.insert(new Transaction(user.getEmail(), amount));
                            bgRealm.insertOrUpdate(user);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(view.getContext(), "Top up of " + amountText + " kr is successful", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(view.getContext(), BikeShareActivity.class);
                            startActivity(intent);
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            showDialogBox(view, error.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void showDialogBox(View view, String message) {
        new AlertDialog.Builder(view.getContext())
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }
}
