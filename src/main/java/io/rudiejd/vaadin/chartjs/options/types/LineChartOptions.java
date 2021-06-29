package io.rudiejd.vaadin.chartjs.options.types;

import elemental.json.JsonObject;
import io.rudiejd.chartjs.config.ChartConfig;
import io.rudiejd.vaadin.chartjs.options.AbstractScalableOptions;
import io.rudiejd.vaadin.chartjs.utils.JUtils;

public class LineChartOptions extends AbstractScalableOptions<LineChartOptions> {

    private static final long serialVersionUID = -5830320660361399534L;

    private Boolean showLines;

    public LineChartOptions(ChartConfig chartConfig) {
        super(chartConfig);
    }

    public LineChartOptions showLines(boolean showLines) {
        this.showLines = showLines;
        return this;
    }

    @Override
    public JsonObject buildJson() {
        JsonObject map = super.buildJson();
        JUtils.putNotNull(map, "showLines", showLines);
        return map;
    }

    @Override
    public LineChartOptions getThis() {
        return this;
    }

}
