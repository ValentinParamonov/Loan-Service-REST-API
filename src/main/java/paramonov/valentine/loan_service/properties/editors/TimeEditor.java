package paramonov.valentine.loan_service.properties.editors;

import paramonov.valentine.loan_service.util.Time;

import java.beans.PropertyEditorSupport;

public class TimeEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        final Time time = Time.valueOf(text);
        setValue(time);
    }
}
