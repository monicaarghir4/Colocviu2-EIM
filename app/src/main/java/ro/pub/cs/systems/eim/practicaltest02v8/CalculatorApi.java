package ro.pub.cs.systems.eim.practicaltest02v8;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CalculatorApi {
    @GET("/expr/expr_get.py")
    Call<CalculatorResponse> performOperation(
            @Query("operation") String operation,
            @Query("t1") int t1,
            @Query("t2") int t2
    );
}