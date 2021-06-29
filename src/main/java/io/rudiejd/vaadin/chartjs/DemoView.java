package io.rudiejd.vaadin.chartjs;

import com.vaadin.flow.router.Route;

import io.rudiejd.chartjs.config.BarChartConfig;
import io.rudiejd.vaadin.chartjs.data.BarDataset;
import io.rudiejd.vaadin.chartjs.data.Dataset;
import io.rudiejd.vaadin.chartjs.data.LineDataset;
import io.rudiejd.vaadin.chartjs.options.Position;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@Route("")
public class DemoView extends VerticalLayout {
    public DemoView() {
        BarChartConfig config = new BarChartConfig();
        config
            .data()
                .labels("January", "February", "March", "April", "May", "June", "July")
                .addDataset(new BarDataset().type().label("Dataset 1").backgroundColor("rgba(151,187,205,0.5)").borderColor("white").borderWidth(2))
                .addDataset(new LineDataset().type().label("Dataset 2").backgroundColor("rgba(151,187,205,0.5)").borderColor("white").borderWidth(2))
                .addDataset(new BarDataset().type().label("Dataset 3").backgroundColor("rgba(220,220,220,0.5)"))
                .and();

        config.
            options()
                .responsive(true)
                .title()
                    .display(true)
                    .position(Position.LEFT)
                    .text("Chart.js Combo Bar Line Chart")
                    .and()
                .zoom()
                .enabled(true)
                .and()
               .done();

        List<String> labels = config.data().getLabels();
        for (Dataset<?, ?> ds : config.data().getDatasets()) {
            List<Double> data = new ArrayList<>();
            for (int i = 0; i < labels.size(); i++) {
                data.add((double) (Math.random() > 0.5 ? 1.0 : -1.0) * Math.round(Math.random() * 100));
            }

            if (ds instanceof BarDataset) {
                BarDataset bds = (BarDataset) ds;
                bds.dataAsList(data);    
            }

            if (ds instanceof LineDataset) {
                LineDataset lds = (LineDataset) ds;
                lds.dataAsList(data);    
            }
        }

        ChartJs chart = new ChartJs(config);
        chart.addClickListener((datasetIdx, dataIdx) -> {
            BarDataset dataset = (BarDataset) config.data().getDatasets().get(datasetIdx);
            Notification.show("BarDataset at idx:" + datasetIdx + "; Data: idx=" + dataIdx + "; Value=" + dataset.getData().get(dataIdx));
        }); 
        chart.addLegendClickListener((who, isVisible, visibles) -> {
            Notification.show("Legend clicked");
            
        });
    
        chart.setJsLoggingEnabled(true);
        
        add(chart);
    }

}
