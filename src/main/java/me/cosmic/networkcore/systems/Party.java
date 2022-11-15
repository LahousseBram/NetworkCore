package me.cosmic.networkcore.systems;

import java.util.List;
import java.util.UUID;

public class Party {
    private UUID leader;

    private List<UUID> members;

    public Party(UUID leader, UUID firstMember) {
        this.leader = leader;
        this.members.add(firstMember);
    }

    public boolean isAMember(UUID member) {
        return this.members.contains(member);
    }

    public UUID getLeader() {
        return this.leader;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public boolean addMember(UUID player) {
        if (!isAMember(player))
            this.members.add(player);
        return !isAMember(player);
    }

    public boolean removeMember(UUID member) {
        if (isAMember(member))
            this.members.remove(member);
        return isAMember(member);
    }

    public List<UUID> getMembers() {
        return this.members;
    }
}
