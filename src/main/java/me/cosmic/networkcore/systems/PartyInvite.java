package me.cosmic.networkcore.systems;

import java.util.UUID;

public class PartyInvite {
    private UUID sender;

    private UUID receiver;

    public PartyInvite(UUID sender, UUID receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public void setReceiver(UUID receiver) {
        this.receiver = receiver;
    }

    public UUID getSender() {
        return this.sender;
    }

    public UUID getReceiver() {
        return this.receiver;
    }
}
