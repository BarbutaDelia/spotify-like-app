package pos.PlaylistV2.Model.Exceptions;

public class IncorrectOperationException extends RuntimeException{
    public IncorrectOperationException()
    {
        super("Incorrect operation! Operation should be add or remove");
    }
}

