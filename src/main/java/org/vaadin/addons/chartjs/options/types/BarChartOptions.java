package org.vaadin.addons.chartjs.options.types;

import org.vaadin.addons.chartjs.options.AbstractScalableOptions;
import org.vaadin.chartjs.config.ChartConfig;

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
