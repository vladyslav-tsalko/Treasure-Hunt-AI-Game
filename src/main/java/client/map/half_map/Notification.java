package client.map.half_map;

import java.util.ArrayList;
import java.util.List;

//Das Notification Pattern, Kartenvalidierung, OCP, etc. ist unabhängig von dieser Aufgabe (Fehlerbehandlung) zu bearbeiten.
public class Notification {
    private List<String> errors = new ArrayList<>();

    public void addError(String info) {
        errors.add(info);
    }

    public boolean hasError() {
        return !errors.isEmpty();
    }

    public String errorInformation() {
        return "All previous validation errors: " + String.join(", ", errors);
    }
}
