package org.vaadin.addons.chartjs.options.types;

import org.vaadin.addons.chartjs.options.AbstractOptions;
import org.vaadin.addons.chartjs.options.scale.RadialLinearScale;
import org.vaadin.addons.chartjs.utils.JUtils;
import org.vaadin.chartjs.config.ChartConfig;

import elemental.json.JsonObject;

public class RadarChartOptions extends AbstractOptions<RadarChartOptions> {

    private static final long serialVersionUID = -4046074534117345099L;

    private RadialLinearScale scale;
    private Double offsetAngle;

    public RadarChartOptions(ChartConfig chartConfig) {
        super(chartConfig);
    }

    public RadarChartOptions scale(RadialLinearScale scale) {
        this.scale = scale;
        return this;
    }

    public RadarChartOptions offsetAngle(double offsetAngle) {
        this.offsetAngle = offsetAngle;
        return this;
    }

    @Override
    public JsonObject buildJson() {
        JsonObject map = super.buildJson();
        JUtils.putNotNull(map, "scale", scale);
        JUtils.putNotNull(map, "offsetAngle", offsetAngle);
        return map;
    }

    @Override
    public RadarChartOptions getThis() {
        return this;
    }

}
