package org.vaadin.addons.chartjs;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.PropertyDescriptor;
import com.vaadin.flow.component.PropertyDescriptors;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;

import elemental.json.JsonArray;
import elemental.json.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.vaadin.addons.chartjs.config.ChartConfig;
import org.vaadin.addons.chartjs.options.TooltipsCallbacks;
import org.vaadin.addons.chartjs.utils.JUtils;
import org.vaadin.addons.chartjs.ChartJsUtils;


@JsModule("./hammer.min.js") 
@JsModule("./Moment.js")
@JsModule("./Chart.min.js") 
@JsModule("./chartjs-plugin-zoom.min.js")
@JsModule("./chartjs-plugin-annotation.min.js") 
@Tag("chart")
public class ChartJs extends Component implements HasSize {

    /**
     * indicates whether we've connected to object in javascript
     */
    private boolean connected = false;
    
    private static final long serialVersionUID = 2999562112373836140L;
    
    private static final PropertyDescriptor<String, String> chartIdProperty =
            PropertyDescriptors.propertyWithDefault("id", "");

    private ChartConfig chartConfig;

    /**
     * Construct a ChartJs. Be aware that you have to set a {@link ChartConfig} as well. Use {@link #configure(ChartConfig)} to do so.
     */
    public ChartJs() {
        setChartTagId("chartjs-" + this.hashCode() + "-" + System.nanoTime());
        getElement().appendChild(new Element("canvas")
        		.setAttribute("id", getChartCanvasId()).setAttribute("width", "100%").setAttribute("height", "100%"));
    }

    /**
     * Constructs a chart with a {@link ChartConfig}
     * @param chartConfig a chart configuration implementation
     */
    public ChartJs(ChartConfig chartConfig) {
        this();
        this.chartConfig = chartConfig;
    }
    
    public void addFunction(String domPath, String functionStr) {
           ChartJsUtils.safelyExecuteJs(getUI().orElse(UI.getCurrent()), 
                   "document.getElementById('"+getChartId()+"').chartjs."+domPath+" = "+functionStr); 
    }
    
    public void addAllFunctions() {
        // tooltip callback functions
        for (Map.Entry<String, String> entry : chartConfig.getOptions().tooltips().callbacks().asMap().entrySet()) {
            addFunction("config.options.tooltips.callbacks."+entry.getKey(), JUtils.formatJavascriptFunction(entry.getKey(), entry.getValue(), TooltipsCallbacks.argumentMap().get(entry.getKey())));
        }
    }

    /**
     * Configure a ChartJs chart.
     * @param chartConfig a chart configuration implementation
     */
    public void configure(ChartConfig chartConfig) {
        this.chartConfig = chartConfig;
        if (connected) { 
            ChartJsUtils.safelyExecuteJs(getUI().orElse(UI.getCurrent()), 
                "document.getElementById($0).chartjs.config = $1", getChartId(), chartConfig.buildJson());
            addAllFunctions();
        }
    }

    // todo: make use of pendingjavascriptresult to ensure we are connected
    @Override
    protected void onAttach(AttachEvent e) {
        super.onAttach(e);
        if (!connected) {
            ChartJsUtils.safelyExecuteJs(getUI().orElse(UI.getCurrent()), 
                    "document.getElementById($0).chartjs = new Chart(document.getElementById($1).getContext('2d'), $2)" , 
                    getChartId(), getChartCanvasId(), chartConfig.buildJson());
            addAllFunctions();
        }
        connected = true;
    }
    
    @Override
    protected void onDetach(DetachEvent e) {
        destroy();
        connected = false;
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
			ChartJsUtils.safelyExecuteJs(getUI().orElse(UI.getCurrent()), "document.getElementById($0).chartjs.update()", getChartId());
        }
    }

    /**
     * Destroy the chart. This will call chartjs.destroy();
     */
    public void destroy() {
        ChartJsUtils.safelyExecuteJs(getUI().orElse(UI.getCurrent()), "document.getElementById($0).chartjs.destroy()", getChartId());
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
