package ro.pub.cs.systems.eim.practicaltest02v8;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PracticalTest02v8MainActivity extends AppCompatActivity {

    private EditText currencyInput;
    private Button fetchBitcoinRateButton;
    private TextView resultTextView;
    private Button navigateButton;

    private static final String TAG = "BitcoinApp";
    private static final long CACHE_DURATION = 30 * 1000; // 1 minute in milliseconds

    private HashMap<String, CachedBitcoinRate> cache = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02v8_main);

        currencyInput = findViewById(R.id.currencyInput);
        fetchBitcoinRateButton = findViewById(R.id.fetchBitcoinRateButton);
        resultTextView = findViewById(R.id.resultTextView);
        navigateButton = findViewById(R.id.navigateButton);

        Log.d("BitcoinApp", "App started successfully");

        fetchBitcoinRateButton.setOnClickListener(view -> {
            String currency = currencyInput.getText().toString().toUpperCase();

            if (currency.isEmpty() || (!currency.equals("USD") && !currency.equals("EUR"))) {
                resultTextView.setText("Please enter a valid currency (USD/EUR).");
                return;
            }

            // Check the cache
            CachedBitcoinRate cachedRate = cache.get(currency);
            long currentTime = System.currentTimeMillis();

            if (cachedRate != null && (currentTime - cachedRate.timestamp) < CACHE_DURATION) {
                // Use cached value
                resultTextView.setText("Cached Value:\n1 Bitcoin = " + cachedRate.rate + " " + currency +
                        "\nUpdated at: " + cachedRate.updatedTime);
            } else {
                // Fetch new data from API
                fetchBitcoinRate(currency);
            }
        });

        navigateButton.setOnClickListener(view -> {
            Intent intent = new Intent(PracticalTest02v8MainActivity.this, MainActivity2.class);
            startActivity(intent);
        });
    }

    private void fetchBitcoinRate(String currency) {
        BitcoinApi bitcoinApi = RetrofitClient.getClient().create(BitcoinApi.class);
        Call<BitcoinResponse> call = bitcoinApi.getBitcoinRate(currency);

        call.enqueue(new Callback<BitcoinResponse>() {
            @Override
            public void onResponse(Call<BitcoinResponse> call, Response<BitcoinResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BitcoinResponse bitcoinResponse = response.body();
                    Log.d("BitcoinApp", "Received API Response: " + response.body());


                    // Get the current rate and updated time
                    String rate = currency.equals("USD") ? bitcoinResponse.bpi.USD.rate : bitcoinResponse.bpi.EUR.rate;
                    String updatedTime = bitcoinResponse.time.updated;

                    Log.d("BitcoinApp", "Parsing new data for " + currency + ": Rate = " + rate + ", Updated = " + updatedTime);

                    // Cache the result
                    cache.put(currency, new CachedBitcoinRate(rate, updatedTime, System.currentTimeMillis()));

                    // Display the result
                    resultTextView.setText("1 Bitcoin = " + rate + " " + currency + "\nUpdated at: " + updatedTime);
                } else {
                    resultTextView.setText("Failed to fetch data. Please try again.");
                    Log.e(TAG, "Response failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BitcoinResponse> call, Throwable t) {
                resultTextView.setText("Error: " + t.getMessage());
                Log.e(TAG, "Request failed: " + t.getMessage());
            }
        });
    }

    private static class CachedBitcoinRate {
        String rate;
        String updatedTime;
        long timestamp;

        public CachedBitcoinRate(String rate, String updatedTime, long timestamp) {
            this.rate = rate;
            this.updatedTime = updatedTime;
            this.timestamp = timestamp;
        }
    }
}