import Lombok
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class Error {
    private static final long serialVersionUID = 1L;
    private String ErrorCode;
    private String message;
    private Integer status;
    private String url = "Not available";
    private String reqMethod = "Not available";
}