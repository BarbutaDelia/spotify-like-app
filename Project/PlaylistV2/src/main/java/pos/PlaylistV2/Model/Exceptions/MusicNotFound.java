package pos.PlaylistV2.Model.Exceptions;

public class MusicNotFound extends RuntimeException{
    public MusicNotFound(Integer id) {
        super("Could not find music " + id);
    }
}
