package io.rudiejd.vaadin.chartjs.options.zoom;

import elemental.json.Json;
import elemental.json.JsonObject;
import io.rudiejd.vaadin.chartjs.options.Zoom;
import io.rudiejd.vaadin.chartjs.utils.And;
import io.rudiejd.vaadin.chartjs.utils.JUtils;
import io.rudiejd.vaadin.chartjs.utils.JsonBuilder;

import java.io.Serializable;

public class ZoomRange<T> extends And<Zoom<T>> implements JsonBuilder, Serializable {

    private static final long serialVersionUID = 5655614586948910546L;

    private Object x;
    private Object y;

    public ZoomRange(Zoom<T> parent) {
        super(parent);
    }

    public ZoomRange<T> x(double x) {
        this.x = x;
        return this;
    }

    public ZoomRange<T> x(String x) {
        this.x = x;
        return this;
    }

    public ZoomRange<T> y(double y) {
        this.y = y;
        return this;
    }

    public ZoomRange<T> y(String x) {
        this.x = x;
        return this;
    }


    @Override
    public JsonObject buildJson() {
        JsonObject obj = Json.createObject();
        setValue(obj, "x", x);
        setValue(obj, "y", y);
        return obj;
    }

    private void setValue(JsonObject obj, String key, Object value) {
        if (value instanceof String) {
            JUtils.putNotNull(obj, "x", (String) value);
        } else if (value instanceof Double) {
            JUtils.putNotNull(obj, "x", (Double) value);
        }
    }

}
