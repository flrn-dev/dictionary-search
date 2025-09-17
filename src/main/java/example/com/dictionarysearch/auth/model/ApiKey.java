package example.com.dictionarysearch.auth.model;

public class ApiKey {
    private String key;
    private String owner;
    private boolean active;

    public ApiKey(String key, String owner, boolean active) {
        this.key = key;
        this.owner = owner;
        this.active = active;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
