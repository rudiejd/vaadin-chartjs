package org.vaadin.addons.chartjs.options.scale;

import elemental.json.Json;
import elemental.json.JsonObject;
import org.vaadin.addons.chartjs.utils.And;
import org.vaadin.addons.chartjs.utils.JUtils;
import org.vaadin.addons.chartjs.utils.JsonBuilder;

/**
 * Used to configure angled lines that radiate from the center of the chart to the point labels.
 *
 * <p>Note that these options only apply if `display` is true.
 */
public class RadialAngleLine<T> extends And<T> implements JsonBuilder {

  private static final long serialVersionUID = -2453923201883558083L;

  private Boolean display;
  private String color;
  private Integer lineWidth;

  /** If true, angle lines are shown. Default: true */
  public RadialAngleLine<T> display(boolean display) {
    this.display = display;
    return this;
  }

  /** Color of angled lines. Default: rgba(0, 0, 0, 0.1) */
  public RadialAngleLine<T> color(String color) {
    this.color = color;
    return this;
  }

  /** Width of angled lines. Default: 1 */
  public RadialAngleLine<T> lineWidth(int lineWidth) {
    this.lineWidth = lineWidth;
    return this;
  }

  public RadialAngleLine(T parent) {
    super(parent);
  }

  @Override
  public JsonObject buildJson() {
    JsonObject map = Json.createObject();
    JUtils.putNotNull(map, "display", display);
    JUtils.putNotNull(map, "color", color);
    JUtils.putNotNull(map, "lineWidth", lineWidth);
    return map;
  }
}
