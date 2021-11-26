package pulsar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private int employeeId;
    private String employeeName;
    private String userName;
    private String department;
    private String action;
    private Date added;
    private boolean deleted;

}
