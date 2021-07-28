package org.vaadin.addons.chartjs.options.scale;

import elemental.json.Json;
import elemental.json.JsonObject;
import org.vaadin.addons.chartjs.utils.And;
import org.vaadin.addons.chartjs.utils.JUtils;
import org.vaadin.addons.chartjs.utils.JsonBuilder;

/**
 * It defines options for the tick marks that are generated by the axis.
 *
 * @author michael@byteowls.com
 */
public class Ticks<T> extends And<T> implements JsonBuilder {

  private static final long serialVersionUID = -4740687096401461147L;

  private Boolean autoSkip;
  private Boolean display;
  private String fontColor;
  private String fontFamily;
  private Integer fontSize;
  private String fontStyle;
  private Integer labelOffset;
  private Integer maxRotation;
  private Integer minRotation;
  private Boolean mirror;
  private Integer padding;
  private Boolean reverse;
  private String callback;

  public Ticks(T parent) {
    super(parent);
  }

  /**
   * If true, automatically calculates how many labels that can be shown and hides labels
   * accordingly. Turn it off to show all labels no matter what
   */
  public Ticks<T> autoSkip(boolean autoSkip) {
    this.autoSkip = autoSkip;
    return this;
  }

  /** If true, show the ticks. */
  public Ticks<T> display(boolean display) {
    this.display = display;
    return this;
  }

  /** Font color for the tick labels. */
  public Ticks<T> fontColor(String fontColor) {
    this.fontColor = fontColor;
    return this;
  }

  /** Font family for the tick labels, follows CSS font-family options. */
  public Ticks<T> fontFamily(String fontFamily) {
    this.fontFamily = fontFamily;
    return this;
  }

  /** Font size for the tick labels. */
  public Ticks<T> fontSize(int fontSize) {
    this.fontSize = fontSize;
    return this;
  }

  /**
   * Font style for the tick labels, follows CSS font-style options (i.e. normal, italic, oblique,
   * initial, inherit).
   */
  public Ticks<T> fontStyle(String fontStyle) {
    this.fontStyle = fontStyle;
    return this;
  }

  /**
   * Distance in pixels to offset the label from the centre point of the tick (in the y direction
   * for the x axis, and the x direction for the y axis). Note: this can cause labels at the edges
   * to be cropped by the edge of the canvas
   */
  public Ticks<T> labelOffset(int labelOffset) {
    this.labelOffset = labelOffset;
    return this;
  }

  /**
   * Maximum rotation for tick labels when rotating to condense labels. Note: Rotation doesn't occur
   * until necessary. Note: Only applicable to horizontal scales.
   */
  public Ticks<T> maxRotation(int maxRotation) {
    this.maxRotation = maxRotation;
    return this;
  }

  /** Minimum rotation for tick labels. Note: Only applicable to horizontal scales. */
  public Ticks<T> minRotation(int minRotation) {
    this.minRotation = minRotation;
    return this;
  }

  /**
   * Flips tick labels around axis, displaying the labels inside the chart instead of outside. Note:
   * Only applicable to vertical scales.
   */
  public Ticks<T> mirror(boolean mirror) {
    this.mirror = mirror;
    return this;
  }

  /** Padding between the tick label and the axis. Note: Only applicable to horizontal scales. */
  public Ticks<T> padding(int padding) {
    this.padding = padding;
    return this;
  }

  /** Reverses order of tick labels. */
  public Ticks<T> reverse(boolean reverse) {
    this.reverse = reverse;
    return this;
  }

  /**
   * Set a callback function for
   *
   * <p>Cf. <a href=
   * "https://www.chartjs.org/docs/latest/axes/labelling.html#creating-custom-tick-formats">Labelling</a>
   *
   * <p>Example prepending a dollar sign to the value:
   *
   * <pre>
   * scale.ticks().callback("function(value, index, values) { return '$' + value; }");
   * </pre>
   *
   * Shorter, if only a return statement is required:
   *
   * <pre>
   * scale.ticks().callback("return '$' + value;");
   * </pre>
   *
   * Even shorter, omitting the return statement and simply calculating the value:
   *
   * <pre>
   * scale.ticks().callback("'$' + value");
   * </pre>
   */
  public Ticks<T> callback(String callback) {
    this.callback = callback;
    return this;
  }

  @Override
  public JsonObject buildJson() {
    JsonObject map = Json.createObject();
    JUtils.putNotNull(map, "display", display);
    JUtils.putNotNull(map, "autoSkip", autoSkip);
    JUtils.putNotNull(map, "fontColor", fontColor);
    JUtils.putNotNull(map, "fontFamily", fontFamily);
    JUtils.putNotNull(map, "fontSize", fontSize);
    JUtils.putNotNull(map, "fontStyle", fontStyle);
    JUtils.putNotNull(map, "labelOffset", labelOffset);
    JUtils.putNotNull(map, "maxRotation", maxRotation);
    JUtils.putNotNull(map, "minRotation", minRotation);
    JUtils.putNotNull(map, "mirror", mirror);
    JUtils.putNotNull(map, "padding", padding);
    JUtils.putNotNull(map, "reverse", reverse);
    JUtils.putNotNullCallback(map, "callback", callback, "value", "index", "values");
    return map;
  }
}
