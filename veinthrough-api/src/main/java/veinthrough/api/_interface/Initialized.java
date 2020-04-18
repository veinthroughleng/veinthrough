package veinthrough.api._interface;

// An object should be initialized before working
public interface Initialized {
    void initialize();
    boolean initialized();
    default void checkInitialization() {
        if (!initialized()) {
            initialize();
        }
    }
}
