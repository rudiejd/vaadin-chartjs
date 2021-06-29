package org.vaadin.addons.chartjs;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.PropertyDescriptor;
import com.vaadin.flow.component.PropertyDescriptors;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.function.SerializableConsumer;

import elemental.json.JsonArray;
import elemental.json.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.vaadin.chartjs.config.ChartConfig;


@JavaScript("hammer.min.js") 
@JavaScript("Moment.js")
@JavaScript("Chart.min.js") 
@JavaScript("chartjs-plugin-zoom.min.js")
@JavaScript("chartjs-plugin-annotation.min.js") 
@JavaScript("chartjs-connector.js")
@StyleSheet("chartjs-connector.css")
@Tag("chart")
public class ChartJs extends Component implements HasSize {

    /**
     * indicates whether we've connected to object in javascript connector
     */
    private boolean connected = false;
    
    private static final long serialVersionUID = 2999562112373836140L;
    
    private static final PropertyDescriptor<String, String> chartIdProperty =
            PropertyDescriptors.propertyWithDefault("id", "");
    

//    public enum ImageType {
//        PNG
//    }

    public interface DataPointClickListener {
        void onDataPointClick(int datasetIndex, int dataIndex);
    }

    public interface LegendClickListener {
        void onLegendClick(int who, boolean isVisible, int[] visibles);
    }

//    public interface DownloadListener {
//        void onDownload(byte[] imageData);
//    }

    private List<ChartJs.DataPointClickListener> dataPointClickListeners = new ArrayList<>();
    private List<ChartJs.LegendClickListener> legendClickListeners = new ArrayList<>();
    
//    private List<ChartJs.DownloadListener> downloadListeners = new ArrayList<>();

    private ChartConfig chartConfig;
    private ChartJsState state = new ChartJsState();

    /**
     * Construct a ChartJs. Be aware that you have to set a {@link ChartConfig} as well. Use {@link #configure(ChartConfig)} to do so.
     */
    public ChartJs() {
        getElement().getClassList().add("v-chartjs");
        getElement().getClassList().add("-v-chartjs");
        getElement().getClassList().add("v-widget");
        setChartTagId("chartjs-" + this.hashCode() + "-" + System.nanoTime());
    }

    /**
     * Constructs a chart with a {@link ChartConfig}
     * @param chartConfig a chart configuration implementation
     */
    public ChartJs(ChartConfig chartConfig) {
        this();
        configure(chartConfig);
        initConnector();
    }

    /**
     * Configure a ChartJs chart.
     * @param chartConfig a chart configuration implementation
     */
    public void configure(ChartConfig chartConfig) {
        if (chartConfig != null) {
            this.chartConfig = chartConfig;
            getState().configurationJson = chartConfig.buildJson();
        }
        UI ui = getUI().orElse(null);
        if (ui != null) {
            ui.getPage().executeJs("document.getElementById(\"" + getChartId() + "\").config = $0", getState().configurationJson);

        }
    }

    @Override
    protected void onAttach(AttachEvent e) {
        super.onAttach(e);
    }
    
    protected void onStateChange() {
        UI ui = getUI().orElse(null);
        // only update changes in client if we're connected
        if (ui != null && connected) {
            ui.getPage().executeJs("document.getElementById($0).onStateChange($1);", getChartId(), getState().buildJson());
        }
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> 
            ui.getPage().executeJs("window.com_byteowls_vaadin_chartjs_ChartJs($0)", getElement()).then((r) ->  {
                connected = true;
                onStateChange();
            }));
    }

    void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    } 

    /**
     * @return Chart configuration. Useful for update the data after chart drawing
     */
    public ChartConfig getConfig() {
        return this.chartConfig;
    }

    /**
     * Update the chart. Before calling this method, options must be changed and new data must be supplied.
     */
    public void update() {
        if (chartConfig != null) {
           onStateChange();
        }
    }

    /**
     * Destroy the chart. This will call chartjs.destroy();
     */
    public void destroy() {
        UI ui = getUI().orElse(null);
        if (ui != null) {
            ui.getPage().executeJs("document.getElementById($0).destroyChart()", getChartId());
        }
    }

    /**
     * Update the chart. Before calling this method, options must be changed and new data must be supplied.
     *
     * @deprecated because this method updates not only data but also chart options. Use update() instead.
     */
    @Deprecated
    public void refreshData() {
        update();
    }

    /**
     * @return True if the connector's logs defined messages to "console.log" else logging is disabled.
     */
    public boolean isJsLoggingEnabled() {
        return getElement().getProperty("loggingEnabled").equals("true");
    }

    /**
     * Enable or disables the connector's logging to "console.log"
     * @param jsLoggingEnabled If true the connector script will log defined messages to "console.log". Defaults to false.
     */
    public void setJsLoggingEnabled(boolean jsLoggingEnabled) {
        getState().loggingEnabled = jsLoggingEnabled;
        onStateChange();
    }

    /**
     * Adds a listener handling clicks on charts data points.
     * @param listener the click listener.
     */
    public void addClickListener(ChartJs.DataPointClickListener listener) {
        dataPointClickListeners.add(listener);
        checkListenerState();
    }

    /**
     * Remove a listener handling clicks on a chart's data points
     * @param listener the click listener to remove
     */
    public void removeClickListener(ChartJs.DataPointClickListener listener) {
        dataPointClickListeners.remove(listener);
        checkListenerState();
    }
    
    /**
     * Add a listener handling clicks on a chart's legend
     * @param listener the click listener 
     */
    public void addLegendClickListener(ChartJs.LegendClickListener listener) {
    	legendClickListeners.add(listener);
        checkListenerState();
    }
    
    /**
     * Remove a listener handling clicks on a chart's legend
     * @param listener the listener to remove
     */
    public void removeLegendClickListener(ChartJs.LegendClickListener listener) {
    	legendClickListeners.remove(listener);
        checkListenerState();
    }

    private void checkListenerState() {
        getState().dataPointClickListenerFound = !this.dataPointClickListeners.isEmpty();
        getState().legendClickListenerFound = !this.legendClickListeners.isEmpty();
        onStateChange();
    }
    
    // called by client when a data point is clicked
    @ClientCallable
    private void onDataPointClick(int datasetIdx, int dataIdx) {
        for (DataPointClickListener l : dataPointClickListeners) {
            l.onDataPointClick(datasetIdx, dataIdx);
        }
    }

    // called by client when a legend is clicked
    @ClientCallable
    private void onLegendClick(int datasetIdx, boolean visible, JsonArray visiblesJson) {
        int[] visibles = new int[visiblesJson.length()];
        for (int i = 0 ; i < visiblesJson.length(); i++)
            visibles[i] = (int)visiblesJson.getNumber(i);

        for (LegendClickListener l : legendClickListeners) {
            l.onLegendClick(datasetIdx, visible, visibles);
        }
    }

    protected ChartJsState getState() {
        return state;
    } 
    
    /**
     * Show the download action in the menu.
     *
     * @param showDownloadAction
     *            True, the download action in the menu should be displayed.
     */
    public void setShowDownloadAction(boolean showDownloadAction) {
        getState().showDownloadAction = showDownloadAction;
        onStateChange();
    }

    /**
     * Set the label for the download action to the given text.
     *
     * @param downloadActionText
     *            The new text for the download action.
     */
    public void setDownloadActionText(String downloadActionText) {
        getState().downloadActionText = downloadActionText;
        onStateChange();
    }

    /**
     * Set the filename for the downloaded image.
     *
     * @param downloadActionFilename
     *            The filename for the download including its extension,
     *            defaults to chart.png.
     */
    public void setDownloadActionFilename(String downloadActionFilename) {
        getState().downloadActionFilename = downloadActionFilename;
        onStateChange();
    }

    /**
     * If set to true, the downloaded image will receive a white background (instead of the default, which is
     * transparent).
     *
     * @param downloadSetWhiteBackground
     *            Set to true for downloading images with a white background.
     */
    public void setDownloadSetWhiteBackground(boolean downloadSetWhiteBackground) {
        getState().downloadSetWhiteBackground = downloadSetWhiteBackground;
        onStateChange();
    }

    
    public ChartJs setChartTagId(String value) {
        chartIdProperty.set(this, value);
        return this;
    }
    
    public String getChartId() {
        return chartIdProperty.get(this);
    }
}
