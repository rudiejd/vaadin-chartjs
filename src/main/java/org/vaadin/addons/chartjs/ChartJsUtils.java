package org.vaadin.addons.chartjs;

import java.io.Serializable;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.server.Command;

public class ChartJsUtils {
    public static void safelyExecuteJs(UI ui, String expression, Serializable... parameters) {
       final String modExpression = "try {"
       		+ expression + "} catch(e) { if (e instanceof TypeError) { console.log('Chart not found') } else { console.error(e) } }";
       securelyAccessUI(ui, () -> ui.getPage().executeJs(modExpression, parameters)); 
    }
    
    /**
     * Executes a given {@link Command} after ensuring that the {@link UI} is accessible or already accessed
     * @param ui
     * @param command
     */
    public static void securelyAccessUI(UI ui, Command command) {
        if (ui != null && !ui.isClosing() && ui.getSession() != null) {
            ui.access(command);
        }
    }

}
