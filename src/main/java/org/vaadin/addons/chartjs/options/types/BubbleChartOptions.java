package org.vaadin.addons.chartjs.options.types;

import org.vaadin.addons.chartjs.options.AbstractScalableOptions;
import org.vaadin.chartjs.config.ChartConfig;

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
