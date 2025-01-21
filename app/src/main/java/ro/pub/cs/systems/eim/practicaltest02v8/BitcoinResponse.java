package ro.pub.cs.systems.eim.practicaltest02v8;

import com.google.gson.annotations.SerializedName;

public class BitcoinResponse {

    @SerializedName("time")
    public Time time;

    @SerializedName("bpi")
    public Bpi bpi;

    public static class Time {
        @SerializedName("updated")
        public String updated;
    }

    public static class Bpi {
        @SerializedName("USD")
        public Currency USD;

        @SerializedName("EUR")
        public Currency EUR;

        public static class Currency {
            @SerializedName("code")
            public String code;

            @SerializedName("rate")
            public String rate;

            @SerializedName("description")
            public String description;

            @SerializedName("rate_float")
            public float rateFloat;
        }
    }
}