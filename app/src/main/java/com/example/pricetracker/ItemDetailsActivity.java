package com.example.pricetracker;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.example.pricetracker.api.provider.ItemServiceProvider;
import com.example.pricetracker.dto.response.ItemPriceResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsActivity extends AppCompatActivity {

    private String itemName;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        final String providedItemName = getIntent().getStringExtra("itemName");
        itemName = providedItemName != null ? providedItemName : "# NO ITEM NAME FOUND #";
        itemId = getIntent().getIntExtra("itemId", 0);

        TextView itemNameTextView = findViewById(R.id.itemNameTextView);
        itemNameTextView.setText(itemName);
        getItemPricesForChart();
    }

    private void getItemPricesForChart() {
        Call<List<ItemPriceResponse>> itemPricesCall = ItemServiceProvider.getInstance().getItemPrices(itemId);
        itemPricesCall.enqueue(new Callback<List<ItemPriceResponse>>() {
            @Override
            public void onResponse(Call<List<ItemPriceResponse>> call, Response<List<ItemPriceResponse>> response) {
                if(!response.isSuccessful() || response.body() == null) {
                    Log.d("DEBUG", "Bad response for getting item prices!");
                }
                createChart(response.body());
            }

            @Override
            public void onFailure(Call<List<ItemPriceResponse>> call, Throwable t) {
                Log.e("ERROR", "Couldn't get item prices", t);
            }
        });
    }

    private void createChart(List<ItemPriceResponse> prices) {

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair().yLabel(true);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.xAxis(0).title("Date");
        cartesian.xAxis(0).labels().padding(3d, 3d, 3d, 3d);
        cartesian.yAxis(0).title("Price (PLN)");

        // HERE PRICES ARE PREPARED

        prices.sort(Comparator.comparing(ItemPriceResponse::getDate));

        List<DataEntry> entries = prices
                .stream()
                .map(priceResponse -> new ValueDataEntry(priceResponse.getDateString(), priceResponse.getPrice()))
                .collect(Collectors.toList());

        Set set = Set.instantiate();
        set.data(entries);

        Mapping mapping = set.mapAs("{ x: 'x', value: 'value' }");

        Line series = cartesian.line(mapping);
        series.name(itemName);
        series.hovered().markers().enabled(true);
        series.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);
        anyChartView.setChart(cartesian);
    }
}
