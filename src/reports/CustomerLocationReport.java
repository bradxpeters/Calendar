package reports;

import java.util.concurrent.atomic.AtomicInteger;

public class CustomerLocationReport {
    private String postalCode;

    private Integer count;

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
