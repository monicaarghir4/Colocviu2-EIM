package ro.pub.cs.systems.eim.practicaltest02v8;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BitcoinApi {

    @GET("v1/bpi/currentprice/{currency}.json")
    Call<BitcoinResponse> getBitcoinRate(@Path("currency") String currency);
}