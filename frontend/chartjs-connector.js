window.com_byteowls_vaadin_chartjs_ChartJs = function(e) {
    // see the javadoc of com.vaadin.ui.
    // for all functions on e.
    // Please note that in JavaScript, e is not necessarily defined inside callback functions and it might therefore be necessary to assign the reference to a separate variable
    var self = e;
    e.loggingEnabled = true;
    e.canvas = null;
    e.chartjs = null; 
    e.stateChangedCnt = 0;
    // The button HTML Element (div) opening the dropdown menu
    var menuButton;
    // The popup with the contents of the dropdown menu
    var menuPopup;
    // The the menu title element
    var menuTitle;
    // Set to true while rendering the image for export
    e.renderingExport = false;
    // called every time the state is changed
    e.onStateChange = function(state) {
        e.stateChangedCnt++;
		console.log(state);
        e.loggingEnabled = true; 
        if (e.loggingEnabled) {
            console.log("e.chartjs: accessing onStateChange the "+e.stateChangedCnt+". time");
        }
        if (e.canvas == null) {
            if (e.loggingEnabled) {
                console.log("e.chartjs: create e.canvas");
            }
            e.canvas = document.createElement('canvas');
            if (state.width && state.width.length > 0) {
                if (e.loggingEnabled) {
                    console.log("e.chartjs: e.canvas width " + state.width);
                }
                e.canvas.setAttribute('width', state.width);
            }
            if (state.height && state.height.length > 0) {
                if (e.loggingEnabled) {
                    console.log("e.chartjs: e.canvas height " + state.height);
                }
                e.canvas.setAttribute('height', state.height);
            }
            e.appendChild(e.canvas)
        } else {
            if (e.loggingEnabled) {
                console.log("e.chartjs: e.canvas already exists");
            }
        }

        if (e.chartjs == null && state.configurationJson !== 'undefined') {
            if (e.loggingEnabled) {
                console.log("e.chartjs: init");
            }

            if (e.loggingEnabled) {
                console.log("e.chartjs: configuration is\n", JSON.stringify(state.configurationJson, null, 2));
            }

            Chart.plugins.register({
                beforeDraw: function(chartInstance) {
                    if (e.loggingEnabled) {
                        console.log("e.chartjs: rendering, for export: " + self.renderingExport);
                    }
                    // the image is re-rendered for export
                    if (self.renderingExport) {
                        // set a white background to the chart
                        if (state.downloadSetWhiteBackground) {
                            var ctx = chartInstance.chart.ctx;
                            ctx.fillStyle = 'white';
                            ctx.fillRect(0, 0, chartInstance.chart.width, chartInstance.chart.height);
                        }
                    }
                }
            });

            e.chartjs = new Chart(e.canvas, state.configurationJson);
            // #69 the zoom/plugin captures the wheel event so no vertical scrolling is enabled if mouse is on
            if (state.configurationJson && !state.configurationJson.options.zoom) {
                e.chartjs.ctx.canvas.removeEventListener('wheel', e.chartjs.zoom._wheelHandler );
            }

            // only enable if there is a listener
            if (state.dataPointClickListenerFound) {
                if (e.loggingEnabled) {
                    console.log("e.chartjs: add data point click callback");
                }
                e.canvas.onclick = function(c) {
                    var elementArr = e.chartjs.getElementAtEvent(c);
                    if (elementArr && elementArr.length > 0) {
                        if (e.loggingEnabled) {
                            console.log("e.chartjs: onclick elements at:");
                            console.log(elementArr[0]);
                        }
                        // call on function registered by server side component
                        e.$server.onDataPointClick(elementArr[0]._datasetIndex, elementArr[0]._index);
                    }
                };
            }
            if (state.legendClickListenerFound) {
                if (e.loggingEnabled) {
                    console.log("e.chartjs: add legend click callback");
                }
                e.chartjs.legend.options.onClick = e.chartjs.options.legend.onClick = function (t, c) {
                    var datasets = this.chart.data.datasets;
                    var dataset = datasets[c.datasetIndex];
                    dataset.hidden= !dataset.hidden;
                    this.chart.update();
                    var ret = [];
                    for (var i = 0; i < datasets.length ; i++ ) {
                        if (!datasets[i].hidden) {
                            ret.push(i);
                        }
                    }
                    e.$server.onLegendClick(c.datasetIndex,!dataset.hidden, ret);
                }
            }
        } else {
            // update the data
            e.chartjs.config.data = state.configurationJson.data;
            // update config: options must be copied separately, just copying the "options" object does not work
            e.chartjs.config.options.legend = state.configurationJson.options.legend;
            e.chartjs.config.options.annotation = state.configurationJson.options.annotation;
            e.chartjs.update();
        }

    }

    e.destroyChart = function() {
        if (chartjs) {
            chartjs.destroy();
        }
        if (e.menuPopup) {
            document.removeChild(e.menuPopup);
            e.menuPopup = null;
            document.removeEventListener('click', e.documentClickListener);
        }
    };

    /**
     * Document click listener used for detecting clicks outside of the menu, which should close
     * the menu.
     */
    e.documentClickListener = function(event) {
        var rect = self.menuPopup.getBoundingClientRect();
        var clickedInside = (event.clientX > rect.left && event.clientX < rect.right
                         && event.clientY > rect.top  && event.clientY < rect.bottom)
                         || self.menuButton.contains(event.target)
                         || self.menuPopup.contains(event.target);
        if (!clickedInside) {
          self.setMenuOpenState(false);
        }
    }
};
