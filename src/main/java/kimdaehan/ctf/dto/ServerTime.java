package kimdaehan.ctf.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ServerTime {
    private TimeType type;
    private LocalDate serverDate;
    private LocalTime serverTime;

    public enum TimeType{
        START, END
    }
}
