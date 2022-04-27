package org.vaadin.addons.chartjs.test;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.addons.chartjs.ChartJs;
import org.vaadin.addons.chartjs.config.BarChartConfig;
import org.vaadin.addons.chartjs.data.BarDataset;
import org.vaadin.addons.chartjs.data.Dataset;
import org.vaadin.addons.chartjs.data.LineDataset;
import org.vaadin.addons.chartjs.options.Position;
import org.vaadin.addons.chartjs.options.scale.Axis;
import org.vaadin.addons.chartjs.options.scale.LinearScale;

@Route("")
public class DemoView extends VerticalLayout {
  public DemoView() {
    BarChartConfig conf = new BarChartConfig();
    conf.data()
        .labels("one", "two", "three")
        .addDataset(new BarDataset().type().label("Stuff").data(100.0, 200.0, 300.0))
        .and();
    LinearScale l = new LinearScale();
    l.ticks().beginAtZero(true);
    conf.options().title().text("Test").and().scales().add(Axis.Y, l).and().done();

    ChartJs myChart = new ChartJs(conf);
    BarChartConfig config = new BarChartConfig();
    config
        .data()
        .labels("January", "February", "March", "April", "May", "June", "July")
        .addDataset(
            new BarDataset()
                .type()
                .label("Dataset 1")
                .backgroundColor("rgba(151,187,205,0.5)")
                .borderColor("white")
                .borderWidth(2))
        .addDataset(
            new LineDataset()
                .type()
                .label("Dataset 2")
                .backgroundColor("rgba(151,187,205,0.5)")
                .borderColor("white")
                .borderWidth(2))
        .addDataset(
            new BarDataset().type().label("Dataset 3").backgroundColor("rgba(220,220,220,0.5)"))
        .and();

    config
        .options()
        .responsive(true)
        .title()
        .display(true)
        .position(Position.LEFT)
        .text("Chart.js Combo Bar Line Chart")
        .and()
        .tooltips()
        .enabled(true)
        .callbacks()
        .label("tooltipItem.yLabel  + ' %'")
        .and()
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
    chart.setHeight("800px");
    chart.setWidth("1000px");

    add(chart);
    chart.update();
  }
}
