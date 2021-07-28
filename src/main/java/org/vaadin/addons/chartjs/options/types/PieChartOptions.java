package org.vaadin.addons.chartjs.options.types;

import elemental.json.JsonObject;
import org.vaadin.addons.chartjs.config.ChartConfig;
import org.vaadin.addons.chartjs.options.AbstractOptions;
import org.vaadin.addons.chartjs.options.PieAnimation;
import org.vaadin.addons.chartjs.utils.JUtils;

public class PieChartOptions extends AbstractOptions<PieChartOptions> {

  private static final long serialVersionUID = -2362447185857298842L;

  private Double cutoutPercentage;
  private Double rotation;
  private Double circumference;
  private PieAnimation<PieChartOptions> pieAnimation;

  public PieChartOptions(ChartConfig chartConfig) {
    super(chartConfig);
  }

  /** The percentage of the chart that is cut out of the middle. */
  public PieChartOptions cutoutPercentage(double cutoutPercentage) {
    this.cutoutPercentage = cutoutPercentage;
    return this;
  }

  /** Starting angle to draw arcs from */
  public PieChartOptions rotation(double rotation) {
    this.rotation = rotation;
    return this;
  }

  /** Step into the charts animation configuration */
  public PieAnimation<PieChartOptions> animation() {
    if (pieAnimation == null) {
      pieAnimation = new PieAnimation<>(getThis());
    }
    return pieAnimation;
  }

  /** Sweep to allow arcs to cover */
  public PieChartOptions circumference(double circumference) {
    this.circumference = circumference;
    return this;
  }

  @Override
  public JsonObject buildJson() {
    JsonObject map = super.buildJson();
    JUtils.putNotNull(map, "animation", pieAnimation);
    JUtils.putNotNull(map, "cutoutPercentage", cutoutPercentage);
    JUtils.putNotNull(map, "rotation", rotation);
    JUtils.putNotNull(map, "circumference", circumference);
    return map;
  }

  @Override
  public PieChartOptions getThis() {
    return this;
  }
}
