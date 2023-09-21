package kimdaehan.ctf.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Result <T>{
    private final Code code;
    private final T data;

    private Result(Code code, T data){
        this.code = code;
        this.data = data;
    }

    public enum Code {
        OK, ERROR, ID_EXIST
    }
}
