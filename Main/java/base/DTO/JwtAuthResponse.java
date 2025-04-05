package base.DTO;

public class JwtAuthResponse{
    private final String accessToken;
    private final int userId;
    private final String username;
    private final String email;
    private final String role;

    public JwtAuthResponse(String accessToken, int userId, String username, String email, String role) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
