package io.rudiejd.vaadin.chartjs.options.types;

import io.rudiejd.chartjs.config.ChartConfig;
import io.rudiejd.vaadin.chartjs.options.AbstractScalableOptions;

public class BubbleChartOptions extends AbstractScalableOptions<BubbleChartOptions> {

    private static final long serialVersionUID = -3318129378787232820L;

    public BubbleChartOptions(ChartConfig chartConfig) {
        super(chartConfig);
    }

    @Override
    public BubbleChartOptions getThis() {
        return this;
    }

}
