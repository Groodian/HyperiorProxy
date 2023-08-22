package de.groodian.hyperiorproxy.user;

public enum UserHistoryType {

    BAN(1),
    KICK(2),
    PERMANENT_BAN(3),
    UNBAN(4);


    private final int typeId;

    UserHistoryType(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

}
