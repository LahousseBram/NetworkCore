package me.cosmic.networkcore.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.cosmic.networkcore.systems.FriendRequest;

public class FriendsManager {
    private HashMap<UUID, List<UUID>> friendsList = new HashMap<>();

    private List<FriendRequest> friendRequests;

    public FriendsManager() {
        this.friendRequests = new ArrayList<>();
    }

    public HashMap<UUID, List<UUID>> getFriendsList() {
        return this.friendsList;
    }

    public void setFriendsList(HashMap<UUID, List<UUID>> friendsList) {
        this.friendsList = friendsList;
    }

    public List<FriendRequest> getFriendRequests() {
        return this.friendRequests;
    }

    public void setFriendRequests(List<FriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }
}
