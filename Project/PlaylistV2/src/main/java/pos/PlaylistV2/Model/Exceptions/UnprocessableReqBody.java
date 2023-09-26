package pos.PlaylistV2.Model.Exceptions;

public class UnprocessableReqBody extends RuntimeException{
    public UnprocessableReqBody()
    {
        super("Unprocessable request body!");
    }
}
