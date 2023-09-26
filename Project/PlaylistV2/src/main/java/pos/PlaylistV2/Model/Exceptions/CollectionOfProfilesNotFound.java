package pos.PlaylistV2.Model.Exceptions;

public class CollectionOfProfilesNotFound extends RuntimeException{
    public CollectionOfProfilesNotFound(String id) {
        super("Invalid collection id  " + id);
    }
}
