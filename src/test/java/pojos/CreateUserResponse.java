package pojos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import utils.DateDeserializer;

import java.time.LocalDateTime;
@Data
public class CreateUserResponse {
    private String name;
    private String job;
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")//SSS=milli sec,Z=timezone
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDateTime createdAt;

}
