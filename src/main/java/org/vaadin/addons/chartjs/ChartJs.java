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
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.function.SerializableConsumer;

import elemental.json.JsonArray;
import elemental.json.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.vaadin.addons.chartjs.config.ChartConfig;


//@JsModule("./hammer.min.js") 
//@JsModule("./Moment.js")
//@JsModule("./Chart.min.js") 
//@JsModule("./chartjs-plugin-zoom.min.js")
//@JsModule("./chartjs-plugin-annotation.min.js") 
//@JsModule("./chartjs-connector.js")
//@StyleSheet("./chartjs-connector.css")
@Tag("chart")
public class ChartJs extends Component implements HasSize {

    /**
     * indicates whether we've connected to object in javascript
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

    /**
     * Construct a ChartJs. Be aware that you have to set a {@link ChartConfig} as well. Use {@link #configure(ChartConfig)} to do so.
     */
    public ChartJs() {
        setChartTagId("chartjs-" + this.hashCode() + "-" + System.nanoTime());
    }

    /**
     * Constructs a chart with a {@link ChartConfig}
     * @param chartConfig a chart configuration implementation
     */
    public ChartJs(ChartConfig chartConfig) {
        this();
        configure(chartConfig);
    }

    /**
     * Configure a ChartJs chart.
     * @param chartConfig a chart configuration implementation
     */
    public void configure(ChartConfig chartConfig) {
        this.chartConfig = chartConfig;
        if (connected) {
            UI ui = getUI().orElse(null);
            if (ui != null) {
                ui.getPage().executeJs("document.getElementById($0).chartjs.config = $1", getChartId(), chartConfig.buildJson()); 

            }
        }
    }

    @Override
    protected void onAttach(AttachEvent e) {
        super.onAttach(e);
        getUI().get().getPage().executeJs("let can = document.createElement('canvas'); "
                + "can.setAttribute('id', $1);"
                + "can.setAttribute('width', $2);"
                + "can.setAttribute('height', $3);"
                + "document.getElementById($0).appendChild(can)", 
                getChartId(), getChartCanvasId(), getWidth(), getHeight()).then(
                (r) -> getUI().get().getPage().executeJs("document.getElementById($0).chartjs = new Chart(document.getElementById($1).getContext('2d'), $2)", 
                        getChartId(), getChartCanvasId(), chartConfig.buildJson())
        );
        connected = true;
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
        configure(chartConfig);
        if (connected) {
            UI ui = getUI().orElse(null);
            if (ui != null) {
                ui.getPage().executeJs("document.getElementById($0).chartjs.update()", getChartId());
                
            }
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
        configure(chartConfig);
        update();
    }


    
    public ChartJs setChartTagId(String value) {
        chartIdProperty.set(this, value);
        return this;
    }
    
    public String getChartId() {
        return chartIdProperty.get(this);
    }
    
    public String getChartCanvasId() {
        return chartIdProperty.get(this)+"-canv";
    }
}
