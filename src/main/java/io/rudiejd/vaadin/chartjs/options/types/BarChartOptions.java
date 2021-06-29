package io.rudiejd.vaadin.chartjs.options.types;

import io.rudiejd.chartjs.config.ChartConfig;
import io.rudiejd.vaadin.chartjs.options.AbstractScalableOptions;

public class BarChartOptions extends AbstractScalableOptions<BarChartOptions> {

    private static final long serialVersionUID = 6873332861426634973L;

    public BarChartOptions(ChartConfig chartConfig) {
        super(chartConfig);
    }

    @Override
    public BarChartOptions getThis() {
        return this;
    }

}
