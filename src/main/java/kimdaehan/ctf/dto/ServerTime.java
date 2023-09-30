package kimdaehan.ctf.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ServerTime {
    private String type;
    private LocalDate serverDate;
    private LocalTime serverTime;

}
