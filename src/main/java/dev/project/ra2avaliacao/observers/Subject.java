package dev.project.ra2avaliacao.observers;

public interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(String eventType, Object data);
}
