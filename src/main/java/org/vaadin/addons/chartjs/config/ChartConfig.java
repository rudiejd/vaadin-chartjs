package org.vaadin.addons.chartjs.config;

import java.io.Serializable;
import org.vaadin.addons.chartjs.options.AbstractOptions;
import org.vaadin.addons.chartjs.utils.JsonBuilder;

public interface ChartConfig extends JsonBuilder, Serializable {
  public AbstractOptions<?> getOptions();
}
