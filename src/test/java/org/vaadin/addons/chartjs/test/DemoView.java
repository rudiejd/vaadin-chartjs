package org.vaadin.addons.chartjs.test;
import com.vaadin.flow.router.Route;

import org.vaadin.addons.chartjs.ChartJs;
import org.vaadin.addons.chartjs.config.BarChartConfig;
import org.vaadin.addons.chartjs.data.BarDataset;
import org.vaadin.addons.chartjs.data.Dataset;
import org.vaadin.addons.chartjs.data.LineDataset;
import org.vaadin.addons.chartjs.options.Position;
import org.vaadin.addons.chartjs.options.zoom.XYMode;

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
                .addDataset(new LineDataset().type().label("Dataset 2").backgroundColor("brown").borderColor("brown").borderWidth(2))
                .addDataset(new BarDataset().type().label("Dataset 3").backgroundColor("orange"))
                .and();

        config.
            options()
                .responsive(true)
                .title()
                    .display(true)
                    .position(Position.TOP)
                    .text("Chart.js Combo Bar Line Chart")
                    .and()
                .zoom()
                .mode(XYMode.X)
                .enabled(true)
                .and()
                .pan()
                .enabled(true)
                .mode(XYMode.X)
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
        add(chart);
        BarChartConfig config2 = new BarChartConfig();
        config2
            .data()
                .labels("January", "February", "March", "April", "May", "June", "July")
                .addDataset(new BarDataset().type().label("Dataset 1").backgroundColor("rgba(151,187,205,0.5)").borderColor("white").borderWidth(2))
                .addDataset(new LineDataset().type().label("Go browns").backgroundColor("brown").borderColor("brown").borderWidth(2))
                .addDataset(new BarDataset().type().label("Dataset 3").backgroundColor("orange"))
                .and();

        config2.
            options()
                .responsive(true)
                .title()
                    .display(true)
                    .position(Position.LEFT)
                    .text("Stupid Chart");
        List<String> labels2 = config2.data().getLabels();
        for (Dataset<?, ?> ds : config2.data().getDatasets()) {
            List<Double> data = new ArrayList<>();
            for (int i = 0; i < labels2.size(); i++) {
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
        chart.configure(config2);
      
    }

}