package spring.playground.concurrency;

public enum TestConfig {
    THREAD_COUNT(100),
    THREAD_POOL_SIZE(10),
    SLEEP_MILLIS(10),
    TEST_COUNT(100);

    private final int value;

    TestConfig(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
