package org.vaadin.addons.chartjs.options;

import elemental.json.Json;
import elemental.json.JsonObject;
import java.io.Serializable;
import org.vaadin.addons.chartjs.utils.And;
import org.vaadin.addons.chartjs.utils.JUtils;
import org.vaadin.addons.chartjs.utils.JsonBuilder;

/**
 * @author michael@byteowls.com
 */
public class Animation<T> extends And<T> implements JsonBuilder, Serializable {

  private static final long serialVersionUID = 153634423791436905L;

  public Animation(T parent) {
    super(parent);
  }

  private Integer duration;
  private AnimationEasing easing;

  // TODO callback functions
  // http://www.chartjs.org/docs/#chart-configuration-animation-configuration
  // onProgress Callback called on each step of an animation. Passed a single argument, an object,
  // containing the
  // chart instance and an object with details of the animation.
  // onComplete Callback called at the end of an animation. Passed the same arguments as onProgress

  /** The number of milliseconds an animation takes. */
  public Animation<T> duration(int duration) {
    this.duration = duration;
    return this;
  }

  /** Easing function to use. */
  public Animation<T> easing(AnimationEasing easing) {
    this.easing = easing;
    return this;
  }

  @Override
  public JsonObject buildJson() {
    JsonObject map = Json.createObject();
    JUtils.putNotNull(map, "duration", duration);
    if (easing != null) {
      JUtils.putNotNull(map, "easing", easing.name());
    }
    return map;
  }
}
