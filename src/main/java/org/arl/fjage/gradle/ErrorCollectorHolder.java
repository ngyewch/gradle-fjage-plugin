package org.arl.fjage.gradle;

import org.codehaus.groovy.control.ErrorCollector;

public class ErrorCollectorHolder {

    private ErrorCollector errorCollector;

    public void add(ErrorCollector errorCollector) {
        if (errorCollector == null) {
            return;
        }
        if (this.errorCollector == null) {
            this.errorCollector = errorCollector;
        } else {
            this.errorCollector.addCollectorContents(errorCollector);
        }
    }

    public ErrorCollector getErrorCollector() {
        return errorCollector;
    }
}
