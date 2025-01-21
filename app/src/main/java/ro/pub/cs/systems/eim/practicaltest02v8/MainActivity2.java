package ro.pub.cs.systems.eim.practicaltest02v8;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {

    private EditText operand1EditText, operand2EditText, operationEditText;
    private Button calculateButton;
    private TextView resultTextView;

    private static final String TAG = "CalculatorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize UI components
        operand1EditText = findViewById(R.id.operand1EditText);
        operand2EditText = findViewById(R.id.operand2EditText);
        operationEditText = findViewById(R.id.operationEditText);
        calculateButton = findViewById(R.id.calculateButton);
        resultTextView = findViewById(R.id.resultTextView);

        // Set onClickListener for the Calculate button
        calculateButton.setOnClickListener(view -> {
            // Get user inputs
            String operation = operationEditText.getText().toString();
            int t1 = Integer.parseInt(operand1EditText.getText().toString());
            int t2 = Integer.parseInt(operand2EditText.getText().toString());

            // Perform the calculation using the server
            performOperation(operation, t1, t2);
        });
    }

    private void performOperation(String operation, int t1, int t2) {
        // Initialize Retrofit for server communication
        CalculatorApi calculatorApi = RetrofitClient.getClient("http://127.0.0.1:8080").create(CalculatorApi.class);

        // Call the server API
        Call<CalculatorResponse> call = calculatorApi.performOperation(operation, t1, t2);
        call.enqueue(new Callback<CalculatorResponse>() {
            @Override
            public void onResponse(Call<CalculatorResponse> call, Response<CalculatorResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Display the result in the TextView
                    resultTextView.setText("Result: " + response.body().result);
                    Log.d(TAG, "Result: " + response.body().result);
                } else {
                    // Handle unsuccessful response
                    resultTextView.setText("Error: Unable to perform calculation");
                    Log.e(TAG, "Error response: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CalculatorResponse> call, Throwable t) {
                // Handle failure to connect to the server
                resultTextView.setText("Error: " + t.getMessage());
                Log.e(TAG, "Request failed: " + t.getMessage());
            }
        });
    }
}