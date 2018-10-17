package net.stuha.messages.awards;

import java.math.BigInteger;

public class Standing {
    private String userName;
    private BigInteger count;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigInteger getCount() {
        return count;
    }

    public void setCount(BigInteger count) {
        this.count = count;
    }
}
