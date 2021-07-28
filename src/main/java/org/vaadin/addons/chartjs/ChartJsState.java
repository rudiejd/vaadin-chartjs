package org.vaadin.addons.chartjs;

import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.vaadin.addons.chartjs.utils.JUtils;
import org.vaadin.addons.chartjs.utils.JsonBuilder;

public class ChartJsState implements JsonBuilder, Serializable {
  public boolean loggingEnabled = false;
  public boolean dataPointClickListenerFound = false;
  public boolean legendClickListenerFound = false;
  public boolean showDownloadAction = false;
  public String downloadActionText = "Download";
  public String downloadActionFilename = "chart.jpg";
  public boolean downloadSetWhiteBackground = true;
  public Map<String, String> menuItems = new HashMap<String, String>();
  public JsonValue configurationJson = null;

  @Override
  public JsonObject buildJson() {
    JsonObject map = Json.createObject();
    JUtils.putNotNull(map, "loggingEnabled", loggingEnabled);
    JUtils.putNotNull(map, "dataPointClickListenerFound", dataPointClickListenerFound);
    JUtils.putNotNull(map, "legendClickListenerFound", legendClickListenerFound);
    JUtils.putNotNull(map, "showDownloadAction", showDownloadAction);
    JUtils.putNotNull(map, "downloadActionText", downloadActionText);
    JUtils.putNotNull(map, "downloadActionFilename", downloadActionFilename);
    JUtils.putNotNull(map, "downloadSetWhiteBackground", downloadSetWhiteBackground);
    JUtils.putNotNull(map, "menuItems", menuItems);
    if (configurationJson != null) JUtils.putNotNull(map, "configurationJson", configurationJson);
    return map;
  }
}
