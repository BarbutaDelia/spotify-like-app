package pos.PlaylistV2.Model.Exceptions;

public class ProfileNotFoundException extends RuntimeException{
    public ProfileNotFoundException(String id) {
        super("Invalid profile id  " + id);
    }
}
