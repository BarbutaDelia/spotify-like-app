package pos.PlaylistV2.Model.Exceptions;

public class PlaylistNotFoundException extends RuntimeException{
    public PlaylistNotFoundException(String id) {
        super("Invalid playlist id  " + id);
    }

}
