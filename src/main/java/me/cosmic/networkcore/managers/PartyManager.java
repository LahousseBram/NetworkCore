package me.cosmic.networkcore.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.cosmic.networkcore.systems.Party;
import me.cosmic.networkcore.systems.PartyInvite;

public class PartyManager {
    private List<Party> parties = new ArrayList<>();

    private List<PartyInvite> invites;

    public List<Party> getParties() {
        return this.parties;
    }

    public List<PartyInvite> getInvites() {
        return this.invites;
    }

    public boolean createParty(UUID leader, UUID member) {
        boolean canBeCreated = true;
        for (Party p : this.parties) {
            if (p.getLeader() == leader)
                canBeCreated = false;
            for (UUID m : p.getMembers()) {
                if (m == member)
                    canBeCreated = false;
            }
        }
        if (canBeCreated)
            this.parties.add(new Party(leader, member));
        return canBeCreated;
    }

    public boolean deleteParty(UUID leader) {
        boolean hasBeenDeleted = false;
        for (Party p : this.parties) {
            if (p.getLeader() == leader) {
                this.parties.remove(p);
                hasBeenDeleted = true;
            }
        }
        return hasBeenDeleted;
    }

    public PartyInvite createInvite(UUID sender, UUID receiver) {
        PartyInvite invite = new PartyInvite(sender, receiver);
        this.invites.add(invite);
        return invite;
    }

    public void removeInvite(PartyInvite invite) {
        this.invites.remove(invite);
    }
}
