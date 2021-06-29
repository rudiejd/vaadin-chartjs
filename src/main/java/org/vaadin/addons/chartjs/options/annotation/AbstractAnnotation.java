package org.vaadin.addons.chartjs.options.annotation;

import java.io.Serializable;

import org.vaadin.addons.chartjs.options.AnnotationOptions;
import org.vaadin.addons.chartjs.utils.And;
import org.vaadin.addons.chartjs.utils.JUtils;
import org.vaadin.addons.chartjs.utils.JsonBuilder;

import elemental.json.Json;
import elemental.json.JsonObject;

public abstract class AbstractAnnotation<A, T> extends And<AnnotationOptions<T>> implements JsonBuilder, Serializable {

    private static final long serialVersionUID = 6861210127684854144L;

    private String type;
    private String id;
    private DrawTime drawTime;

    public AbstractAnnotation(AnnotationOptions<T> parent, String type) {
        super(parent);
        this.type = type;
    }

    public A id(String id) {
        this.id = id;
        return getThis();
    }

    /**
     * Defines when the annotations are drawn.
     * This allows positioning of the annotation relative to the other elements to the graph.
     */
    public A drawTime(DrawTime drawTime) {
        this.drawTime = drawTime;
        return getThis();
    }

    public abstract A getThis();

    @Override
    public JsonObject buildJson() {
        JsonObject map = Json.createObject();
        JUtils.putNotNull(map, "type", type);
        JUtils.putNotNull(map, "id", id);
        if (drawTime != null) {
            JUtils.putNotNull(map, "drawTime", drawTime.name());
        }
        return map;
    }


}
