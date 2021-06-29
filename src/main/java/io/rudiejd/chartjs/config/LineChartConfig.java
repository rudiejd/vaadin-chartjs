package io.rudiejd.chartjs.config;

import elemental.json.Json;
import elemental.json.JsonObject;
import io.rudiejd.vaadin.chartjs.data.Data;
import io.rudiejd.vaadin.chartjs.options.types.LineChartOptions;
import io.rudiejd.vaadin.chartjs.utils.JUtils;

/**
 * A line chart is a way of plotting data points on a line.
 *
 * Often, it is used to show trend data, and the comparison of two data sets.
 *
 * @author michael@byteowls.com
 */
public class LineChartConfig implements ChartConfig {

    private static final long serialVersionUID = 4896118079157690609L;

    private Data<LineChartConfig> data;
    private LineChartOptions options;

    public Data<LineChartConfig> data() {
        if (this.data == null) {
            this.data = new Data<>(this);
        }
        return this.data;
    }

    public LineChartOptions options() {
        if (options == null) {
            options = new LineChartOptions(this);
        }
        return options;
    }

    @Override
    public JsonObject buildJson() {
        JsonObject map = Json.createObject();
        JUtils.putNotNull(map, "type", "line");
        if (data != null) {
            JUtils.putNotNull(map, "data", data.buildJson());
        }
        if (options != null) {
            JUtils.putNotNull(map, "options", options.buildJson());
        }
        return map;
    }
}
