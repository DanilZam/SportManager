package org.app.sportmanager.fx_nodes;

public class ResultRow {
    private final String reps;
    private final String brzycki;
    private final String epley;
    private final String lander;
    private final String oconner;

    public ResultRow(String reps, String brzycki, String epley, String lander, String oconner) {
        this.reps = reps;
        this.brzycki = brzycki;
        this.epley = epley;
        this.lander = lander;
        this.oconner = oconner;
    }

    public String getReps() { return reps; }
    public String getBrzycki() { return brzycki; }
    public String getEpley() { return epley; }
    public String getLander() { return lander; }
    public String getOconner() { return oconner; }
}

