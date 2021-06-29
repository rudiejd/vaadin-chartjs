package io.rudiejd.vaadin.chartjs.test;

import org.junit.Assert;
import org.junit.Test;

import elemental.json.JsonValue;
import io.rudiejd.chartjs.config.LineChartConfig;
import io.rudiejd.vaadin.chartjs.data.LineDataset;
import io.rudiejd.vaadin.chartjs.options.AnimationEasing;
import io.rudiejd.vaadin.chartjs.options.InteractionMode;
import io.rudiejd.vaadin.chartjs.options.Position;
import io.rudiejd.vaadin.chartjs.options.elements.Rectangle.RectangleEdge;
import io.rudiejd.vaadin.chartjs.options.scale.Axis;
import io.rudiejd.vaadin.chartjs.options.scale.LinearScale;
import io.rudiejd.vaadin.chartjs.options.scale.LogarithmicScale;

/**
 * @author michael@byteowls.com
 */
public class ChartJsTest {

    @Test
    public void testConfigGeneral() {

        LineChartConfig config = new LineChartConfig();
        config
            .data()
                .labels("A", "B", "C")
                .addDataset(new LineDataset().fill(true).label("Dataset 1").data(1D, 2D, 3D))
                .addDataset(new LineDataset().label("Set 2").data(3.3, 1.3, 2.9))
              .and()
            .options()
                .events("test", "test1")
                .showLines(true)
                .responsive(true)
            .title()
                .display(true)
                .position(Position.LEFT)
                .text("Hello World")
                .and()
            .animation()
                .easing(AnimationEasing.easeOutQuart)
                .and()
            .hover()
                .mode(InteractionMode.INDEX)
                .and()
            .tooltips()
                .mode(InteractionMode.INDEX)
                .and()
            .elements()
                .arc()
                    .borderWidth(2)
                    .and()
                .rectangle()
                    .borderSkipped(RectangleEdge.BOTTOM)
                    .and()
                .line()
                    .fill(false)
                    .and()
                .point()
                    .radius(32)
                    .and()
                .and()
            .scales()
                .add(Axis.X, new LinearScale().position(Position.TOP))
                .add(Axis.Y, new LogarithmicScale().position(Position.LEFT))
                .and()
           .legend()
                .fullWidth(false)
                .labels()
                    .boxWidth(20)
                    .and()
                .position(Position.BOTTOM)
                .and()
                
            .done();

        JsonValue jsonValue = config.buildJson();
        Assert.assertNotNull(jsonValue);
    }

}
