package pos.PlaylistV2.Model.Exceptions;

public class CollectionOfPlaylistsNotFound extends RuntimeException{
    public CollectionOfPlaylistsNotFound(String id) {
        super("Invalid collection id  " + id);
    }
}
