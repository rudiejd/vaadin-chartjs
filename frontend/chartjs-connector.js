window.com_byteowls_vaadin_chartjs_ChartJs = function(e) {
    // see the javadoc of com.vaadin.ui.
    // for all functions on e.
    // Please note that in JavaScript, e is not necessarily defined inside callback functions and it might therefore be necessary to assign the reference to a separate variable
    var self = e;
    e.loggingEnabled = true;
    e.canvas = null;
    e.chartjs = null; 
    e.stateChangedCnt = 0;
    e.cbPrefix = '__cb_'; // Also cf. JUtils
    e.cbArgsPostfix = '_args'; // Also cf. JUtils
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
            // build the menu
            if (state.showDownloadAction || state.menuItems && state.menuItems.length > 0) {
                e.buildMenu();
                e.appendChild(e.menuButton);
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
            // parse callback functions
            //e.parseCallbacks(state.configurationJson);

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
                e.canvas.onclick = function(e) {
                    var elementArr = e.chartjs.getElementAtEvent(e);
                    if (elementArr && elementArr.length > 0) {
                        if (e.loggingEnabled) {
                            console.log("e.chartjs: onclick elements at:");
                            console.log(elementArr[0]);
                        }
                        // call on function registered by server side component
                        self.onDataPointClick(elementArr[0]._datasetIndex, elementArr[0]._index);
                    }
                };
            }
            if (state.legendClickListenerFound) {
                if (e.loggingEnabled) {
                    console.log("e.chartjs: add legend click callback");
                }
                e.chartjs.legend.options.onClick = e.chartjs.options.legend.onClick = function (t,e) {
                    var datasets = e.chart.data.datasets;
                    var dataset = datasets[e.datasetIndex];
                    dataset.hidden= !dataset.hidden;
                    e.chart.update();
                    var ret = [];
                    for (var i = 0; i < datasets.length ; i++ ) {
                        if (!datasets[i].hidden) {
                            ret.push(i);
                        }
                    }
                    self.onLegendClick(e.datasetIndex,!dataset.hidden, ret);
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

    /**
     * Recursively searches obj for string properties starting with e.cbPrefix and sets the
     * property with the name without the prefix with a function based on the JavaScript code found
     * in the property's value.
     * If obj is not set, starts with the chartjs configuration.
     */
    e.parseCallbacks = function(obj) {
        if (!obj) {
            obj = chartjs.config;
        }
        // walk all properties
        for (var key in obj) {
            // skip inherited properties
            if (!obj.hasOwnProperty(key)) {
                continue;
            }
            var prop = obj[key];
            // skip null values
            if (prop == null) {
                continue;
            }
            // recurse into objects (includes arrays)
            else if (typeof prop === 'object') {
                try {
                    e.parseCallbacks(prop);
                }
                catch (err) {
                    console.error('Error parsing script nested in property: ' + key);
                    throw err;
                }
            }
            // found a string property where the property name starts with the callback prefix
            else if (typeof prop === 'string' && key.indexOf(cbPrefix) == 0) {
                // strip the prefix from the property name
                var newKey = key.substring(cbPrefix.length);
                // find argument declaration
                var args = obj[key + cbArgsPostfix];
                // parse the function and set it as property to obj
                try {
                    obj[newKey] = e.parseCallback(prop, args);
                }
                catch (err) {
                    // print property name and fail recursion
                    console.error('Error parsing script in property: ' + key);
                    throw err;
                }
            }
        }
    }

    /**
     * Parses callback code and returns it as function.
     */
    e.parseCallback = function(code, args) {
        var callback = code.trim();
        // declaration of the function or a return statement is not required to be provided for
        // a simple calculation, but required for parsing
        if (callback.indexOf('function') != 0) {
            if (callback.indexOf('return') != 0) {
                callback = 'return ' + callback;
            }
            if (!args) {
                args = '';
            }
            else if (typeof args !== 'string') {
                // multiple arguments, ie. args is an array
                args = args.join(',');
            }
            callback = 'function(' + args + '){' + callback + '}';
        }
        try {
            return eval('(' + callback + ')');
        }
        catch (err) {
            console.error('Unable to parse script:', err, callback);
            throw err;
        }
    }

    e.getImageDataUrl = function(type, quality) {
        if (typeof quality !== 'undefined') {
            console.log("chartjs: download image quality: " + quality);
        }
        // TODO create issue on chart.js to allow jpeg downloads
        // call on function registered by server side component
        self.sendImageDataUrl(chartjs.toBase64Image());
    };

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

    // #21 Build a menu 
    e.buildMenu = function() {
        // create the menu button
        e.menuButton = e.createDiv('v-menubar v-widget');
        e.menuTitle = e.createDiv('v-menubar-menuitem');
        e.menuButton.appendChild(e.menuTitle);
        var menuTitleCaption = e.createDiv('v-menubar-menuitem-caption v-icon Vaadin-Icons');
        // for a text menu remove the v-icon class above and set the text content to some string
        // to be defined in the state
        // menuTitleCaption.textContent = 'Menu';
        e.menuTitle.appendChild(menuTitleCaption);
        // toggle state on click on the button
        e.menuButton.onclick = function() {
            self.setMenuOpenState(self.menuPopup.style.display === 'none');
        };
        // close the menu if the user clicked somewhere outside the menu
        document.addEventListener('click', e.documentClickListener);

        // build the popup / dropdown and its content
        e.menuPopup = e.createDiv('v-chartjs v-menubar-popup');
        // needs to be set here explicitly, since it is also used for detecting the state
        e.menuPopup.style.display = 'none';
        document.getElementsByClassName('v-overlay-container')[0].appendChild(e.menuPopup)
        // manual, absolute positioning on click
        var popupContent = e.createDiv('popupContent');
        e.menuPopup.appendChild(popupContent);
        var subMenu = e.createDiv('v-menubar-submenu v-widget');
        popupContent.appendChild(subMenu);
        var state = e.getState();
        // add the user defined menu items
        var menuItems = e.getState().menuItems;
        if (menuItems) {
            for (var action in menuItems) {
                e.createMenuItem(subMenu, menuItems[action], e[action]);
            }
        }
        // add the download action
        if (state.showDownloadAction) {
            var downloadActionText = 'Download PNG';
            if (state.downloadActionText) {
                downloadActionText = state.downloadActionText;
            }
            e.createMenuItem(subMenu, downloadActionText, e.startImageDownload);
        }
    };

    /**
     * Starts the image download
     */
    e.startImageDownload = function(e) {
        self.renderingExport = true;
        chartjs.render({duration: 0});
        var filename = self.getState().downloadActionFilename;
        if (!filename) {
            filename = 'chart.png';
        }
        if (canvas.msToBlob) {
            var blob = canvas.msToBlob();
            window.navigator.msSaveBlob(blob, filename);
        } else {
            var link = document.createElement('a');
            link.textContent = 'Download';
            link.href = canvas.toDataURL("image/png");
            link.download = filename;
            self.menuPopup.appendChild(link);
            link.click();
            self.menuPopup.removeChild(link);
        }
        self.renderingExport = false;
        chartjs.render({duration: 0});
    }

    /**
     * Creates and adds a new menu item
     * @param subMenu The sub menu HTML element.
     * @param title The menu entry title.
     * @param listener Callback called if the item is clicked.
     */
    e.createMenuItem = function(subMenu, title, listener) {
        var subMenuItem = document.createElement('span');
        subMenuItem.className = 'v-menubar-menuitem';
        subMenu.appendChild(subMenuItem);
        var subMenuItemCaption = document.createElement('span');
        subMenuItemCaption.className = 'v-menubar-menuitem-caption';
        subMenuItemCaption.textContent = title;
        subMenuItem.appendChild(subMenuItemCaption);
        subMenuItem.onclick = function() {
            listener();
            self.setMenuOpenState(false);
        }
    }

    /**
     * Opens or closes the dropdown menu, pass true to open it.
     */
    e.setMenuOpenState = function(open) {
        if (!open) {
            e.menuTitle.className = 'v-menubar-menuitem';
            e.menuPopup.style.display = 'none';
        } else {
            e.menuTitle.className = 'v-menubar-menuitem v-menubar-menuitem-selected';
            e.menuPopup.style.display = 'block';
            var clientRect = e.menuButton.getBoundingClientRect();
            e.menuPopup.style.top = (clientRect.top + clientRect.height) + 'px';
            // e.menuPopup.style.left = clientRect.left + 'px';
            e.menuPopup.style.right = (window.innerWidth - clientRect.right) + 'px';
        }
    }

    /**
     * Creates and returns a new div element and sets the class attribute to the
     * given css classes.
     */
    e.createDiv = function(className) {
        var div = document.createElement('div');
        if (className) {
            div.className = className;
        }
        return div;
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
