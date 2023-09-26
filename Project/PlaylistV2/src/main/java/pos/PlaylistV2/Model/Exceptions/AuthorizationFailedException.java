package pos.PlaylistV2.Model.Exceptions;

public class AuthorizationFailedException extends RuntimeException {

    public AuthorizationFailedException(String message) {
        super("Authorization failed! " + message);
    }
}
